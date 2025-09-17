package com.microservices.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum UserRole {
    // Enum para roles de usuario con permisos específicos

    ADMIN("Administrador"),
    USER("Usuario");

    private final String displayName;

    /**
     * Verifica si el rol tiene permisos de administrador
     * @return true si es ADMIN, false en caso contrario
     */
    public boolean isAdmin() {
        return this == ADMIN ? true : false;
    }

    
    public boolean isUser() {
        return this == USER ? true : false;
    }
}
