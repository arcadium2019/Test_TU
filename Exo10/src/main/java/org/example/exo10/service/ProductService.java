package org.example.exo10.service;

import org.example.exo10.model.Product;
import org.example.exo10.repository.ProductRepository;

import java.util.List;

public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> searchByKeyword(String keyword) {
        return productRepository.findByKeyword(keyword);
    }

    public List<Product> searchByMaxPrice(double maxPrice) {
        return productRepository.findByMaxPrice(maxPrice);
    }

    public List<Product> browseByCategory(String category) {
        return productRepository.findByCategory(category);
    }
}
