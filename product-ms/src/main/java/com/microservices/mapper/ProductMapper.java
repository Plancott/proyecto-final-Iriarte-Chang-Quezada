package com.microservices.mapper;

import com.microservices.dto.*;
import com.microservices.entity.Product;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    
    // Product -> ProductResponseDTO
    @Mapping(source = "category.idCategoria", target = "categoryId")
    @Mapping(source = "category.nombreCategoria", target = "categoryName")
    @Mapping(source = "brand.marcaId", target = "brandId")
    @Mapping(source = "brand.nombreMarca", target = "brandName")
    ProductResponseDTO toResponseDTO(Product product);
    
    // List<Product> -> List<ProductResponseDTO>
    List<ProductResponseDTO> toResponseDTOList(List<Product> products);
    
    // ProductRequestDTO -> Product (sin relaciones)
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "status", ignore = true)
    Product toEntity(ProductRequestDTO requestDTO);
    
    // ProductUpdateDTO -> Product (sin relaciones)
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntityFromDTO(ProductUpdateDTO updateDTO, @MappingTarget Product product);
}
