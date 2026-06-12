Feature: Meeting room reservation

  Scenario: Accepted reservation when the room exists and the slot is free
    Given a room named "Salle Alpha" with capacity 10 exists
    When I book the room "Salle Alpha" for "Alice"
    Then the reservation is confirmed

  Scenario: Refused reservation when the room does not exist
    Given no room exists
    When I try to book a non-existent room for "Bob"
    Then the reservation is rejected because the room does not exist

  Scenario: Refused reservation when the slot overlaps an existing one
    Given a room named "Salle Beta" with capacity 6 exists
    And a reservation for room "Salle Beta" by "Carol" is already confirmed for the same slot
    When I try to book the same room for "Dave" during the same slot
    Then the reservation is rejected due to a time slot conflict
