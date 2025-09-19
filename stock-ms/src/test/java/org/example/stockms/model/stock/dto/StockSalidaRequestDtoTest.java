package org.example.stockms.model.stock.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StockSalidaRequestDto Tests")
class StockSalidaRequestDtoTest {

    private StockSalidaRequestDto stockSalidaRequestDto;

    @BeforeEach
    void setUp() {
        stockSalidaRequestDto = new StockSalidaRequestDto();
    }

    @Test
    @DisplayName("Should create StockSalidaRequestDto with default values")
    void testDefaultValues() {
        // Given & When
        StockSalidaRequestDto newDto = new StockSalidaRequestDto();

        // Then
        assertThat(newDto.getProductId()).isNull();
        assertThat(newDto.getQuantity()).isNull();
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void testSettersAndGetters() {
        // Given
        Long productId = 123L;
        Integer quantity = 50;

        // When
        stockSalidaRequestDto.setProductId(productId);
        stockSalidaRequestDto.setQuantity(quantity);

        // Then
        assertThat(stockSalidaRequestDto.getProductId()).isEqualTo(productId);
        assertThat(stockSalidaRequestDto.getQuantity()).isEqualTo(quantity);
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testNullValues() {
        // Given & When
        stockSalidaRequestDto.setProductId(null);
        stockSalidaRequestDto.setQuantity(null);

        // Then
        assertThat(stockSalidaRequestDto.getProductId()).isNull();
        assertThat(stockSalidaRequestDto.getQuantity()).isNull();
    }

    @Test
    @DisplayName("Should handle different product IDs correctly")
    void testProductIdValues() {
        // Test positive IDs
        stockSalidaRequestDto.setProductId(1L);
        assertThat(stockSalidaRequestDto.getProductId()).isEqualTo(1L);

        stockSalidaRequestDto.setProductId(Long.MAX_VALUE);
        assertThat(stockSalidaRequestDto.getProductId()).isEqualTo(Long.MAX_VALUE);

        // Test zero
        stockSalidaRequestDto.setProductId(0L);
        assertThat(stockSalidaRequestDto.getProductId()).isEqualTo(0L);

        // Test negative (edge case)
        stockSalidaRequestDto.setProductId(-1L);
        assertThat(stockSalidaRequestDto.getProductId()).isEqualTo(-1L);
    }

    @Test
    @DisplayName("Should handle different quantity values correctly")
    void testQuantityValues() {
        // Test positive quantities
        stockSalidaRequestDto.setQuantity(1);
        assertThat(stockSalidaRequestDto.getQuantity()).isEqualTo(1);

        stockSalidaRequestDto.setQuantity(100);
        assertThat(stockSalidaRequestDto.getQuantity()).isEqualTo(100);

        stockSalidaRequestDto.setQuantity(Integer.MAX_VALUE);
        assertThat(stockSalidaRequestDto.getQuantity()).isEqualTo(Integer.MAX_VALUE);

        // Test zero
        stockSalidaRequestDto.setQuantity(0);
        assertThat(stockSalidaRequestDto.getQuantity()).isEqualTo(0);

        // Test negative (edge case)
        stockSalidaRequestDto.setQuantity(-10);
        assertThat(stockSalidaRequestDto.getQuantity()).isEqualTo(-10);
    }

    @Test
    @DisplayName("Should create complete StockSalidaRequestDto object correctly")
    void testCompleteObject() {
        // Given
        Long productId = 999L;
        Integer quantity = 75;

        // When
        stockSalidaRequestDto.setProductId(productId);
        stockSalidaRequestDto.setQuantity(quantity);

        // Then
        assertThat(stockSalidaRequestDto.getProductId()).isEqualTo(productId);
        assertThat(stockSalidaRequestDto.getQuantity()).isEqualTo(quantity);
    }

    @Test
    @DisplayName("Should handle constructor with parameters correctly")
    void testConstructorWithParameters() {
        // Given
        Long productId = 123L;
        Integer quantity = 50;

        // When
        StockSalidaRequestDto dto = new StockSalidaRequestDto(productId, quantity);

        // Then
        assertThat(dto.getProductId()).isEqualTo(productId);
        assertThat(dto.getQuantity()).isEqualTo(quantity);
    }

    @Test
    @DisplayName("Should handle constructor with null parameters correctly")
    void testConstructorWithNullParameters() {
        // When
        StockSalidaRequestDto dto = new StockSalidaRequestDto(null, null);

        // Then
        assertThat(dto.getProductId()).isNull();
        assertThat(dto.getQuantity()).isNull();
    }

    @Test
    @DisplayName("Should handle toString method correctly")
    void testToString() {
        // Given
        stockSalidaRequestDto.setProductId(1L);
        stockSalidaRequestDto.setQuantity(50);

        // When
        String toString = stockSalidaRequestDto.toString();

        // Then
        assertThat(toString).isNotNull();
        assertThat(toString).contains("StockSalidaRequestDto");
        assertThat(toString).contains("productId=1");
        assertThat(toString).contains("quantity=50");
    }

    @Test
    @DisplayName("Should handle edge cases correctly")
    void testEdgeCases() {
        // Test with maximum values
        stockSalidaRequestDto.setProductId(Long.MAX_VALUE);
        stockSalidaRequestDto.setQuantity(Integer.MAX_VALUE);

        assertThat(stockSalidaRequestDto.getProductId()).isEqualTo(Long.MAX_VALUE);
        assertThat(stockSalidaRequestDto.getQuantity()).isEqualTo(Integer.MAX_VALUE);

        // Test with minimum values
        stockSalidaRequestDto.setProductId(Long.MIN_VALUE);
        stockSalidaRequestDto.setQuantity(Integer.MIN_VALUE);

        assertThat(stockSalidaRequestDto.getProductId()).isEqualTo(Long.MIN_VALUE);
        assertThat(stockSalidaRequestDto.getQuantity()).isEqualTo(Integer.MIN_VALUE);
    }

    @Test
    @DisplayName("Should handle validation scenarios correctly")
    void testValidationScenarios() {
        // Test valid positive values
        stockSalidaRequestDto.setProductId(1L);
        stockSalidaRequestDto.setQuantity(1);

        assertThat(stockSalidaRequestDto.getProductId()).isPositive();
        assertThat(stockSalidaRequestDto.getQuantity()).isPositive();

        // Test boundary values
        stockSalidaRequestDto.setQuantity(0);
        assertThat(stockSalidaRequestDto.getQuantity()).isEqualTo(0);

        stockSalidaRequestDto.setQuantity(-1);
        assertThat(stockSalidaRequestDto.getQuantity()).isNegative();
    }

    @Test
    @DisplayName("Should handle multiple updates correctly")
    void testMultipleUpdates() {
        // Test multiple updates of the same property
        stockSalidaRequestDto.setProductId(1L);
        assertThat(stockSalidaRequestDto.getProductId()).isEqualTo(1L);

        stockSalidaRequestDto.setProductId(2L);
        assertThat(stockSalidaRequestDto.getProductId()).isEqualTo(2L);

        stockSalidaRequestDto.setProductId(3L);
        assertThat(stockSalidaRequestDto.getProductId()).isEqualTo(3L);

        // Test multiple updates of quantity
        stockSalidaRequestDto.setQuantity(10);
        assertThat(stockSalidaRequestDto.getQuantity()).isEqualTo(10);

        stockSalidaRequestDto.setQuantity(20);
        assertThat(stockSalidaRequestDto.getQuantity()).isEqualTo(20);

        stockSalidaRequestDto.setQuantity(30);
        assertThat(stockSalidaRequestDto.getQuantity()).isEqualTo(30);
    }

    @Test
    @DisplayName("Should maintain object state correctly")
    void testObjectState() {
        // Set initial values
        stockSalidaRequestDto.setProductId(100L);
        stockSalidaRequestDto.setQuantity(25);

        // Verify state
        assertThat(stockSalidaRequestDto.getProductId()).isEqualTo(100L);
        assertThat(stockSalidaRequestDto.getQuantity()).isEqualTo(25);

        // Change one property and verify the other remains unchanged
        stockSalidaRequestDto.setProductId(200L);
        assertThat(stockSalidaRequestDto.getProductId()).isEqualTo(200L);
        assertThat(stockSalidaRequestDto.getQuantity()).isEqualTo(25); // Should remain unchanged

        // Change the other property and verify the first remains unchanged
        stockSalidaRequestDto.setQuantity(35);
        assertThat(stockSalidaRequestDto.getProductId()).isEqualTo(200L); // Should remain unchanged
        assertThat(stockSalidaRequestDto.getQuantity()).isEqualTo(35);
    }
}
