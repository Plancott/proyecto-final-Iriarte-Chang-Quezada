package com.microservices.repository;

import com.microservices.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    // Buscar por nombre
    Optional<Category> findByNombreCategoria(String nombreCategoria);
    
    // Verificar si existe una categoría con el mismo nombre
    boolean existsByNombreCategoria(String nombreCategoria);
    
    // Buscar todas las categorías ordenadas por nombre
    List<Category> findAllByOrderByNombreCategoriaAsc();
    
    // Buscar categorías que contengan un texto en el nombre
    List<Category> findByNombreCategoriaContainingIgnoreCase(String nombreCategoria);
}

