package org.example.stockms.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductNotFoundException extends RuntimeException {
    private final Integer productId;
    private final LocalDateTime timestamp;

    public ProductNotFoundException(Integer productId) {
        super("Producto con id " + productId + " no existe");
        this.productId = productId;
        this.timestamp = LocalDateTime.now();
    }

}
