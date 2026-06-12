Feature: Accepted order

  Background:
    Given the product "PROD-001" with price 100.0 and stock 10

  Scenario: Order accepted for STANDARD customer
    When the customer STANDARD orders 2 units of product "PROD-001"
    Then the order is accepted
    And the total amount is 200.0
    And the confirmation message is "Order confirmed"

  Scenario: Order accepted for PREMIUM customer with 10% discount
    When the customer PREMIUM orders 2 units of product "PROD-001"
    Then the order is accepted
    And the total amount is 180.0

  Scenario: Order accepted for VIP customer with 20% discount
    When the customer VIP orders 2 units of product "PROD-001"
    Then the order is accepted
    And the total amount is 160.0
