package com.microservices.service;

import com.microservices.dto.CategoryRequestDTO;
import com.microservices.dto.CategoryResponseDTO;
import com.microservices.entity.Category;
import com.microservices.exception.CategoryNotFoundException;
import com.microservices.mapper.CategoryMapper;
import com.microservices.repository.CategoryRepository;
import com.microservices.service.impl.CategoryServiceImpl;
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
@DisplayName("CategoryService Tests")
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category testCategory;
    private CategoryResponseDTO testResponseDTO;

    @BeforeEach
    void setUp() {
        testCategory = Category.builder()
                .idCategoria(1L)
                .nombreCategoria("Test Category")
                .build();

        testResponseDTO = CategoryResponseDTO.builder()
                .idCategoria(1L)
                .nombreCategoria("Test Category")
                .build();
    }

    @Test
    @DisplayName("createCategory - Debería crear categoría exitosamente")
    void createCategory_ShouldCreateCategorySuccessfully() {
        // Given
        CategoryRequestDTO requestDTO = CategoryRequestDTO.builder()
                .nombreCategoria("New Category")
                .build();

        when(categoryRepository.existsByNombreCategoria("New Category")).thenReturn(false);
        when(categoryMapper.toEntity(requestDTO)).thenReturn(testCategory);
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);
        when(categoryMapper.toResponseDTO(testCategory)).thenReturn(testResponseDTO);

        // When
        CategoryResponseDTO result = categoryService.createCategory(requestDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNombreCategoria()).isEqualTo("Test Category");
        verify(categoryRepository).existsByNombreCategoria("New Category");
        verify(categoryMapper).toEntity(requestDTO);
        verify(categoryRepository).save(testCategory);
        verify(categoryMapper).toResponseDTO(testCategory);
    }

    @Test
    @DisplayName("createCategory - Debería lanzar excepción cuando categoría ya existe")
    void createCategory_WithExistingName_ShouldThrowException() {
        // Given
        CategoryRequestDTO requestDTO = CategoryRequestDTO.builder()
                .nombreCategoria("Existing Category")
                .build();

        when(categoryRepository.existsByNombreCategoria("Existing Category")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> categoryService.createCategory(requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Ya existe una categoría con el nombre: Existing Category");

        verify(categoryRepository).existsByNombreCategoria("Existing Category");
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    @DisplayName("getCategoryById - Debería retornar categoría cuando existe")
    void getCategoryById_WithExistingId_ShouldReturnCategory() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(categoryMapper.toResponseDTO(testCategory)).thenReturn(testResponseDTO);

        // When
        CategoryResponseDTO result = categoryService.getCategoryById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNombreCategoria()).isEqualTo("Test Category");
        verify(categoryRepository).findById(1L);
        verify(categoryMapper).toResponseDTO(testCategory);
    }

    @Test
    @DisplayName("getCategoryById - Debería lanzar excepción cuando categoría no existe")
    void getCategoryById_WithNonExistentId_ShouldThrowException() {
        // Given
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> categoryService.getCategoryById(999L))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage("Categoría no encontrada con ID: 999");

        verify(categoryRepository).findById(999L);
        verify(categoryMapper, never()).toResponseDTO(any(Category.class));
    }

        @Test
        @DisplayName("getAllCategories - Debería retornar todas las categorías")
        void getAllCategories_ShouldReturnAllCategories() {
            // Given
            List<Category> categories = Arrays.asList(testCategory);
            when(categoryRepository.findAllByOrderByNombreCategoriaAsc()).thenReturn(categories);
            when(categoryMapper.toResponseDTOList(any())).thenReturn(Arrays.asList(testResponseDTO));

            // When
            List<CategoryResponseDTO> result = categoryService.getAllCategories();

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getNombreCategoria()).isEqualTo("Test Category");
            verify(categoryRepository).findAllByOrderByNombreCategoriaAsc();
            verify(categoryMapper).toResponseDTOList(categories);
        }


    @Test
    @DisplayName("updateCategory - Debería actualizar categoría exitosamente")
    void updateCategory_ShouldUpdateCategorySuccessfully() {
        // Given
        Long categoryId = 1L;
        CategoryRequestDTO updateDTO = CategoryRequestDTO.builder()
                .nombreCategoria("Updated Category")
                .build();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(testCategory));
        when(categoryRepository.existsByNombreCategoria("Updated Category")).thenReturn(false);
        when(categoryRepository.save(testCategory)).thenReturn(testCategory);
        when(categoryMapper.toResponseDTO(testCategory)).thenReturn(testResponseDTO);

        // When
        CategoryResponseDTO result = categoryService.updateCategory(categoryId, updateDTO);

        // Then
        assertThat(result).isNotNull();
        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).existsByNombreCategoria("Updated Category");
        verify(categoryMapper).updateEntityFromDTO(updateDTO, testCategory);
        verify(categoryRepository).save(testCategory);
        verify(categoryMapper).toResponseDTO(testCategory);
    }

    @Test
    @DisplayName("updateCategory - Debería lanzar excepción cuando categoría no existe")
    void updateCategory_WithNonExistentId_ShouldThrowException() {
        // Given
        Long categoryId = 999L;
        CategoryRequestDTO updateDTO = CategoryRequestDTO.builder()
                .nombreCategoria("Updated Category")
                .build();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> categoryService.updateCategory(categoryId, updateDTO))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage("Categoría no encontrada con ID: 999");

        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    @DisplayName("updateCategory - Debería lanzar excepción cuando nombre ya existe")
    void updateCategory_WithExistingName_ShouldThrowException() {
        // Given
        Long categoryId = 1L;
        CategoryRequestDTO updateDTO = CategoryRequestDTO.builder()
                .nombreCategoria("Existing Category")
                .build();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(testCategory));
        when(categoryRepository.existsByNombreCategoria("Existing Category")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> categoryService.updateCategory(categoryId, updateDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Ya existe una categoría con el nombre: Existing Category");

        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).existsByNombreCategoria("Existing Category");
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    @DisplayName("deleteCategory - Debería eliminar categoría exitosamente")
    void deleteCategory_ShouldDeleteCategorySuccessfully() {
        // Given
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(testCategory));

        // When
        categoryService.deleteCategory(categoryId);

        // Then
        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).delete(testCategory);
    }

    @Test
    @DisplayName("deleteCategory - Debería lanzar excepción cuando categoría no existe")
    void deleteCategory_WithNonExistentId_ShouldThrowException() {
        // Given
        Long categoryId = 999L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> categoryService.deleteCategory(categoryId))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage("Categoría no encontrada con ID: 999");

        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository, never()).delete(any(Category.class));
    }

}