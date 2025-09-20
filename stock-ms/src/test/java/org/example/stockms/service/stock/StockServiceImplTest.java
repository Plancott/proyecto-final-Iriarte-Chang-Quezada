package org.example.stockms.service.stock;

import org.example.stockms.mapper.StockMapper;
import org.example.stockms.model.stock.Stock;
import org.example.stockms.model.stock.dto.*;
import org.example.stockms.model.store.Store;
import org.example.stockms.repository.StockRepository;
import org.example.stockms.repository.StoreRepository;
import org.example.stockms.service.store.StoreService;
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
@DisplayName("StockServiceImpl Tests")
class StockServiceImplTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private StoreService storeService;

    @Mock
    private StockMapper stockMapper;

    @InjectMocks
    private StockServiceImpl stockService;

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
    @DisplayName("Should save stocks successfully when store exists")
    void testSave_ExistingStore_Success() {
        // Given
        List<StockRequestDto> requestDtos = Arrays.asList(testStockRequestDto);
        List<Store> existingStores = Arrays.asList(testStore);
        List<Stock> expectedStocks = Arrays.asList(testStock);

        when(storeRepository.findById(1L)).thenReturn(Optional.of(testStore));
        when(storeService.findAllStores()).thenReturn(existingStores);
        when(stockRepository.saveAll(anyList())).thenReturn(expectedStocks);

        // When
        List<Stock> result = stockService.save(requestDtos);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(storeRepository).save(testStore);
        verify(stockRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("Should save stocks successfully when store doesn't exist")
    void testSave_NonExistingStore_Success() {
        // Given
        List<StockRequestDto> requestDtos = Arrays.asList(testStockRequestDto);
        Store newStore = createStore(1L, "store1", 0, 100);
        List<Store> existingStores = Arrays.asList(newStore);
        List<Stock> expectedStocks = Arrays.asList(testStock);

        when(storeRepository.findById(1L)).thenReturn(Optional.empty());
        when(storeRepository.save(any(Store.class))).thenReturn(newStore);
        when(storeService.findAllStores()).thenReturn(existingStores);
        when(stockRepository.saveAll(anyList())).thenReturn(expectedStocks);

        // When
        List<Stock> result = stockService.save(requestDtos);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(storeRepository, atLeastOnce()).save(any(Store.class));
        verify(stockRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("Should save stocks across multiple stores when capacity is exceeded")
    void testSave_MultipleStores_Success() {
        // Given
        StockRequestDto requestDto = createStockRequestDto(1L, 150, 1L); // More than capacity
        List<StockRequestDto> requestDtos = Arrays.asList(requestDto);
        
        Store store1 = createStore(1L, "store1", 0, 100);
        Store store2 = createStore(2L, "store2", 0, 100);
        List<Store> existingStores = Arrays.asList(store1, store2);

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store1));
        when(storeService.findAllStores()).thenReturn(existingStores);
        when(storeRepository.save(any(Store.class))).thenReturn(store1, store2);
        when(stockRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        List<Stock> result = stockService.save(requestDtos);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2); // Should create 2 stocks (100 + 50)
        verify(storeRepository, atLeastOnce()).save(any(Store.class));
        verify(stockRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("Should create new store when existing stores are full")
    void testSave_CreateNewStore_Success() {
        // Given
        StockRequestDto requestDto = createStockRequestDto(1L, 150, 1L);
        List<StockRequestDto> requestDtos = Arrays.asList(requestDto);
        
        Store store1 = createStore(1L, "store1", 100, 100); // Full store
        List<Store> existingStores = Arrays.asList(store1);

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store1));
        when(storeService.findAllStores()).thenReturn(existingStores);
        when(storeRepository.save(any(Store.class))).thenReturn(store1);
        when(stockRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        List<Stock> result = stockService.save(requestDtos);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2); // Should create 2 stocks
        verify(storeRepository, atLeast(2)).save(any(Store.class));
        verify(stockRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("Should find all stocks successfully")
    void testFindAll_Success() {
        // Given
        List<Stock> expectedStocks = Arrays.asList(testStock);
        when(stockRepository.findAll()).thenReturn(expectedStocks);

        // When
        List<Stock> result = stockService.findAll();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testStock);
        verify(stockRepository).findAll();
    }

    @Test
    @DisplayName("Should find stock by id successfully")
    void testFindById_Success() {
        // Given
        Long stockId = 1L;
        when(stockRepository.findById(stockId)).thenReturn(Optional.of(testStock));

        // When
        Stock result = stockService.findById(stockId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(testStock);
        verify(stockRepository).findById(stockId);
    }

    @Test
    @DisplayName("Should throw exception when stock not found by id")
    void testFindById_NotFound_ThrowsException() {
        // Given
        Long stockId = 999L;
        when(stockRepository.findById(stockId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> stockService.findById(stockId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Stock not found");
    }

    @Test
    @DisplayName("Should find stock by product id successfully")
    void testFindByProductId_Success() {
        // Given
        Long productId = 1L;
        testStore.setStocks(new ArrayList<>(Arrays.asList(testStock)));
        List<Store> stores = Arrays.asList(testStore);
        List<StockResponseDto> stockResponseDtos = Arrays.asList(createStockResponseDto());

        when(storeService.findAllStores()).thenReturn(stores);
        when(stockMapper.toListStockResponseDto(anyList())).thenReturn(stockResponseDtos);

        // When
        StockFindProductDto result = stockService.findByProductId(productId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isEqualTo(productId);
        assertThat(result.getSaldoFinal()).isEqualTo(50);
        assertThat(result.getMovimientos()).hasSize(1);
    }

    @Test
    @DisplayName("Should throw exception when product not found")
    void testFindByProductId_NotFound_ThrowsException() {
        // Given
        Long productId = 999L;
        List<Store> stores = Arrays.asList(testStore);
        
        when(storeService.findAllStores()).thenReturn(stores);

        // When & Then
        assertThatThrownBy(() -> stockService.findByProductId(productId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Producto con id "+ productId +" no existe");
    }

    @Test
    @DisplayName("Should subtract stock successfully")
    void testRestarStock_Success() {
        // Given
        StockSalidaRequestDto requestDto = createStockSalidaRequestDto(1L, 20);
        List<StockSalidaRequestDto> requestDtos = Arrays.asList(requestDto);
        
        Store store = createStore(1L, "store1", 50, 100);
        Stock entradaStock = createStock(1L, 1L, 50, "entrada", store, LocalDateTime.now().minusDays(1));
        store.setStocks(new ArrayList<>(Arrays.asList(entradaStock)));
        
        List<Store> stores = Arrays.asList(store);
        when(storeRepository.findAll()).thenReturn(stores);
        when(stockRepository.save(any(Stock.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(storeRepository.save(any(Store.class))).thenReturn(store);

        // When
        List<StockSalidaResponseDto> result = stockService.restarStock(requestDtos);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductId()).isEqualTo(1L);
        assertThat(result.get(0).getCantidadRetirada()).isEqualTo(20);
        verify(stockRepository).save(any(Stock.class));
        verify(storeRepository).save(store);
    }

    @Test
    @DisplayName("Should throw exception when insufficient stock")
    void testRestarStock_InsufficientStock_ThrowsException() {
        // Given
        StockSalidaRequestDto requestDto = createStockSalidaRequestDto(1L, 100);
        List<StockSalidaRequestDto> requestDtos = Arrays.asList(requestDto);
        
        Store store = createStore(1L, "store1", 50, 100);
        Stock entradaStock = createStock(1L, 1L, 30, "entrada", store, LocalDateTime.now().minusDays(1));
        store.setStocks(new ArrayList<>(Arrays.asList(entradaStock)));
        
        List<Store> stores = Arrays.asList(store);
        when(storeRepository.findAll()).thenReturn(stores);

        // When & Then
        assertThatThrownBy(() -> stockService.restarStock(requestDtos))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No hay suficiente stock del producto 1");
    }

    @Test
    @DisplayName("Should handle multiple stock entries and exits correctly")
    void testRestarStock_MultipleEntriesAndExits_Success() {
        // Given
        StockSalidaRequestDto requestDto = createStockSalidaRequestDto(1L, 40);
        List<StockSalidaRequestDto> requestDtos = Arrays.asList(requestDto);
        
        Store store = createStore(1L, "store1", 50, 100);
        Stock entrada1 = createStock(1L, 1L, 30, "entrada", store, LocalDateTime.now().minusDays(2));
        Stock entrada2 = createStock(2L, 1L, 20, "entrada", store, LocalDateTime.now().minusDays(1));
        Stock salida1 = createStock(3L, 1L, 10, "salida", store, LocalDateTime.now().minusHours(1));
        
        store.setStocks(new ArrayList<>(Arrays.asList(entrada1, entrada2, salida1)));
        List<Store> stores = Arrays.asList(store);
        
        when(storeRepository.findAll()).thenReturn(stores);
        when(stockRepository.save(any(Stock.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(storeRepository.save(any(Store.class))).thenReturn(store);

        // When
        List<StockSalidaResponseDto> result = stockService.restarStock(requestDtos);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCantidadRetirada()).isEqualTo(40);
        verify(stockRepository).save(any(Stock.class));
        verify(storeRepository).save(store);
    }



    @Test
    @DisplayName("Should handle empty stock list")
    void testSave_EmptyList_Success() {
        // Given
        List<StockRequestDto> requestDtos = Arrays.asList();
        when(stockRepository.saveAll(anyList())).thenReturn(Arrays.asList());

        // When
        List<Stock> result = stockService.save(requestDtos);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(stockRepository).saveAll(Arrays.asList());
    }

    @Test
    @DisplayName("Should handle empty subtract stock request")
    void testRestarStock_EmptyRequest_Success() {
        // Given
        List<StockSalidaRequestDto> requestDtos = Arrays.asList();

        // When
        List<StockSalidaResponseDto> result = stockService.restarStock(requestDtos);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
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

    private StockRequestDto createStockRequestDto(Long productId, Integer quantity, Long storeId) {
        StockRequestDto dto = new StockRequestDto();
        dto.setProductId(productId);
        dto.setQuantity(quantity);
        dto.setStoreId(storeId);
        return dto;
    }

    private StockSalidaRequestDto createStockSalidaRequestDto(Long productId, Integer quantity) {
        return new StockSalidaRequestDto(productId, quantity);
    }

    private StockResponseDto createStockResponseDto() {
        StockResponseDto dto = new StockResponseDto();
        dto.setId(1L);
        dto.setProductId(1L);
        dto.setQuantity(50);
        dto.setStoreId(1L);
        dto.setState("entrada");
        dto.setDate(LocalDateTime.now());
        return dto;
    }
}
