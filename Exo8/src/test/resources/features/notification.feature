Feature: Notification de réservation

  Scenario: Notification envoyée en cas de succès
    Given la salle "SALLE-A" avec nom "Salle Alpha" et capacité 10
    When l'utilisateur "alice@test.com" réserve "SALLE-A" pour 5 participants du "2026-06-10 09:00" au "2026-06-10 11:00"
    Then la réservation est acceptée
    And une notification a été envoyée à "alice@test.com"

  Scenario: Notification non envoyée en cas d'échec
    Given aucune salle avec le code "INCONNUE"
    When l'utilisateur "alice@test.com" réserve "INCONNUE" pour 5 participants du "2026-06-10 09:00" au "2026-06-10 11:00"
    Then la réservation est refusée avec le message "Salle inconnue"
    And aucune notification n'a été envoyée
