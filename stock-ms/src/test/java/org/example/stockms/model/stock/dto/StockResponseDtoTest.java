package org.example.stockms.model.stock.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StockResponseDto Tests")
class StockResponseDtoTest {

    private StockResponseDto stockResponseDto;

    @BeforeEach
    void setUp() {
        stockResponseDto = new StockResponseDto();
    }

    @Test
    @DisplayName("Should create StockResponseDto with default values")
    void testDefaultValues() {
        // Given & When
        StockResponseDto newDto = new StockResponseDto();

        // Then
        assertThat(newDto.getId()).isNull();
        assertThat(newDto.getProductId()).isNull();
        assertThat(newDto.getQuantity()).isNull();
        assertThat(newDto.getStoreId()).isNull();
        assertThat(newDto.getState()).isNull();
        assertThat(newDto.getDate()).isNull();
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void testSettersAndGetters() {
        // Given
        Long id = 1L;
        Long productId = 123L;
        Integer quantity = 50;
        Long storeId = 2L;
        String state = "entrada";
        LocalDateTime date = LocalDateTime.now();

        // When
        stockResponseDto.setId(id);
        stockResponseDto.setProductId(productId);
        stockResponseDto.setQuantity(quantity);
        stockResponseDto.setStoreId(storeId);
        stockResponseDto.setState(state);
        stockResponseDto.setDate(date);

        // Then
        assertThat(stockResponseDto.getId()).isEqualTo(id);
        assertThat(stockResponseDto.getProductId()).isEqualTo(productId);
        assertThat(stockResponseDto.getQuantity()).isEqualTo(quantity);
        assertThat(stockResponseDto.getStoreId()).isEqualTo(storeId);
        assertThat(stockResponseDto.getState()).isEqualTo(state);
        assertThat(stockResponseDto.getDate()).isEqualTo(date);
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testNullValues() {
        // Given & When
        stockResponseDto.setId(null);
        stockResponseDto.setProductId(null);
        stockResponseDto.setQuantity(null);
        stockResponseDto.setStoreId(null);
        stockResponseDto.setState(null);
        stockResponseDto.setDate(null);

        // Then
        assertThat(stockResponseDto.getId()).isNull();
        assertThat(stockResponseDto.getProductId()).isNull();
        assertThat(stockResponseDto.getQuantity()).isNull();
        assertThat(stockResponseDto.getStoreId()).isNull();
        assertThat(stockResponseDto.getState()).isNull();
        assertThat(stockResponseDto.getDate()).isNull();
    }

    @Test
    @DisplayName("Should handle different ID values correctly")
    void testIdValues() {
        // Test positive IDs
        stockResponseDto.setId(1L);
        assertThat(stockResponseDto.getId()).isEqualTo(1L);

        stockResponseDto.setId(Long.MAX_VALUE);
        assertThat(stockResponseDto.getId()).isEqualTo(Long.MAX_VALUE);

        // Test zero
        stockResponseDto.setId(0L);
        assertThat(stockResponseDto.getId()).isEqualTo(0L);

        // Test negative (edge case)
        stockResponseDto.setId(-1L);
        assertThat(stockResponseDto.getId()).isEqualTo(-1L);
    }

    @Test
    @DisplayName("Should handle different product IDs correctly")
    void testProductIdValues() {
        // Test positive IDs
        stockResponseDto.setProductId(1L);
        assertThat(stockResponseDto.getProductId()).isEqualTo(1L);

        stockResponseDto.setProductId(Long.MAX_VALUE);
        assertThat(stockResponseDto.getProductId()).isEqualTo(Long.MAX_VALUE);

        // Test zero
        stockResponseDto.setProductId(0L);
        assertThat(stockResponseDto.getProductId()).isEqualTo(0L);

        // Test negative (edge case)
        stockResponseDto.setProductId(-1L);
        assertThat(stockResponseDto.getProductId()).isEqualTo(-1L);
    }

    @Test
    @DisplayName("Should handle different quantity values correctly")
    void testQuantityValues() {
        // Test positive quantities
        stockResponseDto.setQuantity(1);
        assertThat(stockResponseDto.getQuantity()).isEqualTo(1);

        stockResponseDto.setQuantity(100);
        assertThat(stockResponseDto.getQuantity()).isEqualTo(100);

        stockResponseDto.setQuantity(Integer.MAX_VALUE);
        assertThat(stockResponseDto.getQuantity()).isEqualTo(Integer.MAX_VALUE);

        // Test zero
        stockResponseDto.setQuantity(0);
        assertThat(stockResponseDto.getQuantity()).isEqualTo(0);

        // Test negative (edge case)
        stockResponseDto.setQuantity(-10);
        assertThat(stockResponseDto.getQuantity()).isEqualTo(-10);
    }

    @Test
    @DisplayName("Should handle different store IDs correctly")
    void testStoreIdValues() {
        // Test positive IDs
        stockResponseDto.setStoreId(1L);
        assertThat(stockResponseDto.getStoreId()).isEqualTo(1L);

        stockResponseDto.setStoreId(Long.MAX_VALUE);
        assertThat(stockResponseDto.getStoreId()).isEqualTo(Long.MAX_VALUE);

        // Test zero
        stockResponseDto.setStoreId(0L);
        assertThat(stockResponseDto.getStoreId()).isEqualTo(0L);

        // Test negative (edge case)
        stockResponseDto.setStoreId(-1L);
        assertThat(stockResponseDto.getStoreId()).isEqualTo(-1L);
    }

    @Test
    @DisplayName("Should handle different states correctly")
    void testStateValues() {
        // Test entrada state
        stockResponseDto.setState("entrada");
        assertThat(stockResponseDto.getState()).isEqualTo("entrada");

        // Test salida state
        stockResponseDto.setState("salida");
        assertThat(stockResponseDto.getState()).isEqualTo("salida");

        // Test case sensitivity
        stockResponseDto.setState("ENTRADA");
        assertThat(stockResponseDto.getState()).isEqualTo("ENTRADA");

        stockResponseDto.setState("SALIDA");
        assertThat(stockResponseDto.getState()).isEqualTo("SALIDA");

        // Test empty string
        stockResponseDto.setState("");
        assertThat(stockResponseDto.getState()).isEqualTo("");

        // Test mixed case
        stockResponseDto.setState("EnTrAdA");
        assertThat(stockResponseDto.getState()).isEqualTo("EnTrAdA");
    }

    @Test
    @DisplayName("Should handle different date values correctly")
    void testDateValues() {
        // Test with different dates
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime past = LocalDateTime.now().minusDays(1);
        LocalDateTime future = LocalDateTime.now().plusDays(1);
        LocalDateTime specific = LocalDateTime.of(2024, 1, 15, 10, 30, 45);

        stockResponseDto.setDate(now);
        assertThat(stockResponseDto.getDate()).isEqualTo(now);

        stockResponseDto.setDate(past);
        assertThat(stockResponseDto.getDate()).isEqualTo(past);

        stockResponseDto.setDate(future);
        assertThat(stockResponseDto.getDate()).isEqualTo(future);

        stockResponseDto.setDate(specific);
        assertThat(stockResponseDto.getDate()).isEqualTo(specific);

        stockResponseDto.setDate(null);
        assertThat(stockResponseDto.getDate()).isNull();
    }

    @Test
    @DisplayName("Should create complete StockResponseDto object correctly")
    void testCompleteObject() {
        // Given
        Long id = 999L;
        Long productId = 456L;
        Integer quantity = 75;
        Long storeId = 789L;
        String state = "salida";
        LocalDateTime date = LocalDateTime.of(2024, 12, 25, 15, 45, 30);

        // When
        stockResponseDto.setId(id);
        stockResponseDto.setProductId(productId);
        stockResponseDto.setQuantity(quantity);
        stockResponseDto.setStoreId(storeId);
        stockResponseDto.setState(state);
        stockResponseDto.setDate(date);

        // Then
        assertThat(stockResponseDto.getId()).isEqualTo(id);
        assertThat(stockResponseDto.getProductId()).isEqualTo(productId);
        assertThat(stockResponseDto.getQuantity()).isEqualTo(quantity);
        assertThat(stockResponseDto.getStoreId()).isEqualTo(storeId);
        assertThat(stockResponseDto.getState()).isEqualTo(state);
        assertThat(stockResponseDto.getDate()).isEqualTo(date);
    }

    @Test
    @DisplayName("Should handle edge cases correctly")
    void testEdgeCases() {
        // Test with maximum values
        stockResponseDto.setId(Long.MAX_VALUE);
        stockResponseDto.setProductId(Long.MAX_VALUE);
        stockResponseDto.setQuantity(Integer.MAX_VALUE);
        stockResponseDto.setStoreId(Long.MAX_VALUE);
        stockResponseDto.setState("MAX_STATE");

        assertThat(stockResponseDto.getId()).isEqualTo(Long.MAX_VALUE);
        assertThat(stockResponseDto.getProductId()).isEqualTo(Long.MAX_VALUE);
        assertThat(stockResponseDto.getQuantity()).isEqualTo(Integer.MAX_VALUE);
        assertThat(stockResponseDto.getStoreId()).isEqualTo(Long.MAX_VALUE);
        assertThat(stockResponseDto.getState()).isEqualTo("MAX_STATE");

        // Test with minimum values
        stockResponseDto.setId(Long.MIN_VALUE);
        stockResponseDto.setProductId(Long.MIN_VALUE);
        stockResponseDto.setQuantity(Integer.MIN_VALUE);
        stockResponseDto.setStoreId(Long.MIN_VALUE);

        assertThat(stockResponseDto.getId()).isEqualTo(Long.MIN_VALUE);
        assertThat(stockResponseDto.getProductId()).isEqualTo(Long.MIN_VALUE);
        assertThat(stockResponseDto.getQuantity()).isEqualTo(Integer.MIN_VALUE);
        assertThat(stockResponseDto.getStoreId()).isEqualTo(Long.MIN_VALUE);
    }

    @Test
    @DisplayName("Should handle long state strings correctly")
    void testLongStateStrings() {
        // Test with long state string
        String longState = "A".repeat(1000);
        stockResponseDto.setState(longState);
        assertThat(stockResponseDto.getState()).isEqualTo(longState);

        // Test with special characters
        String specialState = "state-with_special.chars@123";
        stockResponseDto.setState(specialState);
        assertThat(stockResponseDto.getState()).isEqualTo(specialState);
    }

    @Test
    @DisplayName("Should handle date precision correctly")
    void testDatePrecision() {
        // Test with nanoseconds precision
        LocalDateTime preciseDate = LocalDateTime.of(2024, 6, 15, 14, 30, 45, 123456789);
        stockResponseDto.setDate(preciseDate);
        assertThat(stockResponseDto.getDate()).isEqualTo(preciseDate);

        // Test with zero nanoseconds
        LocalDateTime zeroNanosDate = LocalDateTime.of(2024, 6, 15, 14, 30, 45, 0);
        stockResponseDto.setDate(zeroNanosDate);
        assertThat(stockResponseDto.getDate()).isEqualTo(zeroNanosDate);
    }
}
