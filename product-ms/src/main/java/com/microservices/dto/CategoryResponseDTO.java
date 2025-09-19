package com.microservices.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDTO {
    
    private Long idCategoria;
    private String nombreCategoria;
}

