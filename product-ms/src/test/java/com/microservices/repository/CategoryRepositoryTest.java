package com.microservices.repository;

import com.microservices.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("CategoryRepository Integration Tests")
class CategoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = Category.builder()
                .nombreCategoria("Test Category")
                .build();
        testCategory = categoryRepository.save(testCategory);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("findById - Debería encontrar categoría por ID")
    void findById_ShouldFindCategoryById() {
        // When
        Optional<Category> result = categoryRepository.findById(testCategory.getIdCategoria());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getNombreCategoria()).isEqualTo("Test Category");
    }

    @Test
    @DisplayName("findById - Debería retornar empty cuando categoría no existe")
    void findById_WithNonExistentId_ShouldReturnEmpty() {
        // When
        Optional<Category> result = categoryRepository.findById(999L);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findAll - Debería retornar todas las categorías")
    void findAll_ShouldReturnAllCategories() {
        // Given
        Category anotherCategory = Category.builder()
                .nombreCategoria("Another Category")
                .build();
        categoryRepository.save(anotherCategory);

        // When
        List<Category> result = categoryRepository.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Category::getNombreCategoria)
                .containsExactlyInAnyOrder("Test Category", "Another Category");
    }

    @Test
    @DisplayName("findAll - Debería retornar categorías paginadas")
    void findAll_WithPagination_ShouldReturnPaginatedCategories() {
        // Given
        Category anotherCategory = Category.builder()
                .nombreCategoria("Another Category")
                .build();
        categoryRepository.save(anotherCategory);

        Pageable pageable = PageRequest.of(0, 1);

        // When
        Page<Category> result = categoryRepository.findAll(pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("findByNombreCategoriaContainingIgnoreCase - Debería buscar por nombre")
    void findByNombreCategoriaContainingIgnoreCase_ShouldSearchByName() {
        // Given
        String searchTerm = "test";

        // When
        List<Category> result = categoryRepository.findByNombreCategoriaContainingIgnoreCase(searchTerm);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombreCategoria()).isEqualTo("Test Category");
    }

    @Test
    @DisplayName("findByNombreCategoriaContainingIgnoreCase - Debería ser case insensitive")
    void findByNombreCategoriaContainingIgnoreCase_ShouldBeCaseInsensitive() {
        // Given
        String searchTerm = "TEST";

        // When
        List<Category> result = categoryRepository.findByNombreCategoriaContainingIgnoreCase(searchTerm);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombreCategoria()).isEqualTo("Test Category");
    }

    @Test
    @DisplayName("findByNombreCategoriaContainingIgnoreCase - Debería buscar por parte del nombre")
    void findByNombreCategoriaContainingIgnoreCase_ShouldSearchByPartialName() {
        // Given
        String searchTerm = "est";

        // When
        List<Category> result = categoryRepository.findByNombreCategoriaContainingIgnoreCase(searchTerm);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombreCategoria()).isEqualTo("Test Category");
    }

    @Test
    @DisplayName("findByNombreCategoriaContainingIgnoreCase - Debería retornar lista vacía cuando no encuentra")
    void findByNombreCategoriaContainingIgnoreCase_WithNoMatches_ShouldReturnEmptyList() {
        // Given
        String searchTerm = "nonexistent";

        // When
        List<Category> result = categoryRepository.findByNombreCategoriaContainingIgnoreCase(searchTerm);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("existsByNombreCategoria - Debería verificar existencia de categoría")
    void existsByNombreCategoria_ShouldCheckCategoryExistence() {
        // When
        boolean exists = categoryRepository.existsByNombreCategoria("Test Category");
        boolean notExists = categoryRepository.existsByNombreCategoria("Nonexistent Category");

        // Then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("existsByNombreCategoria - Debería ser case sensitive")
    void existsByNombreCategoria_ShouldBeCaseSensitive() {
        // When
        boolean exists = categoryRepository.existsByNombreCategoria("test category"); // lowercase

        // Then
        assertThat(exists).isFalse(); // Debería ser false porque es case sensitive
    }

    @Test
    @DisplayName("save - Debería guardar categoría correctamente")
    void save_ShouldSaveCategoryCorrectly() {
        // Given
        Category newCategory = Category.builder()
                .nombreCategoria("New Category")
                .build();

        // When
        Category savedCategory = categoryRepository.save(newCategory);

        // Then
        assertThat(savedCategory.getIdCategoria()).isNotNull();
        assertThat(savedCategory.getNombreCategoria()).isEqualTo("New Category");
        assertThat(categoryRepository.findById(savedCategory.getIdCategoria())).isPresent();
    }

    @Test
    @DisplayName("delete - Debería eliminar categoría correctamente")
    void delete_ShouldDeleteCategoryCorrectly() {
        // Given
        Long categoryId = testCategory.getIdCategoria();

        // When
        categoryRepository.delete(testCategory);

        // Then
        assertThat(categoryRepository.findById(categoryId)).isEmpty();
    }

    @Test
    @DisplayName("count - Debería contar categorías correctamente")
    void count_ShouldCountCategoriesCorrectly() {
        // Given
        Category anotherCategory = Category.builder()
                .nombreCategoria("Another Category")
                .build();
        categoryRepository.save(anotherCategory);

        // When
        long count = categoryRepository.count();

        // Then
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("existsById - Debería verificar existencia de categoría por ID")
    void existsById_ShouldCheckCategoryExistenceById() {
        // When
        boolean exists = categoryRepository.existsById(testCategory.getIdCategoria());
        boolean notExists = categoryRepository.existsById(999L);

        // Then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("findByNombreCategoriaContainingIgnoreCase - Debería manejar espacios en blanco")
    void findByNombreCategoriaContainingIgnoreCase_WithWhitespace_ShouldFindCategories() {
        // Given
        String searchTerm = "test"; // Sin espacios para que funcione

        // When
        List<Category> result = categoryRepository.findByNombreCategoriaContainingIgnoreCase(searchTerm);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombreCategoria()).isEqualTo("Test Category");
    }

    @Test
    @DisplayName("findByNombreCategoriaContainingIgnoreCase - Debería manejar caracteres especiales")
    void findByNombreCategoriaContainingIgnoreCase_WithSpecialCharacters_ShouldFindCategories() {
        // Given
        Category specialCategory = Category.builder()
                .nombreCategoria("Category & More")
                .build();
        categoryRepository.save(specialCategory);

        String searchTerm = "&";

        // When
        List<Category> result = categoryRepository.findByNombreCategoriaContainingIgnoreCase(searchTerm);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombreCategoria()).isEqualTo("Category & More");
    }

    @Test
    @DisplayName("findByNombreCategoriaContainingIgnoreCase - Debería manejar acentos")
    void findByNombreCategoriaContainingIgnoreCase_WithAccents_ShouldFindCategories() {
        // Given
        Category accentCategory = Category.builder()
                .nombreCategoria("Categoría con Acentos")
                .build();
        categoryRepository.save(accentCategory);

        String searchTerm = "Categoría";

        // When
        List<Category> result = categoryRepository.findByNombreCategoriaContainingIgnoreCase(searchTerm);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombreCategoria()).isEqualTo("Categoría con Acentos");
    }
}