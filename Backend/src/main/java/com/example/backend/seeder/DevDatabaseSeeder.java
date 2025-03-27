package com.example.backend.seeder;

import com.example.backend.model.DeviceInfo;
import com.example.backend.model.IpAddress;
import com.example.backend.model.Network;
import com.example.backend.repository.DeviceInfoRepository;
import com.example.backend.repository.IpAddressRepository;
import com.example.backend.repository.NetworkRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("dev")
public class DevDatabaseSeeder {

	private final NetworkRepository networkRepository;
	private final IpAddressRepository ipAddressRepository;
	private final DeviceInfoRepository deviceInfoRepository;

	public DevDatabaseSeeder(
			NetworkRepository networkRepository,
			IpAddressRepository ipAddressRepository,
			DeviceInfoRepository deviceInfoRepository
	) {
		this.networkRepository = networkRepository;
		this.ipAddressRepository = ipAddressRepository;
		this.deviceInfoRepository = deviceInfoRepository;
	}

	@PostConstruct
	public void seed() {
		// Clean previous data
		deviceInfoRepository.deleteAll();
		ipAddressRepository.deleteAll();
		networkRepository.deleteAll();

		// Network
		Network network = new Network();
		network.setSubnet("192.168.1.0/24");
		networkRepository.save(network);

		// IPs
		IpAddress ip1 = new IpAddress();
		ip1.setIp("192.168.1.10");
		ip1.setNetwork(network);

		IpAddress ip2 = new IpAddress();
		ip2.setIp("192.168.1.11");
		ip2.setNetwork(network);

		ipAddressRepository.saveAll(List.of(ip1, ip2));

		// Devices
		DeviceInfo device1 = new DeviceInfo();
		device1.setIpAddress(ip1);
		device1.setHostname("laptop-ayman");
		device1.setMac("AA:BB:CC:DD:EE:01");
		device1.setOs("Windows 11");
		device1.setExtraInfo("Intel i7, 16GB RAM");

		DeviceInfo device2 = new DeviceInfo();
		device2.setIpAddress(ip2);
		device2.setHostname("raspberry-pi");
		device2.setMac("AA:BB:CC:DD:EE:02");
		device2.setOs("Raspberry Pi OS");
		device2.setExtraInfo("Used for home automation");

		deviceInfoRepository.saveAll(List.of(device1, device2));

		System.out.println("✅ Dev test data seeded!");
	}
}

