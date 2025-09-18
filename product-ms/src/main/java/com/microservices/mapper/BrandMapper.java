package com.microservices.mapper;

import com.microservices.dto.BrandRequestDTO;
import com.microservices.dto.BrandResponseDTO;
import com.microservices.entity.Brand;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BrandMapper {
    
    // Brand -> BrandResponseDTO
    BrandResponseDTO toResponseDTO(Brand brand);
    
    // List<Brand> -> List<BrandResponseDTO>
    List<BrandResponseDTO> toResponseDTOList(List<Brand> brands);
    
    // BrandRequestDTO -> Brand
    @Mapping(target = "marcaId", ignore = true)
    @Mapping(target = "products", ignore = true)
    Brand toEntity(BrandRequestDTO requestDTO);
    
    // BrandRequestDTO -> Brand (para actualización)
    @Mapping(target = "marcaId", ignore = true)
    @Mapping(target = "products", ignore = true)
    void updateEntityFromDTO(BrandRequestDTO requestDTO, @MappingTarget Brand brand);
}
