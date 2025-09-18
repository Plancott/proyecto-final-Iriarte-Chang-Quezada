package com.microservices.service.impl;

import com.microservices.dto.CategoryRequestDTO;
import com.microservices.dto.CategoryResponseDTO;
import com.microservices.entity.Category;
import com.microservices.exception.CategoryAlreadyExistsException;
import com.microservices.exception.CategoryNotFoundException;
import com.microservices.mapper.CategoryMapper;
import com.microservices.repository.CategoryRepository;
import com.microservices.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {
    
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    
    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO) {
        // Verificar si ya existe una categoría con el mismo nombre
        if (categoryRepository.existsByNombreCategoria(requestDTO.getNombreCategoria())) {
            throw new CategoryAlreadyExistsException("Ya existe una categoría con el nombre: " + requestDTO.getNombreCategoria());
        }
        
        Category category = categoryMapper.toEntity(requestDTO);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponseDTO(savedCategory);
    }
    
    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Categoría no encontrada con ID: " + id));
        return categoryMapper.toResponseDTO(category);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAllByOrderByNombreCategoriaAsc();
        return categoryMapper.toResponseDTOList(categories);
    }
    
    @Override
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Categoría no encontrada con ID: " + id));
        
        // Verificar si el nuevo nombre ya existe (si se está cambiando)
        if (!requestDTO.getNombreCategoria().equals(category.getNombreCategoria()) 
            && categoryRepository.existsByNombreCategoria(requestDTO.getNombreCategoria())) {
            throw new CategoryAlreadyExistsException("Ya existe una categoría con el nombre: " + requestDTO.getNombreCategoria());
        }
        
        categoryMapper.updateEntityFromDTO(requestDTO, category);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponseDTO(savedCategory);
    }
    
    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Categoría no encontrada con ID: " + id));
        categoryRepository.delete(category);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Category getCategoryEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Categoría no encontrada con ID: " + id));
    }
}
