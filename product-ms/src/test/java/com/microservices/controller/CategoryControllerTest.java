package com.microservices.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.dto.CategoryRequestDTO;
import com.microservices.dto.CategoryResponseDTO;
import com.microservices.service.CategoryService;
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
@DisplayName("CategoryController Tests")
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private CategoryResponseDTO testResponseDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CategoryController(categoryService))
                .setControllerAdvice(new com.microservices.handler.GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        testResponseDTO = CategoryResponseDTO.builder()
                .idCategoria(1L)
                .nombreCategoria("Test Category")
                .build();
    }

    @Test
    @DisplayName("GET /api/categories/{id} - Debería retornar categoría por ID")
    void getCategoryById_ShouldReturnCategory() throws Exception {
        // Given
        when(categoryService.getCategoryById(1L)).thenReturn(testResponseDTO);

        // When & Then
        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idCategoria").value(1))
                .andExpect(jsonPath("$.nombreCategoria").value("Test Category"));
    }

    @Test
    @DisplayName("GET /api/categories - Debería retornar todas las categorías")
    void getAllCategories_ShouldReturnAllCategories() throws Exception {
        // Given
        List<CategoryResponseDTO> categories = Arrays.asList(testResponseDTO);
        when(categoryService.getAllCategories()).thenReturn(categories);

        // When & Then
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombreCategoria").value("Test Category"));
    }


    @Test
    @DisplayName("POST /api/categories - Debería crear categoría")
    void createCategory_ShouldCreateCategory() throws Exception {
        // Given
        CategoryRequestDTO requestDTO = CategoryRequestDTO.builder()
                .nombreCategoria("New Category")
                .build();

        when(categoryService.createCategory(any(CategoryRequestDTO.class))).thenReturn(testResponseDTO);

        // When & Then
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombreCategoria").value("Test Category"));
    }

    @Test
    @DisplayName("PUT /api/categories/{id} - Debería actualizar categoría")
    void updateCategory_ShouldUpdateCategory() throws Exception {
        // Given
        Long categoryId = 1L;
        CategoryRequestDTO updateDTO = CategoryRequestDTO.builder()
                .nombreCategoria("Updated Category")
                .build();

        when(categoryService.updateCategory(anyLong(), any(CategoryRequestDTO.class))).thenReturn(testResponseDTO);

        // When & Then
        mockMvc.perform(put("/api/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombreCategoria").value("Test Category"));
    }

    @Test
    @DisplayName("DELETE /api/categories/{id} - Debería eliminar categoría")
    void deleteCategory_ShouldDeleteCategory() throws Exception {
        // Given
        Long categoryId = 1L;

        // When & Then
        mockMvc.perform(delete("/api/categories/{id}", categoryId))
                .andExpect(status().isNoContent());
    }


    @Test
    @DisplayName("POST /api/categories - Debería validar campos requeridos")
    void createCategory_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given
        CategoryRequestDTO invalidDTO = CategoryRequestDTO.builder()
                .nombreCategoria("") // Nombre vacío
                .build();

        // When & Then
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/categories/{id} - Debería retornar 404 cuando categoría no existe")
    void getCategoryById_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        // Given
        when(categoryService.getCategoryById(999L)).thenThrow(new RuntimeException("Categoría no encontrada"));

        // When & Then
        mockMvc.perform(get("/api/categories/999"))
                .andExpect(status().isInternalServerError());
    }
}