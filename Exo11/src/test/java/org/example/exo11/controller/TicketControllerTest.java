package org.example.exo11.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.exo11.exception.InvalidStatusTransitionException;
import org.example.exo11.exception.TicketNotFoundException;
import org.example.exo11.model.Priority;
import org.example.exo11.model.Ticket;
import org.example.exo11.model.TicketStatus;
import org.example.exo11.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TicketController.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TicketService service;

    @Test
    void create_valid_ticket_returns_201_with_body() throws Exception {
        // Arrange
        Ticket ticket = new Ticket("Login error", Priority.HIGH);
        when(service.createTicket(any(), any())).thenReturn(ticket);

        // Act & Assert
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Login error\",\"priority\":\"HIGH\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Login error"))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    void create_ticket_with_blank_title_returns_400() throws Exception {
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"\",\"priority\":\"HIGH\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_ticket_with_missing_priority_returns_400() throws Exception {
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Valid title\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_ticket_with_invalid_priority_returns_400() throws Exception {
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Valid title\",\"priority\":\"UNKNOWN\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void get_existing_ticket_returns_200() throws Exception {
        // Arrange
        Ticket ticket = new Ticket("Server down", Priority.HIGH);
        when(service.findById(ticket.getId())).thenReturn(ticket);

        // Act & Assert
        mockMvc.perform(get("/api/tickets/" + ticket.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ticket.getId()));
    }

    @Test
    void get_non_existent_ticket_returns_404() throws Exception {
        // Arrange
        when(service.findById("bad-id"))
                .thenThrow(new TicketNotFoundException("Ticket not found: bad-id"));

        // Act & Assert
        mockMvc.perform(get("/api/tickets/bad-id"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void get_all_tickets_returns_200_with_list() throws Exception {
        // Arrange
        Ticket t1 = new Ticket("Bug A", Priority.LOW);
        Ticket t2 = new Ticket("Bug B", Priority.MEDIUM);
        when(service.findAll()).thenReturn(List.of(t1, t2));

        // Act & Assert
        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void update_status_returns_200_with_updated_ticket() throws Exception {
        // Arrange
        Ticket ticket = new Ticket("Network issue", Priority.MEDIUM);
        ticket.setStatus(TicketStatus.IN_PROGRESS);
        when(service.updateStatus(eq(ticket.getId()), eq(TicketStatus.IN_PROGRESS)))
                .thenReturn(ticket);

        // Act & Assert
        mockMvc.perform(patch("/api/tickets/" + ticket.getId() + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"IN_PROGRESS\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void update_status_on_non_existent_ticket_returns_404() throws Exception {
        // Arrange
        when(service.updateStatus(eq("bad-id"), any()))
                .thenThrow(new TicketNotFoundException("Ticket not found: bad-id"));

        // Act & Assert
        mockMvc.perform(patch("/api/tickets/bad-id/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"IN_PROGRESS\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_status_with_invalid_transition_returns_409() throws Exception {
        // Arrange
        when(service.updateStatus(eq("some-id"), any()))
                .thenThrow(new InvalidStatusTransitionException("Cannot transition from RESOLVED to IN_PROGRESS"));

        // Act & Assert
        mockMvc.perform(patch("/api/tickets/some-id/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"IN_PROGRESS\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").exists());
    }
}
