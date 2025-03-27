package com.example.backend.repository;

import com.example.backend.model.DeviceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DeviceInfoRepository extends JpaRepository<DeviceInfo, Long> {
    List<DeviceInfo> findByIpAddressId(Long ipId);
}
