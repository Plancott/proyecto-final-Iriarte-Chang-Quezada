package com.microservices.service;

import com.microservices.dto.ProductRequestDTO;
import com.microservices.dto.ProductResponseDTO;
import com.microservices.dto.ProductUpdateDTO;
import com.microservices.entity.Brand;
import com.microservices.entity.Category;
import com.microservices.entity.Product;
import com.microservices.entity.ProductStatus;
import com.microservices.exception.BrandNotFoundException;
import com.microservices.exception.CategoryNotFoundException;
import com.microservices.exception.ProductNotFoundException;
import com.microservices.mapper.ProductMapper;
import com.microservices.repository.BrandRepository;
import com.microservices.repository.CategoryRepository;
import com.microservices.repository.ProductRepository;
import com.microservices.service.impl.ProductServiceImpl;
import com.microservices.service.CategoryService;
import com.microservices.service.BrandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Tests")
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;
    
    @Mock
    private CategoryService categoryService;
    
    @Mock
    private BrandService brandService;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product testProduct;
    private ProductResponseDTO testResponseDTO;
    private Brand testBrand;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testBrand = Brand.builder()
                .marcaId(1L)
                .nombreMarca("Test Brand")
                .build();

        testCategory = Category.builder()
                .idCategoria(1L)
                .nombreCategoria("Test Category")
                .build();

        testProduct = Product.builder()
                .productId(1L)
                .name("Test Product")
                .description("Test Description")
                .unitPrice(BigDecimal.valueOf(100.00))
                .imageUrl("https://example.com/image.jpg")
                .status(ProductStatus.ACTIVE)
                .brand(testBrand)
                .category(testCategory)
                .build();

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
    @DisplayName("createProduct - Debería crear producto exitosamente")
    void createProduct_ShouldCreateProductSuccessfully() {
        // Given
        ProductRequestDTO requestDTO = ProductRequestDTO.builder()
                .name("New Product")
                .description("New Description")
                .unitPrice(BigDecimal.valueOf(200.00))
                .imageUrl("https://example.com/new-image.jpg")
                .brandId(1L)
                .categoryId(1L)
                .build();

        when(productRepository.existsByName("New Product")).thenReturn(false);
        when(brandService.getBrandEntityById(1L)).thenReturn(testBrand);
        when(categoryService.getCategoryEntityById(1L)).thenReturn(testCategory);
        when(productMapper.toEntity(requestDTO)).thenReturn(testProduct);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(productMapper.toResponseDTO(testProduct)).thenReturn(testResponseDTO);

        // When
        ProductResponseDTO result = productService.createProduct(requestDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Product");
        verify(productRepository).existsByName("New Product");
        verify(brandService).getBrandEntityById(1L);
        verify(categoryService).getCategoryEntityById(1L);
        verify(productMapper).toEntity(requestDTO);
        verify(productRepository).save(testProduct);
        verify(productMapper).toResponseDTO(testProduct);
    }

    @Test
    @DisplayName("createProduct - Debería lanzar excepción cuando marca no existe")
    void createProduct_WithNonExistentBrand_ShouldThrowException() {
        // Given
        ProductRequestDTO requestDTO = ProductRequestDTO.builder()
                .name("New Product")
                .brandId(999L)
                .categoryId(1L)
                .build();

        when(productRepository.existsByName("New Product")).thenReturn(false);
        when(brandService.getBrandEntityById(999L)).thenThrow(new BrandNotFoundException("Marca no encontrada"));
        when(categoryService.getCategoryEntityById(1L)).thenReturn(testCategory);

        // When & Then
        assertThatThrownBy(() -> productService.createProduct(requestDTO))
                .isInstanceOf(BrandNotFoundException.class)
                .hasMessage("Marca no encontrada");

        verify(productRepository).existsByName("New Product");
        verify(brandService).getBrandEntityById(999L);
        verify(categoryService).getCategoryEntityById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("createProduct - Debería lanzar excepción cuando categoría no existe")
    void createProduct_WithNonExistentCategory_ShouldThrowException() {
        // Given
        ProductRequestDTO requestDTO = ProductRequestDTO.builder()
                .name("New Product")
                .brandId(1L)
                .categoryId(999L)
                .build();

        when(productRepository.existsByName("New Product")).thenReturn(false);
        when(categoryService.getCategoryEntityById(999L)).thenThrow(new CategoryNotFoundException("Categoría no encontrada"));

        // When & Then
        assertThatThrownBy(() -> productService.createProduct(requestDTO))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage("Categoría no encontrada");

        verify(productRepository).existsByName("New Product");
        verify(categoryService).getCategoryEntityById(999L);
        verify(brandService, never()).getBrandEntityById(anyLong());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("getProductById - Debería retornar producto cuando existe")
    void getProductById_WithExistingId_ShouldReturnProduct() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productMapper.toResponseDTO(testProduct)).thenReturn(testResponseDTO);

        // When
        ProductResponseDTO result = productService.getProductById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Product");
        verify(productRepository).findById(1L);
        verify(productMapper).toResponseDTO(testProduct);
    }

    @Test
    @DisplayName("getProductById - Debería lanzar excepción cuando producto no existe")
    void getProductById_WithNonExistentId_ShouldThrowException() {
        // Given
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productService.getProductById(999L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Producto no encontrado con ID: 999");

        verify(productRepository).findById(999L);
        verify(productMapper, never()).toResponseDTO(any(Product.class));
    }

    @Test
    @DisplayName("getAllProducts - Debería retornar todos los productos")
    void getAllProducts_ShouldReturnAllProducts() {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toResponseDTOList(products)).thenReturn(Arrays.asList(testResponseDTO));

        // When
        List<ProductResponseDTO> result = productService.getAllProducts();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Test Product");
        verify(productRepository).findAll();
        verify(productMapper).toResponseDTOList(products);
    }

    @Test
    @DisplayName("getAllProducts - Debería retornar productos paginados")
    void getAllProducts_WithPagination_ShouldReturnPaginatedProducts() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(Arrays.asList(testProduct));
        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(productMapper.toResponseDTO(testProduct)).thenReturn(testResponseDTO);

        // When
        Page<ProductResponseDTO> result = productService.getAllProducts(pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Test Product");
        verify(productRepository).findAll(pageable);
    }

    @Test
    @DisplayName("updateProduct - Debería actualizar producto exitosamente")
    void updateProduct_ShouldUpdateProductSuccessfully() {
        // Given
        Long productId = 1L;
        ProductUpdateDTO updateDTO = ProductUpdateDTO.builder()
                .name("Updated Product")
                .description("Updated Description")
                .unitPrice(BigDecimal.valueOf(150.00))
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(testProduct)).thenReturn(testProduct);
        when(productMapper.toResponseDTO(testProduct)).thenReturn(testResponseDTO);

        // When
        ProductResponseDTO result = productService.updateProduct(productId, updateDTO);

        // Then
        assertThat(result).isNotNull();
        verify(productRepository).findById(productId);
        verify(productMapper).updateEntityFromDTO(updateDTO, testProduct);
        verify(productRepository).save(testProduct);
        verify(productMapper).toResponseDTO(testProduct);
    }

    @Test
    @DisplayName("updateProduct - Debería lanzar excepción cuando producto no existe")
    void updateProduct_WithNonExistentId_ShouldThrowException() {
        // Given
        Long productId = 999L;
        ProductUpdateDTO updateDTO = ProductUpdateDTO.builder()
                .name("Updated Product")
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productService.updateProduct(productId, updateDTO))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Producto no encontrado con ID: 999");

        verify(productRepository).findById(productId);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("deleteProduct - Debería eliminar producto exitosamente")
    void deleteProduct_ShouldDeleteProductSuccessfully() {
        // Given
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.of(testProduct));

        // When
        productService.deleteProduct(productId);

        // Then
        verify(productRepository).findById(productId);
        verify(productRepository).delete(testProduct);
    }

    @Test
    @DisplayName("deleteProduct - Debería lanzar excepción cuando producto no existe")
    void deleteProduct_WithNonExistentId_ShouldThrowException() {
        // Given
        Long productId = 999L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productService.deleteProduct(productId))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Producto no encontrado con ID: 999");

        verify(productRepository).findById(productId);
        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    @DisplayName("searchProducts - Debería buscar productos por término")
    void searchProducts_ShouldSearchProductsByTerm() {
        // Given
        String searchTerm = "test";
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findByNameOrDescriptionContainingIgnoreCase(searchTerm))
                .thenReturn(products);
        when(productMapper.toResponseDTOList(any())).thenReturn(Arrays.asList(testResponseDTO));

        // When
        List<ProductResponseDTO> result = productService.searchProductsByText(searchTerm);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Test Product");
        verify(productRepository).findByNameOrDescriptionContainingIgnoreCase(searchTerm);
        verify(productMapper).toResponseDTOList(products);
    }

    @Test
    @DisplayName("getProductsByCategory - Debería retornar productos por categoría")
    void getProductsByCategory_ShouldReturnProductsByCategory() {
        // Given
        Long categoryId = 1L;
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findByCategoryIdCategoria(categoryId)).thenReturn(products);
        when(productMapper.toResponseDTOList(products)).thenReturn(Arrays.asList(testResponseDTO));

        // When
        List<ProductResponseDTO> result = productService.getProductsByCategory(categoryId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Test Product");
        verify(productRepository).findByCategoryIdCategoria(categoryId);
        verify(productMapper).toResponseDTOList(products);
    }

    @Test
    @DisplayName("getProductsByBrand - Debería retornar productos por marca")
    void getProductsByBrand_ShouldReturnProductsByBrand() {
        // Given
        Long brandId = 1L;
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findByBrandMarcaId(brandId)).thenReturn(products);
        when(productMapper.toResponseDTOList(products)).thenReturn(Arrays.asList(testResponseDTO));

        // When
        List<ProductResponseDTO> result = productService.getProductsByBrand(brandId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Test Product");
        verify(productRepository).findByBrandMarcaId(brandId);
        verify(productMapper).toResponseDTOList(products);
    }

    @Test
    @DisplayName("getProductsByStatus - Debería retornar productos por estado")
    void getProductsByStatus_ShouldReturnProductsByStatus() {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findByStatus(ProductStatus.ACTIVE)).thenReturn(products);
        when(productMapper.toResponseDTOList(products)).thenReturn(Arrays.asList(testResponseDTO));

        // When
        List<ProductResponseDTO> result = productService.getProductsByStatus(ProductStatus.ACTIVE);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(ProductStatus.ACTIVE);
        verify(productRepository).findByStatus(ProductStatus.ACTIVE);
        verify(productMapper).toResponseDTOList(products);
    }
}