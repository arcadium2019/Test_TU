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

    private SalleRepository salleRepository;
    private ReservationRepository reservationRepository;
    private NotificationService notificationService;
    private ReservationService reservationService;

    private final List<Reservation> reservationsExistantes = new ArrayList<>();
    private ConfirmationReservation confirmation;
    private Exception exception;

    @Before
    public void setUp() {
        salleRepository = mock(SalleRepository.class);
        reservationRepository = mock(ReservationRepository.class);
        notificationService = mock(NotificationService.class);
        reservationService = new ReservationService(salleRepository, reservationRepository, notificationService);
        reservationsExistantes.clear();
        confirmation = null;
        exception = null;
        when(reservationRepository.findByCodeSalle(anyString())).thenReturn(reservationsExistantes);
    }

    @Given("la salle {string} avec nom {string} et capacité {int}")
    public void laSalleAvecNomEtCapacite(String code, String nom, int capacite) {
        Salle salle = new Salle(code, nom, capacite);
        when(salleRepository.findByCode(code)).thenReturn(Optional.of(salle));
    }

    @Given("aucune salle avec le code {string}")
    public void aucuneSalleAvecLeCode(String code) {
        when(salleRepository.findByCode(code)).thenReturn(Optional.empty());
    }

    @Given("une réservation existante sur {string} du {string} au {string}")
    public void uneReservationExistante(String codeSalle, String debut, String fin) {
        Reservation existante = new Reservation(
                "autre@test.com", codeSalle, 3,
                LocalDateTime.parse(debut, FMT),
                LocalDateTime.parse(fin, FMT)
        );
        reservationsExistantes.add(existante);
    }

    @When("l'utilisateur {string} réserve {string} pour {int} participants du {string} au {string}")
    public void lUtilisateurReserve(String email, String codeSalle, int participants, String debut, String fin) {
        Reservation reservation = new Reservation(
                email, codeSalle, participants,
                LocalDateTime.parse(debut, FMT),
                LocalDateTime.parse(fin, FMT)
        );
        try {
            confirmation = reservationService.reserver(reservation);
        } catch (ReservationRefuseeException e) {
            exception = e;
        }
    }

    @Then("la réservation est acceptée")
    public void laReservationEstAcceptee() {
        assertNull(exception, () -> "Réservation refusée de façon inattendue : " + (exception != null ? exception.getMessage() : ""));
        assertNotNull(confirmation);
    }

    @And("le message de confirmation est {string}")
    public void leMessageDeConfirmationEst(String message) {
        assertEquals(message, confirmation.getMessage());
    }

    @Then("la réservation est refusée avec le message {string}")
    public void laReservationEstRefuseeAvecLeMessage(String message) {
        assertNotNull(exception, "Une exception était attendue");
        assertTrue(exception.getMessage().contains(message));
    }

    @And("une notification a été envoyée à {string}")
    public void uneNotificationAEteEnvoyeeA(String email) {
        verify(notificationService).envoyerConfirmation(eq(email), any());
    }

    @And("aucune notification n'a été envoyée")
    public void aucuneNotificationNAEteEnvoyee() {
        verify(notificationService, never()).envoyerConfirmation(any(), any());
    }
}
