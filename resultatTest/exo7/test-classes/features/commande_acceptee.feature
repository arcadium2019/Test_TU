Feature: Commande acceptée

  Background:
    Given le produit "PROD-001" avec prix 100.0 et stock 10

  Scenario: Commande acceptée pour client STANDARD
    When le client STANDARD commande 2 unités du produit "PROD-001"
    Then la commande est acceptée
    And le montant total est 200.0
    And le message de confirmation est "Commande confirmée"

  Scenario: Commande acceptée pour client PREMIUM avec remise de 10%
    When le client PREMIUM commande 2 unités du produit "PROD-001"
    Then la commande est acceptée
    And le montant total est 180.0

  Scenario: Commande acceptée pour client VIP avec remise de 20%
    When le client VIP commande 2 unités du produit "PROD-001"
    Then la commande est acceptée
    And le montant total est 160.0
