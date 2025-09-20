package com.microservices.exception;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(Long userId) {
        super("No cart exists for user id: " + userId);
    }
}
