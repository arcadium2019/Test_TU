package org.example.exo8.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.exo8.model.ConfirmationReservation;
import org.example.exo8.model.Reservation;
import org.example.exo8.model.Salle;
import org.example.exo8.repository.ReservationRepository;
import org.example.exo8.repository.SalleRepository;
import org.example.exo8.service.NotificationService;
import org.example.exo8.service.ReservationRefuseeException;
import org.example.exo8.service.ReservationService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReservationSteps {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private SalleRepository roomRepository;
    private ReservationRepository bookingRepository;
    private NotificationService notificationService;
    private ReservationService reservationService;

    private final List<Reservation> existingReservations = new ArrayList<>();
    private ConfirmationReservation confirmation;
    private Exception exception;

    @Before
    public void setUp() {
        roomRepository = mock(SalleRepository.class);
        bookingRepository = mock(ReservationRepository.class);
        notificationService = mock(NotificationService.class);
        reservationService = new ReservationService(roomRepository, bookingRepository, notificationService);
        existingReservations.clear();
        confirmation = null;
        exception = null;
        when(bookingRepository.findByCodeSalle(anyString())).thenReturn(existingReservations);
    }

    @Given("the room {string} with name {string} and capacity {int}")
    public void theRoomWithNameAndCapacity(String code, String name, int capacity) {
        Salle room = new Salle(code, name, capacity);
        when(roomRepository.findByCode(code)).thenReturn(Optional.of(room));
    }

    @Given("no room with code {string}")
    public void noRoomWithCode(String code) {
        when(roomRepository.findByCode(code)).thenReturn(Optional.empty());
    }

    @Given("an existing reservation on {string} from {string} to {string}")
    public void anExistingReservation(String roomCode, String start, String end) {
        Reservation existing = new Reservation(
                "other@test.com", roomCode, 3,
                LocalDateTime.parse(start, FMT),
                LocalDateTime.parse(end, FMT)
        );
        existingReservations.add(existing);
    }

    @When("the user {string} books {string} for {int} participants from {string} to {string}")
    public void theUserBooks(String email, String roomCode, int participants, String start, String end) {
        Reservation reservation = new Reservation(
                email, roomCode, participants,
                LocalDateTime.parse(start, FMT),
                LocalDateTime.parse(end, FMT)
        );
        try {
            confirmation = reservationService.reserver(reservation);
        } catch (ReservationRefuseeException e) {
            exception = e;
        }
    }

    @Then("the reservation is accepted")
    public void theReservationIsAccepted() {
        assertNull(exception, () -> "Unexpected refused reservation: " + (exception != null ? exception.getMessage() : ""));
        assertNotNull(confirmation);
    }

    @And("the confirmation message is {string}")
    public void theConfirmationMessageIs(String message) {
        assertEquals(message, confirmation.getMessage());
    }

    @Then("the reservation is refused with message {string}")
    public void theReservationIsRefusedWithMessage(String message) {
        assertNotNull(exception, "An exception was expected");
        assertTrue(exception.getMessage().contains(message));
    }

    @And("a notification has been sent to {string}")
    public void aNotificationHasBeenSentTo(String email) {
        verify(notificationService).envoyerConfirmation(eq(email), any());
    }

    @And("no notification has been sent")
    public void noNotificationHasBeenSent() {
        verify(notificationService, never()).envoyerConfirmation(any(), any());
    }
}
