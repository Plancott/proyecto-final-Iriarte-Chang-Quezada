package com.microservices.service.impl;

import com.microservices.exception.*;
import com.microservices.model.entity.DetailOrder;
import com.microservices.model.entity.Order;
import com.microservices.model.entity.StateOrder;
import com.microservices.repository.OrderRepository;
import com.microservices.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> listAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Override
    public Order save(Order order) {
        Optional<Order> existingCart = orderRepository.findByUserIdAndState
                (order.getUserId(), StateOrder.CARRITO);

        if (order.getDetails() == null || order.getDetails().isEmpty()) {
            throw new InvalidQuantityException("Order must have at least one product");
        }

        if (existingCart.isPresent()) {
            Order cart = existingCart.get();

            for (DetailOrder newDetail : order.getDetails()) {
                if (newDetail.getQuantity() <= 0) {
                    throw new InvalidQuantityException("Product quantity must be greater than 0");
                }

                Optional<DetailOrder> existingDetail = cart.getDetails().stream()
                        .filter(d -> d.getProductId().equals(newDetail.getProductId()))
                        .findFirst();

                if (existingDetail.isPresent()) {
                    DetailOrder detail = existingDetail.get();
                    detail.setQuantity(detail.getQuantity() + newDetail.getQuantity());
                } else {
                    newDetail.setOrder(cart);
                    cart.getDetails().add(newDetail);
                }
            }

            double total = cart.getDetails().stream()
                    .mapToDouble(d -> d.getQuantity() * d.getUnitPrice())
                    .sum();
            cart.setTotal(total);

            return orderRepository.save(cart);
        } else {
            order.setState(StateOrder.CARRITO);

            for (DetailOrder d : order.getDetails()) {
                if (d.getQuantity() <= 0) {
                    throw new InvalidQuantityException("Product quantity must be greater than 0");
                }
                d.setOrder(order);
            }

            double total = order.getDetails().stream()
                    .mapToDouble(d -> d.getQuantity() * d.getUnitPrice())
                    .sum();
            order.setTotal(total);

            return orderRepository.save(order);
        }
    }

    @Override
    public void delete(Long userId) {
        Order existingCart = orderRepository.findByUserIdAndState(userId, StateOrder.CARRITO)
                .orElseThrow(() -> new CartNotFoundException(userId));
        orderRepository.delete(existingCart);
    }

    @Override
    public Order confirmOrder(Long userId) {
        Order existingCart = orderRepository.findByUserIdAndState(userId, StateOrder.CARRITO)
                .orElseThrow(() -> new CartNotFoundException(userId));

        existingCart.setState(StateOrder.PENDIENTE);
        return orderRepository.save(existingCart);
    }

    @Override
    public Order finishOrder(Long idOrder) {
        Order order = orderRepository.findById(idOrder)
                .orElseThrow(() -> new OrderNotFoundException(idOrder));

        if (order.getState() != StateOrder.PENDIENTE) {
            throw new InvalidOrderStateException("Order must be in PENDIENTE state to finish. Current: " + order.getState());
        }

        order.setState(StateOrder.ENTREGADO);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long idOrder) {
        Order order = orderRepository.findById(idOrder)
                .orElseThrow(() -> new OrderNotFoundException(idOrder));

        if (order.getState() != StateOrder.PENDIENTE) {
            throw new InvalidOrderStateException("Only orders in PENDIENTE state can be canceled. Current: " + order.getState());
        }

        order.setState(StateOrder.CANCELADO);
        return orderRepository.save(order);
    }

    @Override
    public List<Order> listOrdersForUser(Long userId) {
        return orderRepository.findAllByUserId(userId);
    }

    @Override
    public List<Order> listOrdersForUserByState(Long userId, String state) {
        return orderRepository.findAllByUserIdAndState(userId, StateOrder.valueOf(state));
    }

    @Override
    public Order removeProductFromOrder(Long userId, Long productId) {
        Order order = orderRepository.findByUserIdAndState(userId, StateOrder.CARRITO)
                .orElseThrow(() -> new CartNotFoundException(userId));

        if (order.getState() != StateOrder.CARRITO) {
            throw new OrderStateException("Cannot remove product. Order is in state: " + order.getState());
        }

        boolean removed = order.getDetails().removeIf(d -> d.getProductId().equals(productId));

        if (!removed) {
            throw new ProductNotFoundException("Product with id " + productId + " not found in order");
        }

        double total = order.getDetails().stream()
                .mapToDouble(d -> d.getQuantity() * d.getUnitPrice())
                .sum();
        order.setTotal(total);

        return orderRepository.save(order);
    }
}