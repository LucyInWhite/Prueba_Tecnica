package com.example.prueba_tecnica.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

// Utilidad para manejar la encriptación de datos de forma centralizada.
public class EncryptionUtil {

    // En la vida real, esto vivira en application.properties o variables de entorno.
    private static final String SECRET_KEY = "12345678901234567890123456789012";

    // Encriptación de cadenas mediante el algoritmo AES-256.
    public static String encryptPassword(String password) {
        // Evitamos un NullPointerException si intentan encriptar una contraseña vacía
        if (password == null) return null;

        try {
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(password.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting password");
        }
    }
}