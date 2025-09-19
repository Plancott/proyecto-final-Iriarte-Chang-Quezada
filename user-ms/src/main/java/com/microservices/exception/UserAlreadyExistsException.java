package com.microservices.exception;

import com.microservices.enums.ErrorCode;

/**
 * Excepción lanzada cuando se intenta crear un usuario que ya existe
 */
public class UserAlreadyExistsException extends RuntimeException {
    
    private final ErrorCode errorCode;
    
    public UserAlreadyExistsException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public ErrorCode getErrorCode() {
        return errorCode;
    }
    
    public static UserAlreadyExistsException byUserName(String userName) {
        return new UserAlreadyExistsException("Ya existe un usuario con el nombre de usuario: " + userName, ErrorCode.USER_ALREADY_EXISTS);
    }
    
    public static UserAlreadyExistsException byEmail(String email) {
        return new UserAlreadyExistsException("Ya existe un usuario con el email: " + email, ErrorCode.USER_ALREADY_EXISTS);
    }
}
