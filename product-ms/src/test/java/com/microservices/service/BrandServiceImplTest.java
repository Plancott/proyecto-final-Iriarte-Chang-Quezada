package com.microservices.service;

import com.microservices.dto.BrandRequestDTO;
import com.microservices.dto.BrandResponseDTO;
import com.microservices.entity.Brand;
import com.microservices.exception.BrandNotFoundException;
import com.microservices.mapper.BrandMapper;
import com.microservices.repository.BrandRepository;
import com.microservices.service.impl.BrandServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BrandService Tests")
class BrandServiceImplTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private BrandMapper brandMapper;

    @InjectMocks
    private BrandServiceImpl brandService;

    private Brand testBrand;
    private BrandResponseDTO testResponseDTO;

    @BeforeEach
    void setUp() {
        testBrand = Brand.builder()
                .marcaId(1L)
                .nombreMarca("Test Brand")
                .build();

        testResponseDTO = BrandResponseDTO.builder()
                .marcaId(1L)
                .nombreMarca("Test Brand")
                .build();
    }

    @Test
    @DisplayName("createBrand - Debería crear marca exitosamente")
    void createBrand_ShouldCreateBrandSuccessfully() {
        // Given
        BrandRequestDTO requestDTO = BrandRequestDTO.builder()
                .nombreMarca("New Brand")
                .build();

        when(brandRepository.existsByNombreMarca("New Brand")).thenReturn(false);
        when(brandMapper.toEntity(requestDTO)).thenReturn(testBrand);
        when(brandRepository.save(any(Brand.class))).thenReturn(testBrand);
        when(brandMapper.toResponseDTO(testBrand)).thenReturn(testResponseDTO);

        // When
        BrandResponseDTO result = brandService.createBrand(requestDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNombreMarca()).isEqualTo("Test Brand");
        verify(brandRepository).existsByNombreMarca("New Brand");
        verify(brandMapper).toEntity(requestDTO);
        verify(brandRepository).save(testBrand);
        verify(brandMapper).toResponseDTO(testBrand);
    }

    @Test
    @DisplayName("createBrand - Debería lanzar excepción cuando marca ya existe")
    void createBrand_WithExistingName_ShouldThrowException() {
        // Given
        BrandRequestDTO requestDTO = BrandRequestDTO.builder()
                .nombreMarca("Existing Brand")
                .build();

        when(brandRepository.existsByNombreMarca("Existing Brand")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> brandService.createBrand(requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Ya existe una marca con el nombre: Existing Brand");

        verify(brandRepository).existsByNombreMarca("Existing Brand");
        verify(brandRepository, never()).save(any(Brand.class));
    }

    @Test
    @DisplayName("getBrandById - Debería retornar marca cuando existe")
    void getBrandById_WithExistingId_ShouldReturnBrand() {
        // Given
        when(brandRepository.findById(1L)).thenReturn(Optional.of(testBrand));
        when(brandMapper.toResponseDTO(testBrand)).thenReturn(testResponseDTO);

        // When
        BrandResponseDTO result = brandService.getBrandById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNombreMarca()).isEqualTo("Test Brand");
        verify(brandRepository).findById(1L);
        verify(brandMapper).toResponseDTO(testBrand);
    }

    @Test
    @DisplayName("getBrandById - Debería lanzar excepción cuando marca no existe")
    void getBrandById_WithNonExistentId_ShouldThrowException() {
        // Given
        when(brandRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> brandService.getBrandById(999L))
                .isInstanceOf(BrandNotFoundException.class)
                .hasMessage("Marca no encontrada con ID: 999");

        verify(brandRepository).findById(999L);
        verify(brandMapper, never()).toResponseDTO(any(Brand.class));
    }

        @Test
        @DisplayName("getAllBrands - Debería retornar todas las marcas")
        void getAllBrands_ShouldReturnAllBrands() {
            // Given
            List<Brand> brands = Arrays.asList(testBrand);
            when(brandRepository.findAllByOrderByNombreMarcaAsc()).thenReturn(brands);
            when(brandMapper.toResponseDTOList(any())).thenReturn(Arrays.asList(testResponseDTO));

            // When
            List<BrandResponseDTO> result = brandService.getAllBrands();

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getNombreMarca()).isEqualTo("Test Brand");
            verify(brandRepository).findAllByOrderByNombreMarcaAsc();
            verify(brandMapper).toResponseDTOList(brands);
        }


    @Test
    @DisplayName("updateBrand - Debería actualizar marca exitosamente")
    void updateBrand_ShouldUpdateBrandSuccessfully() {
        // Given
        Long brandId = 1L;
        BrandRequestDTO updateDTO = BrandRequestDTO.builder()
                .nombreMarca("Updated Brand")
                .build();

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(testBrand));
        when(brandRepository.existsByNombreMarca("Updated Brand")).thenReturn(false);
        when(brandRepository.save(testBrand)).thenReturn(testBrand);
        when(brandMapper.toResponseDTO(testBrand)).thenReturn(testResponseDTO);

        // When
        BrandResponseDTO result = brandService.updateBrand(brandId, updateDTO);

        // Then
        assertThat(result).isNotNull();
        verify(brandRepository).findById(brandId);
        verify(brandRepository).existsByNombreMarca("Updated Brand");
        verify(brandMapper).updateEntityFromDTO(updateDTO, testBrand);
        verify(brandRepository).save(testBrand);
        verify(brandMapper).toResponseDTO(testBrand);
    }

    @Test
    @DisplayName("updateBrand - Debería lanzar excepción cuando marca no existe")
    void updateBrand_WithNonExistentId_ShouldThrowException() {
        // Given
        Long brandId = 999L;
        BrandRequestDTO updateDTO = BrandRequestDTO.builder()
                .nombreMarca("Updated Brand")
                .build();

        when(brandRepository.findById(brandId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> brandService.updateBrand(brandId, updateDTO))
                .isInstanceOf(BrandNotFoundException.class)
                .hasMessage("Marca no encontrada con ID: 999");

        verify(brandRepository).findById(brandId);
        verify(brandRepository, never()).save(any(Brand.class));
    }

    @Test
    @DisplayName("updateBrand - Debería lanzar excepción cuando nombre ya existe")
    void updateBrand_WithExistingName_ShouldThrowException() {
        // Given
        Long brandId = 1L;
        BrandRequestDTO updateDTO = BrandRequestDTO.builder()
                .nombreMarca("Existing Brand")
                .build();

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(testBrand));
        when(brandRepository.existsByNombreMarca("Existing Brand")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> brandService.updateBrand(brandId, updateDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Ya existe una marca con el nombre: Existing Brand");

        verify(brandRepository).findById(brandId);
        verify(brandRepository).existsByNombreMarca("Existing Brand");
        verify(brandRepository, never()).save(any(Brand.class));
    }

    @Test
    @DisplayName("deleteBrand - Debería eliminar marca exitosamente")
    void deleteBrand_ShouldDeleteBrandSuccessfully() {
        // Given
        Long brandId = 1L;
        when(brandRepository.findById(brandId)).thenReturn(Optional.of(testBrand));

        // When
        brandService.deleteBrand(brandId);

        // Then
        verify(brandRepository).findById(brandId);
        verify(brandRepository).delete(testBrand);
    }

    @Test
    @DisplayName("deleteBrand - Debería lanzar excepción cuando marca no existe")
    void deleteBrand_WithNonExistentId_ShouldThrowException() {
        // Given
        Long brandId = 999L;
        when(brandRepository.findById(brandId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> brandService.deleteBrand(brandId))
                .isInstanceOf(BrandNotFoundException.class)
                .hasMessage("Marca no encontrada con ID: 999");

        verify(brandRepository).findById(brandId);
        verify(brandRepository, never()).delete(any(Brand.class));
    }

}