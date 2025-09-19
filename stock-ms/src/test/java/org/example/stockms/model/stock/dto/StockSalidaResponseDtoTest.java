package org.example.stockms.model.stock.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StockSalidaResponseDto Tests")
class StockSalidaResponseDtoTest {

    private StockSalidaResponseDto stockSalidaResponseDto;

    @BeforeEach
    void setUp() {
        stockSalidaResponseDto = new StockSalidaResponseDto();
    }

    @Test
    @DisplayName("Should create StockSalidaResponseDto with default values")
    void testDefaultValues() {
        // Given & When
        StockSalidaResponseDto newDto = new StockSalidaResponseDto();

        // Then
        assertThat(newDto.getStoreId()).isNull();
        assertThat(newDto.getCantidadRetirada()).isNull();
        assertThat(newDto.getCapacidadRestante()).isNull();
        assertThat(newDto.getProductId()).isNull();
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void testSettersAndGetters() {
        // Given
        Long storeId = 1L;
        Integer cantidadRetirada = 25;
        Integer capacidadRestante = 75;
        Long productId = 123L;

        // When
        stockSalidaResponseDto.setStoreId(storeId);
        stockSalidaResponseDto.setCantidadRetirada(cantidadRetirada);
        stockSalidaResponseDto.setCapacidadRestante(capacidadRestante);
        stockSalidaResponseDto.setProductId(productId);

        // Then
        assertThat(stockSalidaResponseDto.getStoreId()).isEqualTo(storeId);
        assertThat(stockSalidaResponseDto.getCantidadRetirada()).isEqualTo(cantidadRetirada);
        assertThat(stockSalidaResponseDto.getCapacidadRestante()).isEqualTo(capacidadRestante);
        assertThat(stockSalidaResponseDto.getProductId()).isEqualTo(productId);
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testNullValues() {
        // Given & When
        stockSalidaResponseDto.setStoreId(null);
        stockSalidaResponseDto.setCantidadRetirada(null);
        stockSalidaResponseDto.setCapacidadRestante(null);
        stockSalidaResponseDto.setProductId(null);

        // Then
        assertThat(stockSalidaResponseDto.getStoreId()).isNull();
        assertThat(stockSalidaResponseDto.getCantidadRetirada()).isNull();
        assertThat(stockSalidaResponseDto.getCapacidadRestante()).isNull();
        assertThat(stockSalidaResponseDto.getProductId()).isNull();
    }

    @Test
    @DisplayName("Should handle constructor with parameters correctly")
    void testConstructorWithParameters() {
        // Given
        Long storeId = 1L;
        Integer cantidadRetirada = 25;
        Integer capacidadRestante = 75;
        Long productId = 123L;

        // When
        StockSalidaResponseDto dto = new StockSalidaResponseDto(storeId, cantidadRetirada, capacidadRestante, productId);

        // Then
        assertThat(dto.getStoreId()).isEqualTo(storeId);
        assertThat(dto.getCantidadRetirada()).isEqualTo(cantidadRetirada);
        assertThat(dto.getCapacidadRestante()).isEqualTo(capacidadRestante);
        assertThat(dto.getProductId()).isEqualTo(productId);
    }

    @Test
    @DisplayName("Should handle constructor with null parameters correctly")
    void testConstructorWithNullParameters() {
        // When
        StockSalidaResponseDto dto = new StockSalidaResponseDto(null, null, null, null);

        // Then
        assertThat(dto.getStoreId()).isNull();
        assertThat(dto.getCantidadRetirada()).isNull();
        assertThat(dto.getCapacidadRestante()).isNull();
        assertThat(dto.getProductId()).isNull();
    }

    @Test
    @DisplayName("Should handle different store IDs correctly")
    void testStoreIdValues() {
        // Test positive IDs
        stockSalidaResponseDto.setStoreId(1L);
        assertThat(stockSalidaResponseDto.getStoreId()).isEqualTo(1L);

        stockSalidaResponseDto.setStoreId(Long.MAX_VALUE);
        assertThat(stockSalidaResponseDto.getStoreId()).isEqualTo(Long.MAX_VALUE);

        // Test zero
        stockSalidaResponseDto.setStoreId(0L);
        assertThat(stockSalidaResponseDto.getStoreId()).isEqualTo(0L);

        // Test negative (edge case)
        stockSalidaResponseDto.setStoreId(-1L);
        assertThat(stockSalidaResponseDto.getStoreId()).isEqualTo(-1L);
    }

    @Test
    @DisplayName("Should handle different cantidad retirada values correctly")
    void testCantidadRetiradaValues() {
        // Test positive quantities
        stockSalidaResponseDto.setCantidadRetirada(1);
        assertThat(stockSalidaResponseDto.getCantidadRetirada()).isEqualTo(1);

        stockSalidaResponseDto.setCantidadRetirada(100);
        assertThat(stockSalidaResponseDto.getCantidadRetirada()).isEqualTo(100);

        stockSalidaResponseDto.setCantidadRetirada(Integer.MAX_VALUE);
        assertThat(stockSalidaResponseDto.getCantidadRetirada()).isEqualTo(Integer.MAX_VALUE);

        // Test zero
        stockSalidaResponseDto.setCantidadRetirada(0);
        assertThat(stockSalidaResponseDto.getCantidadRetirada()).isEqualTo(0);

        // Test negative (edge case)
        stockSalidaResponseDto.setCantidadRetirada(-10);
        assertThat(stockSalidaResponseDto.getCantidadRetirada()).isEqualTo(-10);
    }

    @Test
    @DisplayName("Should handle different capacidad restante values correctly")
    void testCapacidadRestanteValues() {
        // Test positive quantities
        stockSalidaResponseDto.setCapacidadRestante(1);
        assertThat(stockSalidaResponseDto.getCapacidadRestante()).isEqualTo(1);

        stockSalidaResponseDto.setCapacidadRestante(100);
        assertThat(stockSalidaResponseDto.getCapacidadRestante()).isEqualTo(100);

        stockSalidaResponseDto.setCapacidadRestante(Integer.MAX_VALUE);
        assertThat(stockSalidaResponseDto.getCapacidadRestante()).isEqualTo(Integer.MAX_VALUE);

        // Test zero
        stockSalidaResponseDto.setCapacidadRestante(0);
        assertThat(stockSalidaResponseDto.getCapacidadRestante()).isEqualTo(0);

        // Test negative (edge case)
        stockSalidaResponseDto.setCapacidadRestante(-10);
        assertThat(stockSalidaResponseDto.getCapacidadRestante()).isEqualTo(-10);
    }

    @Test
    @DisplayName("Should handle different product IDs correctly")
    void testProductIdValues() {
        // Test positive IDs
        stockSalidaResponseDto.setProductId(1L);
        assertThat(stockSalidaResponseDto.getProductId()).isEqualTo(1L);

        stockSalidaResponseDto.setProductId(Long.MAX_VALUE);
        assertThat(stockSalidaResponseDto.getProductId()).isEqualTo(Long.MAX_VALUE);

        // Test zero
        stockSalidaResponseDto.setProductId(0L);
        assertThat(stockSalidaResponseDto.getProductId()).isEqualTo(0L);

        // Test negative (edge case)
        stockSalidaResponseDto.setProductId(-1L);
        assertThat(stockSalidaResponseDto.getProductId()).isEqualTo(-1L);
    }

    @Test
    @DisplayName("Should create complete StockSalidaResponseDto object correctly")
    void testCompleteObject() {
        // Given
        Long storeId = 999L;
        Integer cantidadRetirada = 50;
        Integer capacidadRestante = 150;
        Long productId = 456L;

        // When
        stockSalidaResponseDto.setStoreId(storeId);
        stockSalidaResponseDto.setCantidadRetirada(cantidadRetirada);
        stockSalidaResponseDto.setCapacidadRestante(capacidadRestante);
        stockSalidaResponseDto.setProductId(productId);

        // Then
        assertThat(stockSalidaResponseDto.getStoreId()).isEqualTo(storeId);
        assertThat(stockSalidaResponseDto.getCantidadRetirada()).isEqualTo(cantidadRetirada);
        assertThat(stockSalidaResponseDto.getCapacidadRestante()).isEqualTo(capacidadRestante);
        assertThat(stockSalidaResponseDto.getProductId()).isEqualTo(productId);
    }

    @Test
    @DisplayName("Should handle edge cases correctly")
    void testEdgeCases() {
        // Test with maximum values
        stockSalidaResponseDto.setStoreId(Long.MAX_VALUE);
        stockSalidaResponseDto.setCantidadRetirada(Integer.MAX_VALUE);
        stockSalidaResponseDto.setCapacidadRestante(Integer.MAX_VALUE);
        stockSalidaResponseDto.setProductId(Long.MAX_VALUE);

        assertThat(stockSalidaResponseDto.getStoreId()).isEqualTo(Long.MAX_VALUE);
        assertThat(stockSalidaResponseDto.getCantidadRetirada()).isEqualTo(Integer.MAX_VALUE);
        assertThat(stockSalidaResponseDto.getCapacidadRestante()).isEqualTo(Integer.MAX_VALUE);
        assertThat(stockSalidaResponseDto.getProductId()).isEqualTo(Long.MAX_VALUE);

        // Test with minimum values
        stockSalidaResponseDto.setStoreId(Long.MIN_VALUE);
        stockSalidaResponseDto.setCantidadRetirada(Integer.MIN_VALUE);
        stockSalidaResponseDto.setCapacidadRestante(Integer.MIN_VALUE);
        stockSalidaResponseDto.setProductId(Long.MIN_VALUE);

        assertThat(stockSalidaResponseDto.getStoreId()).isEqualTo(Long.MIN_VALUE);
        assertThat(stockSalidaResponseDto.getCantidadRetirada()).isEqualTo(Integer.MIN_VALUE);
        assertThat(stockSalidaResponseDto.getCapacidadRestante()).isEqualTo(Integer.MIN_VALUE);
        assertThat(stockSalidaResponseDto.getProductId()).isEqualTo(Long.MIN_VALUE);
    }

    @Test
    @DisplayName("Should handle toString method correctly")
    void testToString() {
        // Given
        stockSalidaResponseDto.setStoreId(1L);
        stockSalidaResponseDto.setCantidadRetirada(25);
        stockSalidaResponseDto.setCapacidadRestante(75);
        stockSalidaResponseDto.setProductId(123L);

        // When
        String toString = stockSalidaResponseDto.toString();

        // Then
        assertThat(toString).isNotNull();
        assertThat(toString).contains("StockSalidaResponseDto");
        // Note: DTOs use default Object.toString() implementation, so we only verify it's not null and contains class name
    }

    @Test
    @DisplayName("Should handle validation scenarios correctly")
    void testValidationScenarios() {
        // Test valid positive values
        stockSalidaResponseDto.setStoreId(1L);
        stockSalidaResponseDto.setCantidadRetirada(1);
        stockSalidaResponseDto.setCapacidadRestante(1);
        stockSalidaResponseDto.setProductId(1L);

        assertThat(stockSalidaResponseDto.getStoreId()).isPositive();
        assertThat(stockSalidaResponseDto.getCantidadRetirada()).isPositive();
        assertThat(stockSalidaResponseDto.getCapacidadRestante()).isPositive();
        assertThat(stockSalidaResponseDto.getProductId()).isPositive();

        // Test boundary values
        stockSalidaResponseDto.setCantidadRetirada(0);
        stockSalidaResponseDto.setCapacidadRestante(0);
        assertThat(stockSalidaResponseDto.getCantidadRetirada()).isEqualTo(0);
        assertThat(stockSalidaResponseDto.getCapacidadRestante()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should maintain object state correctly")
    void testObjectState() {
        // Set initial values
        stockSalidaResponseDto.setStoreId(100L);
        stockSalidaResponseDto.setCantidadRetirada(25);
        stockSalidaResponseDto.setCapacidadRestante(75);
        stockSalidaResponseDto.setProductId(200L);

        // Verify state
        assertThat(stockSalidaResponseDto.getStoreId()).isEqualTo(100L);
        assertThat(stockSalidaResponseDto.getCantidadRetirada()).isEqualTo(25);
        assertThat(stockSalidaResponseDto.getCapacidadRestante()).isEqualTo(75);
        assertThat(stockSalidaResponseDto.getProductId()).isEqualTo(200L);

        // Change one property and verify others remain unchanged
        stockSalidaResponseDto.setStoreId(300L);
        assertThat(stockSalidaResponseDto.getStoreId()).isEqualTo(300L);
        assertThat(stockSalidaResponseDto.getCantidadRetirada()).isEqualTo(25); // Should remain unchanged
        assertThat(stockSalidaResponseDto.getCapacidadRestante()).isEqualTo(75); // Should remain unchanged
        assertThat(stockSalidaResponseDto.getProductId()).isEqualTo(200L); // Should remain unchanged
    }
}
