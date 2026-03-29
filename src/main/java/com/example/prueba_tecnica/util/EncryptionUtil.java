package com.example.prueba_tecnica.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

// Convertimos la utilidad en un componente de Spring
// Utilidad para manejar la encriptación de datos de forma centralizada.
@Component
public class EncryptionUtil {

     private static String SECRET_KEY;

    // Spring inyectará el valor del .env aquí
    @Value("${SECRET_KEY}")
    public void setSecretKey(String secretKey) {
        EncryptionUtil.SECRET_KEY = secretKey;
    }

    // Encriptación de cadenas mediante el algoritmo AES-256
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