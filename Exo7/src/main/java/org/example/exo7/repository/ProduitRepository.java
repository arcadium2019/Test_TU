package org.example.exo7.repository;

import org.example.exo7.model.Produit;

import java.util.Optional;

public interface ProduitRepository {
    Optional<Produit> findByReference(String reference);
}
