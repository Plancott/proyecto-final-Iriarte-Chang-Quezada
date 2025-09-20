package org.example.stockms.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductNotFoundException extends RuntimeException {
    private final Long productId;
    private final LocalDateTime timestamp;

    public ProductNotFoundException(Long productId) {
        super("Producto con id " + productId + " no existe");
        this.productId = productId;
        this.timestamp = LocalDateTime.now();
    }

}
