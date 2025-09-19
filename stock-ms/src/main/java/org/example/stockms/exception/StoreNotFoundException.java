package org.example.stockms.exception;

import lombok.Getter;

import java.time.LocalDateTime;
//Cuando se buscar un id de un store que no existe
@Getter
public class StoreNotFoundException extends RuntimeException {
    private final Long storeId;
    private final LocalDateTime timestamp;

    public StoreNotFoundException(Long storeId) {
        super("Almacén con id " + storeId + " no encontrado");
        this.storeId = storeId;
        this.timestamp = LocalDateTime.now();
    }

}
