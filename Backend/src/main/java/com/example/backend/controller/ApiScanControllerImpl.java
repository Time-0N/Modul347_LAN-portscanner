package com.example.backend.controller;

import com.example.backend.service.ApiService;
import com.example.backend.model.DeviceInfo;
import com.example.backend.model.IpAddress;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
public class ApiScanControllerImpl implements ApiScanController {

    private final ApiService apiService;

    public ApiScanControllerImpl(
            ApiService apiService
    ) {
        this.apiService = apiService;
    }

    @Override
    @GetMapping("/scan-ips")
    public ResponseEntity<String> scanIps(@RequestParam String subnet) {
        return ResponseEntity.ok(apiService.scanIps(subnet));
    }

    @Override
    @GetMapping("/full-scan/{ipId}")
    public ResponseEntity<CompletableFuture<String>> fullScan(@PathVariable Long ipId) {
        return ResponseEntity.ok(apiService.fullScan(ipId));
    }

    @GetMapping("/list-ips")
    public ResponseEntity<List<IpAddress>> listAllIps() {
        return ResponseEntity.ok(apiService.listAllIps());
    }

    @GetMapping("/device-info/{ipId}")
    public ResponseEntity<List<DeviceInfo>> getDeviceInfo(@PathVariable Long ipId) {
        return ResponseEntity.ok(apiService.getDeviceInfoForIp(ipId));
    }
}
