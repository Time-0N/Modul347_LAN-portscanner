package com.example.backend.controller;

import com.example.backend.model.DeviceInfo;
import com.example.backend.model.IpAddress;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ApiScanController {
	public ResponseEntity<String> scanIps(@RequestParam String subnet);

	public ResponseEntity<CompletableFuture<String>> fullScan(@PathVariable Long ipId);

	public ResponseEntity<List<IpAddress>> listAllIps();

	public ResponseEntity<List<DeviceInfo>> getDeviceInfo(@PathVariable Long ipId);
}
