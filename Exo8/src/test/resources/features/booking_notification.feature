Feature: Reservation notification

  Scenario: Notification sent on success
    Given the room "SALLE-A" with name "Salle Alpha" and capacity 10
    When the user "alice@test.com" books "SALLE-A" for 5 participants from "2026-06-10 09:00" to "2026-06-10 11:00"
    Then the reservation is accepted
    And a notification has been sent to "alice@test.com"

  Scenario: Notification not sent on failure
    Given no room with code "UNKNOWN"
    When the user "alice@test.com" books "UNKNOWN" for 5 participants from "2026-06-10 09:00" to "2026-06-10 11:00"
    Then the reservation is refused with message "Unknown room"
    And no notification has been sent
