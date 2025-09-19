package com.microservices.repository;

import com.microservices.entity.User;
import com.microservices.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Repositorio JPA para operaciones de base de datos con métodos de búsqueda personalizados

    Optional<User> findByUserName(String userName); 

    Optional<User> findByEmail(String email); 


    boolean existsByUserName(String userName); 

    boolean existsByEmail(String email); 

    List<User> findByRole(UserRole role); 
}
