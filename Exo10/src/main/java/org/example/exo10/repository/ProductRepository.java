package org.example.exo10.repository;

import org.example.exo10.model.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> findByKeyword(String keyword);
    List<Product> findByCategory(String category);
    List<Product> findByMaxPrice(double maxPrice);
}
