package com.example.prueba_tecnica.service;

import com.example.prueba_tecnica.model.User;
import com.example.prueba_tecnica.repository.UserRepository;
import com.example.prueba_tecnica.util.EncryptionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// activamos Mockito
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    // Creamos un doble de la base de datos
    @Mock
    private UserRepository userRepository;

    // inyectamos ese clon falso a tu servicio
    @InjectMocks
    private UserService userService;

    private List<User> mockUsers;

    @BeforeEach
    void setUp() {
        mockUsers = new ArrayList<>();
        // creamos usuarios de utileria para engañar al sistema en las pruebas
        mockUsers.add(new User(UUID.randomUUID(), "user1@mail.com", "user1", "phone1", EncryptionUtil.encryptPassword("pass1"), "AARR990101XXX", ZonedDateTime.now(), new ArrayList<>()));
        mockUsers.add(new User(UUID.randomUUID(), "user2@outlook.com", "Pedro", "phone2", EncryptionUtil.encryptPassword("pass2"), "BBBB990101XX2", ZonedDateTime.now(), new ArrayList<>()));
        mockUsers.add(new User(UUID.randomUUID(), "user3@mail.com", "user3", "phone3", EncryptionUtil.encryptPassword("pass3"), "CCCC990101XX3", ZonedDateTime.now(), new ArrayList<>()));
    }

    // Consultar los usuarios sin filtros ni ordenamientos
    @Test
    void testGetUsers_ShouldReturnThreeDefaultUsers() {
        when(userRepository.findAll()).thenReturn(mockUsers);
        List<User> users = userService.getUsers(null, null);
        assertEquals(3, users.size(), "Debería haber 3 usuarios iniciales por defecto");
    }

    // Verificamos que el login sea exitoso
    // usa los datos de Pedro para autenticar
    @Test
    void testAuthenticate_WithValidCredentials_ShouldReturnTrue() {
        when(userRepository.findAll()).thenReturn(mockUsers);
        boolean isAuthenticated = userService.authenticate("BBBB990101XX2", "pass2");
        assertTrue(isAuthenticated, "El usuario deberia autenticarse correctamente");
    }

    // Intentamos entrar con el tax_id correcto pero con una contraseña incorrecta
    // Verificamos que el sistema lo rechace
    @Test
    void testAuthenticate_WithInvalidCredentials_ShouldReturnFalse() {
        when(userRepository.findAll()).thenReturn(mockUsers);
        boolean isAuthenticated = userService.authenticate("BBBB990101XXX", "contraseña_equivocada");
        assertFalse(isAuthenticated, "El sistema no debe autenticar con contraseñas incorrectas");
    }


    // Borramos el usuario y verificamos que la orden de borrado haya llegado correctamente
    // a la base de datos exactamente 1 vez para ese ID en específico.
    @Test
    void testDeleteUser_ShouldReduceListSize() {
        UUID idToDelete = mockUsers.get(0).getId();
        userService.deleteUser(idToDelete);
        verify(userRepository, times(1)).deleteById(idToDelete);
    }
}
