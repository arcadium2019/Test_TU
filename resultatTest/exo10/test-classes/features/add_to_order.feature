Feature: Add product to order

  Scenario: Product is added to an existing order
    Given an order with id "ORDER-1" exists
    And a product named "Laptop" in category "Electronics" at price 999.0
    When the user adds "Laptop" to order "ORDER-1"
    Then the product is confirmed as added to the order

  Scenario: Adding the same product increases its quantity
    Given an order with id "ORDER-1" exists with "Laptop" in quantity 1
    And a product named "Laptop" in category "Electronics" at price 999.0
    When the user adds "Laptop" to order "ORDER-1"
    Then the quantity of "Laptop" in the order is 2

  Scenario: Adding to a non-existent order fails
    Given no order with id "ORDER-99" exists
    And a product named "Laptop" in category "Electronics" at price 999.0
    When the user adds "Laptop" to order "ORDER-99"
    Then an error is returned with message "Order not found"
