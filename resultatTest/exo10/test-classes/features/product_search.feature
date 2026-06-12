Feature: Product search

  Scenario: Search by keyword returns matching products
    Given a product named "Laptop" in category "Electronics" at price 999.0
    When the user searches for "Laptop"
    Then the results contain "Laptop"

  Scenario: Search by maximum price filters products
    Given a product named "Laptop" in category "Electronics" at price 999.0
    And a product named "Keyboard" in category "Electronics" at price 49.0
    When the user searches with a maximum price of 100.0
    Then the results contain "Keyboard"
    And the results do not contain "Laptop"
