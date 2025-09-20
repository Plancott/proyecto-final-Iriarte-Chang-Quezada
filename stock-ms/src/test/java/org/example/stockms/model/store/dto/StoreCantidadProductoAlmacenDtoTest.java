package org.example.stockms.model.store.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StoreCantidadProductoAlmacenDto Tests")
class StoreCantidadProductoAlmacenDtoTest {

    private StoreCantidadProductoAlmacenDto storeCantidadProductoAlmacenDto;
    private StoreProductQuantityDto testProductQuantity;

    @BeforeEach
    void setUp() {
        storeCantidadProductoAlmacenDto = new StoreCantidadProductoAlmacenDto();
        testProductQuantity = new StoreProductQuantityDto(1L, 50L);
    }

    @Test
    @DisplayName("Should create StoreCantidadProductoAlmacenDto with default values")
    void testDefaultValues() {
        // Given & When
        StoreCantidadProductoAlmacenDto newDto = new StoreCantidadProductoAlmacenDto();

        // Then
        assertThat(newDto.getStoreId()).isNull();
        assertThat(newDto.getProductos()).isNull();
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void testSettersAndGetters() {
        // Given
        Long storeId = 1L;
        List<StoreProductQuantityDto> productos = new ArrayList<>(Arrays.asList(testProductQuantity));

        // When
        storeCantidadProductoAlmacenDto.setStoreId(storeId);
        storeCantidadProductoAlmacenDto.setProductos(productos);

        // Then
        assertThat(storeCantidadProductoAlmacenDto.getStoreId()).isEqualTo(storeId);
        assertThat(storeCantidadProductoAlmacenDto.getProductos()).isEqualTo(productos);
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testNullValues() {
        // Given & When
        storeCantidadProductoAlmacenDto.setStoreId(null);
        storeCantidadProductoAlmacenDto.setProductos(null);

        // Then
        assertThat(storeCantidadProductoAlmacenDto.getStoreId()).isNull();
        assertThat(storeCantidadProductoAlmacenDto.getProductos()).isNull();
    }

    @Test
    @DisplayName("Should handle constructor with parameters correctly")
    void testConstructorWithParameters() {
        // Given
        Long storeId = 1L;
        List<StoreProductQuantityDto> productos = new ArrayList<>(Arrays.asList(testProductQuantity));

        // When
        StoreCantidadProductoAlmacenDto dto = new StoreCantidadProductoAlmacenDto(storeId, productos);

        // Then
        assertThat(dto.getStoreId()).isEqualTo(storeId);
        assertThat(dto.getProductos()).isEqualTo(productos);
    }

    @Test
    @DisplayName("Should handle constructor with null parameters correctly")
    void testConstructorWithNullParameters() {
        // When
        StoreCantidadProductoAlmacenDto dto = new StoreCantidadProductoAlmacenDto(null, null);

        // Then
        assertThat(dto.getStoreId()).isNull();
        assertThat(dto.getProductos()).isNull();
    }

    @Test
    @DisplayName("Should handle different store IDs correctly")
    void testStoreIdValues() {
        // Test positive IDs
        storeCantidadProductoAlmacenDto.setStoreId(1L);
        assertThat(storeCantidadProductoAlmacenDto.getStoreId()).isEqualTo(1L);

        storeCantidadProductoAlmacenDto.setStoreId(Long.MAX_VALUE);
        assertThat(storeCantidadProductoAlmacenDto.getStoreId()).isEqualTo(Long.MAX_VALUE);

        // Test zero
        storeCantidadProductoAlmacenDto.setStoreId(0L);
        assertThat(storeCantidadProductoAlmacenDto.getStoreId()).isEqualTo(0L);

        // Test negative (edge case)
        storeCantidadProductoAlmacenDto.setStoreId(-1L);
        assertThat(storeCantidadProductoAlmacenDto.getStoreId()).isEqualTo(-1L);
    }

    @Test
    @DisplayName("Should handle productos list correctly")
    void testProductosList() {
        // Test empty list
        storeCantidadProductoAlmacenDto.setProductos(new ArrayList<>());
        assertThat(storeCantidadProductoAlmacenDto.getProductos()).isNotNull();
        assertThat(storeCantidadProductoAlmacenDto.getProductos()).isEmpty();

        // Test list with one product
        List<StoreProductQuantityDto> singleProduct = new ArrayList<>(Arrays.asList(testProductQuantity));
        storeCantidadProductoAlmacenDto.setProductos(singleProduct);
        assertThat(storeCantidadProductoAlmacenDto.getProductos()).hasSize(1);
        assertThat(storeCantidadProductoAlmacenDto.getProductos().get(0)).isEqualTo(testProductQuantity);

        // Test list with multiple products
        StoreProductQuantityDto product2 = new StoreProductQuantityDto(2L, 30L);
        StoreProductQuantityDto product3 = new StoreProductQuantityDto(3L, 25L);
        List<StoreProductQuantityDto> multipleProducts = new ArrayList<>(Arrays.asList(testProductQuantity, product2, product3));
        storeCantidadProductoAlmacenDto.setProductos(multipleProducts);
        assertThat(storeCantidadProductoAlmacenDto.getProductos()).hasSize(3);
        assertThat(storeCantidadProductoAlmacenDto.getProductos()).contains(testProductQuantity, product2, product3);
    }

    @Test
    @DisplayName("Should handle product operations correctly")
    void testProductOperations() {
        // Given
        List<StoreProductQuantityDto> products = new ArrayList<>();
        storeCantidadProductoAlmacenDto.setProductos(products);

        // Test adding products
        products.add(testProductQuantity);
        assertThat(storeCantidadProductoAlmacenDto.getProductos()).hasSize(1);

        StoreProductQuantityDto product2 = new StoreProductQuantityDto(2L, 30L);
        products.add(product2);
        assertThat(storeCantidadProductoAlmacenDto.getProductos()).hasSize(2);

        // Test removing products
        products.remove(testProductQuantity);
        assertThat(storeCantidadProductoAlmacenDto.getProductos()).hasSize(1);
        assertThat(storeCantidadProductoAlmacenDto.getProductos()).contains(product2);

        // Test clearing products
        products.clear();
        assertThat(storeCantidadProductoAlmacenDto.getProductos()).isEmpty();
    }

    @Test
    @DisplayName("Should create complete StoreCantidadProductoAlmacenDto object correctly")
    void testCompleteObject() {
        // Given
        Long storeId = 999L;
        StoreProductQuantityDto product1 = new StoreProductQuantityDto(1L, 50L);
        StoreProductQuantityDto product2 = new StoreProductQuantityDto(2L, 75L);
        StoreProductQuantityDto product3 = new StoreProductQuantityDto(3L, 25L);
        List<StoreProductQuantityDto> productos = new ArrayList<>(Arrays.asList(product1, product2, product3));

        // When
        storeCantidadProductoAlmacenDto.setStoreId(storeId);
        storeCantidadProductoAlmacenDto.setProductos(productos);

        // Then
        assertThat(storeCantidadProductoAlmacenDto.getStoreId()).isEqualTo(storeId);
        assertThat(storeCantidadProductoAlmacenDto.getProductos()).isEqualTo(productos);
        assertThat(storeCantidadProductoAlmacenDto.getProductos()).hasSize(3);
    }

    @Test
    @DisplayName("Should handle edge cases correctly")
    void testEdgeCases() {
        // Test with maximum values
        storeCantidadProductoAlmacenDto.setStoreId(Long.MAX_VALUE);

        assertThat(storeCantidadProductoAlmacenDto.getStoreId()).isEqualTo(Long.MAX_VALUE);

        // Test with minimum values
        storeCantidadProductoAlmacenDto.setStoreId(Long.MIN_VALUE);

        assertThat(storeCantidadProductoAlmacenDto.getStoreId()).isEqualTo(Long.MIN_VALUE);
    }

    @Test
    @DisplayName("Should handle toString method correctly")
    void testToString() {
        // Given
        storeCantidadProductoAlmacenDto.setStoreId(1L);
        storeCantidadProductoAlmacenDto.setProductos(new ArrayList<>(Arrays.asList(testProductQuantity)));

        // When
        String toString = storeCantidadProductoAlmacenDto.toString();

        // Then
        assertThat(toString).isNotNull();
        assertThat(toString).contains("StoreCantidadProductoAlmacenDto");
        // Note: DTOs use default Object.toString() implementation, so we only verify it's not null and contains class name
    }

    @Test
    @DisplayName("Should handle validation scenarios correctly")
    void testValidationScenarios() {
        // Test valid positive values
        storeCantidadProductoAlmacenDto.setStoreId(1L);

        assertThat(storeCantidadProductoAlmacenDto.getStoreId()).isPositive();

        // Test boundary values
        storeCantidadProductoAlmacenDto.setStoreId(0L);
        assertThat(storeCantidadProductoAlmacenDto.getStoreId()).isEqualTo(0L);

        storeCantidadProductoAlmacenDto.setStoreId(-1L);
        assertThat(storeCantidadProductoAlmacenDto.getStoreId()).isNegative();
    }

    @Test
    @DisplayName("Should maintain object state correctly")
    void testObjectState() {
        // Set initial values
        storeCantidadProductoAlmacenDto.setStoreId(100L);
        storeCantidadProductoAlmacenDto.setProductos(new ArrayList<>(Arrays.asList(testProductQuantity)));

        // Verify state
        assertThat(storeCantidadProductoAlmacenDto.getStoreId()).isEqualTo(100L);
        assertThat(storeCantidadProductoAlmacenDto.getProductos()).hasSize(1);

        // Change one property and verify the other remains unchanged
        storeCantidadProductoAlmacenDto.setStoreId(200L);
        assertThat(storeCantidadProductoAlmacenDto.getStoreId()).isEqualTo(200L);
        assertThat(storeCantidadProductoAlmacenDto.getProductos()).hasSize(1); // Should remain unchanged

        // Change the other property and verify the first remains unchanged
        StoreProductQuantityDto product2 = new StoreProductQuantityDto(2L, 30L);
        storeCantidadProductoAlmacenDto.setProductos(new ArrayList<>(Arrays.asList(product2)));
        assertThat(storeCantidadProductoAlmacenDto.getStoreId()).isEqualTo(200L); // Should remain unchanged
        assertThat(storeCantidadProductoAlmacenDto.getProductos()).hasSize(1);
    }

    @Test
    @DisplayName("Should handle multiple updates correctly")
    void testMultipleUpdates() {
        // Test multiple updates of store ID
        storeCantidadProductoAlmacenDto.setStoreId(1L);
        assertThat(storeCantidadProductoAlmacenDto.getStoreId()).isEqualTo(1L);

        storeCantidadProductoAlmacenDto.setStoreId(2L);
        assertThat(storeCantidadProductoAlmacenDto.getStoreId()).isEqualTo(2L);

        storeCantidadProductoAlmacenDto.setStoreId(3L);
        assertThat(storeCantidadProductoAlmacenDto.getStoreId()).isEqualTo(3L);

        // Test multiple updates of products list
        storeCantidadProductoAlmacenDto.setProductos(new ArrayList<>(Arrays.asList(testProductQuantity)));
        assertThat(storeCantidadProductoAlmacenDto.getProductos()).hasSize(1);

        StoreProductQuantityDto product2 = new StoreProductQuantityDto(2L, 30L);
        storeCantidadProductoAlmacenDto.setProductos(new ArrayList<>(Arrays.asList(product2)));
        assertThat(storeCantidadProductoAlmacenDto.getProductos()).hasSize(1);

        storeCantidadProductoAlmacenDto.setProductos(new ArrayList<>(Arrays.asList(testProductQuantity, product2)));
        assertThat(storeCantidadProductoAlmacenDto.getProductos()).hasSize(2);
    }

    @Test
    @DisplayName("Should handle large product lists correctly")
    void testLargeProductLists() {
        // Create a large list of products
        List<StoreProductQuantityDto> largeProductList = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            largeProductList.add(new StoreProductQuantityDto((long) i, (long) i * 10));
        }

        storeCantidadProductoAlmacenDto.setProductos(largeProductList);
        assertThat(storeCantidadProductoAlmacenDto.getProductos()).hasSize(1000);
        assertThat(storeCantidadProductoAlmacenDto.getProductos().get(0).getProductId()).isEqualTo(1L);
        assertThat(storeCantidadProductoAlmacenDto.getProductos().get(999).getProductId()).isEqualTo(1000L);
    }
}
