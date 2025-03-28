package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "ip_address")
public class IpAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String ip;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "network_id", nullable = false)
    private Network network;

    @Transient
    @JsonProperty("networkName")
    public String getNetworkName() {
        return network != null ? network.getName() : null;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }
}
