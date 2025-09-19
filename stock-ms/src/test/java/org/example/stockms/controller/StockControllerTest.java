package org.example.stockms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.stockms.mapper.StockMapper;
import org.example.stockms.model.stock.Stock;
import org.example.stockms.model.stock.dto.*;
import org.example.stockms.model.store.Store;
import org.example.stockms.service.stock.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StockController Tests")
class StockControllerTest {

    @Mock
    private StockMapper stockMapper;

    @Mock
    private StockService stockService;

    @InjectMocks
    private StockController stockController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(stockController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should save stocks successfully")
    void testSaveAllStocks_Success() throws Exception {
        // Given
        List<StockRequestDto> requestDtos = Arrays.asList(
                createStockRequestDto(1L, 50, 1L),
                createStockRequestDto(2L, 30, 2L)
        );

        Store store1 = createStore(1L, "store1", 50, 100);
        Store store2 = createStore(2L, "store2", 30, 100);

        List<Stock> savedStocks = Arrays.asList(
                createStock(1L, 1L, 50, "entrada", store1, LocalDateTime.now()),
                createStock(2L, 2L, 30, "entrada", store2, LocalDateTime.now())
        );

        List<StockResponseDto> responseDtos = Arrays.asList(
                createStockResponseDto(1L, 1L, 50, 1L, "entrada", LocalDateTime.now()),
                createStockResponseDto(2L, 2L, 30, 2L, "entrada", LocalDateTime.now())
        );

        when(stockService.save(anyList())).thenReturn(savedStocks);
        when(stockMapper.toListStockResponseDto(savedStocks)).thenReturn(responseDtos);

        // When & Then
        mockMvc.perform(post("/api/stock/entrada")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDtos)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].quantity").value(50))
                .andExpect(jsonPath("$[0].storeId").value(1))
                .andExpect(jsonPath("$[0].state").value("entrada"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].productId").value(2))
                .andExpect(jsonPath("$[1].quantity").value(30))
                .andExpect(jsonPath("$[1].storeId").value(2));
    }

    @Test
    @DisplayName("Should find all stocks successfully")
    void testFindAll_Success() throws Exception {
        // Given
        Store store1 = createStore(1L, "store1", 50, 100);
        Store store2 = createStore(2L, "store2", 30, 100);

        List<Stock> stocks = Arrays.asList(
                createStock(1L, 1L, 50, "entrada", store1, LocalDateTime.now()),
                createStock(2L, 2L, 30, "entrada", store2, LocalDateTime.now())
        );

        List<StockResponseDto> responseDtos = Arrays.asList(
                createStockResponseDto(1L, 1L, 50, 1L, "entrada", LocalDateTime.now()),
                createStockResponseDto(2L, 2L, 30, 2L, "entrada", LocalDateTime.now())
        );

        when(stockService.findAll()).thenReturn(stocks);
        when(stockMapper.toListStockResponseDto(stocks)).thenReturn(responseDtos);

        // When & Then
        mockMvc.perform(get("/api/stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].quantity").value(50))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].productId").value(2))
                .andExpect(jsonPath("$[1].quantity").value(30));
    }

    @Test
    @DisplayName("Should find stock by id successfully")
    void testFindById_Success() throws Exception {
        // Given
        Long stockId = 1L;
        Store store = createStore(1L, "store1", 50, 100);
        Stock stock = createStock(stockId, 1L, 50, "entrada", store, LocalDateTime.now());
        StockResponseDto responseDto = createStockResponseDto(stockId, 1L, 50, 1L, "entrada", LocalDateTime.now());

        when(stockService.findById(stockId)).thenReturn(stock);
        when(stockMapper.toStockResponseDto(stock)).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(get("/api/stock/{id}", stockId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(stockId))
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.quantity").value(50))
                .andExpect(jsonPath("$.storeId").value(1))
                .andExpect(jsonPath("$.state").value("entrada"));
    }

    @Test
    @DisplayName("Should find stock by product id successfully")
    void testFindByProductId_Success() throws Exception {
        // Given
        Long productId = 1L;
        StockResponseDto stockResponseDto = createStockResponseDto(1L, productId, 50, 1L, "entrada", LocalDateTime.now());
        List<StockResponseDto> stockResponses = Arrays.asList(stockResponseDto);
        
        StockFindProductDto findProductDto = new StockFindProductDto(productId, stockResponses, 50);

        when(stockService.findByProductId(productId)).thenReturn(findProductDto);

        // When & Then
        mockMvc.perform(get("/api/stock/totalProduct/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(productId))
                .andExpect(jsonPath("$.saldoFinal").value(50))
                .andExpect(jsonPath("$.movimientos").isArray())
                .andExpect(jsonPath("$.movimientos[0].id").value(1))
                .andExpect(jsonPath("$.movimientos[0].productId").value(productId));
    }

    @Test
    @DisplayName("Should subtract stock successfully")
    void testRestarStock_Success() throws Exception {
        // Given
        List<StockSalidaRequestDto> requestDtos = Arrays.asList(
                createStockSalidaRequestDto(1L, 20)
        );

        List<StockSalidaResponseDto> responseDtos = Arrays.asList(
                createStockSalidaResponseDto(1L, 20, 30, 1L)
        );

        when(stockService.restarStock(anyList())).thenReturn(responseDtos);

        // When & Then
        mockMvc.perform(post("/api/stock/salida")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDtos)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].storeId").value(1))
                .andExpect(jsonPath("$[0].cantidadRetirada").value(20))
                .andExpect(jsonPath("$[0].capacidadRestante").value(30))
                .andExpect(jsonPath("$[0].productId").value(1));
    }

    @Test
    @DisplayName("Should handle empty stock list")
    void testFindAll_EmptyList() throws Exception {
        // Given
        when(stockService.findAll()).thenReturn(Arrays.asList());
        when(stockMapper.toListStockResponseDto(anyList())).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Should handle invalid JSON in save request")
    void testSaveAllStocks_InvalidJson() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/stock/entrada")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("invalid json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle invalid JSON in subtract stock request")
    void testRestarStock_InvalidJson() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/stock/salida")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("invalid json"))
                .andExpect(status().isBadRequest());
    }

    // Helper methods
    private StockRequestDto createStockRequestDto(Long productId, Integer quantity, Long storeId) {
        StockRequestDto dto = new StockRequestDto();
        dto.setProductId(productId);
        dto.setQuantity(quantity);
        dto.setStoreId(storeId);
        return dto;
    }

    private StockResponseDto createStockResponseDto(Long id, Long productId, Integer quantity, 
                                                   Long storeId, String state, LocalDateTime date) {
        StockResponseDto dto = new StockResponseDto();
        dto.setId(id);
        dto.setProductId(productId);
        dto.setQuantity(quantity);
        dto.setStoreId(storeId);
        dto.setState(state);
        dto.setDate(date);
        return dto;
    }

    private StockSalidaRequestDto createStockSalidaRequestDto(Long productId, Integer quantity) {
        return new StockSalidaRequestDto(productId, quantity);
    }

    private StockSalidaResponseDto createStockSalidaResponseDto(Long storeId, Integer cantidadRetirada, 
                                                               Integer capacidadRestante, Long productId) {
        return new StockSalidaResponseDto(storeId, cantidadRetirada, capacidadRestante, productId);
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

    private Store createStore(Long id, String name, Integer capacity, Integer capacityTotal) {
        Store store = new Store();
        store.setId(id);
        store.setName(name);
        store.setCapacity(capacity);
        store.setCapacityTotal(capacityTotal);
        return store;
    }
}
