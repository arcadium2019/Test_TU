package org.example.exo12.bdd.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.example.exo12.exception.ReservationConflictException;
import org.example.exo12.exception.RoomNotFoundException;
import org.example.exo12.model.Reservation;
import org.example.exo12.model.ReservationStatus;
import org.example.exo12.model.Room;
import org.example.exo12.repository.InMemoryReservationRepository;
import org.example.exo12.repository.InMemoryRoomRepository;
import org.example.exo12.service.ReservationService;
import org.example.exo12.service.RoomService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationSteps {

    private InMemoryRoomRepository roomRepository;
    private InMemoryReservationRepository reservationRepository;
    private RoomService roomService;
    private ReservationService reservationService;

    private Room lastRoom;
    private Reservation lastReservation;
    private Exception lastException;

    private static final LocalDateTime DEFAULT_START = LocalDateTime.of(2026, 8, 1, 9, 0);
    private static final LocalDateTime DEFAULT_END   = LocalDateTime.of(2026, 8, 1, 11, 0);

    @Before
    public void setUp() {
        roomRepository = new InMemoryRoomRepository();
        reservationRepository = new InMemoryReservationRepository();
        roomService = new RoomService(roomRepository);
        reservationService = new ReservationService(reservationRepository, roomRepository);
        lastRoom = null;
        lastReservation = null;
        lastException = null;
    }

    @Given("a room named {string} with capacity {int} exists")
    public void aRoomExists(String name, int capacity) {
        lastRoom = roomService.createRoom(name, capacity);
    }

    @Given("no room exists")
    public void noRoomExists() {
        // repository is empty — nothing to do
    }

    @Given("a reservation for room {string} by {string} is already confirmed for the same slot")
    public void aReservationAlreadyConfirmed(String roomName, String bookedBy) {
        try {
            reservationService.createReservation(
                    lastRoom.getId(), bookedBy, DEFAULT_START, DEFAULT_END);
        } catch (Exception e) {
            lastException = e;
        }
    }

    @When("I book the room {string} for {string}")
    public void bookRoom(String roomName, String bookedBy) {
        try {
            lastReservation = reservationService.createReservation(
                    lastRoom != null ? lastRoom.getId() : "unknown-room-id",
                    bookedBy, DEFAULT_START, DEFAULT_END);
        } catch (Exception e) {
            lastException = e;
        }
    }

    @When("I try to book a non-existent room for {string}")
    public void bookNonExistentRoom(String bookedBy) {
        try {
            lastReservation = reservationService.createReservation(
                    "non-existent-room-id", bookedBy, DEFAULT_START, DEFAULT_END);
        } catch (Exception e) {
            lastException = e;
        }
    }

    @When("I try to book the same room for {string} during the same slot")
    public void bookSameRoomSameSlot(String bookedBy) {
        try {
            lastReservation = reservationService.createReservation(
                    lastRoom.getId(), bookedBy, DEFAULT_START, DEFAULT_END);
        } catch (Exception e) {
            lastException = e;
        }
    }

    @Then("the reservation is confirmed")
    public void reservationIsConfirmed() {
        assertNull(lastException);
        assertNotNull(lastReservation);
        assertEquals(ReservationStatus.CONFIRMED, lastReservation.getStatus());
    }

    @Then("the reservation is rejected because the room does not exist")
    public void reservationRejectedRoomNotFound() {
        assertNotNull(lastException);
        assertInstanceOf(RoomNotFoundException.class, lastException);
    }

    @Then("the reservation is rejected due to a time slot conflict")
    public void reservationRejectedDueToConflict() {
        assertNotNull(lastException);
        assertInstanceOf(ReservationConflictException.class, lastException);
        assertTrue(lastException.getMessage().contains("overlap"));
    }
}
