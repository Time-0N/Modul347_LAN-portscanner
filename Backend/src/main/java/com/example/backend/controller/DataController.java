package com.example.backend.controller;

import com.example.backend.model.Network;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;

public interface DataController {
    ResponseEntity<List<Network>> getStoredNetworks();

    ResponseEntity<Network> updateNetworkName(Long networkId, Map<String, String> request);
}
