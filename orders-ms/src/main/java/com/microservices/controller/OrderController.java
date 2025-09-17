package com.microservices.controller;

import com.microservices.mapper.OrderMapper;
import com.microservices.model.dto.OrderRequestDto;
import com.microservices.model.dto.OrderResponseDto;
import com.microservices.model.entity.Order;
import com.microservices.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> listAll() {
        return ResponseEntity.ok(
                orderService.listAll().stream()
                        .map(OrderMapper.INSTANCE::toOrderResponseDto)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                OrderMapper.INSTANCE.toOrderResponseDto(orderService.findById(id))
        );
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> save(@RequestBody OrderRequestDto dto) {
        return ResponseEntity.ok(
                OrderMapper.INSTANCE.toOrderResponseDto(orderService.save(OrderMapper.INSTANCE.toEntity(dto)))
        );
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<OrderResponseDto> confirm(@PathVariable Long id) {
        return ResponseEntity.ok(
                OrderMapper.INSTANCE.toOrderResponseDto(orderService.confirmOrder(id))
        );
    }

    @PutMapping("/{id}/finish")
    public ResponseEntity<OrderResponseDto> finish(@PathVariable Long id) {
        return ResponseEntity.ok(
                OrderMapper.INSTANCE.toOrderResponseDto(orderService.finishOrder(id))
        );
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponseDto> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(
                OrderMapper.INSTANCE.toOrderResponseDto(orderService.cancelOrder(id))
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDto>> listOrdersForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(
                orderService.listOrdersForUser(userId).stream()
                        .map(OrderMapper.INSTANCE::toOrderResponseDto)
                        .toList()
        );
    }

    @GetMapping("/user/{userId}/state/{state}")
    public ResponseEntity<List<OrderResponseDto>> listOrdersForUserByState(@PathVariable Long userId,
                                                                           @PathVariable String state) {
        return ResponseEntity.ok(
                orderService.listOrdersForUserByState(userId, state).stream()
                        .map(OrderMapper.INSTANCE::toOrderResponseDto)
                        .toList()
        );
    }

    @DeleteMapping("delete/{orderId}/product/{productId}")
    public ResponseEntity<OrderResponseDto> removeProduct(
            @PathVariable Long orderId,
            @PathVariable Long productId) {
        Order order = orderService.removeProductFromOrder(orderId, productId);
        return ResponseEntity.ok(OrderMapper.INSTANCE.toOrderResponseDto(order));
    }

}
