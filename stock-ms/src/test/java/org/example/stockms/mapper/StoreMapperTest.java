package org.example.stockms.mapper;

import org.example.stockms.model.stock.Stock;
import org.example.stockms.model.store.Store;
import org.example.stockms.model.store.dto.StoreRequestDto;
import org.example.stockms.model.store.dto.StoreResponseDto;
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
@DisplayName("StoreMapper Tests")
class StoreMapperTest {

    @InjectMocks
    private StoreMapperImpl storeMapper;

    private Store testStore;
    private StoreRequestDto testStoreRequestDto;
    private Stock testStock;

    @BeforeEach
    void setUp() {
        testStore = createStore(1L, "store1", 50, 100);
        testStock = createStock(1L, 1L, 50, "entrada", testStore, LocalDateTime.now());
        testStore.setStocks(Arrays.asList(testStock));
        testStoreRequestDto = createStoreRequestDto("store1", 100);
    }

    @Test
    @DisplayName("Should map StoreRequestDto to Store successfully")
    void testToStore_Success() {
        // When
        Store result = storeMapper.toStore(testStoreRequestDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(testStoreRequestDto.getName());
        assertThat(result.getCapacityTotal()).isEqualTo(testStoreRequestDto.getCapacityTotal());
        assertThat(result.getId()).isNull(); // Not set from DTO
        assertThat(result.getCapacity()).isEqualTo(0); // Default value from model
        assertThat(result.getStocks()).isNotNull(); // Default empty list from model
    }

    @Test
    @DisplayName("Should map Store to StoreResponseDto successfully")
    void testToStoreResponseDto_Success() {
        // When
        StoreResponseDto result = storeMapper.toStoreResponseDto(testStore);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testStore.getId().intValue());
        assertThat(result.getName()).isEqualTo(testStore.getName());
        assertThat(result.getCapacity()).isEqualTo(testStore.getCapacity());
        assertThat(result.getCapacityTotal()).isEqualTo(testStore.getCapacityTotal());
        assertThat(result.getStocks()).isNotNull();
        assertThat(result.getStocks()).hasSize(1);
        assertThat(result.getStocks().get(0)).isEqualTo(testStock);
    }

