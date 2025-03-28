package com.example.backend.service;

import com.example.backend.model.DeviceInfo;
import com.example.backend.model.IpAddress;
import com.example.backend.model.Network;
import com.example.backend.repository.DeviceInfoRepository;
import com.example.backend.repository.IpAddressRepository;
import com.example.backend.repository.NetworkRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class ApiServiceImpl implements ApiService {

    private final NetworkRepository networkRepo;
    private final IpAddressRepository ipRepo;
    private final DeviceInfoRepository deviceRepo;
    private final RestTemplate restTemplate;

    public ApiServiceImpl(
            NetworkRepository networkRepo,
            IpAddressRepository ipRepo,
            DeviceInfoRepository deviceRepo
    ) {
        this.networkRepo = networkRepo;
        this.ipRepo = ipRepo;
        this.deviceRepo = deviceRepo;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Network scanIps(String subnet) {
        String apiUrl = UriComponentsBuilder.fromHttpUrl("http://127.0.0.1:8000/scan")
                .queryParam("subnet", subnet)
                .toUriString();

        Map<String, List<Map<String, String>>> response = restTemplate.getForObject(apiUrl, Map.class);
        List<Map<String, String>> devices = response.get("devices");

        if (devices == null || devices.isEmpty()) {
            //Needs better logic here! Maybe throw error and handle instead?
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
                        return ipRepo.save(newIp);
                    });
        }
        return network;
    }

    @Async
    @Override
    public CompletableFuture<String> fullScan(Long ipId) {
        Optional<IpAddress> ipOpt = ipRepo.findById(ipId);
        if (!ipOpt.isPresent()) {
            return CompletableFuture.completedFuture("404 Not Found");
        }
        IpAddress ipAddress = ipOpt.get();
        String ip = ipAddress.getIp();

        String deviceInfoUrl = UriComponentsBuilder.fromHttpUrl("http://127.0.0.1:8000/device_scan")
                .queryParam("ip", ip)
                .queryParam("mode", "popular")
                .toUriString();

        String extraInfo = restTemplate.getForObject(deviceInfoUrl, String.class);

        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setIpAddress(ipAddress);
        deviceInfo.setExtraInfo(extraInfo);
        deviceRepo.save(deviceInfo);

        return CompletableFuture.completedFuture("Full scan für IP: " + ip);
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

}
