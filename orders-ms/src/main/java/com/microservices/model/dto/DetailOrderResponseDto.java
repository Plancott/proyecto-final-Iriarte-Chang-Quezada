package com.microservices.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailOrderResponseDto {
    private Long id;
    private Long productId;
    private Integer quantity;
    private Double unitPrice;
}
