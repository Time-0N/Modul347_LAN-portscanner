package com.example.backend.service;

import com.example.backend.model.IpAddress;
import com.example.backend.model.Network;
import com.example.backend.repository.DeviceInfoRepository;
import com.example.backend.repository.IpAddressRepository;
import com.example.backend.repository.NetworkRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataServiceImpl implements DataService {

    private final RestTemplate restTemplate;
    private final IpAddressRepository ipRepo;
    private final NetworkRepository networkRepo;

    @Value("${python.api.base-url}")
    private String pythonApiBaseUrl;

    public DataServiceImpl(RestTemplate restTemplate, IpAddressRepository ipRepo, NetworkRepository networkRepo) {
        this.restTemplate = restTemplate;
        this.ipRepo = ipRepo;
        this.networkRepo = networkRepo;
    }

    @Override
    public Network rescanNetwork(Long networkId) {
        Network network = networkRepo.findById(networkId)
                .orElseThrow(() -> new NoSuchElementException("Network not found with id: " + networkId));

        try {
            String apiUrl = UriComponentsBuilder.fromHttpUrl(pythonApiBaseUrl)
                    .path("/scan")
                    .queryParam("subnet", network.getSubnet())
                    .encode()
                    .toUriString();

            Map<String, List<Map<String, String>>> response = restTemplate.getForObject(apiUrl, Map.class);
            List<Map<String, String>> devices = response != null ?
                    response.getOrDefault("devices", Collections.emptyList()) :
                    Collections.emptyList();

            Set<String> existingIps = network.getIpAddresses().stream()
                    .map(IpAddress::getIp)
                    .collect(Collectors.toSet());

            devices.stream()
                    .map(device -> device.get("ip"))
                    .filter(ip -> !existingIps.contains(ip))
                    .forEach(ip -> {
                        IpAddress newIp = new IpAddress();
                        newIp.setIp(ip);
                        newIp.setNetwork(network);
                        ipRepo.save(newIp);
                    });

            return networkRepo.save(network);

        } catch (RestClientException e) {
            throw new RuntimeException("Failed to rescan network: " + e.getMessage(), e);
        }
    }
}
