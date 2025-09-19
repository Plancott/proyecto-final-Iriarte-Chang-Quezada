package com.microservices.exception;

import com.microservices.enums.ErrorCode;

/**
 * Excepción lanzada cuando no se encuentra un usuario
 */
public class UserNotFoundException extends RuntimeException {
    
    private final ErrorCode errorCode;
    
    public UserNotFoundException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public ErrorCode getErrorCode() {
        return errorCode;
    }
    
    public static UserNotFoundException byId(Long id) {
        return new UserNotFoundException("Usuario no encontrado con ID: " + id, ErrorCode.USER_NOT_FOUND);
    }
}
