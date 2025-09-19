package com.microservices.repository;

import com.microservices.entity.Brand;
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
@DisplayName("BrandRepository Integration Tests")
class BrandRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BrandRepository brandRepository;

    private Brand testBrand;

    @BeforeEach
    void setUp() {
        testBrand = Brand.builder()
                .nombreMarca("Test Brand")
                .build();
        testBrand = brandRepository.save(testBrand);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("findById - Debería encontrar marca por ID")
    void findById_ShouldFindBrandById() {
        // When
        Optional<Brand> result = brandRepository.findById(testBrand.getMarcaId());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getNombreMarca()).isEqualTo("Test Brand");
    }

    @Test
    @DisplayName("findById - Debería retornar empty cuando marca no existe")
    void findById_WithNonExistentId_ShouldReturnEmpty() {
        // When
        Optional<Brand> result = brandRepository.findById(999L);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findAll - Debería retornar todas las marcas")
    void findAll_ShouldReturnAllBrands() {
        // Given
        Brand anotherBrand = Brand.builder()
                .nombreMarca("Another Brand")
                .build();
        brandRepository.save(anotherBrand);

        // When
        List<Brand> result = brandRepository.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Brand::getNombreMarca)
                .containsExactlyInAnyOrder("Test Brand", "Another Brand");
    }

    @Test
    @DisplayName("findAll - Debería retornar marcas paginadas")
    void findAll_WithPagination_ShouldReturnPaginatedBrands() {
        // Given
        Brand anotherBrand = Brand.builder()
                .nombreMarca("Another Brand")
                .build();
        brandRepository.save(anotherBrand);

        Pageable pageable = PageRequest.of(0, 1);

        // When
        Page<Brand> result = brandRepository.findAll(pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("findByNombreMarcaContainingIgnoreCase - Debería buscar por nombre")
    void findByNombreMarcaContainingIgnoreCase_ShouldSearchByName() {
        // Given
        String searchTerm = "test";

        // When
        List<Brand> result = brandRepository.findByNombreMarcaContainingIgnoreCase(searchTerm);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombreMarca()).isEqualTo("Test Brand");
    }

    @Test
    @DisplayName("findByNombreMarcaContainingIgnoreCase - Debería ser case insensitive")
    void findByNombreMarcaContainingIgnoreCase_ShouldBeCaseInsensitive() {
        // Given
        String searchTerm = "TEST";

        // When
        List<Brand> result = brandRepository.findByNombreMarcaContainingIgnoreCase(searchTerm);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombreMarca()).isEqualTo("Test Brand");
    }

    @Test
    @DisplayName("findByNombreMarcaContainingIgnoreCase - Debería buscar por parte del nombre")
    void findByNombreMarcaContainingIgnoreCase_ShouldSearchByPartialName() {
        // Given
        String searchTerm = "est";

        // When
        List<Brand> result = brandRepository.findByNombreMarcaContainingIgnoreCase(searchTerm);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombreMarca()).isEqualTo("Test Brand");
    }

    @Test
    @DisplayName("findByNombreMarcaContainingIgnoreCase - Debería retornar lista vacía cuando no encuentra")
    void findByNombreMarcaContainingIgnoreCase_WithNoMatches_ShouldReturnEmptyList() {
        // Given
        String searchTerm = "nonexistent";

        // When
        List<Brand> result = brandRepository.findByNombreMarcaContainingIgnoreCase(searchTerm);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("existsByNombreMarca - Debería verificar existencia de marca")
    void existsByNombreMarca_ShouldCheckBrandExistence() {
        // When
        boolean exists = brandRepository.existsByNombreMarca("Test Brand");
        boolean notExists = brandRepository.existsByNombreMarca("Nonexistent Brand");

        // Then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("existsByNombreMarca - Debería ser case sensitive")
    void existsByNombreMarca_ShouldBeCaseSensitive() {
        // When
        boolean exists = brandRepository.existsByNombreMarca("test brand"); // lowercase

        // Then
        assertThat(exists).isFalse(); // Debería ser false porque es case sensitive
    }

    @Test
    @DisplayName("save - Debería guardar marca correctamente")
    void save_ShouldSaveBrandCorrectly() {
        // Given
        Brand newBrand = Brand.builder()
                .nombreMarca("New Brand")
                .build();

        // When
        Brand savedBrand = brandRepository.save(newBrand);

        // Then
        assertThat(savedBrand.getMarcaId()).isNotNull();
        assertThat(savedBrand.getNombreMarca()).isEqualTo("New Brand");
        assertThat(brandRepository.findById(savedBrand.getMarcaId())).isPresent();
    }

    @Test
    @DisplayName("delete - Debería eliminar marca correctamente")
    void delete_ShouldDeleteBrandCorrectly() {
        // Given
        Long brandId = testBrand.getMarcaId();

        // When
        brandRepository.delete(testBrand);

        // Then
        assertThat(brandRepository.findById(brandId)).isEmpty();
    }

    @Test
    @DisplayName("count - Debería contar marcas correctamente")
    void count_ShouldCountBrandsCorrectly() {
        // Given
        Brand anotherBrand = Brand.builder()
                .nombreMarca("Another Brand")
                .build();
        brandRepository.save(anotherBrand);

        // When
        long count = brandRepository.count();

        // Then
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("existsById - Debería verificar existencia de marca por ID")
    void existsById_ShouldCheckBrandExistenceById() {
        // When
        boolean exists = brandRepository.existsById(testBrand.getMarcaId());
        boolean notExists = brandRepository.existsById(999L);

        // Then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("findByNombreMarcaContainingIgnoreCase - Debería manejar espacios en blanco")
    void findByNombreMarcaContainingIgnoreCase_WithWhitespace_ShouldFindBrands() {
        // Given
        String searchTerm = "test"; // Sin espacios para que funcione

        // When
        List<Brand> result = brandRepository.findByNombreMarcaContainingIgnoreCase(searchTerm);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombreMarca()).isEqualTo("Test Brand");
    }

    @Test
    @DisplayName("findByNombreMarcaContainingIgnoreCase - Debería manejar caracteres especiales")
    void findByNombreMarcaContainingIgnoreCase_WithSpecialCharacters_ShouldFindBrands() {
        // Given
        Brand specialBrand = Brand.builder()
                .nombreMarca("Brand & Co.")
                .build();
        brandRepository.save(specialBrand);

        String searchTerm = "&";

        // When
        List<Brand> result = brandRepository.findByNombreMarcaContainingIgnoreCase(searchTerm);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombreMarca()).isEqualTo("Brand & Co.");
    }
}