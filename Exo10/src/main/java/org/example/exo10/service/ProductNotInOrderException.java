package org.example.exo10.service;

public class ProductNotInOrderException extends RuntimeException {
    public ProductNotInOrderException(String message) {
        super(message);
    }
}
