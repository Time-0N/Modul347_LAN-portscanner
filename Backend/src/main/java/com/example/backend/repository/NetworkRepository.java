package com.example.backend.repository;

import com.example.backend.model.Network;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface NetworkRepository extends JpaRepository<Network, Long> {
    Optional<Network> findBySubnet(String subnet);
}
