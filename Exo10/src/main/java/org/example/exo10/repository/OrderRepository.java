package org.example.exo10.repository;

import org.example.exo10.model.Order;

import java.util.Optional;

public interface OrderRepository {
    Optional<Order> findById(String id);
    void save(Order order);
}
