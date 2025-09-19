package com.microservices.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum para códigos de error del microservicio de usuarios
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    
    USER_NOT_FOUND("USER_001", "Usuario no encontrado"),
    USER_ALREADY_EXISTS("USER_002", "Usuario ya existe"),
    INVALID_CREDENTIALS("USER_003", "Credenciales inválidas"),
    VALIDATION_ERROR("VAL_001", "Error de validación"),
    DATABASE_ERROR("DB_001", "Error de base de datos"),
    INTERNAL_ERROR("SYS_001", "Error interno del servidor");
    
    private final String code; // Código único del error
    private final String message; // Mensaje del error
}
