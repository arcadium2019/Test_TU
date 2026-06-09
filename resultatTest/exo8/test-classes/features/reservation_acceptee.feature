Feature: Réservation acceptée

  Background:
    Given la salle "SALLE-A" avec nom "Salle Alpha" et capacité 10

  Scenario: Réservation acceptée
    When l'utilisateur "alice@test.com" réserve "SALLE-A" pour 5 participants du "2026-06-10 09:00" au "2026-06-10 11:00"
    Then la réservation est acceptée
    And le message de confirmation est "Réservation confirmée"

  Scenario: Réservation acceptée à capacité maximale
    When l'utilisateur "alice@test.com" réserve "SALLE-A" pour 10 participants du "2026-06-10 09:00" au "2026-06-10 11:00"
    Then la réservation est acceptée

  Scenario: Réservation acceptée si le créneau commence après une réservation existante
    Given une réservation existante sur "SALLE-A" du "2026-06-10 09:00" au "2026-06-10 11:00"
    When l'utilisateur "alice@test.com" réserve "SALLE-A" pour 5 participants du "2026-06-10 11:00" au "2026-06-10 13:00"
    Then la réservation est acceptée
