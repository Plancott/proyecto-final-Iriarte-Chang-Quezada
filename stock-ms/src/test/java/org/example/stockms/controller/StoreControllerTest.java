package org.example.stockms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.stockms.mapper.StoreMapper;
import org.example.stockms.model.store.Store;
import org.example.stockms.model.store.dto.*;
import org.example.stockms.service.store.StoreService;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StoreController Tests")
class StoreControllerTest {

    @Mock
    private StoreService storeService;

    @Mock
    private StoreMapper storeMapper;

    @InjectMocks
    private StoreController storeController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(storeController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should get all stores successfully")
    void testGetStores_Success() throws Exception {
        // Given
        List<Store> stores = Arrays.asList(
                createStore(1, "store1", 50, 100),
                createStore(2, "store2", 30, 100)
        );

        List<StoreResponseDto> responseDtos = Arrays.asList(
                createStoreResponseDto(1, "store1", 50, 100, Arrays.asList()),
                createStoreResponseDto(2, "store2", 30, 100, Arrays.asList())
        );

        when(storeService.findAllStores()).thenReturn(stores);
        when(storeMapper.toListResponseDto(stores)).thenReturn(responseDtos);

        // When & Then
        mockMvc.perform(get("/api/store"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("store1"))
                .andExpect(jsonPath("$[0].capacity").value(50))
                .andExpect(jsonPath("$[0].capacityTotal").value(100))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("store2"))
                .andExpect(jsonPath("$[1].capacity").value(30))
                .andExpect(jsonPath("$[1].capacityTotal").value(100));
    }

    @Test
    @DisplayName("Should get store by id successfully")
    void testGetStore_Success() throws Exception {
        // Given
        Long storeId = 1L;
        Store store = createStore(1, "store1", 50, 100);
        StoreResponseDto responseDto = createStoreResponseDto(1, "store1", 50, 100, Arrays.asList());

        when(storeService.findStoreById(storeId)).thenReturn(store);
        when(storeMapper.toStoreResponseDto(store)).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(get("/api/store/{id}", storeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("store1"))
                .andExpect(jsonPath("$.capacity").value(50))
                .andExpect(jsonPath("$.capacityTotal").value(100));
    }

    @Test
    @DisplayName("Should get store products successfully")
    void testProductosAlmacen_Success() throws Exception {
        // Given
        List<StoreCantidadProductoAlmacenDto> storeProductos = Arrays.asList(
                createStoreCantidadProductoAlmacenDto(1L, Arrays.asList(
                        createStoreProductQuantityDto(1L, 50L),
                        createStoreProductQuantityDto(2L, 30L)
                )),
                createStoreCantidadProductoAlmacenDto(2L, Arrays.asList(
                        createStoreProductQuantityDto(3L, 25L)
                ))
        );

        when(storeService.findAllStoreProductosDto()).thenReturn(storeProductos);

        // When & Then
        mockMvc.perform(get("/api/store/productosAlmacen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].storeId").value(1))
                .andExpect(jsonPath("$[0].productos").isArray())
                .andExpect(jsonPath("$[0].productos[0].productId").value(1))
                .andExpect(jsonPath("$[0].productos[0].cantidadTotal").value(50))
                .andExpect(jsonPath("$[0].productos[1].productId").value(2))
                .andExpect(jsonPath("$[0].productos[1].cantidadTotal").value(30))
                .andExpect(jsonPath("$[1].storeId").value(2))
                .andExpect(jsonPath("$[1].productos[0].productId").value(3))
                .andExpect(jsonPath("$[1].productos[0].cantidadTotal").value(25));
    }

    @Test
    @DisplayName("Should create stores successfully")
    void testCreateStore_Success() throws Exception {
        // Given
        Integer cantidad = 2;
        List<Store> createdStores = Arrays.asList(
                createStore(1, "store1", 0, 100),
                createStore(2, "store2", 0, 100)
        );

        List<StoreResponseDto> responseDtos = Arrays.asList(
                createStoreResponseDto(1, "store1", 0, 100, Arrays.asList()),
                createStoreResponseDto(2, "store2", 0, 100, Arrays.asList())
        );

        when(storeService.saveStore(cantidad)).thenReturn(createdStores);
        when(storeMapper.toListStoreResponseDto(createdStores)).thenReturn(responseDtos);

        // When & Then
        mockMvc.perform(post("/api/store/{cantidad}", cantidad))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("store1"))
                .andExpect(jsonPath("$[0].capacity").value(0))
                .andExpect(jsonPath("$[0].capacityTotal").value(100))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("store2"))
                .andExpect(jsonPath("$[1].capacity").value(0))
                .andExpect(jsonPath("$[1].capacityTotal").value(100));
    }

