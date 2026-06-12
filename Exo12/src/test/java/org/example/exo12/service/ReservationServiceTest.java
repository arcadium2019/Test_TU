package org.example.exo12.service;

import org.example.exo12.exception.ReservationConflictException;
import org.example.exo12.exception.ReservationNotFoundException;
import org.example.exo12.exception.RoomNotFoundException;
import org.example.exo12.model.Reservation;
import org.example.exo12.model.ReservationStatus;
import org.example.exo12.model.Room;
import org.example.exo12.repository.ReservationRepository;
import org.example.exo12.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private ReservationService service;

    private static final LocalDateTime START = LocalDateTime.of(2026, 6, 20, 9, 0);
    private static final LocalDateTime END   = LocalDateTime.of(2026, 6, 20, 11, 0);

    @Test
    void create_valid_reservation_returns_confirmed_reservation() {
        // Arrange
        Room room = new Room("Salle A", 10);
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        when(reservationRepository.findByRoomId(room.getId())).thenReturn(List.of());
        when(reservationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Reservation result = service.createReservation(room.getId(), "Alice", START, END);

        // Assert
        assertThat(result.getStatus()).isEqualTo(ReservationStatus.CONFIRMED);
        assertThat(result.getBookedBy()).isEqualTo("Alice");
    }

    @Test
    void create_reservation_for_non_existent_room_throws_not_found() {
        // Arrange
        when(roomRepository.findById("bad-room")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.createReservation("bad-room", "Bob", START, END))
                .isInstanceOf(RoomNotFoundException.class);
    }

    @Test
    void create_reservation_with_end_before_start_throws_conflict() {
        // Arrange
        Room room = new Room("Salle B", 5);
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        // Act & Assert
        assertThatThrownBy(() ->
                service.createReservation(room.getId(), "Carol", END, START))
                .isInstanceOf(ReservationConflictException.class)
                .hasMessageContaining("End time must be strictly after start time");
    }

    @Test
    void create_reservation_with_equal_start_and_end_throws_conflict() {
        // Arrange
        Room room = new Room("Salle C", 5);
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        // Act & Assert
        assertThatThrownBy(() ->
                service.createReservation(room.getId(), "Dan", START, START))
                .isInstanceOf(ReservationConflictException.class);
    }

    @Test
    void create_reservation_with_overlapping_slot_throws_conflict() {
        // Arrange
        Room room = new Room("Salle D", 8);
        Reservation existing = new Reservation(room.getId(), "Eve", START, END);
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        when(reservationRepository.findByRoomId(room.getId())).thenReturn(List.of(existing));

        LocalDateTime overlapStart = LocalDateTime.of(2026, 6, 20, 10, 0);
        LocalDateTime overlapEnd   = LocalDateTime.of(2026, 6, 20, 12, 0);

        // Act & Assert
        assertThatThrownBy(() ->
                service.createReservation(room.getId(), "Frank", overlapStart, overlapEnd))
                .isInstanceOf(ReservationConflictException.class)
                .hasMessageContaining("overlap");
    }

    @Test
    void cancelled_reservation_does_not_block_new_booking_in_same_slot() {
        // Arrange
        Room room = new Room("Salle E", 6);
        Reservation cancelled = new Reservation(room.getId(), "Grace", START, END);
        cancelled.setStatus(ReservationStatus.CANCELLED);
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        when(reservationRepository.findByRoomId(room.getId())).thenReturn(List.of(cancelled));
        when(reservationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Reservation result = service.createReservation(room.getId(), "Henry", START, END);

        // Assert
        assertThat(result.getStatus()).isEqualTo(ReservationStatus.CONFIRMED);
    }

    @Test
    void cancel_confirmed_reservation_sets_status_to_cancelled() {
        // Arrange
        Reservation reservation = new Reservation("room-1", "Iris", START, END);
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Reservation result = service.cancelReservation(reservation.getId());

        // Assert
        assertThat(result.getStatus()).isEqualTo(ReservationStatus.CANCELLED);
    }

    @Test
    void cancel_already_cancelled_reservation_throws_conflict() {
        // Arrange
        Reservation reservation = new Reservation("room-1", "Jack", START, END);
        reservation.setStatus(ReservationStatus.CANCELLED);
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));

        // Act & Assert
        assertThatThrownBy(() -> service.cancelReservation(reservation.getId()))
                .isInstanceOf(ReservationConflictException.class)
                .hasMessageContaining("already cancelled");
    }

    @Test
    void find_non_existent_reservation_throws_not_found() {
        // Arrange
        when(reservationRepository.findById("missing")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.findById("missing"))
                .isInstanceOf(ReservationNotFoundException.class);
    }
}
