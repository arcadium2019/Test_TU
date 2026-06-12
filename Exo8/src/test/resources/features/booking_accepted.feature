Feature: Accepted reservation

  Background:
    Given the room "SALLE-A" with name "Salle Alpha" and capacity 10

  Scenario: Reservation accepted
    When the user "alice@test.com" books "SALLE-A" for 5 participants from "2026-06-10 09:00" to "2026-06-10 11:00"
    Then the reservation is accepted
    And the confirmation message is "Reservation confirmed"

  Scenario: Reservation accepted at maximum capacity
    When the user "alice@test.com" books "SALLE-A" for 10 participants from "2026-06-10 09:00" to "2026-06-10 11:00"
    Then the reservation is accepted

  Scenario: Reservation accepted if slot starts after existing reservation
    Given an existing reservation on "SALLE-A" from "2026-06-10 09:00" to "2026-06-10 11:00"
    When the user "alice@test.com" books "SALLE-A" for 5 participants from "2026-06-10 11:00" to "2026-06-10 13:00"
    Then the reservation is accepted
