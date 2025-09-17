package com.microservices.model.dto;

import com.microservices.model.entity.StateOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDto {
    private Long userId;
    private List<DetailOrderRequestDto> details;
}
