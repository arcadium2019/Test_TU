package org.example.exo11.service;

import org.example.exo11.exception.InvalidStatusTransitionException;
import org.example.exo11.exception.TicketNotFoundException;
import org.example.exo11.model.Priority;
import org.example.exo11.model.Ticket;
import org.example.exo11.model.TicketStatus;
import org.example.exo11.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository repository;

    @InjectMocks
    private TicketService service;

    @Test
    void create_ticket_returns_ticket_with_given_title_and_priority() {
        // Arrange
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Ticket result = service.createTicket("Login bug", Priority.HIGH);

        // Assert
        assertThat(result.getTitle()).isEqualTo("Login bug");
        assertThat(result.getPriority()).isEqualTo(Priority.HIGH);
    }

    @Test
    void create_ticket_default_status_is_open() {
        // Arrange
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Ticket result = service.createTicket("Crash on startup", Priority.MEDIUM);

        // Assert
        assertThat(result.getStatus()).isEqualTo(TicketStatus.OPEN);
    }

    @Test
    void find_existing_ticket_by_id_returns_ticket() {
        // Arrange
        Ticket ticket = new Ticket("Server down", Priority.HIGH);
        when(repository.findById(ticket.getId())).thenReturn(Optional.of(ticket));

        // Act
        Ticket result = service.findById(ticket.getId());

        // Assert
        assertThat(result.getId()).isEqualTo(ticket.getId());
    }

    @Test
    void find_non_existent_ticket_throws_not_found() {
        // Arrange
        when(repository.findById("missing-id")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.findById("missing-id"))
                .isInstanceOf(TicketNotFoundException.class)
                .hasMessageContaining("missing-id");
    }

    @Test
    void find_all_returns_list_of_tickets() {
        // Arrange
        Ticket t1 = new Ticket("Bug A", Priority.LOW);
        Ticket t2 = new Ticket("Bug B", Priority.HIGH);
        when(repository.findAll()).thenReturn(List.of(t1, t2));

        // Act
        List<Ticket> result = service.findAll();

        // Assert
        assertThat(result).hasSize(2);
    }

    @Test
    void update_status_from_open_to_in_progress_is_allowed() {
        // Arrange
        Ticket ticket = new Ticket("Network issue", Priority.MEDIUM);
        when(repository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Ticket result = service.updateStatus(ticket.getId(), TicketStatus.IN_PROGRESS);

        // Assert
        assertThat(result.getStatus()).isEqualTo(TicketStatus.IN_PROGRESS);
    }

    @Test
    void update_status_from_open_to_resolved_is_allowed() {
        // Arrange
        Ticket ticket = new Ticket("Quick fix", Priority.LOW);
        when(repository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Ticket result = service.updateStatus(ticket.getId(), TicketStatus.RESOLVED);

        // Assert
        assertThat(result.getStatus()).isEqualTo(TicketStatus.RESOLVED);
    }

    @Test
    void update_status_from_in_progress_to_resolved_is_allowed() {
        // Arrange
        Ticket ticket = new Ticket("Complex bug", Priority.HIGH);
        ticket.setStatus(TicketStatus.IN_PROGRESS);
        when(repository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Ticket result = service.updateStatus(ticket.getId(), TicketStatus.RESOLVED);

        // Assert
        assertThat(result.getStatus()).isEqualTo(TicketStatus.RESOLVED);
    }

    @Test
    void update_status_on_resolved_ticket_throws_conflict() {
        // Arrange
        Ticket ticket = new Ticket("Old bug", Priority.LOW);
        ticket.setStatus(TicketStatus.RESOLVED);
        when(repository.findById(ticket.getId())).thenReturn(Optional.of(ticket));

        // Act & Assert
        assertThatThrownBy(() -> service.updateStatus(ticket.getId(), TicketStatus.IN_PROGRESS))
                .isInstanceOf(InvalidStatusTransitionException.class);
    }

    @Test
    void update_status_from_in_progress_to_open_is_forbidden() {
        // Arrange
        Ticket ticket = new Ticket("Regression", Priority.MEDIUM);
        ticket.setStatus(TicketStatus.IN_PROGRESS);
        when(repository.findById(ticket.getId())).thenReturn(Optional.of(ticket));

        // Act & Assert
        assertThatThrownBy(() -> service.updateStatus(ticket.getId(), TicketStatus.OPEN))
                .isInstanceOf(InvalidStatusTransitionException.class);
    }
}
