package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "network")
public class Network {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String subnet;

    @Column
    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "network", cascade = CascadeType.ALL)
    private List<IpAddress> ipAddresses;


    public Long getId() {
        return id;
    }

    public void setSubnet(String subnet) {
        this.subnet = subnet;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getSubnet() {return subnet;}

}
