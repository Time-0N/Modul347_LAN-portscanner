package com.example.backend.service;

import com.example.backend.model.Network;

public interface DataService {
    Network rescanNetwork(Long networkId);
}
