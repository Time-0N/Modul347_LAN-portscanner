package com.example.backend.controller;

import com.example.backend.model.Network;
import com.example.backend.service.ApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
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

    @Override
    @PostMapping("/network/{networkId}/name")
    public ResponseEntity<Network> updateNetworkName(
            @PathVariable Long networkId,
            @RequestBody Map<String, String> request
    ) {
        String name = request.get("name");
        Network updatedNetwork = apiService.updateNetworkName(networkId, name);
        return ResponseEntity.ok(updatedNetwork);
    }

    @Override
    @PutMapping("/network/{networkId}")
    public ResponseEntity<Network> updateNetwork(@PathVariable Long networkId, @RequestBody Map<String, String> request) {
        return ResponseEntity.ok(new Network());
    }
}
