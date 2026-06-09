package org.example.exo6;

import java.util.List;

public class RechercheVille {

    private final List<String> villes = List.of(
            "Paris", "Budapest", "Skopje", "Rotterdam", "Valence", "Vancouver",
            "Amsterdam", "Vienne", "Sydney", "New York", "Londres", "Bangkok",
            "Hong Kong", "Dubaï", "Rome", "Istanbul"
    );

    public List<String> Rechercher(String mot) throws NotFoundException {
        if (!"*".equals(mot) && (mot == null || mot.length() < 2)) {
            throw new NotFoundException("Le texte de recherche doit contenir au moins 2 caractères");
        }
        if ("*".equals(mot)) {
            return villes;
        }
        String motLower = mot.toLowerCase();
        return villes.stream()
                .filter(v -> v.toLowerCase().contains(motLower))
                .toList();
    }
}
