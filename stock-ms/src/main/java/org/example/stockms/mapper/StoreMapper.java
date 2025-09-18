package org.example.stockms.mapper;

import org.example.stockms.model.store.Store;
import org.example.stockms.model.store.dto.StoreRequestDto;
import org.example.stockms.model.store.dto.StoreResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StoreMapper {
    //Store <- StoreRequestDto
    Store toStore(StoreRequestDto storeRequestDto);

    //List<StoreResponseDto> <- List<Store>
    List<StoreResponseDto> toListResponseDto(List<Store> stores);

    //StoreResponseDto <- Store
    StoreResponseDto toStoreResponseDto(Store store);
}
