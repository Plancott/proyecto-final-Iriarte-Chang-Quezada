package com.microservices.service;

import com.microservices.dto.*;
import com.microservices.entity.Product;
import com.microservices.entity.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    
    // CRUD básico
    ProductResponseDTO createProduct(ProductRequestDTO requestDTO);
    ProductResponseDTO getProductById(Long id);
    List<ProductResponseDTO> getAllProducts();
    Page<ProductResponseDTO> getAllProducts(Pageable pageable);
    ProductResponseDTO updateProduct(Long id, ProductUpdateDTO updateDTO);
    void deleteProduct(Long id);
    
    // Búsquedas específicas
    List<ProductResponseDTO> getProductsByStatus(ProductStatus status);
    Page<ProductResponseDTO> getProductsByStatus(ProductStatus status, Pageable pageable);
    
    List<ProductResponseDTO> getProductsByCategory(Long categoryId);
    Page<ProductResponseDTO> getProductsByCategory(Long categoryId, Pageable pageable);
    
    List<ProductResponseDTO> getProductsByBrand(Long brandId);
    Page<ProductResponseDTO> getProductsByBrand(Long brandId, Pageable pageable);
    
    List<ProductResponseDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    Page<ProductResponseDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    List<ProductResponseDTO> searchProductsByName(String name);
    Page<ProductResponseDTO> searchProductsByName(String name, Pageable pageable);
    
    List<ProductResponseDTO> searchProductsByText(String searchText);
    Page<ProductResponseDTO> searchProductsByText(String searchText, Pageable pageable);
    
    // Búsquedas combinadas
    List<ProductResponseDTO> getProductsByCategoryAndPriceRange(Long categoryId, BigDecimal minPrice, BigDecimal maxPrice);
    Page<ProductResponseDTO> getProductsByCategoryAndPriceRange(Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    List<ProductResponseDTO> getProductsByBrandAndPriceRange(Long brandId, BigDecimal minPrice, BigDecimal maxPrice);
    Page<ProductResponseDTO> getProductsByBrandAndPriceRange(Long brandId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    List<ProductResponseDTO> getProductsByCategoryAndBrandAndPriceRange(Long categoryId, Long brandId, BigDecimal minPrice, BigDecimal maxPrice);
    Page<ProductResponseDTO> getProductsByCategoryAndBrandAndPriceRange(Long categoryId, Long brandId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    List<ProductResponseDTO> getProductsByStatusAndCategory(ProductStatus status, Long categoryId);
    Page<ProductResponseDTO> getProductsByStatusAndCategory(ProductStatus status, Long categoryId, Pageable pageable);
    
    List<ProductResponseDTO> getProductsByStatusAndBrand(ProductStatus status, Long brandId);
    Page<ProductResponseDTO> getProductsByStatusAndBrand(ProductStatus status, Long brandId, Pageable pageable);
    
    // Búsqueda avanzada
    Page<ProductResponseDTO> searchProducts(ProductSearchDTO searchDTO, Pageable pageable);
    
    // Cambio de estado
    ProductResponseDTO activateProduct(Long id);
    ProductResponseDTO deactivateProduct(Long id);
    
    // Gestión de imágenes
    ProductResponseDTO updateProductImage(Long id, String imageUrl);
}
