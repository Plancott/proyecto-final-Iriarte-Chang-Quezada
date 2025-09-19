package com.microservices.controller;

import com.microservices.dto.BrandRequestDTO;
import com.microservices.dto.BrandResponseDTO;
import com.microservices.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {
    
    private final BrandService brandService;
    
    /**
     * Crear una nueva marca
     * POST /api/brands
     * Requiere: BrandRequestDTO con nombreMarca
     * Retorna: BrandResponseDTO con la marca creada
     */
    @PostMapping
    public ResponseEntity<BrandResponseDTO> createBrand(@Valid @RequestBody BrandRequestDTO requestDTO) {
        BrandResponseDTO response = brandService.createBrand(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    /**
     * Obtener una marca por su ID
     * GET /api/brands/{id}
     * Retorna: BrandResponseDTO con los datos de la marca
     */
    @GetMapping("/{id}")
    public ResponseEntity<BrandResponseDTO> getBrandById(@PathVariable Long id) {
        BrandResponseDTO response = brandService.getBrandById(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Listar todas las marcas
     * GET /api/brands
     * Retorna: Lista de BrandResponseDTO ordenadas por nombre
     */
    @GetMapping
    public ResponseEntity<List<BrandResponseDTO>> getAllBrands() {
        List<BrandResponseDTO> response = brandService.getAllBrands();
        return ResponseEntity.ok(response);
    }
    
    /**
     * Actualizar una marca existente
     * PUT /api/brands/{id}
     * Requiere: BrandRequestDTO con los campos a actualizar
     * Retorna: BrandResponseDTO con la marca actualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<BrandResponseDTO> updateBrand(@PathVariable Long id, 
                                                      @Valid @RequestBody BrandRequestDTO requestDTO) {
        BrandResponseDTO response = brandService.updateBrand(id, requestDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Eliminar una marca
     * DELETE /api/brands/{id}
     * Retorna: 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }
}
