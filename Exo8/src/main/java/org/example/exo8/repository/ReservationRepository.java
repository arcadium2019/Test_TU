package org.example.exo8.repository;

import org.example.exo8.model.Reservation;

import java.util.List;

public interface ReservationRepository {
    List<Reservation> findByCodeSalle(String codeSalle);
}
