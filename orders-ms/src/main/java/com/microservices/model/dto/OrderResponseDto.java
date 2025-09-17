package com.microservices.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private String state;
    private Double total;
    private LocalDateTime orderDate;
    private List<DetailOrderResponseDto> details;
}
