package org.example.exo8.service;

import org.example.exo8.model.ConfirmationReservation;
import org.example.exo8.model.Reservation;
import org.example.exo8.model.Salle;
import org.example.exo8.repository.ReservationRepository;
import org.example.exo8.repository.SalleRepository;

import java.util.List;
import java.util.Optional;

public class ReservationService {

    private final SalleRepository salleRepository;
    private final ReservationRepository reservationRepository;
    private final NotificationService notificationService;

    public ReservationService(SalleRepository salleRepository,
                              ReservationRepository reservationRepository,
                              NotificationService notificationService) {
        this.salleRepository = salleRepository;
        this.reservationRepository = reservationRepository;
        this.notificationService = notificationService;
    }

    public ConfirmationReservation reserver(Reservation reservation) {
        Optional<Salle> optSalle = salleRepository.findByCode(reservation.getCodeSalle());
        if (optSalle.isEmpty()) {
            throw new ReservationRefuseeException("Salle inconnue");
        }

        Salle salle = optSalle.get();
        if (reservation.getNombreParticipants() > salle.getCapaciteMax()) {
            throw new ReservationRefuseeException("Capacité insuffisante");
        }

        if (!reservation.getDateFin().isAfter(reservation.getDateDebut())) {
            throw new ReservationRefuseeException("Période invalide");
        }

        List<Reservation> existantes = reservationRepository.findByCodeSalle(reservation.getCodeSalle());
        for (Reservation r : existantes) {
            // chevauchement : [r.debut, r.fin[ ∩ [new.debut, new.fin[ ≠ ∅
            if (r.getDateDebut().isBefore(reservation.getDateFin())
                    && reservation.getDateDebut().isBefore(r.getDateFin())) {
                throw new ReservationRefuseeException("Créneau indisponible");
            }
        }

        ConfirmationReservation confirmation = new ConfirmationReservation(
                reservation.getCodeSalle(),
                reservation.getNombreParticipants(),
                reservation.getDateDebut(),
                reservation.getDateFin(),
                "Réservation confirmée"
        );
        notificationService.envoyerConfirmation(reservation.getEmailUtilisateur(), confirmation);
        return confirmation;
    }
}
