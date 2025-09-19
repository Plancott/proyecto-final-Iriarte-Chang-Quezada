package com.microservices.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.dto.ProductRequestDTO;
import com.microservices.dto.ProductResponseDTO;
import com.microservices.dto.ProductUpdateDTO;
import com.microservices.entity.ProductStatus;
import com.microservices.service.ProductService;
import com.microservices.service.CloudinaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductController Tests")
class ProductControllerTest {

    @Mock
    private ProductService productService;
    
    @Mock
    private CloudinaryService cloudinaryService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private ProductResponseDTO testResponseDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ProductController(productService, cloudinaryService))
                .setControllerAdvice(new com.microservices.handler.GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        testResponseDTO = ProductResponseDTO.builder()
                .productId(1L)
                .name("Test Product")
                .description("Test Description")
                .unitPrice(BigDecimal.valueOf(100.00))
                .imageUrl("https://example.com/image.jpg")
                .status(ProductStatus.ACTIVE)
                .brandName("Test Brand")
                .categoryName("Test Category")
                .build();
    }

    @Test
    @DisplayName("GET /api/products/{id} - Debería retornar producto por ID")
    void getProductById_ShouldReturnProduct() throws Exception {
        // Given
        when(productService.getProductById(1L)).thenReturn(testResponseDTO);

        // When & Then
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.unitPrice").value(100.00))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @DisplayName("GET /api/products - Debería retornar todos los productos")
    void getAllProducts_ShouldReturnAllProducts() throws Exception {
        // Given
        List<ProductResponseDTO> products = Arrays.asList(testResponseDTO);
        Page<ProductResponseDTO> page = new PageImpl<>(products);
        when(productService.getAllProducts(any(Pageable.class))).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Product"));
    }

    @Test
    @DisplayName("GET /api/products - Debería retornar productos paginados")
    void getAllProducts_WithPagination_ShouldReturnPaginatedProducts() throws Exception {
        // Given
        Page<ProductResponseDTO> page = new PageImpl<>(Arrays.asList(testResponseDTO));
        when(productService.getAllProducts(any(Pageable.class))).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "name")
                        .param("sortDirection", "asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("POST /api/products - Debería crear producto")
    void createProduct_ShouldCreateProduct() throws Exception {
        // Given
        ProductRequestDTO requestDTO = ProductRequestDTO.builder()
                .name("New Product")
                .description("New Description")
                .unitPrice(BigDecimal.valueOf(200.00))
                .imageUrl("https://example.com/new-image.jpg")
                .brandId(1L)
                .categoryId(1L)
                .build();

        when(productService.createProduct(any(ProductRequestDTO.class))).thenReturn(testResponseDTO);

        // When & Then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    @DisplayName("PUT /api/products/{id} - Debería actualizar producto")
    void updateProduct_ShouldUpdateProduct() throws Exception {
        // Given
        Long productId = 1L;
        ProductUpdateDTO updateDTO = ProductUpdateDTO.builder()
                .name("Updated Product")
                .description("Updated Description")
                .unitPrice(BigDecimal.valueOf(150.00))
                .build();

        when(productService.updateProduct(anyLong(), any(ProductUpdateDTO.class))).thenReturn(testResponseDTO);

        // When & Then
        mockMvc.perform(put("/api/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    @DisplayName("DELETE /api/products/{id} - Debería eliminar producto")
    void deleteProduct_ShouldDeleteProduct() throws Exception {
        // Given
        Long productId = 1L;

        // When & Then
        mockMvc.perform(delete("/api/products/{id}", productId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/products/search/text - Debería buscar productos por texto")
    void searchProducts_ShouldSearchProducts() throws Exception {
        // Given
        String searchTerm = "test";
        List<ProductResponseDTO> products = Arrays.asList(testResponseDTO);
        Page<ProductResponseDTO> page = new PageImpl<>(products);
        when(productService.searchProductsByText(eq(searchTerm), any(Pageable.class))).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/products/search/text")
                        .param("searchText", searchTerm))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Product"));
    }

    @Test
    @DisplayName("GET /api/products/by-category/{categoryId} - Debería retornar productos por categoría")
    void getProductsByCategory_ShouldReturnProductsByCategory() throws Exception {
        // Given
        Long categoryId = 1L;
        List<ProductResponseDTO> products = Arrays.asList(testResponseDTO);
        Page<ProductResponseDTO> page = new PageImpl<>(products);
        when(productService.getProductsByCategory(eq(categoryId), any(Pageable.class))).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/products/by-category/{categoryId}", categoryId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Product"));
    }

    @Test
    @DisplayName("GET /api/products/by-brand/{brandId} - Debería retornar productos por marca")
    void getProductsByBrand_ShouldReturnProductsByBrand() throws Exception {
        // Given
        Long brandId = 1L;
        List<ProductResponseDTO> products = Arrays.asList(testResponseDTO);
        Page<ProductResponseDTO> page = new PageImpl<>(products);
        when(productService.getProductsByBrand(eq(brandId), any(Pageable.class))).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/products/by-brand/{brandId}", brandId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Product"));
    }

    @Test
    @DisplayName("GET /api/products/by-status/{status} - Debería retornar productos por estado")
    void getProductsByStatus_ShouldReturnProductsByStatus() throws Exception {
        // Given
        String status = "ACTIVE";
        List<ProductResponseDTO> products = Arrays.asList(testResponseDTO);
        Page<ProductResponseDTO> page = new PageImpl<>(products);
        when(productService.getProductsByStatus(eq(ProductStatus.ACTIVE), any(Pageable.class))).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/products/by-status/{status}", status))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));
    }

    @Test
    @DisplayName("POST /api/products - Debería validar campos requeridos")
    void createProduct_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given
        ProductRequestDTO invalidDTO = ProductRequestDTO.builder()
                .name("") // Nombre vacío
                .unitPrice(BigDecimal.valueOf(-100.00)) // Precio negativo
                .build();

        // When & Then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/products/{id} - Debería retornar 500 cuando producto no existe")
    void getProductById_WithNonExistentId_ShouldReturnInternalServerError() throws Exception {
        // Given
        when(productService.getProductById(999L)).thenThrow(new RuntimeException("Producto no encontrado"));

        // When & Then
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isInternalServerError());
    }
}