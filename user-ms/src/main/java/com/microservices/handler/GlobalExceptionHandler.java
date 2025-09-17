package com.microservices.handler;

import com.microservices.dto.error.ErrorDetailDTO;
import com.microservices.enums.ErrorCode;
import com.microservices.exception.InvalidCredentialsException;
import com.microservices.exception.UserAlreadyExistsException;
import com.microservices.exception.UserNotFoundException;
import com.microservices.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manejador global de excepciones para respuestas consistentes de la API
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    /**
     * Maneja excepciones de usuario no encontrado
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException ex) {
        log.error("Usuario no encontrado: {}", ex.getMessage());
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getErrorCode().getCode());
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    /**
     * Maneja excepciones de usuario ya existente
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        log.error("Usuario ya existe: {}", ex.getMessage());
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getErrorCode().getCode());
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    
    /**
     * Maneja excepciones de credenciales inválidas
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        log.error("Credenciales inválidas: {}", ex.getMessage());
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getErrorCode().getCode());
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    
    /**
     * Maneja excepciones de validación personalizadas
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ValidationException ex) {
        log.error("Error de validación: {}", ex.getMessage());
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getErrorCode().getCode());
        response.put("message", ex.getMessage());
        response.put("details", ex.getErrorDetails());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    // Maneja excepciones de validación
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Error de validación de argumentos: {}", ex.getMessage());
        
        Map<String, Object> response = new HashMap<>();
        response.put("error", ErrorCode.VALIDATION_ERROR.getCode());
        response.put("message", "Datos de entrada inválidos");
        
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });
        
        response.put("fieldErrors", fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    /**
     * Maneja excepciones generales no controladas
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("Error interno del servidor: {}", ex.getMessage(), ex);
        Map<String, Object> response = new HashMap<>();
        response.put("error", ErrorCode.INTERNAL_ERROR.getCode());
        response.put("message", "Error interno del servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
