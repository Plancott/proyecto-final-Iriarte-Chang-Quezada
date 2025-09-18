package com.microservices.controller;

import com.microservices.dto.*;
import com.microservices.entity.ProductStatus;
import com.microservices.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;
    
    // ==================== CRUD BÁSICO ====================
    
    /**
     * Crear un nuevo producto
     * POST /api/products
     * Requiere: ProductRequestDTO con name, categoryId, brandId, unitPrice, etc.
     * Retorna: ProductResponseDTO con el producto creado
     */
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO requestDTO) {
        ProductResponseDTO response = productService.createProduct(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    /**
     * Obtener un producto por su ID
     * GET /api/products/{id}
     * Retorna: ProductResponseDTO con los datos del producto
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        ProductResponseDTO response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Listar todos los productos con paginación opcional
     * GET /api/products?page=0&size=10&sortBy=name&sortDirection=asc
     * Parámetros:
     * - page: Número de página (default: 0)
     * - size: Elementos por página (default: 10, 0 = sin paginación)
     * - sortBy: Campo para ordenar (default: name)
     * - sortDirection: Dirección del ordenamiento (asc/desc, default: asc)
     * Retorna: Lista de ProductResponseDTO
     */
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if (size == 0) {
            List<ProductResponseDTO> response = productService.getAllProducts();
            return ResponseEntity.ok(response);
        } else {
            Page<ProductResponseDTO> response = productService.getAllProducts(pageable);
            return ResponseEntity.ok(response.getContent());
        }
    }
    
    /**
     * Actualizar un producto existente
     * PUT /api/products/{id}
     * Requiere: ProductUpdateDTO con los campos a actualizar
     * Retorna: ProductResponseDTO con el producto actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, 
                                                          @Valid @RequestBody ProductUpdateDTO updateDTO) {
        ProductResponseDTO response = productService.updateProduct(id, updateDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Eliminar un producto
     * DELETE /api/products/{id}
     * Retorna: 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
    
    // ==================== BÚSQUEDAS ESPECÍFICAS ====================
    
    /**
     * Buscar productos por estado (ACTIVE/INACTIVE)
     * GET /api/products/by-status/{status}?page=0&size=10&sortBy=name&sortDirection=asc
     * Parámetros:
     * - status: Estado del producto (ACTIVE o INACTIVE)
     * - page, size, sortBy, sortDirection: Parámetros de paginación y ordenamiento
     * Retorna: Lista de productos con el estado especificado
     */
    @GetMapping("/by-status/{status}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByStatus(
            @PathVariable ProductStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if (size == 0) {
            List<ProductResponseDTO> response = productService.getProductsByStatus(status);
            return ResponseEntity.ok(response);
        } else {
            Page<ProductResponseDTO> response = productService.getProductsByStatus(status, pageable);
            return ResponseEntity.ok(response.getContent());
        }
    }
    
    /**
     * Buscar productos por categoría
     * GET /api/products/by-category/{categoryId}?page=0&size=10&sortBy=name&sortDirection=asc
     * Parámetros:
     * - categoryId: ID de la categoría
     * - page, size, sortBy, sortDirection: Parámetros de paginación y ordenamiento
     * Retorna: Lista de productos de la categoría especificada
     */
    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if (size == 0) {
            List<ProductResponseDTO> response = productService.getProductsByCategory(categoryId);
            return ResponseEntity.ok(response);
        } else {
            Page<ProductResponseDTO> response = productService.getProductsByCategory(categoryId, pageable);
            return ResponseEntity.ok(response.getContent());
        }
    }
    
    /**
     * Buscar productos por marca
     * GET /api/products/by-brand/{brandId}?page=0&size=10&sortBy=name&sortDirection=asc
     * Parámetros:
     * - brandId: ID de la marca
     * - page, size, sortBy, sortDirection: Parámetros de paginación y ordenamiento
     * Retorna: Lista de productos de la marca especificada
     */
    @GetMapping("/by-brand/{brandId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByBrand(
            @PathVariable Long brandId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if (size == 0) {
            List<ProductResponseDTO> response = productService.getProductsByBrand(brandId);
            return ResponseEntity.ok(response);
        } else {
            Page<ProductResponseDTO> response = productService.getProductsByBrand(brandId, pageable);
            return ResponseEntity.ok(response.getContent());
        }
    }
    
    /**
     * Buscar productos por rango de precios
     * GET /api/products/by-price-range?minPrice=10.00&maxPrice=100.00&page=0&size=10&sortBy=unitPrice&sortDirection=asc
     * Parámetros:
     * - minPrice: Precio mínimo (obligatorio)
     * - maxPrice: Precio máximo (obligatorio)
     * - page, size, sortBy, sortDirection: Parámetros de paginación y ordenamiento
     * Retorna: Lista de productos dentro del rango de precios especificado
     */
    @GetMapping("/by-price-range")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if (size == 0) {
            List<ProductResponseDTO> response = productService.getProductsByPriceRange(minPrice, maxPrice);
            return ResponseEntity.ok(response);
        } else {
            Page<ProductResponseDTO> response = productService.getProductsByPriceRange(minPrice, maxPrice, pageable);
            return ResponseEntity.ok(response.getContent());
        }
    }
    
    /**
     * Buscar productos por nombre (búsqueda parcial, case-insensitive)
     * GET /api/products/search/name?name=iPhone&page=0&size=10&sortBy=name&sortDirection=asc
     * Parámetros:
     * - name: Texto a buscar en el nombre del producto
     * - page, size, sortBy, sortDirection: Parámetros de paginación y ordenamiento
     * Retorna: Lista de productos cuyo nombre contenga el texto especificado
     */
    @GetMapping("/search/name")
    public ResponseEntity<List<ProductResponseDTO>> searchProductsByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if (size == 0) {
            List<ProductResponseDTO> response = productService.searchProductsByName(name);
            return ResponseEntity.ok(response);
        } else {
            Page<ProductResponseDTO> response = productService.searchProductsByName(name, pageable);
            return ResponseEntity.ok(response.getContent());
        }
    }
    
    /**
     * Buscar productos por texto (nombre o descripción)
     * GET /api/products/search/text?searchText=smartphone&page=0&size=10&sortBy=name&sortDirection=asc
     * Parámetros:
     * - searchText: Texto a buscar en nombre o descripción del producto
     * - page, size, sortBy, sortDirection: Parámetros de paginación y ordenamiento
     * Retorna: Lista de productos cuyo nombre o descripción contenga el texto especificado
     */
    @GetMapping("/search/text")
    public ResponseEntity<List<ProductResponseDTO>> searchProductsByText(
            @RequestParam String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if (size == 0) {
            List<ProductResponseDTO> response = productService.searchProductsByText(searchText);
            return ResponseEntity.ok(response);
        } else {
            Page<ProductResponseDTO> response = productService.searchProductsByText(searchText, pageable);
            return ResponseEntity.ok(response.getContent());
        }
    }
    
    // ==================== BÚSQUEDAS COMBINADAS ====================
    
    /**
     * Buscar productos por categoría Y rango de precios
     * GET /api/products/by-category-and-price-range?categoryId=1&minPrice=10.00&maxPrice=100.00&page=0&size=10&sortBy=unitPrice&sortDirection=asc
     * Parámetros:
     * - categoryId: ID de la categoría (obligatorio)
     * - minPrice: Precio mínimo (obligatorio)
     * - maxPrice: Precio máximo (obligatorio)
     * - page, size, sortBy, sortDirection: Parámetros de paginación y ordenamiento
     * Retorna: Lista de productos de la categoría especificada dentro del rango de precios
     */
    @GetMapping("/by-category-and-price-range")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategoryAndPriceRange(
            @RequestParam Long categoryId,
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if (size == 0) {
            List<ProductResponseDTO> response = productService.getProductsByCategoryAndPriceRange(categoryId, minPrice, maxPrice);
            return ResponseEntity.ok(response);
        } else {
            Page<ProductResponseDTO> response = productService.getProductsByCategoryAndPriceRange(categoryId, minPrice, maxPrice, pageable);
            return ResponseEntity.ok(response.getContent());
        }
    }
    
    /**
     * Buscar productos por marca Y rango de precios
     * GET /api/products/by-brand-and-price-range?brandId=1&minPrice=10.00&maxPrice=100.00&page=0&size=10&sortBy=unitPrice&sortDirection=asc
     * Parámetros:
     * - brandId: ID de la marca (obligatorio)
     * - minPrice: Precio mínimo (obligatorio)
     * - maxPrice: Precio máximo (obligatorio)
     * - page, size, sortBy, sortDirection: Parámetros de paginación y ordenamiento
     * Retorna: Lista de productos de la marca especificada dentro del rango de precios
     */
    @GetMapping("/by-brand-and-price-range")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByBrandAndPriceRange(
            @RequestParam Long brandId,
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if (size == 0) {
            List<ProductResponseDTO> response = productService.getProductsByBrandAndPriceRange(brandId, minPrice, maxPrice);
            return ResponseEntity.ok(response);
        } else {
            Page<ProductResponseDTO> response = productService.getProductsByBrandAndPriceRange(brandId, minPrice, maxPrice, pageable);
            return ResponseEntity.ok(response.getContent());
        }
    }
    
    /**
     * Buscar productos por categoría Y marca Y rango de precios (búsqueda triple combinada)
     * GET /api/products/by-category-brand-and-price-range?categoryId=1&brandId=1&minPrice=10.00&maxPrice=100.00&page=0&size=10&sortBy=unitPrice&sortDirection=asc
     * Parámetros:
     * - categoryId: ID de la categoría (obligatorio)
     * - brandId: ID de la marca (obligatorio)
     * - minPrice: Precio mínimo (obligatorio)
     * - maxPrice: Precio máximo (obligatorio)
     * - page, size, sortBy, sortDirection: Parámetros de paginación y ordenamiento
     * Retorna: Lista de productos que cumplan los tres criterios simultáneamente
     */
    @GetMapping("/by-category-brand-and-price-range")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategoryAndBrandAndPriceRange(
            @RequestParam Long categoryId,
            @RequestParam Long brandId,
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if (size == 0) {
            List<ProductResponseDTO> response = productService.getProductsByCategoryAndBrandAndPriceRange(categoryId, brandId, minPrice, maxPrice);
            return ResponseEntity.ok(response);
        } else {
            Page<ProductResponseDTO> response = productService.getProductsByCategoryAndBrandAndPriceRange(categoryId, brandId, minPrice, maxPrice, pageable);
            return ResponseEntity.ok(response.getContent());
        }
    }
    
    /**
     * Buscar productos por estado Y categoría
     * GET /api/products/by-status-and-category?status=ACTIVE&categoryId=1&page=0&size=10&sortBy=name&sortDirection=asc
     * Parámetros:
     * - status: Estado del producto (ACTIVE o INACTIVE) (obligatorio)
     * - categoryId: ID de la categoría (obligatorio)
     * - page, size, sortBy, sortDirection: Parámetros de paginación y ordenamiento
     * Retorna: Lista de productos activos/inactivos de la categoría especificada
     */
    @GetMapping("/by-status-and-category")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByStatusAndCategory(
            @RequestParam ProductStatus status,
            @RequestParam Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if (size == 0) {
            List<ProductResponseDTO> response = productService.getProductsByStatusAndCategory(status, categoryId);
            return ResponseEntity.ok(response);
        } else {
            Page<ProductResponseDTO> response = productService.getProductsByStatusAndCategory(status, categoryId, pageable);
            return ResponseEntity.ok(response.getContent());
        }
    }
    
    /**
     * Buscar productos por estado Y marca
     * GET /api/products/by-status-and-brand?status=ACTIVE&brandId=1&page=0&size=10&sortBy=name&sortDirection=asc
     * Parámetros:
     * - status: Estado del producto (ACTIVE o INACTIVE) (obligatorio)
     * - brandId: ID de la marca (obligatorio)
     * - page, size, sortBy, sortDirection: Parámetros de paginación y ordenamiento
     * Retorna: Lista de productos activos/inactivos de la marca especificada
     */
    @GetMapping("/by-status-and-brand")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByStatusAndBrand(
            @RequestParam ProductStatus status,
            @RequestParam Long brandId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if (size == 0) {
            List<ProductResponseDTO> response = productService.getProductsByStatusAndBrand(status, brandId);
            return ResponseEntity.ok(response);
        } else {
            Page<ProductResponseDTO> response = productService.getProductsByStatusAndBrand(status, brandId, pageable);
            return ResponseEntity.ok(response.getContent());
        }
    }
    
    // ==================== BÚSQUEDA AVANZADA ====================
    
    /**
     * Búsqueda avanzada con múltiples criterios
     * POST /api/products/search?page=0&size=10&sortBy=name&sortDirection=asc
     * Body: ProductSearchDTO con criterios de búsqueda
     * Parámetros:
     * - searchDTO: Objeto con criterios de búsqueda (searchText, categoryId, brandId, status, minPrice, maxPrice)
     * - page, size, sortBy, sortDirection: Parámetros de paginación y ordenamiento
     * Retorna: Lista de productos que cumplan los criterios especificados
     */
    @PostMapping("/search")
    public ResponseEntity<List<ProductResponseDTO>> searchProducts(
            @RequestBody ProductSearchDTO searchDTO,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<ProductResponseDTO> response = productService.searchProducts(searchDTO, pageable);
        return ResponseEntity.ok(response.getContent());
    }
    
    // ==================== GESTIÓN DE ESTADO ====================
    
    /**
     * Activar un producto (cambiar estado a ACTIVE)
     * PATCH /api/products/{id}/activate
     * Retorna: ProductResponseDTO con el producto activado
     */
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ProductResponseDTO> activateProduct(@PathVariable Long id) {
        ProductResponseDTO response = productService.activateProduct(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Desactivar un producto (cambiar estado a INACTIVE)
     * PATCH /api/products/{id}/deactivate
     * Retorna: ProductResponseDTO con el producto desactivado
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ProductResponseDTO> deactivateProduct(@PathVariable Long id) {
        ProductResponseDTO response = productService.deactivateProduct(id);
        return ResponseEntity.ok(response);
    }
    
    // ==================== ENDPOINTS DE CONVENIENCIA ====================
    
    /**
     * Obtener solo productos activos (endpoint de conveniencia)
     * GET /api/products/active?page=0&size=10&sortBy=name&sortDirection=asc
     * Parámetros:
     * - page, size, sortBy, sortDirection: Parámetros de paginación y ordenamiento
     * Retorna: Lista de productos con estado ACTIVE
     * Nota: Es un alias para /api/products/by-status/ACTIVE
     */
    @GetMapping("/active")
    public ResponseEntity<List<ProductResponseDTO>> getActiveProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        return getProductsByStatus(ProductStatus.ACTIVE, page, size, sortBy, sortDirection);
    }
}
