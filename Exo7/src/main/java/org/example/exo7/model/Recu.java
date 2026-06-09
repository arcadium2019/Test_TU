package org.example.exo7.model;

public class Recu {
    private final String referenceProduit;
    private final int quantite;
    private final double montantTotal;
    private final String messageConfirmation;

    public Recu(String referenceProduit, int quantite, double montantTotal, String messageConfirmation) {
        this.referenceProduit = referenceProduit;
        this.quantite = quantite;
        this.montantTotal = montantTotal;
        this.messageConfirmation = messageConfirmation;
    }

    public String getReferenceProduit() { return referenceProduit; }
    public int getQuantite() { return quantite; }
    public double getMontantTotal() { return montantTotal; }
    public String getMessageConfirmation() { return messageConfirmation; }
}
