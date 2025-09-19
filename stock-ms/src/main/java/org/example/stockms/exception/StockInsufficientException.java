package org.example.stockms.exception;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class StockInsufficientException extends RuntimeException {
    private final Long productId;
    private final Integer remaining;
    private final LocalDateTime timestamp;

    public StockInsufficientException(Long productId, Integer remaining) {
        super("No hay suficiente stock del producto " + productId + ". Faltan " + remaining + " unidades.");
        this.productId = productId;
        this.remaining = remaining;
        this.timestamp = LocalDateTime.now();
    }
}
