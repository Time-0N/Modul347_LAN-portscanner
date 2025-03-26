package com.example.backend.controller;

import com.example.backend.model.Network;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface DataController {
    ResponseEntity<List<Network>> getStoredNetworks();
}
