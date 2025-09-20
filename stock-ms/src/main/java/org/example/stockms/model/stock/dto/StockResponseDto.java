package org.example.stockms.model.stock.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class StockResponseDto {
    private Long id;
    private Long productId;
    private Integer quantity;
    private Long storeId;
    private String state;
    private LocalDateTime date;

    // Constructor completo
    public StockResponseDto(Long id, Long productId, Integer quantity, Long storeId, String state, LocalDateTime date) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.storeId = storeId;
        this.state = state;
        this.date = date;
    }
    // Constructor vacío
    public StockResponseDto() {}
}
