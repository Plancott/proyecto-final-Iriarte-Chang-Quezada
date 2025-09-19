package org.example.stockms.model.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockFindProductDto {
    private Long productId;
    private List<StockResponseDto> movimientos;
    private Integer saldoFinal;
}
