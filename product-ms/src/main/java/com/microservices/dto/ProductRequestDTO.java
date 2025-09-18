package com.microservices.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDTO {
    
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre del producto debe tener entre 2 y 100 caracteres")
    private String name;
    
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String description;
    
    @NotNull(message = "El ID de categoría es obligatorio")
    private Long categoryId;
    
    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio unitario debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "El precio unitario debe tener máximo 8 dígitos enteros y 2 decimales")
    private BigDecimal unitPrice;
    
    @Size(max = 500, message = "La URL de la imagen no puede exceder 500 caracteres")
    @Pattern(regexp = "^(https?://.*|)$", message = "La URL de la imagen debe ser válida o estar vacía")
    private String imageUrl;
    
    @NotNull(message = "El ID de marca es obligatorio")
    private Long brandId;
}
