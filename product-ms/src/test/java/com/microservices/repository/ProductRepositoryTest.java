package com.microservices.repository;

import com.microservices.entity.Brand;
import com.microservices.entity.Category;
import com.microservices.entity.Product;
import com.microservices.entity.ProductStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("ProductRepository Integration Tests")
class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Brand testBrand;
    private Category testCategory;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        // Crear marca
        testBrand = Brand.builder()
                .nombreMarca("Test Brand")
                .build();
        testBrand = brandRepository.save(testBrand);

        // Crear categoría
        testCategory = Category.builder()
                .nombreCategoria("Test Category")
                .build();
        testCategory = categoryRepository.save(testCategory);

        // Crear producto
        testProduct = Product.builder()
                .name("Test Product")
                .description("Test Description")
                .unitPrice(BigDecimal.valueOf(100.00))
                .imageUrl("https://example.com/image.jpg")
                .status(ProductStatus.ACTIVE)
                .brand(testBrand)
                .category(testCategory)
                .build();
        testProduct = productRepository.save(testProduct);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("findById - Debería encontrar producto por ID")
    void findById_ShouldFindProductById() {
        // When
        Optional<Product> result = productRepository.findById(testProduct.getProductId());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Test Product");
        assertThat(result.get().getStatus()).isEqualTo(ProductStatus.ACTIVE);
    }

    @Test
    @DisplayName("findById - Debería retornar empty cuando producto no existe")
    void findById_WithNonExistentId_ShouldReturnEmpty() {
        // When
        Optional<Product> result = productRepository.findById(999L);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findAll - Debería retornar todos los productos")
    void findAll_ShouldReturnAllProducts() {
        // Given
        Product anotherProduct = Product.builder()
                .name("Another Product")
                .description("Another Description")
                .unitPrice(BigDecimal.valueOf(200.00))
                .status(ProductStatus.ACTIVE)
                .brand(testBrand)
                .category(testCategory)
                .build();
        productRepository.save(anotherProduct);

        // When
        List<Product> result = productRepository.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Product::getName)
                .containsExactlyInAnyOrder("Test Product", "Another Product");
    }

    @Test
    @DisplayName("findAll - Debería retornar productos paginados")
    void findAll_WithPagination_ShouldReturnPaginatedProducts() {
        // Given
        Product anotherProduct = Product.builder()
                .name("Another Product")
                .description("Another Description")
                .unitPrice(BigDecimal.valueOf(200.00))
                .status(ProductStatus.ACTIVE)
                .brand(testBrand)
                .category(testCategory)
                .build();
        productRepository.save(anotherProduct);

        Pageable pageable = PageRequest.of(0, 1);

        // When
        Page<Product> result = productRepository.findAll(pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("findByNameContainingIgnoreCase - Debería buscar por nombre")
    void findByNameContainingIgnoreCase_ShouldSearchByName() {
        // Given
        String searchTerm = "Test";

        // When
        List<Product> result = productRepository.findByNameContainingIgnoreCase(searchTerm);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Test Product");
    }

    @Test
    @DisplayName("findByDescriptionContainingIgnoreCase - Debería buscar por descripción")
    void findByDescriptionContainingIgnoreCase_ShouldSearchByDescription() {
        // Given
        String searchTerm = "Description";

        // When
        List<Product> result = productRepository.findByDescriptionContainingIgnoreCase(searchTerm);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDescription()).isEqualTo("Test Description");
    }

    @Test
    @DisplayName("findByCategoryId - Debería retornar productos por categoría")
    void findByCategoryId_ShouldReturnProductsByCategory() {
        // Given
        Product anotherProduct = Product.builder()
                .name("Another Product")
                .description("Another Description")
                .unitPrice(BigDecimal.valueOf(200.00))
                .status(ProductStatus.ACTIVE)
                .brand(testBrand)
                .category(testCategory)
                .build();
        productRepository.save(anotherProduct);

        // When
        List<Product> result = productRepository.findByCategoryIdCategoria(testCategory.getIdCategoria());

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Product::getName)
                .containsExactlyInAnyOrder("Test Product", "Another Product");
    }

    @Test
    @DisplayName("findByBrandId - Debería retornar productos por marca")
    void findByBrandId_ShouldReturnProductsByBrand() {
        // Given
        Product anotherProduct = Product.builder()
                .name("Another Product")
                .description("Another Description")
                .unitPrice(BigDecimal.valueOf(200.00))
                .status(ProductStatus.ACTIVE)
                .brand(testBrand)
                .category(testCategory)
                .build();
        productRepository.save(anotherProduct);

        // When
        List<Product> result = productRepository.findByBrandMarcaId(testBrand.getMarcaId());

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Product::getName)
                .containsExactlyInAnyOrder("Test Product", "Another Product");
    }

    @Test
    @DisplayName("findByStatus - Debería retornar productos por estado")
    void findByStatus_ShouldReturnProductsByStatus() {
        // Given
        Product inactiveProduct = Product.builder()
                .name("Inactive Product")
                .description("Inactive Description")
                .unitPrice(BigDecimal.valueOf(200.00))
                .status(ProductStatus.INACTIVE)
                .brand(testBrand)
                .category(testCategory)
                .build();
        productRepository.save(inactiveProduct);

        // When
        List<Product> activeProducts = productRepository.findByStatus(ProductStatus.ACTIVE);
        List<Product> inactiveProducts = productRepository.findByStatus(ProductStatus.INACTIVE);

        // Then
        assertThat(activeProducts).hasSize(1);
        assertThat(activeProducts.get(0).getStatus()).isEqualTo(ProductStatus.ACTIVE);

        assertThat(inactiveProducts).hasSize(1);
        assertThat(inactiveProducts.get(0).getStatus()).isEqualTo(ProductStatus.INACTIVE);
    }

    @Test
    @DisplayName("save - Debería guardar producto correctamente")
    void save_ShouldSaveProductCorrectly() {
        // Given
        Product newProduct = Product.builder()
                .name("New Product")
                .description("New Description")
                .unitPrice(BigDecimal.valueOf(300.00))
                .status(ProductStatus.ACTIVE)
                .brand(testBrand)
                .category(testCategory)
                .build();

        // When
        Product savedProduct = productRepository.save(newProduct);

        // Then
        assertThat(savedProduct.getProductId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo("New Product");
        assertThat(productRepository.findById(savedProduct.getProductId())).isPresent();
    }

    @Test
    @DisplayName("delete - Debería eliminar producto correctamente")
    void delete_ShouldDeleteProductCorrectly() {
        // Given
        Long productId = testProduct.getProductId();

        // When
        productRepository.delete(testProduct);

        // Then
        assertThat(productRepository.findById(productId)).isEmpty();
    }

    @Test
    @DisplayName("count - Debería contar productos correctamente")
    void count_ShouldCountProductsCorrectly() {
        // Given
        Product anotherProduct = Product.builder()
                .name("Another Product")
                .description("Another Description")
                .unitPrice(BigDecimal.valueOf(200.00))
                .status(ProductStatus.ACTIVE)
                .brand(testBrand)
                .category(testCategory)
                .build();
        productRepository.save(anotherProduct);

        // When
        long count = productRepository.count();

        // Then
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("existsById - Debería verificar existencia de producto")
    void existsById_ShouldCheckProductExistence() {
        // When
        boolean exists = productRepository.existsById(testProduct.getProductId());
        boolean notExists = productRepository.existsById(999L);

        // Then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}