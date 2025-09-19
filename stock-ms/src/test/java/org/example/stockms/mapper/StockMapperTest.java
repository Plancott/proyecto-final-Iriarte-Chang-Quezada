package org.example.stockms.mapper;

import org.example.stockms.model.stock.Stock;
import org.example.stockms.model.stock.dto.StockRequestDto;
import org.example.stockms.model.stock.dto.StockResponseDto;
import org.example.stockms.model.store.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StockMapper Tests")
class StockMapperTest {

    @InjectMocks
    private StockMapperImpl stockMapper;

    private Store testStore;
    private Stock testStock;
    private StockRequestDto testStockRequestDto;

    @BeforeEach
    void setUp() {
        testStore = createStore(1L, "store1", 50, 100);
        testStock = createStock(1L, 1L, 50, "entrada", testStore, LocalDateTime.now());
        testStockRequestDto = createStockRequestDto(1L, 50, 1L);
    }

    @Test
    @DisplayName("Should map Stock to StockRequestDto successfully")
    void testToStockRequestDto_Success() {
        // When
        StockRequestDto result = stockMapper.toStockRequestDto(testStock);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isEqualTo(testStock.getProductId());
        assertThat(result.getQuantity()).isEqualTo(testStock.getQuantity());
        assertThat(result.getStoreId()).isNull(); // Not mapped in this direction
    }

    @Test
    @DisplayName("Should map StockRequestDto to Stock successfully")
    void testToStockEntity_Success() {
        // When
        Stock result = stockMapper.toStockEntity(testStockRequestDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isEqualTo(testStockRequestDto.getProductId());
        assertThat(result.getQuantity()).isEqualTo(testStockRequestDto.getQuantity());
        assertThat(result.getId()).isNull(); // Not set from DTO
        assertThat(result.getState()).isEqualTo("entrada"); // Default value from model
        assertThat(result.getStore()).isNull(); // Not set from DTO
        assertThat(result.getDate()).isNull(); // Not set from DTO
    }

    @Test
    @DisplayName("Should map Stock to StockResponseDto successfully")
    void testToStockResponseDto_Success() {
        // When
        StockResponseDto result = stockMapper.toStockResponseDto(testStock);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testStock.getId());
        assertThat(result.getProductId()).isEqualTo(testStock.getProductId());
        assertThat(result.getQuantity()).isEqualTo(testStock.getQuantity());
        assertThat(result.getState()).isEqualTo(testStock.getState());
        assertThat(result.getDate()).isEqualTo(testStock.getDate());
        assertThat(result.getStoreId()).isEqualTo(testStore.getId());
    }

    @Test
    @DisplayName("Should map Stock with null store to StockResponseDto")
    void testToStockResponseDto_NullStore_Success() {
        // Given
        Stock stockWithoutStore = createStock(1L, 1L, 50, "entrada", null, LocalDateTime.now());

        // When
        StockResponseDto result = stockMapper.toStockResponseDto(stockWithoutStore);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(stockWithoutStore.getId());
        assertThat(result.getProductId()).isEqualTo(stockWithoutStore.getProductId());
        assertThat(result.getQuantity()).isEqualTo(stockWithoutStore.getQuantity());
        assertThat(result.getState()).isEqualTo(stockWithoutStore.getState());
        assertThat(result.getDate()).isEqualTo(stockWithoutStore.getDate());
        assertThat(result.getStoreId()).isNull(); // Store is null
    }

    @Test
    @DisplayName("Should map list of Stocks to list of StockResponseDtos successfully")
    void testToListStockResponseDto_Success() {
        // Given
        Stock stock2 = createStock(2L, 2L, 30, "salida", testStore, LocalDateTime.now());
        List<Stock> stocks = Arrays.asList(testStock, stock2);

        // When
        List<StockResponseDto> result = stockMapper.toListStockResponseDto(stocks);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        
        StockResponseDto firstResult = result.get(0);
        assertThat(firstResult.getId()).isEqualTo(testStock.getId());
        assertThat(firstResult.getProductId()).isEqualTo(testStock.getProductId());
        assertThat(firstResult.getQuantity()).isEqualTo(testStock.getQuantity());
        assertThat(firstResult.getState()).isEqualTo(testStock.getState());
        assertThat(firstResult.getStoreId()).isEqualTo(testStore.getId());
        
        StockResponseDto secondResult = result.get(1);
        assertThat(secondResult.getId()).isEqualTo(stock2.getId());
        assertThat(secondResult.getProductId()).isEqualTo(stock2.getProductId());
        assertThat(secondResult.getQuantity()).isEqualTo(stock2.getQuantity());
        assertThat(secondResult.getState()).isEqualTo(stock2.getState());
        assertThat(secondResult.getStoreId()).isEqualTo(testStore.getId());
    }

