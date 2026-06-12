package org.example.exo7.service;

import org.example.exo7.model.Commande;
import org.example.exo7.model.Produit;
import org.example.exo7.model.Recu;
import org.example.exo7.repository.ProduitRepository;

import java.util.Optional;

public class CommandeService {

    private final ProduitRepository produitRepository;

    public CommandeService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public Recu passerCommande(Commande commande) {
        Optional<Produit> optProduit = produitRepository.findByReference(commande.getReferenceProduit());
        if (optProduit.isEmpty()) {
            throw new CommandeRefuseeException("Unknown product");
        }

        Produit produit = optProduit.get();
        if (commande.getQuantite() > produit.getStock()) {
            throw new CommandeRefuseeException("Insufficient stock");
        }

        double remise = switch (commande.getTypeClient()) {
            case STANDARD -> 0.0;
            case PREMIUM  -> 0.10;
            case VIP      -> 0.20;
        };

        double montant = produit.getPrixUnitaire() * commande.getQuantite() * (1 - remise);
        return new Recu(commande.getReferenceProduit(), commande.getQuantite(), montant, "Order confirmed");
    }
}
