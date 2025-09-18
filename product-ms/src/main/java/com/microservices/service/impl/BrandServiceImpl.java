package com.microservices.service.impl;

import com.microservices.dto.BrandRequestDTO;
import com.microservices.dto.BrandResponseDTO;
import com.microservices.entity.Brand;
import com.microservices.exception.BrandAlreadyExistsException;
import com.microservices.exception.BrandNotFoundException;
import com.microservices.mapper.BrandMapper;
import com.microservices.repository.BrandRepository;
import com.microservices.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BrandServiceImpl implements BrandService {
    
    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;
    
    @Override
    public BrandResponseDTO createBrand(BrandRequestDTO requestDTO) {
        // Verificar si ya existe una marca con el mismo nombre
        if (brandRepository.existsByNombreMarca(requestDTO.getNombreMarca())) {
            throw new BrandAlreadyExistsException("Ya existe una marca con el nombre: " + requestDTO.getNombreMarca());
        }
        
        Brand brand = brandMapper.toEntity(requestDTO);
        Brand savedBrand = brandRepository.save(brand);
        return brandMapper.toResponseDTO(savedBrand);
    }
    
    @Override
    @Transactional(readOnly = true)
    public BrandResponseDTO getBrandById(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new BrandNotFoundException("Marca no encontrada con ID: " + id));
        return brandMapper.toResponseDTO(brand);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BrandResponseDTO> getAllBrands() {
        List<Brand> brands = brandRepository.findAllByOrderByNombreMarcaAsc();
        return brandMapper.toResponseDTOList(brands);
    }
    
    @Override
    public BrandResponseDTO updateBrand(Long id, BrandRequestDTO requestDTO) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new BrandNotFoundException("Marca no encontrada con ID: " + id));
        
        // Verificar si el nuevo nombre ya existe (si se está cambiando)
        if (!requestDTO.getNombreMarca().equals(brand.getNombreMarca()) 
            && brandRepository.existsByNombreMarca(requestDTO.getNombreMarca())) {
            throw new BrandAlreadyExistsException("Ya existe una marca con el nombre: " + requestDTO.getNombreMarca());
        }
        
        brandMapper.updateEntityFromDTO(requestDTO, brand);
        Brand savedBrand = brandRepository.save(brand);
        return brandMapper.toResponseDTO(savedBrand);
    }
    
    @Override
    public void deleteBrand(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new BrandNotFoundException("Marca no encontrada con ID: " + id));
        brandRepository.delete(brand);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Brand getBrandEntityById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new BrandNotFoundException("Marca no encontrada con ID: " + id));
    }
}
