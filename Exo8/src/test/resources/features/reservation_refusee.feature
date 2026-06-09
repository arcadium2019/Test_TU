Feature: Réservation refusée

  Scenario: Réservation refusée si salle inconnue
    Given aucune salle avec le code "INCONNUE"
    When l'utilisateur "alice@test.com" réserve "INCONNUE" pour 5 participants du "2026-06-10 09:00" au "2026-06-10 11:00"
    Then la réservation est refusée avec le message "Salle inconnue"

  Scenario: Réservation refusée si capacité insuffisante
    Given la salle "SALLE-B" avec nom "Salle Beta" et capacité 5
    When l'utilisateur "alice@test.com" réserve "SALLE-B" pour 10 participants du "2026-06-10 09:00" au "2026-06-10 11:00"
    Then la réservation est refusée avec le message "Capacité insuffisante"

  Scenario: Réservation refusée si la date de fin est avant la date de début
    Given la salle "SALLE-C" avec nom "Salle Gamma" et capacité 10
    When l'utilisateur "alice@test.com" réserve "SALLE-C" pour 5 participants du "2026-06-10 11:00" au "2026-06-10 09:00"
    Then la réservation est refusée avec le message "Période invalide"

  Scenario: Réservation refusée si la date de fin est égale à la date de début
    Given la salle "SALLE-C" avec nom "Salle Gamma" et capacité 10
    When l'utilisateur "alice@test.com" réserve "SALLE-C" pour 5 participants du "2026-06-10 09:00" au "2026-06-10 09:00"
    Then la réservation est refusée avec le message "Période invalide"

  Scenario: Réservation refusée si conflit de réservation
    Given la salle "SALLE-A" avec nom "Salle Alpha" et capacité 10
    And une réservation existante sur "SALLE-A" du "2026-06-10 09:00" au "2026-06-10 11:00"
    When l'utilisateur "alice@test.com" réserve "SALLE-A" pour 5 participants du "2026-06-10 10:00" au "2026-06-10 12:00"
    Then la réservation est refusée avec le message "Créneau indisponible"
