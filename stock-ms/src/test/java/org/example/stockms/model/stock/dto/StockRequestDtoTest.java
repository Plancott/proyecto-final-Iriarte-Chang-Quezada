package org.example.stockms.model.stock.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StockRequestDto Tests")
class StockRequestDtoTest {

    private StockRequestDto stockRequestDto;

    @BeforeEach
    void setUp() {
        stockRequestDto = new StockRequestDto();
    }

    @Test
    @DisplayName("Should create StockRequestDto with default values")
    void testDefaultValues() {
        // Given & When
        StockRequestDto newDto = new StockRequestDto();

        // Then
        assertThat(newDto.getProductId()).isNull();
        assertThat(newDto.getQuantity()).isNull();
        assertThat(newDto.getStoreId()).isNull();
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void testSettersAndGetters() {
        // Given
        Long productId = 123L;
        Integer quantity = 50;
        Long storeId = 1L;

        // When
        stockRequestDto.setProductId(productId);
        stockRequestDto.setQuantity(quantity);
        stockRequestDto.setStoreId(storeId);

        // Then
        assertThat(stockRequestDto.getProductId()).isEqualTo(productId);
        assertThat(stockRequestDto.getQuantity()).isEqualTo(quantity);
        assertThat(stockRequestDto.getStoreId()).isEqualTo(storeId);
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testNullValues() {
        // Given & When
        stockRequestDto.setProductId(null);
        stockRequestDto.setQuantity(null);
        stockRequestDto.setStoreId(null);

        // Then
        assertThat(stockRequestDto.getProductId()).isNull();
        assertThat(stockRequestDto.getQuantity()).isNull();
        assertThat(stockRequestDto.getStoreId()).isNull();
    }

    @Test
    @DisplayName("Should handle different product IDs correctly")
    void testProductIdValues() {
        // Test positive IDs
        stockRequestDto.setProductId(1L);
        assertThat(stockRequestDto.getProductId()).isEqualTo(1L);

        stockRequestDto.setProductId(Long.MAX_VALUE);
        assertThat(stockRequestDto.getProductId()).isEqualTo(Long.MAX_VALUE);

        // Test zero
        stockRequestDto.setProductId(0L);
        assertThat(stockRequestDto.getProductId()).isEqualTo(0L);

        // Test negative (edge case)
        stockRequestDto.setProductId(-1L);
        assertThat(stockRequestDto.getProductId()).isEqualTo(-1L);
    }

    @Test
    @DisplayName("Should handle different quantity values correctly")
    void testQuantityValues() {
        // Test positive quantities
        stockRequestDto.setQuantity(1);
        assertThat(stockRequestDto.getQuantity()).isEqualTo(1);

        stockRequestDto.setQuantity(100);
        assertThat(stockRequestDto.getQuantity()).isEqualTo(100);

        stockRequestDto.setQuantity(Integer.MAX_VALUE);
        assertThat(stockRequestDto.getQuantity()).isEqualTo(Integer.MAX_VALUE);

        // Test zero
        stockRequestDto.setQuantity(0);
        assertThat(stockRequestDto.getQuantity()).isEqualTo(0);

        // Test negative (edge case)
        stockRequestDto.setQuantity(-10);
        assertThat(stockRequestDto.getQuantity()).isEqualTo(-10);
    }

    @Test
    @DisplayName("Should handle different store IDs correctly")
    void testStoreIdValues() {
        // Test positive IDs
        stockRequestDto.setStoreId(1L);
        assertThat(stockRequestDto.getStoreId()).isEqualTo(1L);

        stockRequestDto.setStoreId(Long.MAX_VALUE);
        assertThat(stockRequestDto.getStoreId()).isEqualTo(Long.MAX_VALUE);

        // Test zero
        stockRequestDto.setStoreId(0L);
        assertThat(stockRequestDto.getStoreId()).isEqualTo(0L);

        // Test negative (edge case)
        stockRequestDto.setStoreId(-1L);
        assertThat(stockRequestDto.getStoreId()).isEqualTo(-1L);
    }

    @Test
    @DisplayName("Should create complete StockRequestDto object correctly")
    void testCompleteObject() {
        // Given
        Long productId = 999L;
        Integer quantity = 75;
        Long storeId = 456L;

        // When
        stockRequestDto.setProductId(productId);
        stockRequestDto.setQuantity(quantity);
        stockRequestDto.setStoreId(storeId);

        // Then
        assertThat(stockRequestDto.getProductId()).isEqualTo(productId);
        assertThat(stockRequestDto.getQuantity()).isEqualTo(quantity);
        assertThat(stockRequestDto.getStoreId()).isEqualTo(storeId);
    }

    @Test
    @DisplayName("Should handle constructor with parameters correctly")
    void testConstructorWithParameters() {
        // Given
        Long productId = 123L;
        Integer quantity = 50;
        Long storeId = 1L;

        // When
        StockRequestDto dto = new StockRequestDto(productId, quantity, storeId);

        // Then
        assertThat(dto.getProductId()).isEqualTo(productId);
        assertThat(dto.getQuantity()).isEqualTo(quantity);
        assertThat(dto.getStoreId()).isEqualTo(storeId);
    }

    @Test
    @DisplayName("Should handle constructor with null parameters correctly")
    void testConstructorWithNullParameters() {
        // When
        StockRequestDto dto = new StockRequestDto(null, null, null);

        // Then
        assertThat(dto.getProductId()).isNull();
        assertThat(dto.getQuantity()).isNull();
        assertThat(dto.getStoreId()).isNull();
    }

    @Test
    @DisplayName("Should handle toString method correctly")
    void testToString() {
        // Given
        stockRequestDto.setProductId(1L);
        stockRequestDto.setQuantity(50);
        stockRequestDto.setStoreId(2L);

        // When
        String toString = stockRequestDto.toString();

        // Then
        assertThat(toString).isNotNull();
        assertThat(toString).contains("StockRequestDto");
        assertThat(toString).contains("productId=1");
        assertThat(toString).contains("quantity=50");
        assertThat(toString).contains("storeId=2");
    }

    @Test
    @DisplayName("Should handle edge cases correctly")
    void testEdgeCases() {
        // Test with maximum values
        stockRequestDto.setProductId(Long.MAX_VALUE);
        stockRequestDto.setQuantity(Integer.MAX_VALUE);
        stockRequestDto.setStoreId(Long.MAX_VALUE);

        assertThat(stockRequestDto.getProductId()).isEqualTo(Long.MAX_VALUE);
        assertThat(stockRequestDto.getQuantity()).isEqualTo(Integer.MAX_VALUE);
        assertThat(stockRequestDto.getStoreId()).isEqualTo(Long.MAX_VALUE);

        // Test with minimum values
        stockRequestDto.setProductId(Long.MIN_VALUE);
        stockRequestDto.setQuantity(Integer.MIN_VALUE);
        stockRequestDto.setStoreId(Long.MIN_VALUE);

        assertThat(stockRequestDto.getProductId()).isEqualTo(Long.MIN_VALUE);
        assertThat(stockRequestDto.getQuantity()).isEqualTo(Integer.MIN_VALUE);
        assertThat(stockRequestDto.getStoreId()).isEqualTo(Long.MIN_VALUE);
    }

    @Test
    @DisplayName("Should handle validation scenarios correctly")
    void testValidationScenarios() {
        // Test valid positive values
        stockRequestDto.setProductId(1L);
        stockRequestDto.setQuantity(1);
        stockRequestDto.setStoreId(1L);

        assertThat(stockRequestDto.getProductId()).isPositive();
        assertThat(stockRequestDto.getQuantity()).isPositive();
        assertThat(stockRequestDto.getStoreId()).isPositive();

        // Test boundary values
        stockRequestDto.setQuantity(0);
        assertThat(stockRequestDto.getQuantity()).isEqualTo(0);

        stockRequestDto.setQuantity(-1);
        assertThat(stockRequestDto.getQuantity()).isNegative();
    }
}
