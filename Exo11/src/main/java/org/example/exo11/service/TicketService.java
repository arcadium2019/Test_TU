package org.example.exo11.service;

import org.example.exo11.exception.InvalidStatusTransitionException;
import org.example.exo11.exception.TicketNotFoundException;
import org.example.exo11.model.Priority;
import org.example.exo11.model.Ticket;
import org.example.exo11.model.TicketStatus;
import org.example.exo11.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class TicketService {

    private static final Map<TicketStatus, Set<TicketStatus>> ALLOWED_TRANSITIONS;

    static {
        ALLOWED_TRANSITIONS = new EnumMap<>(TicketStatus.class);
        ALLOWED_TRANSITIONS.put(TicketStatus.OPEN,
                EnumSet.of(TicketStatus.IN_PROGRESS, TicketStatus.RESOLVED));
        ALLOWED_TRANSITIONS.put(TicketStatus.IN_PROGRESS,
                EnumSet.of(TicketStatus.RESOLVED));
        ALLOWED_TRANSITIONS.put(TicketStatus.RESOLVED,
                EnumSet.noneOf(TicketStatus.class));
    }

    private final TicketRepository repository;

    public TicketService(TicketRepository repository) {
        this.repository = repository;
    }

    public Ticket createTicket(String title, Priority priority) {
        Ticket ticket = new Ticket(title, priority);
        return repository.save(ticket);
    }

    public Ticket findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found: " + id));
    }

    public List<Ticket> findAll() {
        return repository.findAll();
    }

    public Ticket updateStatus(String id, TicketStatus newStatus) {
        Ticket ticket = findById(id);
        Set<TicketStatus> allowed = ALLOWED_TRANSITIONS.get(ticket.getStatus());
        if (!allowed.contains(newStatus)) {
            throw new InvalidStatusTransitionException(
                    "Cannot transition from " + ticket.getStatus() + " to " + newStatus);
        }
        ticket.setStatus(newStatus);
        return repository.save(ticket);
    }
}
