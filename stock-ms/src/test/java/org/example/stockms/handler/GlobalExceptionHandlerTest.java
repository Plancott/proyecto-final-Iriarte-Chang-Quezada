package org.example.stockms.handler;

import org.example.stockms.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        // No setup needed for this test
    }

    @Test
    @DisplayName("Should handle StoreNotEmptyException correctly")
    void testHandleStoreNotEmpty_Success() {
        // Given
        Long storeId = 1L;
        int capacity = 50;
        StoreNotEmptyException exception = new StoreNotEmptyException(storeId, capacity);

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleStoreNotEmpty(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        
        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.get("status")).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(body.get("error")).isEqualTo("StoreNotEmpty");
        assertThat(body.get("storeId")).isEqualTo(storeId);
        assertThat(body.get("remainingCapacity")).isEqualTo(capacity);
        assertThat(body.get("timestamp")).isEqualTo(exception.getTimestamp());
        assertThat(body.get("message")).isEqualTo("No se puede eliminar el almacén porque aún contiene stock");
    }

    @Test
    @DisplayName("Should handle InvalidStockRequestException correctly")
    void testHandleInvalidStockRequest_Success() {
        // Given
        String message = "Invalid stock request data";
        InvalidStockRequestException exception = new InvalidStockRequestException(message);

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleInvalidStockRequest(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        
        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.get("status")).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(body.get("error")).isEqualTo("InvalidStockRequest");
        assertThat(body.get("timestamp")).isEqualTo(exception.getTimestamp());
        assertThat(body.get("message")).isEqualTo(message);
    }

    @Test
    @DisplayName("Should handle ProductNotFoundException correctly")
    void testHandleProductNotFound_Success() {
        // Given
        Long productId = 999L;
        ProductNotFoundException exception = new ProductNotFoundException(productId);

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleProductNotFound(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        
        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.get("status")).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(body.get("error")).isEqualTo("ProductNotFound");
        assertThat(body.get("productId")).isEqualTo(productId);
        assertThat(body.get("timestamp")).isEqualTo(exception.getTimestamp());
        assertThat(body.get("message")).isEqualTo("Producto con id " + productId + " no existe");
    }

    @Test
    @DisplayName("Should handle StockInsufficientStockException correctly")
    void testHandleInsufficientStock_Success() {
        // Given
        Long productId = 1L;
        Integer remaining = 25;
        StockInsufficientException exception = new StockInsufficientException(productId, remaining);

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleInsufficientStock(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        
        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.get("status")).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(body.get("error")).isEqualTo("StockInsufficient");
        assertThat(body.get("productId")).isEqualTo(productId);
        assertThat(body.get("remaining")).isEqualTo(remaining);
        assertThat(body.get("timestamp")).isEqualTo(exception.getTimestamp());
        assertThat(body.get("message")).isEqualTo("No hay suficiente stock del producto " + productId + ". Faltan " + remaining + " unidades.");
    }

    @Test
    @DisplayName("Should handle StoreNotEmptyException with zero capacity")
    void testHandleStoreNotEmpty_ZeroCapacity_Success() {
        // Given
        Long storeId = 2L;
        int capacity = 0;
        StoreNotEmptyException exception = new StoreNotEmptyException(storeId, capacity);

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleStoreNotEmpty(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        
        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.get("remainingCapacity")).isEqualTo(0);
        assertThat(body.get("storeId")).isEqualTo(storeId);
    }

    @Test
    @DisplayName("Should handle InvalidStockRequestException with empty message")
    void testHandleInvalidStockRequest_EmptyMessage_Success() {
        // Given
        String message = "";
        InvalidStockRequestException exception = new InvalidStockRequestException(message);

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleInvalidStockRequest(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        
        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.get("message")).isEqualTo("");
    }

    @Test
    @DisplayName("Should handle ProductNotFoundException with zero product ID")
    void testHandleProductNotFound_ZeroProductId_Success() {
        // Given
        Long productId = 0L;
        ProductNotFoundException exception = new ProductNotFoundException(productId);

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleProductNotFound(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        
        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.get("productId")).isEqualTo(0L);
    }

    @Test
    @DisplayName("Should handle StockInsufficientStockException with zero remaining")
    void testHandleInsufficientStock_ZeroRemaining_Success() {
        // Given
        Long productId = 5L;
        Integer remaining = 0;
        StockInsufficientException exception = new StockInsufficientException(productId, remaining);

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleInsufficientStock(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        
        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.get("remaining")).isEqualTo(0);
    }

    @Test
    @DisplayName("Should handle StoreNotEmptyException with negative capacity")
    void testHandleStoreNotEmpty_NegativeCapacity_Success() {
        // Given
        Long storeId = 3L;
        int capacity = -10;
        StoreNotEmptyException exception = new StoreNotEmptyException(storeId, capacity);

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleStoreNotEmpty(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        
        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.get("remainingCapacity")).isEqualTo(-10);
    }

    @Test
    @DisplayName("Should handle StockInsufficientStockException with negative remaining")
    void testHandleInsufficientStock_NegativeRemaining_Success() {
        // Given
        Long productId = 7L;
        Integer remaining = -5;
        StockInsufficientException exception = new StockInsufficientException(productId, remaining);

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleInsufficientStock(exception);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        
        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.get("remaining")).isEqualTo(-5);
    }

    @Test
    @DisplayName("Should verify timestamp is set correctly for all exceptions")
    void testTimestampHandling_Success() {
        // Given
        LocalDateTime beforeTest = LocalDateTime.now();
        
        StoreNotEmptyException storeException = new StoreNotEmptyException(1L, 50);
        InvalidStockRequestException invalidException = new InvalidStockRequestException("Test message");
        ProductNotFoundException productException = new ProductNotFoundException(123L);
        StockInsufficientException stockException = new StockInsufficientException(1L, 10);
        
        LocalDateTime afterTest = LocalDateTime.now();

        // When
        ResponseEntity<Map<String, Object>> storeResponse = globalExceptionHandler.handleStoreNotEmpty(storeException);
        ResponseEntity<Map<String, Object>> invalidResponse = globalExceptionHandler.handleInvalidStockRequest(invalidException);
        ResponseEntity<Map<String, Object>> productResponse = globalExceptionHandler.handleProductNotFound(productException);
        ResponseEntity<Map<String, Object>> stockResponse = globalExceptionHandler.handleInsufficientStock(stockException);

        // Then
        LocalDateTime storeTimestamp = (LocalDateTime) storeResponse.getBody().get("timestamp");
        assertThat(storeTimestamp).isBetween(beforeTest, afterTest);
                
        LocalDateTime invalidTimestamp = (LocalDateTime) invalidResponse.getBody().get("timestamp");
        assertThat(invalidTimestamp).isBetween(beforeTest, afterTest);
                
        LocalDateTime productTimestamp = (LocalDateTime) productResponse.getBody().get("timestamp");
        assertThat(productTimestamp).isBetween(beforeTest, afterTest);
                
        LocalDateTime stockTimestamp = (LocalDateTime) stockResponse.getBody().get("timestamp");
        assertThat(stockTimestamp).isBetween(beforeTest, afterTest);
    }

    @Test
    @DisplayName("Should verify response structure for all exception handlers")
    void testResponseStructure_Success() {
        // Given
        StoreNotEmptyException storeException = new StoreNotEmptyException(1L, 50);
        InvalidStockRequestException invalidException = new InvalidStockRequestException("Test");
        ProductNotFoundException productException = new ProductNotFoundException(123L);
        StockInsufficientException stockException = new StockInsufficientException(1L, 10);

        // When
        ResponseEntity<Map<String, Object>> storeResponse = globalExceptionHandler.handleStoreNotEmpty(storeException);
        ResponseEntity<Map<String, Object>> invalidResponse = globalExceptionHandler.handleInvalidStockRequest(invalidException);
        ResponseEntity<Map<String, Object>> productResponse = globalExceptionHandler.handleProductNotFound(productException);
        ResponseEntity<Map<String, Object>> stockResponse = globalExceptionHandler.handleInsufficientStock(stockException);

        // Then - Verify all responses have required fields
        verifyResponseStructure(storeResponse, new String[]{"status", "error", "storeId", "remainingCapacity", "timestamp", "message"});
        verifyResponseStructure(invalidResponse, new String[]{"status", "error", "timestamp", "message"});
        verifyResponseStructure(productResponse, new String[]{"status", "error", "productId", "timestamp", "message"});
        verifyResponseStructure(stockResponse, new String[]{"status", "error", "productId", "remaining", "timestamp", "message"});
    }

    private void verifyResponseStructure(ResponseEntity<Map<String, Object>> response, String[] expectedKeys) {
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        
        Map<String, Object> body = response.getBody();
        for (String key : expectedKeys) {
            assertThat(body).containsKey(key);
            assertThat(body.get(key)).isNotNull();
        }
    }
}
