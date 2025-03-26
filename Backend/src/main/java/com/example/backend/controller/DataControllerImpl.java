package com.example.backend.controller;

import com.example.backend.model.Network;
import com.example.backend.service.ApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DataControllerImpl implements DataController {

    private final ApiService apiService;

    public DataControllerImpl(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    @GetMapping("/getStoredNetworks")
    public ResponseEntity<List<Network>> getStoredNetworks() {
        return ResponseEntity.ok(apiService.getAllNetworks());
    }
}

