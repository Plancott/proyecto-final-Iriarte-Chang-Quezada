package com.microservices.dto;

import com.microservices.entity.ProductStatus;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSearchDTO {
    
    private String searchText;
    private Long categoryId;
    private Long brandId;
    private ProductStatus status;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortDirection;
}

