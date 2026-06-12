Feature: Ticket management

  Scenario: Creating a valid ticket
    Given no tickets exist
    When I create a ticket with title "Login error" and priority "HIGH"
    Then the ticket is created with status "OPEN"

  Scenario: Resolving a ticket
    Given a ticket with title "Crash on startup" and priority "MEDIUM" exists with status "OPEN"
    When I update the status to "RESOLVED"
    Then the ticket status is "RESOLVED"

  Scenario: Refusing to modify a resolved ticket
    Given a ticket with title "Old bug" and priority "LOW" exists with status "RESOLVED"
    When I try to update the status to "IN_PROGRESS"
    Then an error occurs

  Scenario: Consulting a non-existent ticket
    When I look for ticket with id "non-existent-id"
    Then a not found error occurs

  Scenario: Progressing a ticket from OPEN to IN_PROGRESS
    Given a ticket with title "Performance issue" and priority "HIGH" exists with status "OPEN"
    When I update the status to "IN_PROGRESS"
    Then the ticket status is "IN_PROGRESS"

  Scenario: Resolving a ticket that is in progress
    Given a ticket with title "Memory leak" and priority "HIGH" exists with status "IN_PROGRESS"
    When I update the status to "RESOLVED"
    Then the ticket status is "RESOLVED"
