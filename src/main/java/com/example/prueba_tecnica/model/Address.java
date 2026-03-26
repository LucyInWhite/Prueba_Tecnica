package com.example.prueba_tecnica.model;

// Representación de la dirección física del usuario.
public class Address {
    private Integer id;
    private String name;
    private String street;
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