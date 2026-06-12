Feature: Category navigation

  Scenario: Browse products by category
    Given a product named "Laptop" in category "Electronics" at price 999.0
    And a product named "T-Shirt" in category "Clothing" at price 19.99
    When the user browses the category "Electronics"
    Then the results contain "Laptop"
    And the results do not contain "T-Shirt"
