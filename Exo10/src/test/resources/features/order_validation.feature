Feature: Order validation

  Scenario: Order is validated successfully
    Given an order with id "ORDER-1" exists
    When the user validates order "ORDER-1"
    Then the order is confirmed

  Scenario: Validating a non-existent order fails
    Given no order with id "ORDER-99" exists
    When the user validates order "ORDER-99"
    Then an error is returned with message "Order not found"
