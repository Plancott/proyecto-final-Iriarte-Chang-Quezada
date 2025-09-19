package com.microservices.mapper;

import com.microservices.dto.CategoryRequestDTO;
import com.microservices.dto.CategoryResponseDTO;
import com.microservices.entity.Category;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    
    // Category -> CategoryResponseDTO
    CategoryResponseDTO toResponseDTO(Category category);
    
    // List<Category> -> List<CategoryResponseDTO>
    List<CategoryResponseDTO> toResponseDTOList(List<Category> categories);
    
    // CategoryRequestDTO -> Category
    @Mapping(target = "idCategoria", ignore = true)
    @Mapping(target = "products", ignore = true)
    Category toEntity(CategoryRequestDTO requestDTO);
    
    // CategoryRequestDTO -> Category (para actualización)
    @Mapping(target = "idCategoria", ignore = true)
    @Mapping(target = "products", ignore = true)
    void updateEntityFromDTO(CategoryRequestDTO requestDTO, @MappingTarget Category category);
}

