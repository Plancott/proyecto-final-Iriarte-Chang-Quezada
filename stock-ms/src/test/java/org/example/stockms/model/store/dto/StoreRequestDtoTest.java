package org.example.stockms.model.store.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StoreRequestDto Tests")
class StoreRequestDtoTest {

    private StoreRequestDto storeRequestDto;

    @BeforeEach
    void setUp() {
        storeRequestDto = new StoreRequestDto();
    }

    @Test
    @DisplayName("Should create StoreRequestDto with default values")
    void testDefaultValues() {
        // Given & When
        StoreRequestDto newDto = new StoreRequestDto();

        // Then
        assertThat(newDto.getName()).isNull();
        assertThat(newDto.getCapacityTotal()).isNull();
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void testSettersAndGetters() {
        // Given
        String name = "Test Store";
        Integer capacityTotal = 100;

        // When
        storeRequestDto.setName(name);
        storeRequestDto.setCapacityTotal(capacityTotal);

        // Then
        assertThat(storeRequestDto.getName()).isEqualTo(name);
        assertThat(storeRequestDto.getCapacityTotal()).isEqualTo(capacityTotal);
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testNullValues() {
        // Given & When
        storeRequestDto.setName(null);
        storeRequestDto.setCapacityTotal(null);

        // Then
        assertThat(storeRequestDto.getName()).isNull();
        assertThat(storeRequestDto.getCapacityTotal()).isNull();
    }

    @Test
    @DisplayName("Should handle different store names correctly")
    void testStoreNames() {
        // Test normal names
        storeRequestDto.setName("Store 1");
        assertThat(storeRequestDto.getName()).isEqualTo("Store 1");

        storeRequestDto.setName("Almacén Principal");
        assertThat(storeRequestDto.getName()).isEqualTo("Almacén Principal");

        storeRequestDto.setName("Warehouse-123");
        assertThat(storeRequestDto.getName()).isEqualTo("Warehouse-123");

        // Test empty string
        storeRequestDto.setName("");
        assertThat(storeRequestDto.getName()).isEqualTo("");

        // Test long name
        String longName = "A".repeat(1000);
        storeRequestDto.setName(longName);
        assertThat(storeRequestDto.getName()).isEqualTo(longName);
    }

    @Test
    @DisplayName("Should handle different capacity total values correctly")
    void testCapacityTotalValues() {
        // Test positive capacity totals
        storeRequestDto.setCapacityTotal(1);
        assertThat(storeRequestDto.getCapacityTotal()).isEqualTo(1);

        storeRequestDto.setCapacityTotal(1000);
        assertThat(storeRequestDto.getCapacityTotal()).isEqualTo(1000);

        storeRequestDto.setCapacityTotal(Integer.MAX_VALUE);
        assertThat(storeRequestDto.getCapacityTotal()).isEqualTo(Integer.MAX_VALUE);

        // Test zero
        storeRequestDto.setCapacityTotal(0);
        assertThat(storeRequestDto.getCapacityTotal()).isEqualTo(0);

        // Test negative (edge case)
        storeRequestDto.setCapacityTotal(-50);
        assertThat(storeRequestDto.getCapacityTotal()).isEqualTo(-50);
    }

    @Test
    @DisplayName("Should create complete StoreRequestDto object correctly")
    void testCompleteObject() {
        // Given
        String name = "Complete Store";
        Integer capacityTotal = 200;

        // When
        storeRequestDto.setName(name);
        storeRequestDto.setCapacityTotal(capacityTotal);

        // Then
        assertThat(storeRequestDto.getName()).isEqualTo(name);
        assertThat(storeRequestDto.getCapacityTotal()).isEqualTo(capacityTotal);
    }

    @Test
    @DisplayName("Should handle edge cases correctly")
    void testEdgeCases() {
        // Test with maximum values
        storeRequestDto.setName("Max Store");
        storeRequestDto.setCapacityTotal(Integer.MAX_VALUE);

        assertThat(storeRequestDto.getName()).isEqualTo("Max Store");
        assertThat(storeRequestDto.getCapacityTotal()).isEqualTo(Integer.MAX_VALUE);

        // Test with minimum values
        storeRequestDto.setCapacityTotal(Integer.MIN_VALUE);

        assertThat(storeRequestDto.getCapacityTotal()).isEqualTo(Integer.MIN_VALUE);
    }

    @Test
    @DisplayName("Should handle special characters in names correctly")
    void testSpecialCharactersInNames() {
        // Test with special characters
        String specialName = "Store-with_special.chars@123";
        storeRequestDto.setName(specialName);
        assertThat(storeRequestDto.getName()).isEqualTo(specialName);

        // Test with unicode characters
        String unicodeName = "Almacén de Productos 仓库";
        storeRequestDto.setName(unicodeName);
        assertThat(storeRequestDto.getName()).isEqualTo(unicodeName);

        // Test with numbers in name
        String numericName = "Store123";
        storeRequestDto.setName(numericName);
        assertThat(storeRequestDto.getName()).isEqualTo(numericName);
    }

    @Test
    @DisplayName("Should handle validation scenarios correctly")
    void testValidationScenarios() {
        // Test valid positive values
        storeRequestDto.setName("Valid Store");
        storeRequestDto.setCapacityTotal(1);

        assertThat(storeRequestDto.getName()).isNotBlank();
        assertThat(storeRequestDto.getCapacityTotal()).isPositive();

        // Test boundary values
        storeRequestDto.setCapacityTotal(0);
        assertThat(storeRequestDto.getCapacityTotal()).isEqualTo(0);

        storeRequestDto.setCapacityTotal(-1);
        assertThat(storeRequestDto.getCapacityTotal()).isNegative();
    }

    @Test
    @DisplayName("Should maintain object state correctly")
    void testObjectState() {
        // Set initial values
        storeRequestDto.setName("Initial Store");
        storeRequestDto.setCapacityTotal(100);

        // Verify state
        assertThat(storeRequestDto.getName()).isEqualTo("Initial Store");
        assertThat(storeRequestDto.getCapacityTotal()).isEqualTo(100);

        // Change one property and verify the other remains unchanged
        storeRequestDto.setName("Updated Store");
        assertThat(storeRequestDto.getName()).isEqualTo("Updated Store");
        assertThat(storeRequestDto.getCapacityTotal()).isEqualTo(100); // Should remain unchanged

        // Change the other property and verify the first remains unchanged
        storeRequestDto.setCapacityTotal(200);
        assertThat(storeRequestDto.getName()).isEqualTo("Updated Store"); // Should remain unchanged
        assertThat(storeRequestDto.getCapacityTotal()).isEqualTo(200);
    }

    @Test
    @DisplayName("Should handle multiple updates correctly")
    void testMultipleUpdates() {
        // Test multiple updates of name
        storeRequestDto.setName("Store 1");
        assertThat(storeRequestDto.getName()).isEqualTo("Store 1");

        storeRequestDto.setName("Store 2");
        assertThat(storeRequestDto.getName()).isEqualTo("Store 2");

        storeRequestDto.setName("Store 3");
        assertThat(storeRequestDto.getName()).isEqualTo("Store 3");

        // Test multiple updates of capacity total
        storeRequestDto.setCapacityTotal(10);
        assertThat(storeRequestDto.getCapacityTotal()).isEqualTo(10);

        storeRequestDto.setCapacityTotal(20);
        assertThat(storeRequestDto.getCapacityTotal()).isEqualTo(20);

        storeRequestDto.setCapacityTotal(30);
        assertThat(storeRequestDto.getCapacityTotal()).isEqualTo(30);
    }
}
