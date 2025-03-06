package com.example.backend.controller;

import com.example.backend.service.ApiService;
import com.example.backend.model.DeviceInfo;
import com.example.backend.model.IpAddress;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
public class ApiScanController {

    private final ApiService apiService;

    public ApiScanController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/scan-ips")
    public String scanIps(@RequestParam String subnet) {
        return apiService.scanIps(subnet);
    }

    @GetMapping("/full-scan/{ipId}")
    public CompletableFuture<String> fullScan(@PathVariable Long ipId) {
        return apiService.fullScan(ipId);
    }

    @GetMapping("/list-ips")
    public List<IpAddress> listAllIps() {
        return apiService.listAllIps();
    }

    @GetMapping("/device-info/{ipId}")
    public List<DeviceInfo> getDeviceInfo(@PathVariable Long ipId) {
        return apiService.getDeviceInfoForIp(ipId);
    }
}
