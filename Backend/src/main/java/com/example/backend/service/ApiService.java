package com.example.backend.service;

import com.example.backend.model.DeviceInfo;
import com.example.backend.model.IpAddress;
import com.example.backend.model.Network;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ApiService {
	Network scanIps(String subnet);
	CompletableFuture<String> fullScan(Long ipId);
	List<IpAddress> listAllIps();
	List<DeviceInfo> getDeviceInfoForIp(Long ipId);
	List<Network> getAllNetworks();
	Network updateNetworkName(Long networkId, String name);

	void deleteNetworkById(Long networkId);
}
