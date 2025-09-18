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
}
