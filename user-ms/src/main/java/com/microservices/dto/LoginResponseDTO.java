package com.microservices.dto;

import com.microservices.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {

    private String token; // JWT Token
    private String tokenType; // "Bearer"
    private Long expiresIn; // Tiempo de expiración en milisegundos
    
    // Datos del usuario
    private Long userId;
    private String userName;
    private String email;
    private String name;
    private String lastName;
    private UserRole role;
    private LocalDateTime registerDate;
}
