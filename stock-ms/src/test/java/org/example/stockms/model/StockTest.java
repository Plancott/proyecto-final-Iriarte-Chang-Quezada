package org.example.stockms.model;

import org.example.stockms.model.store.Store;
import org.example.stockms.model.stock.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Stock Entity Tests")
class StockTest {

    private Store testStore;
    private Stock stock;

    @BeforeEach
    void setUp() {
        testStore = createStore(1L, "store1", 50, 100);
        stock = new Stock();
    }

    @Test
    @DisplayName("Should create stock with default values")
    void testDefaultValues() {
        // Given & When
        Stock newStock = new Stock();

        // Then
        assertThat(newStock.getId()).isNull();
        assertThat(newStock.getQuantity()).isNull();
        assertThat(newStock.getState()).isEqualTo("entrada");
        assertThat(newStock.getProductId()).isNull();
        assertThat(newStock.getStore()).isNull();
        assertThat(newStock.getDate()).isNull();
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void testSettersAndGetters() {
        // Given
        Long id = 1L;
        Integer quantity = 50;
        String state = "salida";
        Long productId = 123L;
        LocalDateTime date = LocalDateTime.now();

        // When
        stock.setId(id);
        stock.setQuantity(quantity);
        stock.setState(state);
        stock.setProductId(productId);
        stock.setStore(testStore);
        stock.setDate(date);

        // Then
        assertThat(stock.getId()).isEqualTo(id);
        assertThat(stock.getQuantity()).isEqualTo(quantity);
        assertThat(stock.getState()).isEqualTo(state);
        assertThat(stock.getProductId()).isEqualTo(productId);
        assertThat(stock.getStore()).isEqualTo(testStore);
        assertThat(stock.getDate()).isEqualTo(date);
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testNullValues() {
        // Given & When
        stock.setId(null);
        stock.setQuantity(null);
        stock.setState(null);
        stock.setProductId(null);
        stock.setStore(null);
        stock.setDate(null);

        // Then
        assertThat(stock.getId()).isNull();
        assertThat(stock.getQuantity()).isNull();
        assertThat(stock.getState()).isNull();
        assertThat(stock.getProductId()).isNull();
        assertThat(stock.getStore()).isNull();
        assertThat(stock.getDate()).isNull();
    }

    @Test
    @DisplayName("Should handle different states correctly")
    void testDifferentStates() {
        // Given
        String entradaState = "entrada";
        String salidaState = "salida";

        // When
        stock.setState(entradaState);
        assertThat(stock.getState()).isEqualTo(entradaState);

        stock.setState(salidaState);
        assertThat(stock.getState()).isEqualTo(salidaState);
    }

    @Test
    @DisplayName("Should handle different quantity values correctly")
    void testQuantityValues() {
        // Test positive quantities
        stock.setQuantity(1);
        assertThat(stock.getQuantity()).isEqualTo(1);

        stock.setQuantity(100);
        assertThat(stock.getQuantity()).isEqualTo(100);

        stock.setQuantity(Integer.MAX_VALUE);
        assertThat(stock.getQuantity()).isEqualTo(Integer.MAX_VALUE);

        // Test zero
        stock.setQuantity(0);
        assertThat(stock.getQuantity()).isEqualTo(0);

        // Test negative (edge case)
        stock.setQuantity(-10);
        assertThat(stock.getQuantity()).isEqualTo(-10);
    }

    @Test
    @DisplayName("Should handle different product IDs correctly")
    void testProductIdValues() {
        // Test positive IDs
        stock.setProductId(1L);
        assertThat(stock.getProductId()).isEqualTo(1L);

        stock.setProductId(Long.MAX_VALUE);
        assertThat(stock.getProductId()).isEqualTo(Long.MAX_VALUE);

        // Test zero
        stock.setProductId(0L);
        assertThat(stock.getProductId()).isEqualTo(0L);

        // Test negative (edge case)
        stock.setProductId(-1L);
        assertThat(stock.getProductId()).isEqualTo(-1L);
    }

    @Test
    @DisplayName("Should handle different store associations correctly")
    void testStoreAssociation() {
        // Test with different stores
        Store store1 = createStore(1L, "store1", 50, 100);
        Store store2 = createStore(2L, "store2", 75, 150);

        stock.setStore(store1);
        assertThat(stock.getStore()).isEqualTo(store1);
        assertThat(stock.getStore().getName()).isEqualTo("store1");

        stock.setStore(store2);
        assertThat(stock.getStore()).isEqualTo(store2);
        assertThat(stock.getStore().getName()).isEqualTo("store2");

        stock.setStore(null);
        assertThat(stock.getStore()).isNull();
    }

    @Test
    @DisplayName("Should handle date values correctly")
    void testDateValues() {
        // Test with different dates
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime past = LocalDateTime.now().minusDays(1);
        LocalDateTime future = LocalDateTime.now().plusDays(1);

        stock.setDate(now);
        assertThat(stock.getDate()).isEqualTo(now);

        stock.setDate(past);
        assertThat(stock.getDate()).isEqualTo(past);

        stock.setDate(future);
        assertThat(stock.getDate()).isEqualTo(future);

        stock.setDate(null);
        assertThat(stock.getDate()).isNull();
    }

    @Test
    @DisplayName("Should create complete stock object correctly")
    void testCompleteStockObject() {
        // Given
        Long id = 999L;
        Integer quantity = 75;
        String state = "entrada";
        Long productId = 456L;
        LocalDateTime date = LocalDateTime.of(2024, 1, 15, 10, 30);

        // When
        stock.setId(id);
        stock.setQuantity(quantity);
        stock.setState(state);
        stock.setProductId(productId);
        stock.setStore(testStore);
        stock.setDate(date);

        // Then
        assertThat(stock.getId()).isEqualTo(id);
        assertThat(stock.getQuantity()).isEqualTo(quantity);
        assertThat(stock.getState()).isEqualTo(state);
        assertThat(stock.getProductId()).isEqualTo(productId);
        assertThat(stock.getStore()).isEqualTo(testStore);
        assertThat(stock.getDate()).isEqualTo(date);
    }

    @Test
    @DisplayName("Should handle toString method correctly")
    void testToString() {
        // Given
        stock.setId(1L);
        stock.setQuantity(50);
        stock.setState("entrada");
        stock.setProductId(123L);
        stock.setStore(testStore);

        // When
        String toString = stock.toString();

        // Then
        assertThat(toString).isNotNull();
        assertThat(toString).contains("Stock");
        assertThat(toString).contains("id=1");
        assertThat(toString).contains("quantity=50");
        assertThat(toString).contains("state=entrada");
        assertThat(toString).contains("productId=123");
    }

    // Helper method
    private Store createStore(Long id, String name, Integer capacity, Integer capacityTotal) {
        Store store = new Store();
        store.setId(id);
        store.setName(name);
        store.setCapacity(capacity);
        store.setCapacityTotal(capacityTotal);
        return store;
    }
}
