package com.example.backend.controller;

import com.example.backend.model.Network;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface DataController {
    ResponseEntity<List<Network>> getStoredNetworks();
    ResponseEntity<Network> updateNetworkName(Long networkId, Map<String, String> request);

    ResponseEntity<Network> rescanNetwork(@PathVariable Long networkId);

    ResponseEntity<Void> deleteNetworkById(@PathVariable Long networkId);
}
