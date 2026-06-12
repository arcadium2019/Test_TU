package org.example.exo12.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.exo12.exception.ReservationConflictException;
import org.example.exo12.exception.ReservationNotFoundException;
import org.example.exo12.model.Reservation;
import org.example.exo12.model.ReservationStatus;
import org.example.exo12.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    private static final String START = "2026-06-20T09:00:00";
    private static final String END   = "2026-06-20T11:00:00";

    @Test
    void create_valid_reservation_returns_201() throws Exception {
        // Arrange
        Reservation res = new Reservation("room-1", "Alice",
                LocalDateTime.parse(START), LocalDateTime.parse(END));
        when(reservationService.createReservation(any(), any(), any(), any())).thenReturn(res);

        // Act & Assert
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roomId\":\"room-1\",\"bookedBy\":\"Alice\"," +
                                "\"startTime\":\"" + START + "\",\"endTime\":\"" + END + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookedBy").value("Alice"))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void create_reservation_with_missing_booker_returns_400() throws Exception {
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roomId\":\"room-1\"," +
                                "\"startTime\":\"" + START + "\",\"endTime\":\"" + END + "\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_reservation_with_overlapping_slot_returns_409() throws Exception {
        // Arrange
        when(reservationService.createReservation(any(), any(), any(), any()))
                .thenThrow(new ReservationConflictException("Time slot overlaps"));

        // Act & Assert
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roomId\":\"room-1\",\"bookedBy\":\"Bob\"," +
                                "\"startTime\":\"" + START + "\",\"endTime\":\"" + END + "\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void get_existing_reservation_returns_200() throws Exception {
        // Arrange
        Reservation res = new Reservation("room-1", "Carol",
                LocalDateTime.parse(START), LocalDateTime.parse(END));
        when(reservationService.findById(res.getId())).thenReturn(res);

        // Act & Assert
        mockMvc.perform(get("/api/reservations/" + res.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookedBy").value("Carol"));
    }

    @Test
    void get_non_existent_reservation_returns_404() throws Exception {
        // Arrange
        when(reservationService.findById("bad-id"))
                .thenThrow(new ReservationNotFoundException("Reservation not found: bad-id"));

        // Act & Assert
        mockMvc.perform(get("/api/reservations/bad-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    void cancel_reservation_returns_200_with_cancelled_status() throws Exception {
        // Arrange
        Reservation res = new Reservation("room-1", "Dave",
                LocalDateTime.parse(START), LocalDateTime.parse(END));
        res.setStatus(ReservationStatus.CANCELLED);
        when(reservationService.cancelReservation(res.getId())).thenReturn(res);

        // Act & Assert
        mockMvc.perform(patch("/api/reservations/" + res.getId() + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void cancel_already_cancelled_reservation_returns_409() throws Exception {
        // Arrange
        when(reservationService.cancelReservation("some-id"))
                .thenThrow(new ReservationConflictException("Reservation is already cancelled"));

        // Act & Assert
        mockMvc.perform(patch("/api/reservations/some-id/cancel"))
                .andExpect(status().isConflict());
    }
}
