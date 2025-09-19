package com.microservices.handler;

import com.microservices.exception.BrandNotFoundException;
import com.microservices.exception.CategoryNotFoundException;
import com.microservices.exception.ProductNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    @DisplayName("handleProductNotFoundException - Debería manejar ProductNotFoundException")
    void handleProductNotFoundException_ShouldHandleException() {
        // Given
        ProductNotFoundException exception = new ProductNotFoundException("Producto no encontrado con ID: 1");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleProductNotFoundException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Producto no encontrado con ID: 1");
        assertThat(response.getBody().getStatus()).isEqualTo(404);
    }

    @Test
    @DisplayName("handleBrandNotFoundException - Debería manejar BrandNotFoundException")
    void handleBrandNotFoundException_ShouldHandleException() {
        // Given
        BrandNotFoundException exception = new BrandNotFoundException("Marca no encontrada con ID: 1");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleBrandNotFoundException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Marca no encontrada con ID: 1");
        assertThat(response.getBody().getStatus()).isEqualTo(404);
    }

    @Test
    @DisplayName("handleCategoryNotFoundException - Debería manejar CategoryNotFoundException")
    void handleCategoryNotFoundException_ShouldHandleException() {
        // Given
        CategoryNotFoundException exception = new CategoryNotFoundException("Categoría no encontrada con ID: 1");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleCategoryNotFoundException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Categoría no encontrada con ID: 1");
        assertThat(response.getBody().getStatus()).isEqualTo(404);
    }

    @Test
    @DisplayName("handleGenericException - Debería manejar RuntimeException genérica")
    void handleGenericException_ShouldHandleException() {
        // Given
        RuntimeException exception = new RuntimeException("Error interno del servidor");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("An unexpected error occurred");
        assertThat(response.getBody().getStatus()).isEqualTo(500);
    }

    @Test
    @DisplayName("handleGenericException - Debería manejar Exception genérica")
    void handleGenericException_WithException_ShouldHandleException() {
        // Given
        Exception exception = new Exception("Error genérico");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("An unexpected error occurred");
        assertThat(response.getBody().getStatus()).isEqualTo(500);
    }

    @Test
    @DisplayName("ErrorResponse - Debería crear instancia correctamente")
    void errorResponse_ShouldCreateInstanceCorrectly() {
        // Given
        String message = "Test error message";
        int status = 400;
        String error = "Bad Request";
        String path = "/api/test";

        // When
        ErrorResponse errorResponse = new ErrorResponse(
            java.time.LocalDateTime.now(), 
            status, 
            error, 
            message, 
            path, 
            null
        );

        // Then
        assertThat(errorResponse.getMessage()).isEqualTo(message);
        assertThat(errorResponse.getStatus()).isEqualTo(status);
        assertThat(errorResponse.getError()).isEqualTo(error);
        assertThat(errorResponse.getPath()).isEqualTo(path);
    }
}