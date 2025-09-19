package org.example.stockms.model.store.dto;

import org.example.stockms.model.stock.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StoreResponseDto Tests")
class StoreResponseDtoTest {

    private StoreResponseDto storeResponseDto;
    private Stock testStock;

    @BeforeEach
    void setUp() {
        storeResponseDto = new StoreResponseDto();
        testStock = createStock(1L, 1L, 50, "entrada", LocalDateTime.now());
    }

    @Test
    @DisplayName("Should create StoreResponseDto with default values")
    void testDefaultValues() {
        // Given & When
        StoreResponseDto newDto = new StoreResponseDto();

        // Then
        assertThat(newDto.getId()).isNull();
        assertThat(newDto.getName()).isNull();
        assertThat(newDto.getCapacity()).isNull();
        assertThat(newDto.getCapacityTotal()).isNull();
        assertThat(newDto.getStocks()).isNull();
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void testSettersAndGetters() {
        // Given
        Integer id = 1;
        String name = "Test Store";
        Integer capacity = 75;
        Integer capacityTotal = 100;
        List<Stock> stocks = new ArrayList<>(Arrays.asList(testStock));

        // When
        storeResponseDto.setId(id);
        storeResponseDto.setName(name);
        storeResponseDto.setCapacity(capacity);
        storeResponseDto.setCapacityTotal(capacityTotal);
        storeResponseDto.setStocks(stocks);

        // Then
        assertThat(storeResponseDto.getId()).isEqualTo(id);
        assertThat(storeResponseDto.getName()).isEqualTo(name);
        assertThat(storeResponseDto.getCapacity()).isEqualTo(capacity);
        assertThat(storeResponseDto.getCapacityTotal()).isEqualTo(capacityTotal);
        assertThat(storeResponseDto.getStocks()).isEqualTo(stocks);
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testNullValues() {
        // Given & When
        storeResponseDto.setId(null);
        storeResponseDto.setName(null);
        storeResponseDto.setCapacity(null);
        storeResponseDto.setCapacityTotal(null);
        storeResponseDto.setStocks(null);

        // Then
        assertThat(storeResponseDto.getId()).isNull();
        assertThat(storeResponseDto.getName()).isNull();
        assertThat(storeResponseDto.getCapacity()).isNull();
        assertThat(storeResponseDto.getCapacityTotal()).isNull();
        assertThat(storeResponseDto.getStocks()).isNull();
    }

    @Test
    @DisplayName("Should handle different ID values correctly")
    void testIdValues() {
        // Test positive IDs
        storeResponseDto.setId(1);
        assertThat(storeResponseDto.getId()).isEqualTo(1);

        storeResponseDto.setId(Integer.MAX_VALUE);
        assertThat(storeResponseDto.getId()).isEqualTo(Integer.MAX_VALUE);

        // Test zero
        storeResponseDto.setId(0);
        assertThat(storeResponseDto.getId()).isEqualTo(0);

        // Test negative (edge case)
        storeResponseDto.setId(-1);
        assertThat(storeResponseDto.getId()).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should handle different store names correctly")
    void testStoreNames() {
        // Test normal names
        storeResponseDto.setName("Store 1");
        assertThat(storeResponseDto.getName()).isEqualTo("Store 1");

        storeResponseDto.setName("Almacén Principal");
        assertThat(storeResponseDto.getName()).isEqualTo("Almacén Principal");

        storeResponseDto.setName("Warehouse-123");
        assertThat(storeResponseDto.getName()).isEqualTo("Warehouse-123");

        // Test empty string
        storeResponseDto.setName("");
        assertThat(storeResponseDto.getName()).isEqualTo("");

        // Test long name
        String longName = "A".repeat(1000);
        storeResponseDto.setName(longName);
        assertThat(storeResponseDto.getName()).isEqualTo(longName);
    }

    @Test
    @DisplayName("Should handle different capacity values correctly")
    void testCapacityValues() {
        // Test positive capacities
        storeResponseDto.setCapacity(1);
        assertThat(storeResponseDto.getCapacity()).isEqualTo(1);

        storeResponseDto.setCapacity(100);
        assertThat(storeResponseDto.getCapacity()).isEqualTo(100);

        storeResponseDto.setCapacity(Integer.MAX_VALUE);
        assertThat(storeResponseDto.getCapacity()).isEqualTo(Integer.MAX_VALUE);

        // Test zero
        storeResponseDto.setCapacity(0);
        assertThat(storeResponseDto.getCapacity()).isEqualTo(0);

        // Test negative (edge case)
        storeResponseDto.setCapacity(-10);
        assertThat(storeResponseDto.getCapacity()).isEqualTo(-10);
    }

    @Test
    @DisplayName("Should handle different capacity total values correctly")
    void testCapacityTotalValues() {
        // Test positive capacity totals
        storeResponseDto.setCapacityTotal(1);
        assertThat(storeResponseDto.getCapacityTotal()).isEqualTo(1);

        storeResponseDto.setCapacityTotal(1000);
        assertThat(storeResponseDto.getCapacityTotal()).isEqualTo(1000);

        storeResponseDto.setCapacityTotal(Integer.MAX_VALUE);
        assertThat(storeResponseDto.getCapacityTotal()).isEqualTo(Integer.MAX_VALUE);

        // Test zero
        storeResponseDto.setCapacityTotal(0);
        assertThat(storeResponseDto.getCapacityTotal()).isEqualTo(0);

        // Test negative (edge case)
        storeResponseDto.setCapacityTotal(-50);
        assertThat(storeResponseDto.getCapacityTotal()).isEqualTo(-50);
    }

    @Test
    @DisplayName("Should handle stocks list correctly")
    void testStocksList() {
        // Test empty list
        storeResponseDto.setStocks(new ArrayList<>());
        assertThat(storeResponseDto.getStocks()).isNotNull();
        assertThat(storeResponseDto.getStocks()).isEmpty();

        // Test list with one stock
        List<Stock> singleStock = new ArrayList<>(Arrays.asList(testStock));
        storeResponseDto.setStocks(singleStock);
        assertThat(storeResponseDto.getStocks()).hasSize(1);
        assertThat(storeResponseDto.getStocks().get(0)).isEqualTo(testStock);

        // Test list with multiple stocks
        Stock stock2 = createStock(2L, 2L, 30, "salida", LocalDateTime.now());
        Stock stock3 = createStock(3L, 3L, 25, "entrada", LocalDateTime.now());
        List<Stock> multipleStocks = new ArrayList<>(Arrays.asList(testStock, stock2, stock3));
        storeResponseDto.setStocks(multipleStocks);
        assertThat(storeResponseDto.getStocks()).hasSize(3);
        assertThat(storeResponseDto.getStocks()).contains(testStock, stock2, stock3);
    }

    @Test
    @DisplayName("Should handle stock operations correctly")
    void testStockOperations() {
        // Given
        List<Stock> stocks = new ArrayList<>();
        storeResponseDto.setStocks(stocks);

        // Test adding stocks
        stocks.add(testStock);
        assertThat(storeResponseDto.getStocks()).hasSize(1);

        Stock stock2 = createStock(2L, 2L, 30, "salida", LocalDateTime.now());
        stocks.add(stock2);
        assertThat(storeResponseDto.getStocks()).hasSize(2);

        // Test removing stocks
        stocks.remove(testStock);
        assertThat(storeResponseDto.getStocks()).hasSize(1);
        assertThat(storeResponseDto.getStocks()).contains(stock2);

        // Test clearing stocks
        stocks.clear();
        assertThat(storeResponseDto.getStocks()).isEmpty();
    }

    @Test
    @DisplayName("Should create complete StoreResponseDto object correctly")
    void testCompleteObject() {
        // Given
        Integer id = 999;
        String name = "Complete Store";
        Integer capacity = 85;
        Integer capacityTotal = 200;
        Stock stock1 = createStock(1L, 1L, 50, "entrada", LocalDateTime.now());
        Stock stock2 = createStock(2L, 2L, 35, "salida", LocalDateTime.now());
        List<Stock> stocks = new ArrayList<>(Arrays.asList(stock1, stock2));

        // When
        storeResponseDto.setId(id);
        storeResponseDto.setName(name);
        storeResponseDto.setCapacity(capacity);
        storeResponseDto.setCapacityTotal(capacityTotal);
        storeResponseDto.setStocks(stocks);

        // Then
        assertThat(storeResponseDto.getId()).isEqualTo(id);
        assertThat(storeResponseDto.getName()).isEqualTo(name);
        assertThat(storeResponseDto.getCapacity()).isEqualTo(capacity);
        assertThat(storeResponseDto.getCapacityTotal()).isEqualTo(capacityTotal);
        assertThat(storeResponseDto.getStocks()).isEqualTo(stocks);
        assertThat(storeResponseDto.getStocks()).hasSize(2);
    }

    @Test
    @DisplayName("Should handle capacity and capacity total relationship correctly")
    void testCapacityRelationship() {
        // Test capacity less than capacity total
        storeResponseDto.setCapacity(50);
        storeResponseDto.setCapacityTotal(100);
        assertThat(storeResponseDto.getCapacity()).isLessThanOrEqualTo(storeResponseDto.getCapacityTotal());

        // Test capacity equal to capacity total
        storeResponseDto.setCapacity(100);
        storeResponseDto.setCapacityTotal(100);
        assertThat(storeResponseDto.getCapacity()).isEqualTo(storeResponseDto.getCapacityTotal());

        // Test capacity greater than capacity total (edge case)
        storeResponseDto.setCapacity(150);
        storeResponseDto.setCapacityTotal(100);
        assertThat(storeResponseDto.getCapacity()).isGreaterThan(storeResponseDto.getCapacityTotal());
    }

    @Test
    @DisplayName("Should handle edge cases correctly")
    void testEdgeCases() {
        // Test with maximum values
        storeResponseDto.setId(Integer.MAX_VALUE);
        storeResponseDto.setName("Max Store");
        storeResponseDto.setCapacity(Integer.MAX_VALUE);
        storeResponseDto.setCapacityTotal(Integer.MAX_VALUE);

        assertThat(storeResponseDto.getId()).isEqualTo(Integer.MAX_VALUE);
        assertThat(storeResponseDto.getName()).isEqualTo("Max Store");
        assertThat(storeResponseDto.getCapacity()).isEqualTo(Integer.MAX_VALUE);
        assertThat(storeResponseDto.getCapacityTotal()).isEqualTo(Integer.MAX_VALUE);

        // Test with minimum values
        storeResponseDto.setId(Integer.MIN_VALUE);
        storeResponseDto.setCapacity(Integer.MIN_VALUE);
        storeResponseDto.setCapacityTotal(Integer.MIN_VALUE);

        assertThat(storeResponseDto.getId()).isEqualTo(Integer.MIN_VALUE);
        assertThat(storeResponseDto.getCapacity()).isEqualTo(Integer.MIN_VALUE);
        assertThat(storeResponseDto.getCapacityTotal()).isEqualTo(Integer.MIN_VALUE);
    }

    @Test
    @DisplayName("Should handle toString method correctly")
    void testToString() {
        // Given
        storeResponseDto.setId(1);
        storeResponseDto.setName("Test Store");
        storeResponseDto.setCapacity(50);
        storeResponseDto.setCapacityTotal(100);

        // When
        String toString = storeResponseDto.toString();

        // Then
        assertThat(toString).isNotNull();
        assertThat(toString).contains("StoreResponseDto");
        // Note: DTOs use default Object.toString() implementation, so we only verify it's not null and contains class name
    }

    @Test
    @DisplayName("Should handle validation scenarios correctly")
    void testValidationScenarios() {
        // Test valid positive values
        storeResponseDto.setId(1);
        storeResponseDto.setName("Valid Store");
        storeResponseDto.setCapacity(1);
        storeResponseDto.setCapacityTotal(1);

        assertThat(storeResponseDto.getId()).isPositive();
        assertThat(storeResponseDto.getName()).isNotBlank();
        assertThat(storeResponseDto.getCapacity()).isPositive();
        assertThat(storeResponseDto.getCapacityTotal()).isPositive();

        // Test boundary values
        storeResponseDto.setCapacity(0);
        storeResponseDto.setCapacityTotal(0);
        assertThat(storeResponseDto.getCapacity()).isEqualTo(0);
        assertThat(storeResponseDto.getCapacityTotal()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should maintain object state correctly")
    void testObjectState() {
        // Set initial values
        storeResponseDto.setId(100);
        storeResponseDto.setName("Initial Store");
        storeResponseDto.setCapacity(50);
        storeResponseDto.setCapacityTotal(100);
        storeResponseDto.setStocks(new ArrayList<>(Arrays.asList(testStock)));

        // Verify state
        assertThat(storeResponseDto.getId()).isEqualTo(100);
        assertThat(storeResponseDto.getName()).isEqualTo("Initial Store");
        assertThat(storeResponseDto.getCapacity()).isEqualTo(50);
        assertThat(storeResponseDto.getCapacityTotal()).isEqualTo(100);
        assertThat(storeResponseDto.getStocks()).hasSize(1);

        // Change one property and verify others remain unchanged
        storeResponseDto.setId(200);
        assertThat(storeResponseDto.getId()).isEqualTo(200);
        assertThat(storeResponseDto.getName()).isEqualTo("Initial Store"); // Should remain unchanged
        assertThat(storeResponseDto.getCapacity()).isEqualTo(50); // Should remain unchanged
        assertThat(storeResponseDto.getCapacityTotal()).isEqualTo(100); // Should remain unchanged
        assertThat(storeResponseDto.getStocks()).hasSize(1); // Should remain unchanged
    }

    // Helper method
    private Stock createStock(Long id, Long productId, Integer quantity, String state, LocalDateTime date) {
        Stock stock = new Stock();
        stock.setId(id);
        stock.setProductId(productId);
        stock.setQuantity(quantity);
        stock.setState(state);
        stock.setDate(date);
        return stock;
    }
}
