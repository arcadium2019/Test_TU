Feature: Refused order

  Scenario: Order refused if unknown product
    Given no product with reference "UNKNOWN"
    When the customer STANDARD orders 1 units of product "UNKNOWN"
    Then the order is refused with message "Unknown product"

  Scenario: Order refused if insufficient stock
    Given the product "PROD-002" with price 50.0 and stock 3
    When the customer STANDARD orders 5 units of product "PROD-002"
    Then the order is refused with message "Insufficient stock"
