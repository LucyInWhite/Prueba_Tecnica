package com.example.prueba_tecnica.service;

import com.example.prueba_tecnica.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void testGetUsers_ShouldReturnThreeDefaultUsers() {
        // Sin filtros ni ordenamiento
        List<User> users = userService.getUsers(null, null);

        // Verifica que la lista inicial tenga exactamente 3 usuarios
        assertEquals(3, users.size(), "Debería haber 3 usuarios iniciales por defecto");
    }

    @Test
    void testAuthenticate_WithValidCredentials_ShouldReturnTrue() {
        // Usa los datos reales de Pedro
        boolean isAuthenticated = userService.authenticate("BBBB990101XX2", "pass2");

        // Verificamos que el login sea exitoso
        assertTrue(isAuthenticated, "El usuario deberia autenticarse correctamente");
    }

    @Test
    void testAuthenticate_WithInvalidCredentials_ShouldReturnFalse() {
        // Intentamos entrar con el tax_id correcto pero con una contraseña incorrecta
        boolean isAuthenticated = userService.authenticate("BBBB990101XXX", "contraseña_equivocada");

        // Verificamos que el sistema lo rechace
        assertFalse(isAuthenticated, "El sistema no debe autenticar con contraseñas incorrectas");
    }

    @Test
    void testDeleteUser_ShouldReduceListSize() {
        // Obtenemos la lista actual y tomar el ID del primer usuario
        List<User> users = userService.getUsers(null, null);
        UUID idToDelete = users.get(0).getId();

        // Borrar al usuario
        userService.deleteUser(idToDelete);

        // Verificamos que la lista ahora tenga 2 usuarios en lugar de 3
        List<User> remainingUsers = userService.getUsers(null, null);
        assertEquals(2, remainingUsers.size(), "La lista debería tener 2 usuarios tras borrar uno");
    }
}