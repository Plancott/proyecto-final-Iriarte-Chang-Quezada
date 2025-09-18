package com.microservices.service;

import com.microservices.dto.BrandRequestDTO;
import com.microservices.dto.BrandResponseDTO;
import com.microservices.entity.Brand;

import java.util.List;

public interface BrandService {
    
    BrandResponseDTO createBrand(BrandRequestDTO requestDTO);
    BrandResponseDTO getBrandById(Long id);
    List<BrandResponseDTO> getAllBrands();
    BrandResponseDTO updateBrand(Long id, BrandRequestDTO requestDTO);
    void deleteBrand(Long id);
    
    // Método interno para obtener la entidad
    Brand getBrandEntityById(Long id);
}
