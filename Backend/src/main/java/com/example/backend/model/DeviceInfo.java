package com.example.backend.model;

import jakarta.persistence.*;




@Entity
@Table(name = "device_info")
public class DeviceInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ip_id", nullable = false)
    private IpAddress ipAddress;

    private String mac;
    private String hostname;
    private String os;

    @Column(columnDefinition = "TEXT")
    private String extraInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IpAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(IpAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }
}
