package org.example.stockms.model.stock.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StockFindProductDto Tests")
class StockFindProductDtoTest {

    private StockFindProductDto stockFindProductDto;
    private StockResponseDto testStockResponse;

    @BeforeEach
    void setUp() {
        stockFindProductDto = new StockFindProductDto();
        testStockResponse = createStockResponseDto(1L, 1L, 50, 1L, "entrada", LocalDateTime.now());
    }

    @Test
    @DisplayName("Should create StockFindProductDto with default values")
    void testDefaultValues() {
        // Given & When
        StockFindProductDto newDto = new StockFindProductDto();

        // Then
        assertThat(newDto.getProductId()).isNull();
        assertThat(newDto.getMovimientos()).isNull();
        assertThat(newDto.getSaldoFinal()).isNull();
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void testSettersAndGetters() {
        // Given
        Long productId = 123L;
        List<StockResponseDto> movimientos = new ArrayList<>(Arrays.asList(testStockResponse));
        Integer saldoFinal = 50;

        // When
        stockFindProductDto.setProductId(productId);
        stockFindProductDto.setMovimientos(movimientos);
        stockFindProductDto.setSaldoFinal(saldoFinal);

        // Then
        assertThat(stockFindProductDto.getProductId()).isEqualTo(productId);
        assertThat(stockFindProductDto.getMovimientos()).isEqualTo(movimientos);
        assertThat(stockFindProductDto.getSaldoFinal()).isEqualTo(saldoFinal);
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testNullValues() {
        // Given & When
        stockFindProductDto.setProductId(null);
        stockFindProductDto.setMovimientos(null);
        stockFindProductDto.setSaldoFinal(null);

        // Then
        assertThat(stockFindProductDto.getProductId()).isNull();
        assertThat(stockFindProductDto.getMovimientos()).isNull();
        assertThat(stockFindProductDto.getSaldoFinal()).isNull();
    }

    @Test
    @DisplayName("Should handle constructor with parameters correctly")
    void testConstructorWithParameters() {
        // Given
        Long productId = 123L;
        List<StockResponseDto> movimientos = new ArrayList<>(Arrays.asList(testStockResponse));
        Integer saldoFinal = 50;

        // When
        StockFindProductDto dto = new StockFindProductDto(productId, movimientos, saldoFinal);

        // Then
        assertThat(dto.getProductId()).isEqualTo(productId);
        assertThat(dto.getMovimientos()).isEqualTo(movimientos);
        assertThat(dto.getSaldoFinal()).isEqualTo(saldoFinal);
    }

    @Test
    @DisplayName("Should handle constructor with null parameters correctly")
    void testConstructorWithNullParameters() {
        // When
        StockFindProductDto dto = new StockFindProductDto(null, null, null);

        // Then
        assertThat(dto.getProductId()).isNull();
        assertThat(dto.getMovimientos()).isNull();
        assertThat(dto.getSaldoFinal()).isNull();
    }

    @Test
    @DisplayName("Should handle different product IDs correctly")
    void testProductIdValues() {
        // Test positive IDs
        stockFindProductDto.setProductId(1L);
        assertThat(stockFindProductDto.getProductId()).isEqualTo(1L);

        stockFindProductDto.setProductId(Long.MAX_VALUE);
        assertThat(stockFindProductDto.getProductId()).isEqualTo(Long.MAX_VALUE);

        // Test zero
        stockFindProductDto.setProductId(0L);
        assertThat(stockFindProductDto.getProductId()).isEqualTo(0L);

        // Test negative (edge case)
        stockFindProductDto.setProductId(-1L);
        assertThat(stockFindProductDto.getProductId()).isEqualTo(-1L);
    }

    @Test
    @DisplayName("Should handle different saldo final values correctly")
    void testSaldoFinalValues() {
        // Test positive values
        stockFindProductDto.setSaldoFinal(1);
        assertThat(stockFindProductDto.getSaldoFinal()).isEqualTo(1);

        stockFindProductDto.setSaldoFinal(100);
        assertThat(stockFindProductDto.getSaldoFinal()).isEqualTo(100);

        stockFindProductDto.setSaldoFinal(Integer.MAX_VALUE);
        assertThat(stockFindProductDto.getSaldoFinal()).isEqualTo(Integer.MAX_VALUE);

        // Test zero
        stockFindProductDto.setSaldoFinal(0);
        assertThat(stockFindProductDto.getSaldoFinal()).isEqualTo(0);

        // Test negative (edge case)
        stockFindProductDto.setSaldoFinal(-10);
        assertThat(stockFindProductDto.getSaldoFinal()).isEqualTo(-10);
    }

    @Test
    @DisplayName("Should handle movimientos list correctly")
    void testMovimientosList() {
        // Test empty list
        stockFindProductDto.setMovimientos(new ArrayList<>());
        assertThat(stockFindProductDto.getMovimientos()).isNotNull();
        assertThat(stockFindProductDto.getMovimientos()).isEmpty();

        // Test list with one movement
        List<StockResponseDto> singleMovement = new ArrayList<>(Arrays.asList(testStockResponse));
        stockFindProductDto.setMovimientos(singleMovement);
        assertThat(stockFindProductDto.getMovimientos()).hasSize(1);
        assertThat(stockFindProductDto.getMovimientos().get(0)).isEqualTo(testStockResponse);

        // Test list with multiple movements
        StockResponseDto movement2 = createStockResponseDto(2L, 1L, 30, 1L, "salida", LocalDateTime.now());
        StockResponseDto movement3 = createStockResponseDto(3L, 1L, 25, 1L, "entrada", LocalDateTime.now());
        List<StockResponseDto> multipleMovements = new ArrayList<>(Arrays.asList(testStockResponse, movement2, movement3));
        stockFindProductDto.setMovimientos(multipleMovements);
        assertThat(stockFindProductDto.getMovimientos()).hasSize(3);
        assertThat(stockFindProductDto.getMovimientos()).contains(testStockResponse, movement2, movement3);
    }

    @Test
    @DisplayName("Should handle movements operations correctly")
    void testMovementsOperations() {
        // Given
        List<StockResponseDto> movements = new ArrayList<>();
        stockFindProductDto.setMovimientos(movements);

        // Test adding movements
        movements.add(testStockResponse);
        assertThat(stockFindProductDto.getMovimientos()).hasSize(1);

        StockResponseDto movement2 = createStockResponseDto(2L, 1L, 30, 1L, "salida", LocalDateTime.now());
        movements.add(movement2);
        assertThat(stockFindProductDto.getMovimientos()).hasSize(2);

        // Test removing movements
        movements.remove(testStockResponse);
        assertThat(stockFindProductDto.getMovimientos()).hasSize(1);
        assertThat(stockFindProductDto.getMovimientos()).contains(movement2);

        // Test clearing movements
        movements.clear();
        assertThat(stockFindProductDto.getMovimientos()).isEmpty();
    }

    @Test
    @DisplayName("Should create complete StockFindProductDto object correctly")
    void testCompleteObject() {
        // Given
        Long productId = 999L;
        StockResponseDto movement1 = createStockResponseDto(1L, productId, 50, 1L, "entrada", LocalDateTime.now());
        StockResponseDto movement2 = createStockResponseDto(2L, productId, 25, 1L, "salida", LocalDateTime.now());
        List<StockResponseDto> movimientos = new ArrayList<>(Arrays.asList(movement1, movement2));
        Integer saldoFinal = 25; // 50 - 25

        // When
        stockFindProductDto.setProductId(productId);
        stockFindProductDto.setMovimientos(movimientos);
        stockFindProductDto.setSaldoFinal(saldoFinal);

        // Then
        assertThat(stockFindProductDto.getProductId()).isEqualTo(productId);
        assertThat(stockFindProductDto.getMovimientos()).isEqualTo(movimientos);
        assertThat(stockFindProductDto.getSaldoFinal()).isEqualTo(saldoFinal);
        assertThat(stockFindProductDto.getMovimientos()).hasSize(2);
    }

    @Test
    @DisplayName("Should handle edge cases correctly")
    void testEdgeCases() {
        // Test with maximum values
        stockFindProductDto.setProductId(Long.MAX_VALUE);
        stockFindProductDto.setSaldoFinal(Integer.MAX_VALUE);

        assertThat(stockFindProductDto.getProductId()).isEqualTo(Long.MAX_VALUE);
        assertThat(stockFindProductDto.getSaldoFinal()).isEqualTo(Integer.MAX_VALUE);

        // Test with minimum values
        stockFindProductDto.setProductId(Long.MIN_VALUE);
        stockFindProductDto.setSaldoFinal(Integer.MIN_VALUE);

        assertThat(stockFindProductDto.getProductId()).isEqualTo(Long.MIN_VALUE);
        assertThat(stockFindProductDto.getSaldoFinal()).isEqualTo(Integer.MIN_VALUE);
    }

    @Test
    @DisplayName("Should handle toString method correctly")
    void testToString() {
        // Given
        stockFindProductDto.setProductId(1L);
        stockFindProductDto.setSaldoFinal(50);
        stockFindProductDto.setMovimientos(new ArrayList<>(Arrays.asList(testStockResponse)));

        // When
        String toString = stockFindProductDto.toString();

        // Then
        assertThat(toString).isNotNull();
        assertThat(toString).contains("StockFindProductDto");
        // Note: DTOs use default Object.toString() implementation, so we only verify it's not null and contains class name
    }

    @Test
    @DisplayName("Should handle validation scenarios correctly")
    void testValidationScenarios() {
        // Test valid positive values
        stockFindProductDto.setProductId(1L);
        stockFindProductDto.setSaldoFinal(1);

        assertThat(stockFindProductDto.getProductId()).isPositive();
        assertThat(stockFindProductDto.getSaldoFinal()).isPositive();

        // Test boundary values
        stockFindProductDto.setSaldoFinal(0);
        assertThat(stockFindProductDto.getSaldoFinal()).isEqualTo(0);

        stockFindProductDto.setSaldoFinal(-1);
        assertThat(stockFindProductDto.getSaldoFinal()).isNegative();
    }

    @Test
    @DisplayName("Should maintain object state correctly")
    void testObjectState() {
        // Set initial values
        stockFindProductDto.setProductId(100L);
        stockFindProductDto.setSaldoFinal(75);
        stockFindProductDto.setMovimientos(new ArrayList<>(Arrays.asList(testStockResponse)));

        // Verify state
        assertThat(stockFindProductDto.getProductId()).isEqualTo(100L);
        assertThat(stockFindProductDto.getSaldoFinal()).isEqualTo(75);
        assertThat(stockFindProductDto.getMovimientos()).hasSize(1);

        // Change one property and verify others remain unchanged
        stockFindProductDto.setProductId(200L);
        assertThat(stockFindProductDto.getProductId()).isEqualTo(200L);
        assertThat(stockFindProductDto.getSaldoFinal()).isEqualTo(75); // Should remain unchanged
        assertThat(stockFindProductDto.getMovimientos()).hasSize(1); // Should remain unchanged
    }

    // Helper method
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
}
