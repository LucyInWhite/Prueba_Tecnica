package com.example.prueba_tecnica.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

// Entidad principal de usuario del sistema.
public class User {
    private UUID id;
    private String email;
    private String name;
    private String phone;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String tax_id;

    // Internamente es UTC, pero se formatea a Madagascar al serializar el JSON.
    //Antananarivo es la capital de Madagascar
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm", timezone = "Indian/Antananarivo")
    private ZonedDateTime created_at;

    private List<Address> addresses;

    public User() {}
    public User(UUID id, String email, String name, String phone, String password, String tax_id, ZonedDateTime created_at, List<Address> addresses) {
        this.id = id; this.email = email; this.name = name; this.phone = phone;
        this.password = password; this.tax_id = tax_id; this.created_at = created_at; this.addresses = addresses;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getTax_id() { return tax_id; }
    public void setTax_id(String tax_id) { this.tax_id = tax_id; }
    public ZonedDateTime getCreated_at() { return created_at; }
    public void setCreated_at(ZonedDateTime created_at) { this.created_at = created_at; }
    public List<Address> getAddresses() { return addresses; }
    public void setAddresses(List<Address> addresses) { this.addresses = addresses; }
}