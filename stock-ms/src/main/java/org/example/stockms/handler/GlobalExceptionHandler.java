package org.example.stockms.handler;

import org.example.stockms.exception.InvalidStockRequestException;
import org.example.stockms.exception.ProductNotFoundException;
import org.example.stockms.exception.StockInsufficientStockException;
import org.example.stockms.exception.StoreNotEmptyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(StoreNotEmptyException.class)
    public ResponseEntity<Map<String, Object>> handleStoreNotEmpty(StoreNotEmptyException ex) {
        Map<String, Object> body = Map.of(
                "status", HttpStatus.CONFLICT.value(),
                "error", "StoreNotEmpty",
                "storeId", ex.getStoreId(),
                "remainingCapacity", ex.getCapacity(),
                "timestamp", ex.getTimestamp(),
                "message", "No se puede eliminar el almacén porque aún contiene stock"
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(InvalidStockRequestException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidStockRequest(InvalidStockRequestException ex) {
        Map<String, Object> body = Map.of(
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "InvalidStockRequest",
                "timestamp", ex.getTimestamp(),
                "message", ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleProductNotFound(ProductNotFoundException ex) {
        Map<String, Object> body = Map.of(
                "status", HttpStatus.NOT_FOUND.value(),
                "error", "ProductNotFound",
                "productId", ex.getProductId(),
                "timestamp", ex.getTimestamp(),
                "message", ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(StockInsufficientStockException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientStock(StockInsufficientStockException ex) {
        Map<String, Object> body = Map.of(
                "status", HttpStatus.CONFLICT.value(),
                "error", "StockInsufficient",
                "productId", ex.getProductId(),
                "remaining", ex.getRemaining(),
                "timestamp", ex.getTimestamp(),
                "message", ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
}
