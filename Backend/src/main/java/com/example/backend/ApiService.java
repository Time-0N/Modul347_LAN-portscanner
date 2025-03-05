package com.example.backend;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ApiService {

    public String scanIps() {
        String apiUrl = "http://127.0.0.1:8000/scan?subnet=172.20.10.0/28";
        RestTemplate restTemplate = new RestTemplate();

        try {
            Map<String, List<Map<String, String>>> response = restTemplate.getForObject(apiUrl, Map.class);
            List<Map<String, String>> devices = response.get("devices");

            StringBuilder result = new StringBuilder("Abfrage\n");
            for (Map<String, String> device : devices) {
                String ip = device.get("ip");
                result.append("Infos:").append(ip);

                String deviceInfo = restTemplate.getForObject("http://127.0.0.1:8000/device_scan?ip=172.20.10.3&mode=popular" + ip, String.class);
                result.append(deviceInfo);
            }

            return result.toString();
        } catch (Exception e) {
            System.out.println("Fehler beim Abrufen: " + e.getMessage());
            return "Fehler beim Scannen: " + e.getMessage();
        }
    }
}
