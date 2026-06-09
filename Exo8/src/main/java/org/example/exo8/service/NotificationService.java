package org.example.exo8.service;

import org.example.exo8.model.ConfirmationReservation;

public interface NotificationService {
    void envoyerConfirmation(String email, ConfirmationReservation confirmation);
}
