package org.example.stockms.exception;

public class StoreNotFoundException extends RuntimeException {
    public StoreNotFoundException(Long storeId) {
        super("Almacén con id " + storeId + " no encontrado");
    }
}
