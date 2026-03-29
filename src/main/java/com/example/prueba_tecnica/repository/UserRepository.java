package com.example.prueba_tecnica.repository;

import com.example.prueba_tecnica.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    // Java-springboot crea automaticamente todos los comandos sql
}