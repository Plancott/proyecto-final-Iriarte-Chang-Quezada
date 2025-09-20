package com.microservices.mapper;

import com.microservices.model.ProductEntity;
import com.microservices.model.dto.ProductDto;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {
    ProductDto toDto(ProductEntity e);
    ProductEntity toEntity(ProductDto d);
}
