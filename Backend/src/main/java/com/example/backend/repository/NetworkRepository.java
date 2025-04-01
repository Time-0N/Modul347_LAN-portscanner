package com.example.backend.repository;

import com.example.backend.model.Network;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface NetworkRepository extends JpaRepository<Network, Long> {
    Optional<Network> findBySubnet(String subnet);

    @Query("SELECT n FROM Network n LEFT JOIN FETCH n.ipAddresses WHERE n.subnet = :subnet")
    Optional<Network> findBySubnetWithIpAddresses(String subnet);
}