    @Test
    @DisplayName("Should handle null Stock input")
    void testToStockRequestDto_NullInput_ReturnsNull() {
        // When
        StockRequestDto result = stockMapper.toStockRequestDto(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should handle null StockRequestDto input")
    void testToStockEntity_NullInput_ReturnsNull() {
        // When
        Stock result = stockMapper.toStockEntity(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should handle null Stock input for response DTO")
    void testToStockResponseDto_NullInput_ReturnsNull() {
        // When
        StockResponseDto result = stockMapper.toStockResponseDto(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should handle null list input")
    void testToListStockResponseDto_NullInput_ReturnsNull() {
        // When
        List<StockResponseDto> result = stockMapper.toListStockResponseDto(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should handle empty list input")
    void testToListStockResponseDto_EmptyList_Success() {
        // Given
        List<Stock> emptyStocks = Arrays.asList();

        // When
        List<StockResponseDto> result = stockMapper.toListStockResponseDto(emptyStocks);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should handle Stock with null values")
    void testToStockRequestDto_NullValues_Success() {
        // Given
        Stock stockWithNulls = new Stock();
        stockWithNulls.setId(1L);
        // productId and quantity are null

        // When
        StockRequestDto result = stockMapper.toStockRequestDto(stockWithNulls);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isNull();
        assertThat(result.getQuantity()).isNull();
    }

    @Test
    @DisplayName("Should handle StockRequestDto with null values")
    void testToStockEntity_NullValues_Success() {
        // Given
        StockRequestDto dtoWithNulls = new StockRequestDto();
        dtoWithNulls.setStoreId(1L);
        // productId and quantity are null

        // When
        Stock result = stockMapper.toStockEntity(dtoWithNulls);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isNull();
        assertThat(result.getQuantity()).isNull();
    }

    @Test
    @DisplayName("Should handle Stock with null values for response DTO")
    void testToStockResponseDto_NullValues_Success() {
        // Given
        Stock stockWithNulls = new Stock();
        stockWithNulls.setId(1L);
        // Other fields are null

        // When
        StockResponseDto result = stockMapper.toStockResponseDto(stockWithNulls);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getProductId()).isNull();
        assertThat(result.getQuantity()).isNull();
        assertThat(result.getState()).isEqualTo("entrada"); // Default value from model
        assertThat(result.getDate()).isNull();
        assertThat(result.getStoreId()).isNull(); // Store is null
    }

    @Test
    @DisplayName("Should map Stock with different states correctly")
    void testToStockResponseDto_DifferentStates_Success() {
        // Given
        Stock entradaStock = createStock(1L, 1L, 50, "entrada", testStore, LocalDateTime.now());
        Stock salidaStock = createStock(2L, 2L, 30, "salida", testStore, LocalDateTime.now());

        // When
        StockResponseDto entradaResult = stockMapper.toStockResponseDto(entradaStock);
        StockResponseDto salidaResult = stockMapper.toStockResponseDto(salidaStock);

        // Then
        assertThat(entradaResult.getState()).isEqualTo("entrada");
        assertThat(salidaResult.getState()).isEqualTo("salida");
    }

    @Test
    @DisplayName("Should map multiple stocks with different stores")
    void testToListStockResponseDto_DifferentStores_Success() {
        // Given
        Store store2 = createStore(2L, "store2", 30, 100);
        Stock stock1 = createStock(1L, 1L, 50, "entrada", testStore, LocalDateTime.now());
        Stock stock2 = createStock(2L, 2L, 30, "entrada", store2, LocalDateTime.now());
        List<Stock> stocks = Arrays.asList(stock1, stock2);

        // When
        List<StockResponseDto> result = stockMapper.toListStockResponseDto(stocks);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getStoreId()).isEqualTo(1L);
        assertThat(result.get(1).getStoreId()).isEqualTo(2L);
    }

    // Helper methods
    private Store createStore(Long id, String name, Integer capacity, Integer capacityTotal) {
        Store store = new Store();
        store.setId(id);
        store.setName(name);
        store.setCapacity(capacity);
        store.setCapacityTotal(capacityTotal);
        return store;
    }

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

    private StockRequestDto createStockRequestDto(Long productId, Integer quantity, Long storeId) {
        StockRequestDto dto = new StockRequestDto();
        dto.setProductId(productId);
        dto.setQuantity(quantity);
        dto.setStoreId(storeId);
        return dto;
    }
}
