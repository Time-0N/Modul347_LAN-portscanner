package com.example.backend.service;

import com.example.backend.model.DeviceInfo;
import com.example.backend.model.IpAddress;
import com.example.backend.model.Network;
import com.example.backend.repository.DeviceInfoRepository;
import com.example.backend.repository.IpAddressRepository;
import com.example.backend.repository.NetworkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class ApiServiceImpl implements ApiService {

    private static final Logger logger = LoggerFactory.getLogger(ApiServiceImpl.class);

    private final NetworkRepository networkRepo;
    private final IpAddressRepository ipRepo;
    private final DeviceInfoRepository deviceRepo;
    private final RestTemplate restTemplate;

    @Value("${python.api.base-url}")
    private String pythonApiBaseUrl;

    public ApiServiceImpl(
            NetworkRepository networkRepo,
            IpAddressRepository ipRepo,
            DeviceInfoRepository deviceRepo,
            RestTemplate restTemplate
    ) {
        this.networkRepo = networkRepo;
        this.ipRepo = ipRepo;
        this.deviceRepo = deviceRepo;
        this.restTemplate = restTemplate;
    }

    @Override
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    @Transactional
    public Network scanIps(String subnet) {
        String apiUrl = UriComponentsBuilder.fromHttpUrl(pythonApiBaseUrl)
                .path("/scan")
                .queryParam("subnet", subnet)
                .encode()
                .toUriString();

        Map<String, List<Map<String, String>>> response = restTemplate.getForObject(apiUrl, Map.class);
        List<Map<String, String>> devices = response.get("devices");

        if (devices == null || devices.isEmpty()) {
            return new Network();
        }

        Network network = networkRepo.findBySubnet(subnet).orElseGet(() -> {
            Network newNetwork = new Network();
            newNetwork.setSubnet(subnet);
            return networkRepo.save(newNetwork);
        });

        for (Map<String, String> device : devices) {
            String ip = device.get("ip");
            IpAddress ipAddress = ipRepo.findByNetworkId(network.getId()).stream()
                    .filter(i -> i.getIp().equals(ip))
                    .findFirst()
                    .orElseGet(() -> {
                        IpAddress newIp = new IpAddress();
                        newIp.setIp(ip);
                        newIp.setNetwork(network);
                        network.getIpAddresses().add(newIp);
                        return ipRepo.save(newIp);
                    });
        }
        return network;
    }

    @Async
    @Override
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public CompletableFuture<String> fullScan(Long ipId) {
        Optional<IpAddress> ipOpt = ipRepo.findById(ipId);
        if (!ipOpt.isPresent()) {
            return CompletableFuture.completedFuture("404 Not Found");
        }

        IpAddress ipAddress = ipOpt.get();
        String ip = ipAddress.getIp();

        String deviceInfoUrl = UriComponentsBuilder.fromHttpUrl(pythonApiBaseUrl + "/device_scan")
                .queryParam("ip", ip)
                .queryParam("mode", "popular")
                .toUriString();

        logger.debug("Calling Python device scan at: {}", deviceInfoUrl);

        try {
            String extraInfo = restTemplate.getForObject(deviceInfoUrl, String.class);

            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.setIpAddress(ipAddress);
            deviceInfo.setExtraInfo(extraInfo);
            deviceRepo.save(deviceInfo);

            return CompletableFuture.completedFuture("Full scan completed for IP: " + ip);
        } catch (RestClientException e) {
            logger.error("Failed to call Python device scan: {}", e.getMessage());
            return CompletableFuture.completedFuture("Failed to scan IP: " + ip);
        }
    }

    @Override
    public List<IpAddress> listAllIps() {
        return ipRepo.findAll();
    }

    @Override
    public List<DeviceInfo> getDeviceInfoForIp(Long ipId) {
        return deviceRepo.findByIpAddressId(ipId);
    }

    @Override
    public List<Network> getAllNetworks() {
        return networkRepo.findAll();
    }

    @Override
    public Network updateNetworkName(Long networkId, String name) {
        return networkRepo.findById(networkId)
                .map(network -> {
                    network.setName(name);
                    return networkRepo.save(network);
                })
                .orElseThrow(() -> new RuntimeException("Network not found with id " + networkId));
    }

    @Override
    @Transactional
    public void deleteNetworkById(Long networkId) {
        Network network = networkRepo.findById(networkId)
                .orElseThrow(() -> new RuntimeException("Network not found with id " + networkId));
        networkRepo.delete(network);
    }
}
