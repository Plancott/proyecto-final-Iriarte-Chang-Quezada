package com.microservices.exception;

public class BrandAlreadyExistsException extends RuntimeException {
    
    public BrandAlreadyExistsException(String message) {
        super(message);
    }
    
    public BrandAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
