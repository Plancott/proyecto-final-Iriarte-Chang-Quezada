package com.microservices.repository;

import com.microservices.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    
    // Buscar por nombre
    Optional<Brand> findByNombreMarca(String nombreMarca);
    
    // Verificar si existe una marca con el mismo nombre
    boolean existsByNombreMarca(String nombreMarca);
    
    // Buscar todas las marcas ordenadas por nombre
    List<Brand> findAllByOrderByNombreMarcaAsc();
    
    // Buscar marcas que contengan un texto en el nombre
    List<Brand> findByNombreMarcaContainingIgnoreCase(String nombreMarca);
}
