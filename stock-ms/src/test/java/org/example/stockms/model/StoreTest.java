package org.example.stockms.model;

import org.example.stockms.model.stock.Stock;
import org.example.stockms.model.store.Store;
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
@DisplayName("Store Entity Tests")
class StoreTest {

    private Store store;
    private Stock testStock;

    @BeforeEach
    void setUp() {
        store = new Store();
        testStock = createStock(1L, 1L, 50, "entrada", store, LocalDateTime.now());
    }

    @Test
    @DisplayName("Should create store with default values")
    void testDefaultValues() {
        // Given & When
        Store newStore = new Store();

        // Then
        assertThat(newStore.getId()).isNull();
        assertThat(newStore.getName()).isNull();
        assertThat(newStore.getCapacity()).isEqualTo(0);
        assertThat(newStore.getCapacityTotal()).isNull();
        assertThat(newStore.getStocks()).isNotNull();
        assertThat(newStore.getStocks()).isEmpty();
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void testSettersAndGetters() {
        // Given
        Long id = 1L;
        String name = "Test Store";
        Integer capacity = 75;
        Integer capacityTotal = 100;
        List<Stock> stocks = new ArrayList<>(Arrays.asList(testStock));

        // When
        store.setId(id);
        store.setName(name);
        store.setCapacity(capacity);
        store.setCapacityTotal(capacityTotal);
        store.setStocks(stocks);

        // Then
        assertThat(store.getId()).isEqualTo(id);
        assertThat(store.getName()).isEqualTo(name);
        assertThat(store.getCapacity()).isEqualTo(capacity);
        assertThat(store.getCapacityTotal()).isEqualTo(capacityTotal);
        assertThat(store.getStocks()).isEqualTo(stocks);
        assertThat(store.getStocks()).hasSize(1);
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testNullValues() {
        // Given & When
        store.setId(null);
        store.setName(null);
        store.setCapacity(null);
        store.setCapacityTotal(null);
        store.setStocks(null);

        // Then
        assertThat(store.getId()).isNull();
        assertThat(store.getName()).isNull();
        assertThat(store.getCapacity()).isNull();
        assertThat(store.getCapacityTotal()).isNull();
        assertThat(store.getStocks()).isNull();
    }

    @Test
    @DisplayName("Should handle different capacity values correctly")
    void testCapacityValues() {
        // Test positive capacities
        store.setCapacity(1);
        assertThat(store.getCapacity()).isEqualTo(1);

        store.setCapacity(100);
        assertThat(store.getCapacity()).isEqualTo(100);

        store.setCapacity(Integer.MAX_VALUE);
        assertThat(store.getCapacity()).isEqualTo(Integer.MAX_VALUE);

        // Test zero
        store.setCapacity(0);
        assertThat(store.getCapacity()).isEqualTo(0);

        // Test negative (edge case)
        store.setCapacity(-10);
        assertThat(store.getCapacity()).isEqualTo(-10);
    }

    @Test
    @DisplayName("Should handle different capacity total values correctly")
    void testCapacityTotalValues() {
        // Test positive capacity totals
        store.setCapacityTotal(1);
        assertThat(store.getCapacityTotal()).isEqualTo(1);

        store.setCapacityTotal(1000);
        assertThat(store.getCapacityTotal()).isEqualTo(1000);

        store.setCapacityTotal(Integer.MAX_VALUE);
        assertThat(store.getCapacityTotal()).isEqualTo(Integer.MAX_VALUE);

        // Test zero
        store.setCapacityTotal(0);
        assertThat(store.getCapacityTotal()).isEqualTo(0);

        // Test negative (edge case)
        store.setCapacityTotal(-50);
        assertThat(store.getCapacityTotal()).isEqualTo(-50);
    }

    @Test
    @DisplayName("Should handle different store names correctly")
    void testStoreNames() {
        // Test normal names
        store.setName("Store 1");
        assertThat(store.getName()).isEqualTo("Store 1");

        store.setName("Almacén Principal");
        assertThat(store.getName()).isEqualTo("Almacén Principal");

        store.setName("Warehouse-123");
        assertThat(store.getName()).isEqualTo("Warehouse-123");

        // Test empty string
        store.setName("");
        assertThat(store.getName()).isEqualTo("");

        // Test long name
        String longName = "A".repeat(1000);
        store.setName(longName);
        assertThat(store.getName()).isEqualTo(longName);
    }

    @Test
    @DisplayName("Should handle stocks list correctly")
    void testStocksList() {
        // Test empty list
        store.setStocks(new ArrayList<>());
        assertThat(store.getStocks()).isNotNull();
        assertThat(store.getStocks()).isEmpty();

        // Test list with one stock
        List<Stock> singleStock = new ArrayList<>(Arrays.asList(testStock));
        store.setStocks(singleStock);
        assertThat(store.getStocks()).hasSize(1);
        assertThat(store.getStocks().get(0)).isEqualTo(testStock);

        // Test list with multiple stocks
        Stock stock2 = createStock(2L, 2L, 30, "salida", store, LocalDateTime.now());
        Stock stock3 = createStock(3L, 3L, 25, "entrada", store, LocalDateTime.now());
        List<Stock> multipleStocks = new ArrayList<>(Arrays.asList(testStock, stock2, stock3));
        store.setStocks(multipleStocks);
        assertThat(store.getStocks()).hasSize(3);
        assertThat(store.getStocks()).contains(testStock, stock2, stock3);
    }

    @Test
    @DisplayName("Should handle stock operations correctly")
    void testStockOperations() {
        // Given
        List<Stock> stocks = new ArrayList<>();
        store.setStocks(stocks);

        // Test adding stocks
        stocks.add(testStock);
        assertThat(store.getStocks()).hasSize(1);

        Stock stock2 = createStock(2L, 2L, 30, "salida", store, LocalDateTime.now());
        stocks.add(stock2);
        assertThat(store.getStocks()).hasSize(2);

        // Test removing stocks
        stocks.remove(testStock);
        assertThat(store.getStocks()).hasSize(1);
        assertThat(store.getStocks()).contains(stock2);

        // Test clearing stocks
        stocks.clear();
        assertThat(store.getStocks()).isEmpty();
    }

    @Test
    @DisplayName("Should handle different ID values correctly")
    void testIdValues() {
        // Test positive IDs
        store.setId(1L);
        assertThat(store.getId()).isEqualTo(1L);

        store.setId(Long.MAX_VALUE);
        assertThat(store.getId()).isEqualTo(Long.MAX_VALUE);

        // Test zero
        store.setId(0L);
        assertThat(store.getId()).isEqualTo(0L);

        // Test negative (edge case)
        store.setId(-1L);
        assertThat(store.getId()).isEqualTo(-1L);
    }

    @Test
    @DisplayName("Should create complete store object correctly")
    void testCompleteStoreObject() {
        // Given
        Long id = 999L;
        String name = "Complete Store";
        Integer capacity = 85;
        Integer capacityTotal = 200;
        List<Stock> stocks = new ArrayList<>(Arrays.asList(testStock));

        // When
        store.setId(id);
        store.setName(name);
        store.setCapacity(capacity);
        store.setCapacityTotal(capacityTotal);
        store.setStocks(stocks);

        // Then
        assertThat(store.getId()).isEqualTo(id);
        assertThat(store.getName()).isEqualTo(name);
        assertThat(store.getCapacity()).isEqualTo(capacity);
        assertThat(store.getCapacityTotal()).isEqualTo(capacityTotal);
        assertThat(store.getStocks()).isEqualTo(stocks);
        assertThat(store.getStocks()).hasSize(1);
    }

    @Test
    @DisplayName("Should handle capacity and capacity total relationship correctly")
    void testCapacityRelationship() {
        // Test capacity less than capacity total
        store.setCapacity(50);
        store.setCapacityTotal(100);
        assertThat(store.getCapacity()).isLessThanOrEqualTo(store.getCapacityTotal());

        // Test capacity equal to capacity total
        store.setCapacity(100);
        store.setCapacityTotal(100);
        assertThat(store.getCapacity()).isEqualTo(store.getCapacityTotal());

        // Test capacity greater than capacity total (edge case)
        store.setCapacity(150);
        store.setCapacityTotal(100);
        assertThat(store.getCapacity()).isGreaterThan(store.getCapacityTotal());
    }

    @Test
    @DisplayName("Should handle toString method correctly")
    void testToString() {
        // Given
        store.setId(1L);
        store.setName("Test Store");
        store.setCapacity(50);
        store.setCapacityTotal(100);

        // When
        String toString = store.toString();

        // Then
        assertThat(toString).isNotNull();
        assertThat(toString).contains("Store");
        assertThat(toString).contains("id=1");
        assertThat(toString).contains("name=Test Store");
        assertThat(toString).contains("capacity=50");
        assertThat(toString).contains("capacityTotal=100");
    }

    @Test
    @DisplayName("Should handle edge cases correctly")
    void testEdgeCases() {
        // Test with maximum values
        store.setId(Long.MAX_VALUE);
        store.setName("Max Store");
        store.setCapacity(Integer.MAX_VALUE);
        store.setCapacityTotal(Integer.MAX_VALUE);

        assertThat(store.getId()).isEqualTo(Long.MAX_VALUE);
        assertThat(store.getName()).isEqualTo("Max Store");
        assertThat(store.getCapacity()).isEqualTo(Integer.MAX_VALUE);
        assertThat(store.getCapacityTotal()).isEqualTo(Integer.MAX_VALUE);

        // Test with minimum values
        store.setId(Long.MIN_VALUE);
        store.setCapacity(Integer.MIN_VALUE);
        store.setCapacityTotal(Integer.MIN_VALUE);

        assertThat(store.getId()).isEqualTo(Long.MIN_VALUE);
        assertThat(store.getCapacity()).isEqualTo(Integer.MIN_VALUE);
        assertThat(store.getCapacityTotal()).isEqualTo(Integer.MIN_VALUE);
    }

    // Helper method
    private Stock createStock(Long id, Long productId, Integer quantity, String state, Store store, LocalDateTime date) {
        Stock stock = new Stock();
        stock.setId(id);
        stock.setProductId(productId);
        stock.setQuantity(quantity);
        stock.setState(state);
        stock.setStore(store);
        stock.setDate(date);
        return stock;
    }
}
