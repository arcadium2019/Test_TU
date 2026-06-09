package org.example.exo8.repository;

import org.example.exo8.model.Salle;

import java.util.Optional;

public interface SalleRepository {
    Optional<Salle> findByCode(String code);
}
