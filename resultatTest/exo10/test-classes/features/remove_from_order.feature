Feature: Remove product from order

  Scenario: Product quantity is decremented when greater than 1
    Given an order with id "ORDER-1" exists with "Laptop" in quantity 2
    And a product named "Laptop" in category "Electronics" at price 999.0
    When the user removes "Laptop" from order "ORDER-1"
    Then the quantity of "Laptop" in the order is 1

  Scenario: Product is removed when quantity is 1
    Given an order with id "ORDER-1" exists with "Laptop" in quantity 1
    And a product named "Laptop" in category "Electronics" at price 999.0
    When the user removes "Laptop" from order "ORDER-1"
    Then "Laptop" is no longer in the order

  Scenario: Removing a product not in the order fails
    Given an order with id "ORDER-1" exists
    And a product named "Laptop" in category "Electronics" at price 999.0
    When the user removes "Laptop" from order "ORDER-1"
    Then an error is returned with message "Product not in order"

  Scenario: Removing from a non-existent order fails
    Given no order with id "ORDER-99" exists
    And a product named "Laptop" in category "Electronics" at price 999.0
    When the user removes "Laptop" from order "ORDER-99"
    Then an error is returned with message "Order not found"
