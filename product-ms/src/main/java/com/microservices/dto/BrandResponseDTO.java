package com.microservices.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandResponseDTO {
    
    private Long marcaId;
    private String nombreMarca;
}
