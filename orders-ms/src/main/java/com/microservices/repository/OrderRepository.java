package com.microservices.repository;

import com.microservices.model.entity.Order;
import com.microservices.model.entity.StateOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByUserIdAndState(Long userId, StateOrder state);
    List<Order> findAllByUserId(Long userId);
    List<Order> findAllByUserIdAndState(Long userId, StateOrder state);
}
