Feature: Refused reservation

  Scenario: Reservation refused if unknown room
    Given no room with code "UNKNOWN"
    When the user "alice@test.com" books "UNKNOWN" for 5 participants from "2026-06-10 09:00" to "2026-06-10 11:00"
    Then the reservation is refused with message "Unknown room"

  Scenario: Reservation refused if insufficient capacity
    Given the room "SALLE-B" with name "Salle Beta" and capacity 5
    When the user "alice@test.com" books "SALLE-B" for 10 participants from "2026-06-10 09:00" to "2026-06-10 11:00"
    Then the reservation is refused with message "Insufficient capacity"

  Scenario: Reservation refused if end date is before start date
    Given the room "SALLE-C" with name "Salle Gamma" and capacity 10
    When the user "alice@test.com" books "SALLE-C" for 5 participants from "2026-06-10 11:00" to "2026-06-10 09:00"
    Then the reservation is refused with message "Invalid period"

  Scenario: Reservation refused if end date equals start date
    Given the room "SALLE-C" with name "Salle Gamma" and capacity 10
    When the user "alice@test.com" books "SALLE-C" for 5 participants from "2026-06-10 09:00" to "2026-06-10 09:00"
    Then the reservation is refused with message "Invalid period"

  Scenario: Reservation refused if booking conflict
    Given the room "SALLE-A" with name "Salle Alpha" and capacity 10
    And an existing reservation on "SALLE-A" from "2026-06-10 09:00" to "2026-06-10 11:00"
    When the user "alice@test.com" books "SALLE-A" for 5 participants from "2026-06-10 10:00" to "2026-06-10 12:00"
    Then the reservation is refused with message "Slot unavailable"
