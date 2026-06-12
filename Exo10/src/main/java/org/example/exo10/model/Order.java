package org.example.exo10.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Order {
    private final String id;
    private final Map<String, Integer> items = new LinkedHashMap<>();
    private boolean validated;

    public Order(String id) {
        this.id = id;
    }

    public void addProduct(Product product) {
        items.merge(product.getName(), 1, Integer::sum);
    }

    public void removeProduct(Product product) {
        String name = product.getName();
        int qty = items.get(name);
        if (qty <= 1) {
            items.remove(name);
        } else {
            items.put(name, qty - 1);
        }
    }

    public boolean containsProduct(Product product) {
        return items.containsKey(product.getName());
    }

    public int getQuantity(String productName) {
        return items.getOrDefault(productName, 0);
    }

    public String getId()              { return id; }
    public Map<String, Integer> getItems() { return items; }
    public boolean isValidated()       { return validated; }
    public void setValidated(boolean v) { this.validated = v; }
}
