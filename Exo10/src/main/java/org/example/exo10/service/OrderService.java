package org.example.exo10.service;

import org.example.exo10.model.Order;
import org.example.exo10.model.Product;
import org.example.exo10.repository.OrderRepository;

public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void addProduct(String orderId, Product product) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.addProduct(product);
        orderRepository.save(order);
    }

    public void removeProduct(String orderId, Product product) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        if (!order.containsProduct(product)) {
            throw new ProductNotInOrderException("Product not in order");
        }
        order.removeProduct(product);
        orderRepository.save(order);
    }

    public Order validateOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.setValidated(true);
        orderRepository.save(order);
        return order;
    }
}
