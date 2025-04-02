package com.example.backend.controller;

import com.example.backend.model.Network;
import com.example.backend.service.ApiService;
import com.example.backend.service.DataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class DataControllerImpl implements DataController {

    private final ApiService apiService;
    private final DataService dataService;

    public DataControllerImpl(ApiService apiService, DataService dataService) {
        this.apiService = apiService;
        this.dataService = dataService;
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
    public ResponseEntity<Network> rescanNetwork(@PathVariable Long networkId) {
        try {
            return ResponseEntity.ok(dataService.rescanNetwork(networkId));
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @Override
    @DeleteMapping("/network/{networkId}")
    public ResponseEntity<Void> deleteNetworkById(@PathVariable Long networkId) {
        apiService.deleteNetworkById(networkId);
        return ResponseEntity.noContent().build();
    }
}
