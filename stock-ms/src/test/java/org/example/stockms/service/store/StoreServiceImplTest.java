package org.example.stockms.service.store;

import org.example.stockms.exception.StoreNotEmptyException;
import org.example.stockms.model.stock.Stock;
import org.example.stockms.model.store.Store;
import org.example.stockms.model.store.dto.StoreCantidadProductoAlmacenDto;
import org.example.stockms.model.store.dto.StoreProductQuantityDto;
import org.example.stockms.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StoreServiceImpl Tests")
class StoreServiceImplTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreServiceImpl storeService;

    private Store testStore;
    private Stock testStock;

    @BeforeEach
    void setUp() {
        testStore = createStore(1L, "store1", 50, 100);
        testStock = createStock(1L, 1L, 50, "entrada", testStore, LocalDateTime.now());
    }

    @Test
    @DisplayName("Should save multiple stores successfully")
    void testSaveStore_MultipleStores_Success() {
        // Given
        Integer cantidad = 3;
        long existingCount = 2L;
        
        when(storeRepository.count()).thenReturn(existingCount);
        when(storeRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        List<Store> result = storeService.saveStore(cantidad);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        
        // Verify store names are correct
        assertThat(result.get(0).getName()).isEqualTo("store3");
        assertThat(result.get(1).getName()).isEqualTo("store4");
        assertThat(result.get(2).getName()).isEqualTo("store5");
        
        // Verify all stores have correct initial values
        result.forEach(store -> {
            assertThat(store.getCapacity()).isEqualTo(0);
            assertThat(store.getCapacityTotal()).isEqualTo(100);
        });
        
        verify(storeRepository).count();
        verify(storeRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("Should save single store successfully")
    void testSaveStore_SingleStore_Success() {
        // Given
        Integer cantidad = 1;
        long existingCount = 0L;
        
        when(storeRepository.count()).thenReturn(existingCount);
        when(storeRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        List<Store> result = storeService.saveStore(cantidad);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("store1");
        assertThat(result.get(0).getCapacity()).isEqualTo(0);
        assertThat(result.get(0).getCapacityTotal()).isEqualTo(100);
        
        verify(storeRepository).count();
        verify(storeRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("Should save zero stores successfully")
    void testSaveStore_ZeroStores_Success() {
        // Given
        Integer cantidad = 0;
        long existingCount = 5L;
        
        when(storeRepository.count()).thenReturn(existingCount);
        when(storeRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        List<Store> result = storeService.saveStore(cantidad);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        
        verify(storeRepository).count();
        verify(storeRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("Should find all stores successfully")
    void testFindAllStores_Success() {
        // Given
        List<Store> expectedStores = Arrays.asList(testStore);
        when(storeRepository.findAll()).thenReturn(expectedStores);

        // When
        List<Store> result = storeService.findAllStores();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testStore);
        verify(storeRepository).findAll();
    }

    @Test
    @DisplayName("Should find store by id successfully")
    void testFindStoreById_Success() {
        // Given
        Long storeId = 1L;
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(testStore));

        // When
        Store result = storeService.findStoreById(storeId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(testStore);
        verify(storeRepository).findById(storeId);
    }

    @Test
    @DisplayName("Should throw exception when store not found by id")
    void testFindStoreById_NotFound_ThrowsException() {
        // Given
        Long storeId = 999L;
        when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> storeService.findStoreById(storeId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Store not found");
    }

    @Test
    @DisplayName("Should delete store successfully when empty")
    void testDeleteStoreById_EmptyStore_Success() {
        // Given
        Long storeId = 1L;
        Store emptyStore = createStore(storeId, "store1", 0, 100);
        emptyStore.setStocks(new ArrayList<>());
        
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(emptyStore));

        // When
        storeService.deleteStoreById(storeId);

        // Then
        verify(storeRepository).findById(storeId);
        verify(storeRepository).deleteById(storeId);
    }

    @Test
    @DisplayName("Should throw exception when deleting store with stock")
    void testDeleteStoreById_StoreWithStock_ThrowsException() {
        // Given
        Long storeId = 1L;
        Store storeWithStock = createStore(storeId, "store1", 50, 100);
        storeWithStock.setStocks(Arrays.asList(testStock));
        
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(storeWithStock));

        // When & Then
        assertThatThrownBy(() -> storeService.deleteStoreById(storeId))
                .isInstanceOf(StoreNotEmptyException.class);
        
        verify(storeRepository).findById(storeId);
        verify(storeRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should throw exception when store not found for deletion")
    void testDeleteStoreById_NotFound_ThrowsException() {
        // Given
        Long storeId = 999L;
        when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> storeService.deleteStoreById(storeId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Store no encontrada");
        
        verify(storeRepository).findById(storeId);
        verify(storeRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should update store successfully")
    void testUpdateStoreById_Success() {
        // Given
        Long storeId = 1L;
        Store existingStore = createStore(storeId, "oldName", 50, 100);
        Store updateData = createStore(null, "newName", 75, 150);
        Store expectedUpdatedStore = createStore(storeId, "newName", 50, 150);
        
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(existingStore));
        when(storeRepository.save(any(Store.class))).thenReturn(expectedUpdatedStore);

        // When
        Store result = storeService.updateStoreById(storeId, updateData);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("newName");
        assertThat(result.getCapacity()).isEqualTo(50); // Should remain unchanged
        assertThat(result.getCapacityTotal()).isEqualTo(150);
        
        verify(storeRepository).findById(storeId);
        verify(storeRepository).save(existingStore);
    }

    @Test
    @DisplayName("Should find all store products successfully")
    void testFindAllStoreProductosDto_Success() {
        // Given
        Store store1 = createStore(1L, "store1", 50, 100);
        Store store2 = createStore(2L, "store2", 30, 100);
        
        // Store 1 has products 1 and 2
        Stock stock1 = createStock(1L, 1L, 50, "entrada", store1, LocalDateTime.now());
        Stock stock2 = createStock(2L, 2L, 30, "entrada", store1, LocalDateTime.now());
        Stock stock3 = createStock(3L, 1L, 10, "salida", store1, LocalDateTime.now()); // Exit reduces stock
        
        // Store 2 has product 3
        Stock stock4 = createStock(4L, 3L, 25, "entrada", store2, LocalDateTime.now());
        
        store1.setStocks(Arrays.asList(stock1, stock2, stock3));
        store2.setStocks(Arrays.asList(stock4));
        
        List<Store> stores = Arrays.asList(store1, store2);
        when(storeRepository.findAll()).thenReturn(stores);

        // When
        List<StoreCantidadProductoAlmacenDto> result = storeService.findAllStoreProductosDto();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        
        // Store 1 should have 2 products: product 1 (50-10=40) and product 2 (30)
        StoreCantidadProductoAlmacenDto store1Dto = result.stream()
                .filter(dto -> dto.getStoreId().equals(1L))
                .findFirst()
                .orElse(null);
        assertThat(store1Dto).isNotNull();
        assertThat(store1Dto.getProductos()).hasSize(2);
        
        // Store 2 should have 1 product: product 3 (25)
        StoreCantidadProductoAlmacenDto store2Dto = result.stream()
                .filter(dto -> dto.getStoreId().equals(2L))
                .findFirst()
                .orElse(null);
        assertThat(store2Dto).isNotNull();
        assertThat(store2Dto.getProductos()).hasSize(1);
        
        verify(storeRepository).findAll();
    }

    @Test
    @DisplayName("Should handle stores with no products")
    void testFindAllStoreProductosDto_EmptyStores_Success() {
        // Given
        Store emptyStore1 = createStore(1L, "store1", 0, 100);
        Store emptyStore2 = createStore(2L, "store2", 0, 100);
        emptyStore1.setStocks(new ArrayList<>());
        emptyStore2.setStocks(new ArrayList<>());
        
        List<Store> stores = Arrays.asList(emptyStore1, emptyStore2);
        when(storeRepository.findAll()).thenReturn(stores);

        // When
        List<StoreCantidadProductoAlmacenDto> result = storeService.findAllStoreProductosDto();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        
        // Both stores should have empty product lists
        result.forEach(dto -> {
            assertThat(dto.getProductos()).isEmpty();
        });
        
        verify(storeRepository).findAll();
    }

    @Test
    @DisplayName("Should handle stores with mixed entry and exit movements")
    void testFindAllStoreProductosDto_MixedMovements_Success() {
        // Given
        Store store = createStore(1L, "store1", 50, 100);
        
        // Multiple entries and exits for the same product
        Stock entrada1 = createStock(1L, 1L, 100, "entrada", store, LocalDateTime.now());
        Stock salida1 = createStock(2L, 1L, 30, "salida", store, LocalDateTime.now());
        Stock entrada2 = createStock(3L, 1L, 20, "entrada", store, LocalDateTime.now());
        Stock salida2 = createStock(4L, 1L, 10, "salida", store, LocalDateTime.now());
        
        store.setStocks(Arrays.asList(entrada1, salida1, entrada2, salida2));
        
        List<Store> stores = Arrays.asList(store);
        when(storeRepository.findAll()).thenReturn(stores);

        // When
        List<StoreCantidadProductoAlmacenDto> result = storeService.findAllStoreProductosDto();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        
        StoreCantidadProductoAlmacenDto storeDto = result.get(0);
        assertThat(storeDto.getProductos()).hasSize(1);
        
        StoreProductQuantityDto productDto = storeDto.getProductos().get(0);
        assertThat(productDto.getProductId()).isEqualTo(1L);
        // Total should be: 100 + 20 - 30 - 10 = 80
        assertThat(productDto.getCantidadTotal()).isEqualTo(80L);
        
        verify(storeRepository).findAll();
    }

    @Test
    @DisplayName("Should handle empty store list")
    void testFindAllStores_EmptyList_Success() {
        // Given
        when(storeRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Store> result = storeService.findAllStores();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(storeRepository).findAll();
    }

    @Test
    @DisplayName("Should handle null capacity in update")
    void testUpdateStoreById_NullCapacity_Success() {
        // Given
        Long storeId = 1L;
        Store existingStore = createStore(storeId, "oldName", 50, 100);
        Store updateData = new Store();
        updateData.setName("newName");
        updateData.setCapacityTotal(150);
        // capacity is null
        
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(existingStore));
        when(storeRepository.save(any(Store.class))).thenReturn(existingStore);

        // When
        Store result = storeService.updateStoreById(storeId, updateData);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("newName");
        assertThat(result.getCapacity()).isEqualTo(50); // Should remain unchanged
        assertThat(result.getCapacityTotal()).isEqualTo(150);
        
        verify(storeRepository).findById(storeId);
        verify(storeRepository).save(existingStore);
    }

    // Helper methods
    private Store createStore(Long id, String name, Integer capacity, Integer capacityTotal) {
        Store store = new Store();
        store.setId(id);
        store.setName(name);
        store.setCapacity(capacity);
        store.setCapacityTotal(capacityTotal);
        store.setStocks(new ArrayList<>());
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
}
