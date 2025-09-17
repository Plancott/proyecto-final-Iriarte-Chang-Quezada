package com.microservices.mapper;

import com.microservices.model.dto.DetailOrderRequestDto;
import com.microservices.model.dto.DetailOrderResponseDto;
import com.microservices.model.dto.OrderRequestDto;
import com.microservices.model.dto.OrderResponseDto;
import com.microservices.model.entity.DetailOrder;
import com.microservices.model.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "orderDate", expression = "java(java.time.LocalDateTime.now())")
    Order toEntity(OrderRequestDto dto);

    OrderResponseDto toOrderResponseDto(Order order);

    DetailOrder toDetailEntity(DetailOrderRequestDto request);

    DetailOrderResponseDto toDetailResponse(DetailOrder entity);

    List<OrderResponseDto> toResponseList(List<Order> orders);
}
