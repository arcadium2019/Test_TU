package org.example.exo12.controller;

import jakarta.validation.Valid;
import org.example.exo12.dto.CreateReservationRequest;
import org.example.exo12.model.Reservation;
import org.example.exo12.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reservation create(@Valid @RequestBody CreateReservationRequest request) {
        return service.createReservation(
                request.getRoomId(),
                request.getBookedBy(),
                request.getStartTime(),
                request.getEndTime());
    }

    @GetMapping("/{id}")
    public Reservation getById(@PathVariable("id") String id) {
        return service.findById(id);
    }

    @PatchMapping("/{id}/cancel")
    public Reservation cancel(@PathVariable("id") String id) {
        return service.cancelReservation(id);
    }
}
