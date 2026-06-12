package org.example.exo11.controller;

import jakarta.validation.Valid;
import org.example.exo11.dto.CreateTicketRequest;
import org.example.exo11.dto.UpdateStatusRequest;
import org.example.exo11.model.Ticket;
import org.example.exo11.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Ticket create(@Valid @RequestBody CreateTicketRequest request) {
        return service.createTicket(request.getTitle(), request.getPriority());
    }

    @GetMapping("/{id}")
    public Ticket getById(@PathVariable("id") String id) {
        return service.findById(id);
    }

    @GetMapping
    public List<Ticket> getAll() {
        return service.findAll();
    }

    @PatchMapping("/{id}/status")
    public Ticket updateStatus(@PathVariable("id") String id,
                               @Valid @RequestBody UpdateStatusRequest request) {
        return service.updateStatus(id, request.getStatus());
    }
}
