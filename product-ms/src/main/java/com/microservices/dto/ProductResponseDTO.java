package com.microservices.dto;

import com.microservices.entity.ProductStatus;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO {
    
    private Long productId;
    private String name;
    private String description;
    private Long categoryId;
    private String categoryName;
    private BigDecimal unitPrice;
    private String imageUrl;
    private Long brandId;
    private String brandName;
    private ProductStatus status;
}
