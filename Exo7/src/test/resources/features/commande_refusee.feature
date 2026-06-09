Feature: Commande refusée

  Scenario: Commande refusée si produit inconnu
    Given aucun produit avec la référence "INCONNU"
    When le client STANDARD commande 1 unités du produit "INCONNU"
    Then la commande est refusée avec le message "Produit inconnu"

  Scenario: Commande refusée si stock insuffisant
    Given le produit "PROD-002" avec prix 50.0 et stock 3
    When le client STANDARD commande 5 unités du produit "PROD-002"
    Then la commande est refusée avec le message "Stock insuffisant"
