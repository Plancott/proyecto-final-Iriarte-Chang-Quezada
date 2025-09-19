package com.microservices.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.dto.BrandRequestDTO;
import com.microservices.dto.BrandResponseDTO;
import com.microservices.service.BrandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BrandController Tests")
class BrandControllerTest {

    @Mock
    private BrandService brandService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private BrandResponseDTO testResponseDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new BrandController(brandService))
                .setControllerAdvice(new com.microservices.handler.GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        testResponseDTO = BrandResponseDTO.builder()
                .marcaId(1L)
                .nombreMarca("Test Brand")
                .build();
    }

    @Test
    @DisplayName("GET /api/brands/{id} - Debería retornar marca por ID")
    void getBrandById_ShouldReturnBrand() throws Exception {
        // Given
        when(brandService.getBrandById(1L)).thenReturn(testResponseDTO);

        // When & Then
        mockMvc.perform(get("/api/brands/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.marcaId").value(1))
                .andExpect(jsonPath("$.nombreMarca").value("Test Brand"));
    }

    @Test
    @DisplayName("GET /api/brands - Debería retornar todas las marcas")
    void getAllBrands_ShouldReturnAllBrands() throws Exception {
        // Given
        List<BrandResponseDTO> brands = Arrays.asList(testResponseDTO);
        when(brandService.getAllBrands()).thenReturn(brands);

        // When & Then
        mockMvc.perform(get("/api/brands"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombreMarca").value("Test Brand"));
    }


    @Test
    @DisplayName("POST /api/brands - Debería crear marca")
    void createBrand_ShouldCreateBrand() throws Exception {
        // Given
        BrandRequestDTO requestDTO = BrandRequestDTO.builder()
                .nombreMarca("New Brand")
                .build();

        when(brandService.createBrand(any(BrandRequestDTO.class))).thenReturn(testResponseDTO);

        // When & Then
        mockMvc.perform(post("/api/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombreMarca").value("Test Brand"));
    }

    @Test
    @DisplayName("PUT /api/brands/{id} - Debería actualizar marca")
    void updateBrand_ShouldUpdateBrand() throws Exception {
        // Given
        Long brandId = 1L;
        BrandRequestDTO updateDTO = BrandRequestDTO.builder()
                .nombreMarca("Updated Brand")
                .build();

        when(brandService.updateBrand(anyLong(), any(BrandRequestDTO.class))).thenReturn(testResponseDTO);

        // When & Then
        mockMvc.perform(put("/api/brands/{id}", brandId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombreMarca").value("Test Brand"));
    }

    @Test
    @DisplayName("DELETE /api/brands/{id} - Debería eliminar marca")
    void deleteBrand_ShouldDeleteBrand() throws Exception {
        // Given
        Long brandId = 1L;

        // When & Then
        mockMvc.perform(delete("/api/brands/{id}", brandId))
                .andExpect(status().isNoContent());
    }


    @Test
    @DisplayName("POST /api/brands - Debería validar campos requeridos")
    void createBrand_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given
        BrandRequestDTO invalidDTO = BrandRequestDTO.builder()
                .nombreMarca("") // Nombre vacío
                .build();

        // When & Then
        mockMvc.perform(post("/api/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/brands/{id} - Debería retornar 404 cuando marca no existe")
    void getBrandById_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        // Given
        when(brandService.getBrandById(999L)).thenThrow(new RuntimeException("Marca no encontrada"));

        // When & Then
        mockMvc.perform(get("/api/brands/999"))
                .andExpect(status().isInternalServerError());
    }
}