package org.example.stockms.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class InvalidStockRequestException extends RuntimeException {
    private final LocalDateTime timestamp;

    public InvalidStockRequestException(String message) {
        super(message);
        this.timestamp = LocalDateTime.now();
    }

}
