package org.example.exo12.repository;

import org.example.exo12.model.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
    Optional<Reservation> findById(String id);
    List<Reservation> findAll();
    List<Reservation> findByRoomId(String roomId);
}
