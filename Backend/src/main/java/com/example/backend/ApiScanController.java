package com.example.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiScanController {

    private final ApiService apiService;

    @Autowired
    public ApiScanController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/scan-ips")
    public String scanIps() {
        return apiService.scanIps();
    }
}
