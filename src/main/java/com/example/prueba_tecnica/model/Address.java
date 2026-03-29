package com.example.prueba_tecnica.model;

import jakarta.persistence.*;

// es una tabla de base de datos
@Entity
@Table(name = "addresses")
public class Address {

    // que mysql auto-incremente en id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String country_code;

    public Address() {}

    public Address(Integer id, String name, String street, String country_code) {
        this.id = id; this.name = name; this.street = street; this.country_code = country_code;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }
    public String getCountry_code() { return country_code; }
    public void setCountry_code(String country_code) { this.country_code = country_code; }
}