package org.example.stockms.exception;

import lombok.Getter;

import java.time.LocalDateTime;
//Cuando se intenta eliminar un stock que tiene capacity aún
@Getter
public class StoreNotEmptyException extends RuntimeException {
    private final Long storeId;
    private final int capacity;
    private final LocalDateTime timestamp;

    public StoreNotEmptyException(Long storeId, int capacity) {
        super("El store no puede eliminarse porque aún tiene stock"); // mensaje base
        this.storeId = storeId;
        this.capacity = capacity;
        this.timestamp = LocalDateTime.now();
    }

}
