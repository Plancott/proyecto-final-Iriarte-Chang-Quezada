package com.microservices.model.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class DetailOrderRequestDto {
    private Long productId;

    private Integer quantity;

    private double unitPrice;
}
