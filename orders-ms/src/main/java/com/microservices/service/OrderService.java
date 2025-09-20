package com.microservices.service;

import com.microservices.model.entity.Order;

import java.util.List;

public interface OrderService {
    List<Order> listAll();
    Order findById(Long id);
    Order save(Order order);
    void delete(Long id);
    Order confirmOrder(Long id);
    Order finishOrder(Long id);
    Order cancelOrder(Long id);
    List<Order> listOrdersForUser(Long id);
    List<Order> listOrdersForUserByState(Long id, String state);
    Order removeProductFromOrder(Long orderId, Long productId);
}
