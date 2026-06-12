package org.example.exo11.bdd.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.example.exo11.exception.InvalidStatusTransitionException;
import org.example.exo11.exception.TicketNotFoundException;
import org.example.exo11.model.Priority;
import org.example.exo11.model.Ticket;
import org.example.exo11.model.TicketStatus;
import org.example.exo11.repository.InMemoryTicketRepository;
import org.example.exo11.service.TicketService;

import static org.junit.jupiter.api.Assertions.*;

public class TicketSteps {

    private InMemoryTicketRepository repository;
    private TicketService service;

    private Ticket lastTicket;
    private Exception lastException;

    @Before
    public void setUp() {
        repository = new InMemoryTicketRepository();
        service = new TicketService(repository);
        lastTicket = null;
        lastException = null;
    }

    @Given("no tickets exist")
    public void noTicketsExist() {
        // repository is fresh — nothing to do
    }

    @Given("a ticket with title {string} and priority {string} exists with status {string}")
    public void aTicketExistsWithStatus(String title, String priority, String status) {
        Ticket ticket = service.createTicket(title, Priority.valueOf(priority));
        if (!status.equals("OPEN")) {
            ticket.setStatus(TicketStatus.valueOf(status));
            repository.save(ticket);
        }
        lastTicket = ticket;
    }

    @When("I create a ticket with title {string} and priority {string}")
    public void createTicket(String title, String priority) {
        try {
            lastTicket = service.createTicket(title, Priority.valueOf(priority));
        } catch (Exception e) {
            lastException = e;
        }
    }

    @When("I update the status to {string}")
    public void updateStatus(String status) {
        try {
            lastTicket = service.updateStatus(lastTicket.getId(), TicketStatus.valueOf(status));
        } catch (Exception e) {
            lastException = e;
        }
    }

    @When("I try to update the status to {string}")
    public void tryUpdateStatus(String status) {
        try {
            lastTicket = service.updateStatus(lastTicket.getId(), TicketStatus.valueOf(status));
        } catch (Exception e) {
            lastException = e;
        }
    }

    @When("I look for ticket with id {string}")
    public void lookForTicket(String id) {
        try {
            lastTicket = service.findById(id);
        } catch (Exception e) {
            lastException = e;
        }
    }

    @Then("the ticket is created with status {string}")
    public void ticketCreatedWithStatus(String status) {
        assertNull(lastException);
        assertNotNull(lastTicket);
        assertEquals(TicketStatus.valueOf(status), lastTicket.getStatus());
    }

    @Then("the ticket status is {string}")
    public void ticketStatusIs(String status) {
        assertNull(lastException);
        assertNotNull(lastTicket);
        assertEquals(TicketStatus.valueOf(status), lastTicket.getStatus());
    }

    @Then("an error occurs")
    public void anErrorOccurs() {
        assertNotNull(lastException);
        assertInstanceOf(InvalidStatusTransitionException.class, lastException);
    }

    @Then("a not found error occurs")
    public void aNotFoundErrorOccurs() {
        assertNotNull(lastException);
        assertInstanceOf(TicketNotFoundException.class, lastException);
    }
}
