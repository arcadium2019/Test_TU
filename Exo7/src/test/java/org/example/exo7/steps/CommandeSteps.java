package org.example.exo7.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.exo7.model.Commande;
import org.example.exo7.model.Produit;
import org.example.exo7.model.Recu;
import org.example.exo7.model.TypeClient;
import org.example.exo7.repository.ProduitRepository;
import org.example.exo7.service.CommandeRefuseeException;
import org.example.exo7.service.CommandeService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommandeSteps {

    private ProduitRepository produitRepository;
    private CommandeService commandeService;
    private Recu recu;
    private Exception exception;

    @Before
    public void setUp() {
        produitRepository = mock(ProduitRepository.class);
        commandeService = new CommandeService(produitRepository);
        recu = null;
        exception = null;
    }

    @Given("le produit {string} avec prix {double} et stock {int}")
    public void leProduitAvecPrixEtStock(String ref, double prix, int stock) {
        Produit produit = new Produit(ref, "Produit " + ref, prix, stock);
        when(produitRepository.findByReference(ref)).thenReturn(Optional.of(produit));
    }

    @Given("aucun produit avec la référence {string}")
    public void aucunProduitAvecLaReference(String ref) {
        when(produitRepository.findByReference(ref)).thenReturn(Optional.empty());
    }

    @When("le client {word} commande {int} unités du produit {string}")
    public void leClientCommande(String typeClient, int quantite, String ref) {
        Commande commande = new Commande("client@test.com", ref, quantite, TypeClient.valueOf(typeClient));
        try {
            recu = commandeService.passerCommande(commande);
        } catch (CommandeRefuseeException e) {
            exception = e;
        }
    }

    @Then("la commande est acceptée")
    public void laCommandeEstAcceptee() {
        assertNull(exception, () -> "Commande refusée inattendue : " + (exception != null ? exception.getMessage() : ""));
        assertNotNull(recu);
    }

    @And("le montant total est {double}")
    public void leMontantTotalEst(double montant) {
        assertEquals(montant, recu.getMontantTotal(), 0.01);
    }

    @And("le message de confirmation est {string}")
    public void leMessageDeConfirmationEst(String message) {
        assertEquals(message, recu.getMessageConfirmation());
    }

    @Then("la commande est refusée avec le message {string}")
    public void laCommandeEstRefuseeAvecLeMessage(String message) {
        assertNotNull(exception, "Une exception était attendue");
        assertTrue(exception.getMessage().contains(message));
    }
}
