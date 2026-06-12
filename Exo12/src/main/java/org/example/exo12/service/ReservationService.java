package org.example.exo12.service;

import org.example.exo12.exception.ReservationConflictException;
import org.example.exo12.exception.ReservationNotFoundException;
import org.example.exo12.exception.RoomNotFoundException;
import org.example.exo12.model.Reservation;
import org.example.exo12.model.ReservationStatus;
import org.example.exo12.repository.ReservationRepository;
import org.example.exo12.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
    }

    public Reservation createReservation(String roomId, String bookedBy,
                                         LocalDateTime startTime, LocalDateTime endTime) {
        roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found: " + roomId));

        if (!endTime.isAfter(startTime)) {
            throw new ReservationConflictException("End time must be strictly after start time");
        }

        boolean hasOverlap = reservationRepository.findByRoomId(roomId).stream()
                .filter(r -> r.getStatus() == ReservationStatus.CONFIRMED)
                .anyMatch(r -> startTime.isBefore(r.getEndTime()) && endTime.isAfter(r.getStartTime()));

        if (hasOverlap) {
            throw new ReservationConflictException("Time slot overlaps with an existing reservation");
        }

        return reservationRepository.save(new Reservation(roomId, bookedBy, startTime, endTime));
    }

    public Reservation findById(String id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found: " + id));
    }

    public Reservation cancelReservation(String id) {
        Reservation reservation = findById(id);
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new ReservationConflictException("Reservation is already cancelled");
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
        return reservationRepository.save(reservation);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }
}
