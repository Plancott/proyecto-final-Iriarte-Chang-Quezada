package com.microservices.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDto {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Order details are required")
    @Size(min = 1, message = "Order must have at least one product")
    @Valid
    private List<DetailOrderRequestDto> details;
}
