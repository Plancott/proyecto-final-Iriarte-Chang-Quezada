package com.microservices.service.impl;

import com.microservices.dto.*;
import com.microservices.entity.*;
import com.microservices.exception.*;
import com.microservices.mapper.ProductMapper;
import com.microservices.repository.ProductRepository;
import com.microservices.service.CategoryService;
import com.microservices.service.BrandService;
import com.microservices.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final ProductMapper productMapper;
    
    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        // Verificar si ya existe un producto con el mismo nombre
        if (productRepository.existsByName(requestDTO.getName())) {
            throw new ProductAlreadyExistsException("Ya existe un producto con el nombre: " + requestDTO.getName());
        }
        
        // Obtener categoría y marca
        Category category = categoryService.getCategoryEntityById(requestDTO.getCategoryId());
        Brand brand = brandService.getBrandEntityById(requestDTO.getBrandId());
        
        // Crear producto
        Product product = productMapper.toEntity(requestDTO);
        product.setCategory(category);
        product.setBrand(brand);
        product.setStatus(ProductStatus.ACTIVE);
        
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponseDTO(savedProduct);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado con ID: " + id));
        return productMapper.toResponseDTO(product);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productMapper.toResponseDTOList(products);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(productMapper::toResponseDTO);
    }
    
    @Override
    public ProductResponseDTO updateProduct(Long id, ProductUpdateDTO updateDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado con ID: " + id));
        
        // Verificar si el nuevo nombre ya existe (si se está cambiando)
        if (updateDTO.getName() != null && !updateDTO.getName().equals(product.getName()) 
            && productRepository.existsByName(updateDTO.getName())) {
            throw new ProductAlreadyExistsException("Ya existe un producto con el nombre: " + updateDTO.getName());
        }
        
        // Actualizar campos
        productMapper.updateEntityFromDTO(updateDTO, product);
        
        // Actualizar relaciones si se proporcionan
        if (updateDTO.getCategoryId() != null) {
            Category category = categoryService.getCategoryEntityById(updateDTO.getCategoryId());
            product.setCategory(category);
        }
        
        if (updateDTO.getBrandId() != null) {
            Brand brand = brandService.getBrandEntityById(updateDTO.getBrandId());
            product.setBrand(brand);
        }
        
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponseDTO(savedProduct);
    }
    
    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado con ID: " + id));
        productRepository.delete(product);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getProductsByStatus(ProductStatus status) {
        List<Product> products = productRepository.findByStatus(status);
        return productMapper.toResponseDTOList(products);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getProductsByStatus(ProductStatus status, Pageable pageable) {
        Page<Product> products = productRepository.findByStatus(status, pageable);
        return products.map(productMapper::toResponseDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryIdCategoria(categoryId);
        return productMapper.toResponseDTOList(products);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getProductsByCategory(Long categoryId, Pageable pageable) {
        Page<Product> products = productRepository.findByCategoryIdCategoria(categoryId, pageable);
        return products.map(productMapper::toResponseDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getProductsByBrand(Long brandId) {
        List<Product> products = productRepository.findByBrandMarcaId(brandId);
        return productMapper.toResponseDTOList(products);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getProductsByBrand(Long brandId, Pageable pageable) {
        Page<Product> products = productRepository.findByBrandMarcaId(brandId, pageable);
        return products.map(productMapper::toResponseDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        List<Product> products = productRepository.findByUnitPriceBetween(minPrice, maxPrice);
        return productMapper.toResponseDTOList(products);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        Page<Product> products = productRepository.findByUnitPriceBetween(minPrice, maxPrice, pageable);
        return products.map(productMapper::toResponseDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> searchProductsByName(String name) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        return productMapper.toResponseDTOList(products);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> searchProductsByName(String name, Pageable pageable) {
        Page<Product> products = productRepository.findByNameContainingIgnoreCase(name, pageable);
        return products.map(productMapper::toResponseDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> searchProductsByText(String searchText) {
        List<Product> products = productRepository.findByNameOrDescriptionContainingIgnoreCase(searchText);
        return productMapper.toResponseDTOList(products);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> searchProductsByText(String searchText, Pageable pageable) {
        Page<Product> products = productRepository.findByNameOrDescriptionContainingIgnoreCase(searchText, pageable);
        return products.map(productMapper::toResponseDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getProductsByCategoryAndPriceRange(Long categoryId, BigDecimal minPrice, BigDecimal maxPrice) {
        List<Product> products = productRepository.findByCategoryAndPriceRange(categoryId, minPrice, maxPrice);
        return productMapper.toResponseDTOList(products);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getProductsByCategoryAndPriceRange(Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        Page<Product> products = productRepository.findByCategoryAndPriceRange(categoryId, minPrice, maxPrice, pageable);
        return products.map(productMapper::toResponseDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getProductsByBrandAndPriceRange(Long brandId, BigDecimal minPrice, BigDecimal maxPrice) {
        List<Product> products = productRepository.findByBrandAndPriceRange(brandId, minPrice, maxPrice);
        return productMapper.toResponseDTOList(products);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getProductsByBrandAndPriceRange(Long brandId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        Page<Product> products = productRepository.findByBrandAndPriceRange(brandId, minPrice, maxPrice, pageable);
        return products.map(productMapper::toResponseDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getProductsByCategoryAndBrandAndPriceRange(Long categoryId, Long brandId, BigDecimal minPrice, BigDecimal maxPrice) {
        List<Product> products = productRepository.findByCategoryAndBrandAndPriceRange(categoryId, brandId, minPrice, maxPrice);
        return productMapper.toResponseDTOList(products);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getProductsByCategoryAndBrandAndPriceRange(Long categoryId, Long brandId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        Page<Product> products = productRepository.findByCategoryAndBrandAndPriceRange(categoryId, brandId, minPrice, maxPrice, pageable);
        return products.map(productMapper::toResponseDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getProductsByStatusAndCategory(ProductStatus status, Long categoryId) {
        List<Product> products = productRepository.findByStatusAndCategoryIdCategoria(status, categoryId);
        return productMapper.toResponseDTOList(products);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getProductsByStatusAndCategory(ProductStatus status, Long categoryId, Pageable pageable) {
        Page<Product> products = productRepository.findByStatusAndCategoryIdCategoria(status, categoryId, pageable);
        return products.map(productMapper::toResponseDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getProductsByStatusAndBrand(ProductStatus status, Long brandId) {
        List<Product> products = productRepository.findByStatusAndBrandMarcaId(status, brandId);
        return productMapper.toResponseDTOList(products);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getProductsByStatusAndBrand(ProductStatus status, Long brandId, Pageable pageable) {
        Page<Product> products = productRepository.findByStatusAndBrandMarcaId(status, brandId, pageable);
        return products.map(productMapper::toResponseDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> searchProducts(ProductSearchDTO searchDTO, Pageable pageable) {
        // Implementar búsqueda avanzada basada en los criterios del DTO
        // Por simplicidad, implementaré una búsqueda básica
        // En una implementación real, se podría usar Criteria API o QueryDSL
        
        if (searchDTO.getSearchText() != null && !searchDTO.getSearchText().trim().isEmpty()) {
            return searchProductsByText(searchDTO.getSearchText(), pageable);
        }
        
        if (searchDTO.getCategoryId() != null && searchDTO.getBrandId() != null 
            && searchDTO.getMinPrice() != null && searchDTO.getMaxPrice() != null) {
            return getProductsByCategoryAndBrandAndPriceRange(
                searchDTO.getCategoryId(), searchDTO.getBrandId(), 
                searchDTO.getMinPrice(), searchDTO.getMaxPrice(), pageable);
        }
        
        if (searchDTO.getCategoryId() != null && searchDTO.getMinPrice() != null && searchDTO.getMaxPrice() != null) {
            return getProductsByCategoryAndPriceRange(
                searchDTO.getCategoryId(), searchDTO.getMinPrice(), searchDTO.getMaxPrice(), pageable);
        }
        
        if (searchDTO.getBrandId() != null && searchDTO.getMinPrice() != null && searchDTO.getMaxPrice() != null) {
            return getProductsByBrandAndPriceRange(
                searchDTO.getBrandId(), searchDTO.getMinPrice(), searchDTO.getMaxPrice(), pageable);
        }
        
        if (searchDTO.getStatus() != null) {
            return getProductsByStatus(searchDTO.getStatus(), pageable);
        }
        
        return getAllProducts(pageable);
    }
    
    @Override
    public ProductResponseDTO activateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado con ID: " + id));
        product.activate();
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponseDTO(savedProduct);
    }
    
    @Override
    public ProductResponseDTO deactivateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado con ID: " + id));
        product.deactivate();
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponseDTO(savedProduct);
    }
}
