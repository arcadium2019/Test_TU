package org.example.exo8.model;

import java.time.LocalDateTime;

public class ConfirmationReservation {
    private final String codeSalle;
    private final int nombreParticipants;
    private final LocalDateTime dateDebut;
    private final LocalDateTime dateFin;
    private final String message;

    public ConfirmationReservation(String codeSalle, int nombreParticipants,
                                   LocalDateTime dateDebut, LocalDateTime dateFin, String message) {
        this.codeSalle = codeSalle;
        this.nombreParticipants = nombreParticipants;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.message = message;
    }

    public String getCodeSalle() { return codeSalle; }
    public int getNombreParticipants() { return nombreParticipants; }
    public LocalDateTime getDateDebut() { return dateDebut; }
    public LocalDateTime getDateFin() { return dateFin; }
    public String getMessage() { return message; }
}
