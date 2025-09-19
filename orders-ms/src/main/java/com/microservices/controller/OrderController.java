package com.microservices.controller;

import com.microservices.mapper.OrderMapper;
import com.microservices.model.dto.OrderRequestDto;
import com.microservices.model.dto.OrderResponseDto;
import com.microservices.model.entity.Order;
import com.microservices.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> listAll() {
        log.info("Getting all orders");
        List<OrderResponseDto> orders = orderService.listAll().stream()
                .map(OrderMapper.INSTANCE::toOrderResponseDto)
                .toList();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> findById(@PathVariable @Positive Long id) {
        log.info("Getting order by ID: {}", id);
        OrderResponseDto order = OrderMapper.INSTANCE.toOrderResponseDto(orderService.findById(id));
        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> save(@Valid @RequestBody OrderRequestDto dto) {
        log.info("Creating order for user: {}", dto.getUserId());
        Order order = OrderMapper.INSTANCE.toEntity(dto);
        OrderResponseDto savedOrder = OrderMapper.INSTANCE.toOrderResponseDto(orderService.save(order));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<OrderResponseDto> confirm(@PathVariable @Positive Long id) {
        log.info("Confirming order: {}", id);
        OrderResponseDto order = OrderMapper.INSTANCE.toOrderResponseDto(orderService.confirmOrder(id));
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}/finish")
    public ResponseEntity<OrderResponseDto> finish(@PathVariable @Positive Long id) {
        log.info("Finishing order: {}", id);
        OrderResponseDto order = OrderMapper.INSTANCE.toOrderResponseDto(orderService.finishOrder(id));
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponseDto> cancel(@PathVariable @Positive Long id) {
        log.info("Canceling order: {}", id);
        OrderResponseDto order = OrderMapper.INSTANCE.toOrderResponseDto(orderService.cancelOrder(id));
        return ResponseEntity.ok(order);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDto>> listOrdersForUser(@PathVariable @Positive Long userId) {
        log.info("Getting orders for user: {}", userId);
        List<OrderResponseDto> orders = orderService.listOrdersForUser(userId).stream()
                .map(OrderMapper.INSTANCE::toOrderResponseDto)
                .toList();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}/state/{state}")
    public ResponseEntity<List<OrderResponseDto>> listOrdersForUserByState(
            @PathVariable @Positive Long userId,
            @PathVariable String state) {
        log.info("Getting orders for user: {} with state: {}", userId, state);
        List<OrderResponseDto> orders = orderService.listOrdersForUserByState(userId, state).stream()
                .map(OrderMapper.INSTANCE::toOrderResponseDto)
                .toList();
        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("delete/{orderId}/product/{productId}")
    public ResponseEntity<OrderResponseDto> removeProduct(
            @PathVariable @Positive Long orderId,
            @PathVariable @Positive Long productId) {
        log.info("Removing product: {} from order: {}", productId, orderId);
        Order order = orderService.removeProductFromOrder(orderId, productId);
        return ResponseEntity.ok(OrderMapper.INSTANCE.toOrderResponseDto(order));
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteCart(@PathVariable @Positive Long userId) {
        log.info("Deleting cart for user: {}", userId);
        orderService.delete(userId);
        return ResponseEntity.noContent().build();
    }


}
