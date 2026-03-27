package com.example.prueba_tecnica.service;

import com.example.prueba_tecnica.model.Address;
import com.example.prueba_tecnica.model.User;
import com.example.prueba_tecnica.util.EncryptionUtil;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

// Lógica principal para la gestión y validación de usuarios.
@Service
public class UserService {

    private final List<User> users = new CopyOnWriteArrayList<>();

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");


    public UserService() {
        // Usuario 1
        users.add(new User(
                UUID.randomUUID(),
                "user1@mail.com",
                "user1",
                "+1 55 555 555 55",
                "7c4a8d09ca3762af61e59520943dc26494f8941b",
                "AARR990101XXX",
                "01-01-2026 00:00:00",
                List.of(
                        new Address(1, "workaddress", "street No. 1", "UK"),
                        new Address(2, "homeaddress", "street No. 2", "AU")
                )
        ));

        // Usuario 2
        users.add(new User(
                UUID.randomUUID(),
                "user2@outlook.com",
                "Pedro",
                "+1 57 123 456 78",
                EncryptionUtil.encryptPassword("pass2"),
                "BBBB990101XX2",
                getMadagascarTime(),
                List.of(
                        new Address(3, "workaddress", "street No. 3", "MX"),
                        new Address(4, "homeaddress", "street No. 4", "AU")
                )
        ));

        // Usuario 3:
        users.add(new User(
                UUID.randomUUID(),
                "user3@mail.com",
                "user3",
                "+52 55 987 654 32",
                EncryptionUtil.encryptPassword("pass3"),
                "CCCC990101XX3",
                "28-02-2026 00:00",
                List.of(
                        new Address(5, "workaddress", "street No. 7", "UK"),
                        new Address(6, "homeaddress", "street No. 2", "UK")
                )
        ));
    }

    // Obtención de la fecha actual en la zona horaria requerida.
    private String getMadagascarTime() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Indian/Antananarivo"));
        return now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    // Reglas de negocio para la integridad de los datos del usuario.
    private void validateUser(User user) {
        if (users.stream().anyMatch(u -> u.getTax_id().equals(user.getTax_id()) && !u.getId().equals(user.getId()))) {
            throw new IllegalArgumentException("tax_id must be unique");
        }
        if (!user.getTax_id().matches("^[A-Z&Ñ]{3,4}\\d{6}[A-Z\\d]{3}$")) {
            throw new IllegalArgumentException("Invalid RFC format for tax_id");
        }
        if (!user.getPhone().matches("^\\+\\d{1,3}\\s\\d{2}\\s\\d{3}\\s\\d{3}\\s\\d{2}$") && !user.getPhone().replaceAll("\\D", "").matches("^\\d{10,}$")) {
            throw new IllegalArgumentException("Phone must pass formatting validations");
        }
    }

    // Retorno de usuarios con soporte para filtrado y ordenamiento dinámico.
    public List<User> getUsers(String sortedBy, String filter) {
        List<User> result = new ArrayList<>(users);

        if (filter != null && !filter.isEmpty()) {
            String[] parts = filter.split(" ");
            if (parts.length == 3) {
                String attribute = parts[0];
                String operator = parts[1];
                String value = parts[2];

                result = result.stream().filter(u -> matchFilter(u, attribute, operator, value)).collect(Collectors.toList());
            }
        }

        if (sortedBy != null && !sortedBy.isEmpty()) {
            result.sort((u1, u2) -> {
                switch (sortedBy) {
                    case "email": return u1.getEmail().compareTo(u2.getEmail());
                    case "id": return u1.getId().compareTo(u2.getId());
                    case "name": return u1.getName().compareTo(u2.getName());
                    case "phone": return u1.getPhone().compareTo(u2.getPhone());
                    case "tax_id": return u1.getTax_id().compareTo(u2.getTax_id());
                    case "created_at":
                        // Conversión de texto a fecha real para ordenamiento cronologico.
                        return java.time.LocalDateTime.parse(u1.getCreated_at(), DATE_FORMATTER)
                                .compareTo(java.time.LocalDateTime.parse(u2.getCreated_at(), DATE_FORMATTER));
                    default: return 0;
                }
            });
        }
        return result;
    }

    // Evaluación de condiciones para los filtros de busqueda.
    private boolean matchFilter(User user, String attribute, String operator, String value) {
        String fieldValue = "";
        switch (attribute) {
            case "email": fieldValue = user.getEmail(); break;
            case "id": fieldValue = user.getId().toString(); break;
            case "name": fieldValue = user.getName(); break;
            case "phone": fieldValue = user.getPhone(); break;
            case "tax_id": fieldValue = user.getTax_id(); break;
            case "created_at": fieldValue = user.getCreated_at(); break;
        }

        if (fieldValue == null) return false;

        // Ignora el codigo de país (lada) si existe y limpiamos el formato.
        if (attribute.equals("phone")) {
            // Elimina el patrón de lada (ej. "+52 ", "+1 ") al inicio de la cadena
            fieldValue = fieldValue.replaceFirst("^\\+\\d{1,3}\\s?", "");

            fieldValue = fieldValue.replaceAll("\\D", "");
            value = value.replaceAll("\\D", "");
        }

        switch (operator) {
            case "co": return fieldValue.contains(value);
            case "eq": return fieldValue.equals(value);
            case "sw": return fieldValue.startsWith(value);
            case "ew": return fieldValue.endsWith(value);
            default: return false;
        }
    }

    public User createUser(User user) {
        validateUser(user);
        user.setId(UUID.randomUUID());
        user.setPassword(EncryptionUtil.encryptPassword(user.getPassword()));
        user.setCreated_at(getMadagascarTime());
        users.add(user);
        return user;
    }

    public User updateUser(UUID id, User updates) {
        User existingUser = users.stream().filter(u -> u.getId().equals(id)).findFirst()
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (updates.getEmail() != null) existingUser.setEmail(updates.getEmail());
        if (updates.getName() != null) existingUser.setName(updates.getName());
        if (updates.getPhone() != null) existingUser.setPhone(updates.getPhone());
        if (updates.getTax_id() != null) {
            existingUser.setTax_id(updates.getTax_id());
            validateUser(existingUser);
        }
        if (updates.getPassword() != null) existingUser.setPassword(EncryptionUtil.encryptPassword(updates.getPassword()));
        if (updates.getAddresses() != null) existingUser.setAddresses(updates.getAddresses());

        return existingUser;
    }

    public void deleteUser(UUID id) {
        users.removeIf(u -> u.getId().equals(id));
    }

    // Validación de credenciales contra los registros existentes.
    public boolean authenticate(String tax_id, String rawPassword) {
        return users.stream().anyMatch(u -> u.getTax_id().equals(tax_id) && u.getPassword().equals(EncryptionUtil.encryptPassword(rawPassword)));
    }
}