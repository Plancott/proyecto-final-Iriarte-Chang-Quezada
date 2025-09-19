package org.example.stockms.mapper;

import org.example.stockms.model.stock.Stock;
import org.example.stockms.model.stock.dto.StockRequestDto;
import org.example.stockms.model.stock.dto.StockResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StockMapper {
    //StockRequestDto <- Stock
    StockRequestDto toStockRequestDto(Stock stock);

    //Stock <- StockRequestDto
    Stock toStockEntity(StockRequestDto stockRequestDto);

    //StockResponseEntity <- Stock
    @Mapping(target = "storeId", source = "store.id")
    StockResponseDto toStockResponseDto(Stock stock);

    //List<StockResponseDto> <- List<Stock>
    List<StockResponseDto> toListStockResponseDto(List<Stock> stocks);
}
