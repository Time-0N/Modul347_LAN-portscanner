package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "ip_address")
public class IpAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String ip;

    public List<Integer> getPorts() {
        return ports;
    }

    public void setPorts(List<Integer> ports) {
        this.ports = ports;
    }

    @ElementCollection
    @CollectionTable(name = "ip_ports", joinColumns = @JoinColumn(name = "ip_id"))
    @Column(name = "port")
    private List<Integer> ports;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "network_id", nullable = false)
    private Network network;

    @OneToOne(mappedBy = "ipAddress", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private DeviceInfo deviceInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public DeviceInfo getDeviceInfo() {return deviceInfo;}

    public void setDeviceInfo(DeviceInfo deviceInfo) {this.deviceInfo = deviceInfo;}
}
