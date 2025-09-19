package org.example.stockms.model.store.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StoreProductQuantityDto Tests")
class StoreProductQuantityDtoTest {

    private StoreProductQuantityDto storeProductQuantityDto;

    @BeforeEach
    void setUp() {
        storeProductQuantityDto = new StoreProductQuantityDto();
    }

    @Test
    @DisplayName("Should create StoreProductQuantityDto with default values")
    void testDefaultValues() {
        // Given & When
        StoreProductQuantityDto newDto = new StoreProductQuantityDto();

        // Then
        assertThat(newDto.getProductId()).isNull();
        assertThat(newDto.getCantidadTotal()).isNull();
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void testSettersAndGetters() {
        // Given
        Long productId = 123L;
        Long cantidadTotal = 50L;

        // When
        storeProductQuantityDto.setProductId(productId);
        storeProductQuantityDto.setCantidadTotal(cantidadTotal);

        // Then
        assertThat(storeProductQuantityDto.getProductId()).isEqualTo(productId);
        assertThat(storeProductQuantityDto.getCantidadTotal()).isEqualTo(cantidadTotal);
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testNullValues() {
        // Given & When
        storeProductQuantityDto.setProductId(null);
        storeProductQuantityDto.setCantidadTotal(null);

        // Then
        assertThat(storeProductQuantityDto.getProductId()).isNull();
        assertThat(storeProductQuantityDto.getCantidadTotal()).isNull();
    }

    @Test
    @DisplayName("Should handle constructor with parameters correctly")
    void testConstructorWithParameters() {
        // Given
        Long productId = 123L;
        Long cantidadTotal = 50L;

        // When
        StoreProductQuantityDto dto = new StoreProductQuantityDto(productId, cantidadTotal);

        // Then
        assertThat(dto.getProductId()).isEqualTo(productId);
        assertThat(dto.getCantidadTotal()).isEqualTo(cantidadTotal);
    }

    @Test
    @DisplayName("Should handle constructor with null parameters correctly")
    void testConstructorWithNullParameters() {
        // When
        StoreProductQuantityDto dto = new StoreProductQuantityDto(null, null);

        // Then
        assertThat(dto.getProductId()).isNull();
        assertThat(dto.getCantidadTotal()).isNull();
    }

    @Test
    @DisplayName("Should handle different product IDs correctly")
    void testProductIdValues() {
        // Test positive IDs
        storeProductQuantityDto.setProductId(1L);
        assertThat(storeProductQuantityDto.getProductId()).isEqualTo(1L);

        storeProductQuantityDto.setProductId(Long.MAX_VALUE);
        assertThat(storeProductQuantityDto.getProductId()).isEqualTo(Long.MAX_VALUE);

        // Test zero
        storeProductQuantityDto.setProductId(0L);
        assertThat(storeProductQuantityDto.getProductId()).isEqualTo(0L);

        // Test negative (edge case)
        storeProductQuantityDto.setProductId(-1L);
        assertThat(storeProductQuantityDto.getProductId()).isEqualTo(-1L);
    }

    @Test
    @DisplayName("Should handle different cantidad total values correctly")
    void testCantidadTotalValues() {
        // Test positive quantities
        storeProductQuantityDto.setCantidadTotal(1L);
        assertThat(storeProductQuantityDto.getCantidadTotal()).isEqualTo(1L);

        storeProductQuantityDto.setCantidadTotal(100L);
        assertThat(storeProductQuantityDto.getCantidadTotal()).isEqualTo(100L);

        storeProductQuantityDto.setCantidadTotal(Long.MAX_VALUE);
        assertThat(storeProductQuantityDto.getCantidadTotal()).isEqualTo(Long.MAX_VALUE);

        // Test zero
        storeProductQuantityDto.setCantidadTotal(0L);
        assertThat(storeProductQuantityDto.getCantidadTotal()).isEqualTo(0L);

        // Test negative (edge case)
        storeProductQuantityDto.setCantidadTotal(-10L);
        assertThat(storeProductQuantityDto.getCantidadTotal()).isEqualTo(-10L);
    }

    @Test
    @DisplayName("Should create complete StoreProductQuantityDto object correctly")
    void testCompleteObject() {
        // Given
        Long productId = 999L;
        Long cantidadTotal = 75L;

        // When
        storeProductQuantityDto.setProductId(productId);
        storeProductQuantityDto.setCantidadTotal(cantidadTotal);

        // Then
        assertThat(storeProductQuantityDto.getProductId()).isEqualTo(productId);
        assertThat(storeProductQuantityDto.getCantidadTotal()).isEqualTo(cantidadTotal);
    }

    @Test
    @DisplayName("Should handle edge cases correctly")
    void testEdgeCases() {
        // Test with maximum values
        storeProductQuantityDto.setProductId(Long.MAX_VALUE);
        storeProductQuantityDto.setCantidadTotal(Long.MAX_VALUE);

        assertThat(storeProductQuantityDto.getProductId()).isEqualTo(Long.MAX_VALUE);
        assertThat(storeProductQuantityDto.getCantidadTotal()).isEqualTo(Long.MAX_VALUE);

        // Test with minimum values
        storeProductQuantityDto.setProductId(Long.MIN_VALUE);
        storeProductQuantityDto.setCantidadTotal(Long.MIN_VALUE);

        assertThat(storeProductQuantityDto.getProductId()).isEqualTo(Long.MIN_VALUE);
        assertThat(storeProductQuantityDto.getCantidadTotal()).isEqualTo(Long.MIN_VALUE);
    }

    @Test
    @DisplayName("Should handle toString method correctly")
    void testToString() {
        // Given
        storeProductQuantityDto.setProductId(1L);
        storeProductQuantityDto.setCantidadTotal(50L);

        // When
        String toString = storeProductQuantityDto.toString();

        // Then
        assertThat(toString).isNotNull();
        assertThat(toString).contains("StoreProductQuantityDto");
        // Note: DTOs use default Object.toString() implementation, so we only verify it's not null and contains class name
    }

    @Test
    @DisplayName("Should handle validation scenarios correctly")
    void testValidationScenarios() {
        // Test valid positive values
        storeProductQuantityDto.setProductId(1L);
        storeProductQuantityDto.setCantidadTotal(1L);

        assertThat(storeProductQuantityDto.getProductId()).isPositive();
        assertThat(storeProductQuantityDto.getCantidadTotal()).isPositive();

        // Test boundary values
        storeProductQuantityDto.setCantidadTotal(0L);
        assertThat(storeProductQuantityDto.getCantidadTotal()).isEqualTo(0L);

        storeProductQuantityDto.setCantidadTotal(-1L);
        assertThat(storeProductQuantityDto.getCantidadTotal()).isNegative();
    }

    @Test
    @DisplayName("Should maintain object state correctly")
    void testObjectState() {
        // Set initial values
        storeProductQuantityDto.setProductId(100L);
        storeProductQuantityDto.setCantidadTotal(50L);

        // Verify state
        assertThat(storeProductQuantityDto.getProductId()).isEqualTo(100L);
        assertThat(storeProductQuantityDto.getCantidadTotal()).isEqualTo(50L);

        // Change one property and verify the other remains unchanged
        storeProductQuantityDto.setProductId(200L);
        assertThat(storeProductQuantityDto.getProductId()).isEqualTo(200L);
        assertThat(storeProductQuantityDto.getCantidadTotal()).isEqualTo(50L); // Should remain unchanged

        // Change the other property and verify the first remains unchanged
        storeProductQuantityDto.setCantidadTotal(75L);
        assertThat(storeProductQuantityDto.getProductId()).isEqualTo(200L); // Should remain unchanged
        assertThat(storeProductQuantityDto.getCantidadTotal()).isEqualTo(75L);
    }

    @Test
    @DisplayName("Should handle multiple updates correctly")
    void testMultipleUpdates() {
        // Test multiple updates of product ID
        storeProductQuantityDto.setProductId(1L);
        assertThat(storeProductQuantityDto.getProductId()).isEqualTo(1L);

        storeProductQuantityDto.setProductId(2L);
        assertThat(storeProductQuantityDto.getProductId()).isEqualTo(2L);

        storeProductQuantityDto.setProductId(3L);
        assertThat(storeProductQuantityDto.getProductId()).isEqualTo(3L);

        // Test multiple updates of cantidad total
        storeProductQuantityDto.setCantidadTotal(10L);
        assertThat(storeProductQuantityDto.getCantidadTotal()).isEqualTo(10L);

        storeProductQuantityDto.setCantidadTotal(20L);
        assertThat(storeProductQuantityDto.getCantidadTotal()).isEqualTo(20L);

        storeProductQuantityDto.setCantidadTotal(30L);
        assertThat(storeProductQuantityDto.getCantidadTotal()).isEqualTo(30L);
    }

    @Test
    @DisplayName("Should handle large quantity values correctly")
    void testLargeQuantityValues() {
        // Test with very large quantities
        storeProductQuantityDto.setCantidadTotal(1_000_000L);
        assertThat(storeProductQuantityDto.getCantidadTotal()).isEqualTo(1_000_000L);

        storeProductQuantityDto.setCantidadTotal(1_000_000_000L);
        assertThat(storeProductQuantityDto.getCantidadTotal()).isEqualTo(1_000_000_000L);

        storeProductQuantityDto.setCantidadTotal(Long.MAX_VALUE);
        assertThat(storeProductQuantityDto.getCantidadTotal()).isEqualTo(Long.MAX_VALUE);
    }
}