    @Test
    @DisplayName("Should map Store with null ID to StoreResponseDto")
    void testToStoreResponseDto_NullId_Success() {
        // Given
        Store storeWithNullId = createStore(null, "store1", 50, 100);

        // When
        StoreResponseDto result = storeMapper.toStoreResponseDto(storeWithNullId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNull();
        assertThat(result.getName()).isEqualTo(storeWithNullId.getName());
        assertThat(result.getCapacity()).isEqualTo(storeWithNullId.getCapacity());
        assertThat(result.getCapacityTotal()).isEqualTo(storeWithNullId.getCapacityTotal());
    }

    @Test
    @DisplayName("Should map Store with null stocks to StoreResponseDto")
    void testToStoreResponseDto_NullStocks_Success() {
        // Given
        Store storeWithNullStocks = createStore(1L, "store1", 50, 100);
        storeWithNullStocks.setStocks(null);

        // When
        StoreResponseDto result = storeMapper.toStoreResponseDto(storeWithNullStocks);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("store1");
        assertThat(result.getCapacity()).isEqualTo(50);
        assertThat(result.getCapacityTotal()).isEqualTo(100);
        assertThat(result.getStocks()).isNull();
    }

    @Test
    @DisplayName("Should map Store with empty stocks to StoreResponseDto")
    void testToStoreResponseDto_EmptyStocks_Success() {
        // Given
        Store storeWithEmptyStocks = createStore(1L, "store1", 50, 100);
        storeWithEmptyStocks.setStocks(Arrays.asList());

        // When
        StoreResponseDto result = storeMapper.toStoreResponseDto(storeWithEmptyStocks);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("store1");
        assertThat(result.getCapacity()).isEqualTo(50);
        assertThat(result.getCapacityTotal()).isEqualTo(100);
        assertThat(result.getStocks()).isNotNull();
        assertThat(result.getStocks()).isEmpty();
    }

    @Test
    @DisplayName("Should map list of Stores to list of StoreResponseDtos successfully")
    void testToListResponseDto_Success() {
        // Given
        Store store2 = createStore(2L, "store2", 30, 100);
        Stock stock2 = createStock(2L, 2L, 30, "salida", store2, LocalDateTime.now());
        store2.setStocks(Arrays.asList(stock2));
        
        List<Store> stores = Arrays.asList(testStore, store2);

        // When
        List<StoreResponseDto> result = storeMapper.toListResponseDto(stores);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        
        StoreResponseDto firstResult = result.get(0);
        assertThat(firstResult.getId()).isEqualTo(testStore.getId().intValue());
        assertThat(firstResult.getName()).isEqualTo(testStore.getName());
        assertThat(firstResult.getCapacity()).isEqualTo(testStore.getCapacity());
        assertThat(firstResult.getCapacityTotal()).isEqualTo(testStore.getCapacityTotal());
        assertThat(firstResult.getStocks()).hasSize(1);
        
        StoreResponseDto secondResult = result.get(1);
        assertThat(secondResult.getId()).isEqualTo(store2.getId().intValue());
        assertThat(secondResult.getName()).isEqualTo(store2.getName());
        assertThat(secondResult.getCapacity()).isEqualTo(store2.getCapacity());
        assertThat(secondResult.getCapacityTotal()).isEqualTo(store2.getCapacityTotal());
        assertThat(secondResult.getStocks()).hasSize(1);
    }

    @Test
    @DisplayName("Should map list of Stores to list of StoreResponseDtos using toListStoreResponseDto")
    void testToListStoreResponseDto_Success() {
        // Given
        Store store2 = createStore(2L, "store2", 30, 100);
        List<Store> stores = Arrays.asList(testStore, store2);

        // When
        List<StoreResponseDto> result = storeMapper.toListStoreResponseDto(stores);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(testStore.getId().intValue());
        assertThat(result.get(1).getId()).isEqualTo(store2.getId().intValue());
    }

    @Test
    @DisplayName("Should handle null StoreRequestDto input")
    void testToStore_NullInput_ReturnsNull() {
        // When
        Store result = storeMapper.toStore(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should handle null Store input for response DTO")
    void testToStoreResponseDto_NullInput_ReturnsNull() {
        // When
        StoreResponseDto result = storeMapper.toStoreResponseDto(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should handle null list input for toListResponseDto")
    void testToListResponseDto_NullInput_ReturnsNull() {
        // When
        List<StoreResponseDto> result = storeMapper.toListResponseDto(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should handle null list input for toListStoreResponseDto")
    void testToListStoreResponseDto_NullInput_ReturnsNull() {
        // When
        List<StoreResponseDto> result = storeMapper.toListStoreResponseDto(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should handle empty list input for toListResponseDto")
    void testToListResponseDto_EmptyList_Success() {
        // Given
        List<Store> emptyStores = Arrays.asList();

        // When
        List<StoreResponseDto> result = storeMapper.toListResponseDto(emptyStores);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should handle empty list input for toListStoreResponseDto")
    void testToListStoreResponseDto_EmptyList_Success() {
        // Given
        List<Store> emptyStores = Arrays.asList();

        // When
        List<StoreResponseDto> result = storeMapper.toListStoreResponseDto(emptyStores);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should handle StoreRequestDto with null values")
    void testToStore_NullValues_Success() {
        // Given
        StoreRequestDto dtoWithNulls = new StoreRequestDto();
        // name and capacityTotal are null

        // When
        Store result = storeMapper.toStore(dtoWithNulls);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isNull();
        assertThat(result.getCapacityTotal()).isNull();
    }

    @Test
    @DisplayName("Should handle Store with null values for response DTO")
    void testToStoreResponseDto_NullValues_Success() {
        // Given
        Store storeWithNulls = new Store();
        storeWithNulls.setId(1L);
        // name, capacity, capacityTotal are null

        // When
        StoreResponseDto result = storeMapper.toStoreResponseDto(storeWithNulls);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getName()).isNull();
        assertThat(result.getCapacity()).isEqualTo(0); // Default value from model
        assertThat(result.getCapacityTotal()).isNull();
    }

    @Test
    @DisplayName("Should handle Store with multiple stocks")
    void testToStoreResponseDto_MultipleStocks_Success() {
        // Given
        Stock stock2 = createStock(2L, 2L, 30, "salida", testStore, LocalDateTime.now());
        testStore.setStocks(Arrays.asList(testStock, stock2));

        // When
        StoreResponseDto result = storeMapper.toStoreResponseDto(testStore);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStocks()).hasSize(2);
        assertThat(result.getStocks()).containsExactly(testStock, stock2);
    }

    @Test
    @DisplayName("Should handle different capacity values correctly")
    void testToStoreResponseDto_DifferentCapacities_Success() {
        // Given
        Store storeZeroCapacity = createStore(1L, "store1", 0, 100);
        Store storeFullCapacity = createStore(2L, "store2", 100, 100);

        // When
        StoreResponseDto zeroResult = storeMapper.toStoreResponseDto(storeZeroCapacity);
        StoreResponseDto fullResult = storeMapper.toStoreResponseDto(storeFullCapacity);

        // Then
        assertThat(zeroResult.getCapacity()).isEqualTo(0);
        assertThat(zeroResult.getCapacityTotal()).isEqualTo(100);
        assertThat(fullResult.getCapacity()).isEqualTo(100);
        assertThat(fullResult.getCapacityTotal()).isEqualTo(100);
    }

    @Test
    @DisplayName("Should handle large ID values correctly")
    void testToStoreResponseDto_LargeId_Success() {
        // Given
        Store storeWithLargeId = createStore(Long.MAX_VALUE, "store1", 50, 100);

        // When
        StoreResponseDto result = storeMapper.toStoreResponseDto(storeWithLargeId);

        // Then
        assertThat(result.getId()).isEqualTo((int) Long.MAX_VALUE); // Cast to int
    }

    // Helper methods
    private Store createStore(Long id, String name, Integer capacity, Integer capacityTotal) {
        Store store = new Store();
        store.setId(id);
        store.setName(name);
        store.setCapacity(capacity);
        store.setCapacityTotal(capacityTotal);
        store.setStocks(new java.util.ArrayList<>());
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

    private StoreRequestDto createStoreRequestDto(String name, Integer capacityTotal) {
        StoreRequestDto dto = new StoreRequestDto();
        dto.setName(name);
        dto.setCapacityTotal(capacityTotal);
        return dto;
    }
}
