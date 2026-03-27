package com.example.prueba_tecnica.controller;

import com.example.prueba_tecnica.model.LoginRequest;
import com.example.prueba_tecnica.model.User;
import com.example.prueba_tecnica.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// Rutas de la API para las operaciones sobre la entidad User.
@RestController
@RequestMapping()
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Exposición de la lista de usuarios.
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(
            @RequestParam(required = false) String sortedBy,
            @RequestParam(required = false) String filter) {
        return ResponseEntity.ok(userService.getUsers(sortedBy, filter));
    }

    // Registro de un nuevo usuario en el sistema.
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            return ResponseEntity.ok(userService.createUser(user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Modificación parcial de los datos de un usuario.
    @PatchMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody User updates) {
        try {
            return ResponseEntity.ok(userService.updateUser(id, updates));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminación de un registro de usuario.
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Punto de acceso para la autenticación de usuarios.
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        boolean authenticated = userService.authenticate(loginRequest.getTax_id(), loginRequest.getPassword());
        if (authenticated) {
            return ResponseEntity.ok("Authenticated successfully");
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}