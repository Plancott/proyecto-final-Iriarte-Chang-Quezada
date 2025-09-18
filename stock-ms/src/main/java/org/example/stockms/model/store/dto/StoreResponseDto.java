package org.example.stockms.model.store.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.stockms.model.stock.Stock;

import java.util.List;

@Getter
@Setter
public class StoreResponseDto {
    private Integer id;
    private String name;
    private Integer capacity;
    private Integer capacityTotal;
    private List<Stock> stocks;
}