package com.microservices.controller;

import com.microservices.dto.CategoryRequestDTO;
import com.microservices.dto.CategoryResponseDTO;
import com.microservices.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    
    private final CategoryService categoryService;
    
    /**
     * Crear una nueva categoría
     * POST /api/categories
     * Requiere: CategoryRequestDTO con nombreCategoria
     * Retorna: CategoryResponseDTO con la categoría creada
     */
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO requestDTO) {
        CategoryResponseDTO response = categoryService.createCategory(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    /**
     * Obtener una categoría por su ID
     * GET /api/categories/{id}
     * Retorna: CategoryResponseDTO con los datos de la categoría
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
        CategoryResponseDTO response = categoryService.getCategoryById(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Listar todas las categorías
     * GET /api/categories
     * Retorna: Lista de CategoryResponseDTO ordenadas por nombre
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        List<CategoryResponseDTO> response = categoryService.getAllCategories();
        return ResponseEntity.ok(response);
    }
    
    /**
     * Actualizar una categoría existente
     * PUT /api/categories/{id}
     * Requiere: CategoryRequestDTO con los campos a actualizar
     * Retorna: CategoryResponseDTO con la categoría actualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable Long id, 
                                                            @Valid @RequestBody CategoryRequestDTO requestDTO) {
        CategoryResponseDTO response = categoryService.updateCategory(id, requestDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Eliminar una categoría
     * DELETE /api/categories/{id}
     * Retorna: 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