    @Test
    @DisplayName("Should delete store successfully")
    void testDeleteStore_Success() throws Exception {
        // Given
        Long storeId = 1L;
        
        // When & Then
        mockMvc.perform(delete("/api/store/{id}", storeId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should update store successfully")
    void testUpdateStore_Success() throws Exception {
        // Given
        Long storeId = 1L;
        StoreRequestDto requestDto = createStoreRequestDto("Updated Store", 150);
        Store updatedStore = createStore(1, "Updated Store", 50, 150);
        StoreResponseDto responseDto = createStoreResponseDto(1, "Updated Store", 50, 150, Arrays.asList());

        when(storeMapper.toStore(any())).thenReturn(updatedStore);
        when(storeService.updateStoreById(storeId, updatedStore)).thenReturn(updatedStore);
        when(storeMapper.toStoreResponseDto(updatedStore)).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(put("/api/store/{id}", storeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Store"))
                .andExpect(jsonPath("$.capacity").value(50))
                .andExpect(jsonPath("$.capacityTotal").value(150));
    }

    @Test
    @DisplayName("Should handle empty stores list")
    void testGetStores_EmptyList() throws Exception {
        // Given
        when(storeService.findAllStores()).thenReturn(Arrays.asList());
        when(storeMapper.toListResponseDto(anyList())).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/store"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Should handle empty store products list")
    void testProductosAlmacen_EmptyList() throws Exception {
        // Given
        when(storeService.findAllStoreProductosDto()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/store/productosAlmacen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Should handle invalid JSON in update request")
    void testUpdateStore_InvalidJson() throws Exception {
        // Given
        Long storeId = 1L;

        // When & Then
        mockMvc.perform(put("/api/store/{id}", storeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("invalid json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle zero quantity in create store")
    void testCreateStore_ZeroQuantity() throws Exception {
        // Given
        Integer cantidad = 0;
        List<Store> emptyStores = Arrays.asList();
        List<StoreResponseDto> emptyResponseDtos = Arrays.asList();

        when(storeService.saveStore(cantidad)).thenReturn(emptyStores);
        when(storeMapper.toListStoreResponseDto(emptyStores)).thenReturn(emptyResponseDtos);

        // When & Then
        mockMvc.perform(post("/api/store/{cantidad}", cantidad))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    // Helper methods
    private Store createStore(Integer id, String name, Integer capacity, Integer capacityTotal) {
        Store store = new Store();
        store.setId(Long.valueOf(id));
        store.setName(name);
        store.setCapacity(capacity);
        store.setCapacityTotal(capacityTotal);
        return store;
    }

    private StoreRequestDto createStoreRequestDto(String name, Integer capacityTotal) {
        StoreRequestDto dto = new StoreRequestDto();
        dto.setName(name);
        dto.setCapacityTotal(capacityTotal);
        return dto;
    }

    private StoreResponseDto createStoreResponseDto(Integer id, String name, Integer capacity, 
                                                   Integer capacityTotal, List<Object> stocks) {
        StoreResponseDto dto = new StoreResponseDto();
        dto.setId(id);
        dto.setName(name);
        dto.setCapacity(capacity);
        dto.setCapacityTotal(capacityTotal);
        // Note: StoreResponseDto expects List<Stock>, but we pass List<Object> for testing
        return dto;
    }

    private StoreCantidadProductoAlmacenDto createStoreCantidadProductoAlmacenDto(Long storeId, 
                                                                                  List<StoreProductQuantityDto> productos) {
        return new StoreCantidadProductoAlmacenDto(storeId, productos);
    }

    private StoreProductQuantityDto createStoreProductQuantityDto(Long productId, Long cantidadTotal) {
        return new StoreProductQuantityDto(productId, cantidadTotal);
    }
}
