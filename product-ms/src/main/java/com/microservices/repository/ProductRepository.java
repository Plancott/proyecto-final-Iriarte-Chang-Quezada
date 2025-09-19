package com.microservices.repository;

import com.microservices.entity.Product;
import com.microservices.entity.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Buscar por estado
    List<Product> findByStatus(ProductStatus status);
    Page<Product> findByStatus(ProductStatus status, Pageable pageable);
    
    // Buscar por categoría
    List<Product> findByCategoryIdCategoria(Long categoryId);
    Page<Product> findByCategoryIdCategoria(Long categoryId, Pageable pageable);
    
    // Buscar por marca
    List<Product> findByBrandMarcaId(Long brandId);
    Page<Product> findByBrandMarcaId(Long brandId, Pageable pageable);
    
    // Buscar por rango de precios
    List<Product> findByUnitPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    Page<Product> findByUnitPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    // Buscar por texto en nombre
    List<Product> findByNameContainingIgnoreCase(String name);
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    // Buscar por texto en descripción
    List<Product> findByDescriptionContainingIgnoreCase(String description);
    Page<Product> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);
    
    // Búsqueda combinada: categoría y rango de precios
    @Query("SELECT p FROM Product p WHERE p.category.idCategoria = :categoryId AND p.unitPrice BETWEEN :minPrice AND :maxPrice")
    List<Product> findByCategoryAndPriceRange(@Param("categoryId") Long categoryId, 
                                            @Param("minPrice") BigDecimal minPrice, 
                                            @Param("maxPrice") BigDecimal maxPrice);
    
    @Query("SELECT p FROM Product p WHERE p.category.idCategoria = :categoryId AND p.unitPrice BETWEEN :minPrice AND :maxPrice")
    Page<Product> findByCategoryAndPriceRange(@Param("categoryId") Long categoryId, 
                                            @Param("minPrice") BigDecimal minPrice, 
                                            @Param("maxPrice") BigDecimal maxPrice, 
                                            Pageable pageable);
    
    // Búsqueda combinada: marca y rango de precios
    @Query("SELECT p FROM Product p WHERE p.brand.marcaId = :brandId AND p.unitPrice BETWEEN :minPrice AND :maxPrice")
    List<Product> findByBrandAndPriceRange(@Param("brandId") Long brandId, 
                                         @Param("minPrice") BigDecimal minPrice, 
                                         @Param("maxPrice") BigDecimal maxPrice);
    
    @Query("SELECT p FROM Product p WHERE p.brand.marcaId = :brandId AND p.unitPrice BETWEEN :minPrice AND :maxPrice")
    Page<Product> findByBrandAndPriceRange(@Param("brandId") Long brandId, 
                                         @Param("minPrice") BigDecimal minPrice, 
                                         @Param("maxPrice") BigDecimal maxPrice, 
                                         Pageable pageable);
    
    // Búsqueda combinada: categoría, marca y rango de precios
    @Query("SELECT p FROM Product p WHERE p.category.idCategoria = :categoryId AND p.brand.marcaId = :brandId AND p.unitPrice BETWEEN :minPrice AND :maxPrice")
    List<Product> findByCategoryAndBrandAndPriceRange(@Param("categoryId") Long categoryId, 
                                                    @Param("brandId") Long brandId,
                                                    @Param("minPrice") BigDecimal minPrice, 
                                                    @Param("maxPrice") BigDecimal maxPrice);
    
    @Query("SELECT p FROM Product p WHERE p.category.idCategoria = :categoryId AND p.brand.marcaId = :brandId AND p.unitPrice BETWEEN :minPrice AND :maxPrice")
    Page<Product> findByCategoryAndBrandAndPriceRange(@Param("categoryId") Long categoryId, 
                                                    @Param("brandId") Long brandId,
                                                    @Param("minPrice") BigDecimal minPrice, 
                                                    @Param("maxPrice") BigDecimal maxPrice, 
                                                    Pageable pageable);
    
    // Búsqueda por texto en nombre o descripción
    @Query("SELECT p FROM Product p WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%', :searchText, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchText, '%')))")
    List<Product> findByNameOrDescriptionContainingIgnoreCase(@Param("searchText") String searchText);
    
    @Query("SELECT p FROM Product p WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%', :searchText, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchText, '%')))")
    Page<Product> findByNameOrDescriptionContainingIgnoreCase(@Param("searchText") String searchText, Pageable pageable);
    
    // Búsqueda por estado y categoría
    List<Product> findByStatusAndCategoryIdCategoria(ProductStatus status, Long categoryId);
    Page<Product> findByStatusAndCategoryIdCategoria(ProductStatus status, Long categoryId, Pageable pageable);
    
    // Búsqueda por estado y marca
    List<Product> findByStatusAndBrandMarcaId(ProductStatus status, Long brandId);
    Page<Product> findByStatusAndBrandMarcaId(ProductStatus status, Long brandId, Pageable pageable);
    
    // Verificar si existe un producto con el mismo nombre
    boolean existsByName(String name);
    
    // Buscar productos activos por categoría
    List<Product> findByStatusAndCategoryIdCategoriaOrderByNameAsc(ProductStatus status, Long categoryId);
    
    // Buscar productos activos por marca
    List<Product> findByStatusAndBrandMarcaIdOrderByNameAsc(ProductStatus status, Long brandId);
}

