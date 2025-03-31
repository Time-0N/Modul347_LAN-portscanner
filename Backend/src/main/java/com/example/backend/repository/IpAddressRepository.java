package com.example.backend.repository;

import com.example.backend.model.IpAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IpAddressRepository extends JpaRepository<IpAddress, Long> {
    List<IpAddress> findByNetworkId(Long networkId);
    void deleteByNetworkId(Long networkId);
}
