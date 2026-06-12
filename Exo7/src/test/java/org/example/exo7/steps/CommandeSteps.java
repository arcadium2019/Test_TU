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

    private ProduitRepository productRepository;
    private CommandeService orderService;
    private Recu receipt;
    private Exception exception;

    @Before
    public void setUp() {
        productRepository = mock(ProduitRepository.class);
        orderService = new CommandeService(productRepository);
        receipt = null;
        exception = null;
    }

    @Given("the product {string} with price {double} and stock {int}")
    public void theProductWithPriceAndStock(String ref, double price, int stock) {
        Produit product = new Produit(ref, "Product " + ref, price, stock);
        when(productRepository.findByReference(ref)).thenReturn(Optional.of(product));
    }

    @Given("no product with reference {string}")
    public void noProductWithReference(String ref) {
        when(productRepository.findByReference(ref)).thenReturn(Optional.empty());
    }

    @When("the customer {word} orders {int} units of product {string}")
    public void theCustomerOrders(String customerType, int quantity, String ref) {
        Commande order = new Commande("customer@test.com", ref, quantity, TypeClient.valueOf(customerType));
        try {
            receipt = orderService.passerCommande(order);
        } catch (CommandeRefuseeException e) {
            exception = e;
        }
    }

    @Then("the order is accepted")
    public void theOrderIsAccepted() {
        assertNull(exception, () -> "Unexpected refused order: " + (exception != null ? exception.getMessage() : ""));
        assertNotNull(receipt);
    }

    @And("the total amount is {double}")
    public void theTotalAmountIs(double amount) {
        assertEquals(amount, receipt.getMontantTotal(), 0.01);
    }

    @And("the confirmation message is {string}")
    public void theConfirmationMessageIs(String message) {
        assertEquals(message, receipt.getMessageConfirmation());
    }

    @Then("the order is refused with message {string}")
    public void theOrderIsRefusedWithMessage(String message) {
        assertNotNull(exception, "An exception was expected");
        assertTrue(exception.getMessage().contains(message));
    }
}
