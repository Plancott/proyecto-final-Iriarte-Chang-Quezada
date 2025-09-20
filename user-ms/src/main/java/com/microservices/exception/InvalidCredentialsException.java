package com.microservices.exception;

import com.microservices.enums.ErrorCode;

/**
 * Excepción lanzada cuando las credenciales de login son inválidas
 */
public class InvalidCredentialsException extends RuntimeException {
    
    private final ErrorCode errorCode;
    
    public InvalidCredentialsException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public ErrorCode getErrorCode() {
        return errorCode;
    }
    
    public static InvalidCredentialsException invalidLogin() {
        return new InvalidCredentialsException("Credenciales inválidas. Usuario o contraseña incorrectos.", ErrorCode.INVALID_CREDENTIALS);
    }
}
