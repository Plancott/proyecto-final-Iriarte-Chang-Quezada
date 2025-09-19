package com.microservices.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {
    // Enum para estados del producto
    
    ACTIVE("Activo"),
    INACTIVE("Inactivo");

    private final String displayName;

    /**
     * Verifica si el producto está activo
     * @return true si está ACTIVO, false en caso contrario
     */
    public boolean isActive() {
        return this == ACTIVE;
    }

    /**
     * Verifica si el producto está inactivo
     * @return true si está INACTIVE, false en caso contrario
     */
    public boolean isInactive() {
        return this == INACTIVE;
    }
}

