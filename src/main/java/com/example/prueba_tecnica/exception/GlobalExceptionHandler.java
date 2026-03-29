package com.example.prueba_tecnica.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

// Intercepta las excepciones de toda la aplicación y les da un formato estándar JSON.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Maneja los errores de validacion (ej. RFC invalido, telefono incorrecto)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Bad Request");
        errorResponse.put("message", ex.getMessage()); // Aqui imprimira texto de "Invalid RFC format..."
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // explica por que la aplicacion arroja este error
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> errorResponse = new HashMap<>();

        if ("User not found".equals(ex.getMessage())) {
            errorResponse.put("error", "Not Found");
            errorResponse.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        // Para cualquier otro error inesperado
        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}