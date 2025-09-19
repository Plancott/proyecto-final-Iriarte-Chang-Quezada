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

    public StoreResponseDto(Integer id, String name, Integer capacity, Integer capacityTotal, List<Stock> stocks) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.capacityTotal = capacityTotal;
        this.stocks = stocks;
    }

    public StoreResponseDto() {}
}