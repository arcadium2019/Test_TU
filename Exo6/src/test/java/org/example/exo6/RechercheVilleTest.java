package org.example.exo6;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RechercheVilleTest {

    private RechercheVille recherche;

    @BeforeEach
    void setUp() {
        recherche = new RechercheVille();
    }

    // Etape 1 — texte < 2 caractères → NotFoundException
    @Nested
    class Step1_TexteTropCourt {

        @Test
        void rechercher_texteVide_shouldThrowNotFoundException() {
            assertThrows(NotFoundException.class, () -> recherche.Rechercher(""));
        }

        @ParameterizedTest
        @ValueSource(strings = {"a", "P", "R"})
        void rechercher_unSeulCaractere_shouldThrowNotFoundException(String mot) {
            assertThrows(NotFoundException.class, () -> recherche.Rechercher(mot));
        }
    }

    // Etape 2 — préfixe exact (≥ 2 chars)
    @Nested
    class Step2_RecherchePrefixe {

        @Test
        void rechercher_Va_shouldReturnValenceAndVancouver() throws NotFoundException {
            List<String> result = recherche.Rechercher("Va");
            assertTrue(result.contains("Valence"));
            assertTrue(result.contains("Vancouver"));
            assertEquals(2, result.size());
        }

        @Test
        void rechercher_Pa_shouldReturnParis() throws NotFoundException {
            List<String> result = recherche.Rechercher("Pa");
            assertTrue(result.contains("Paris"));
        }

        @Test
        void rechercher_texteInexistant_shouldReturnEmptyList() throws NotFoundException {
            List<String> result = recherche.Rechercher("Zz");
            assertTrue(result.isEmpty());
        }
    }

    // Etape 3 — insensible à la casse
    @Nested
    class Step3_InsensibleCasse {

        @Test
        void rechercher_minuscules_shouldFindVilles() throws NotFoundException {
            List<String> result = recherche.Rechercher("pa");
            assertTrue(result.contains("Paris"));
        }

        @Test
        void rechercher_majuscules_shouldFindVilles() throws NotFoundException {
            List<String> result = recherche.Rechercher("PARIS");
            assertTrue(result.contains("Paris"));
        }

        @Test
        void rechercher_casseMixte_shouldFindVilles() throws NotFoundException {
            List<String> result = recherche.Rechercher("pArIs");
            assertTrue(result.contains("Paris"));
        }
    }

    // Etape 4 — sous-chaîne au milieu du nom
    @Nested
    class Step4_RechercheSousChaîne {

        @Test
        void rechercher_ape_shouldReturnBudapest() throws NotFoundException {
            List<String> result = recherche.Rechercher("ape");
            assertTrue(result.contains("Budapest"));
        }

        @Test
        void rechercher_ong_shouldReturnHongKong() throws NotFoundException {
            List<String> result = recherche.Rechercher("ong");
            assertTrue(result.contains("Hong Kong"));
        }

        @Test
        void rechercher_ang_shouldReturnBangkok() throws NotFoundException {
            List<String> result = recherche.Rechercher("ang");
            assertTrue(result.contains("Bangkok"));
        }
    }

    // Etape 5 — astérisque → toutes les villes
    @Nested
    class Step5_Asterisque {

        @Test
        void rechercher_asterisque_shouldReturnAllCities() throws NotFoundException {
            List<String> result = recherche.Rechercher("*");
            assertEquals(16, result.size());
        }

        @Test
        void rechercher_asterisque_shouldContainParis() throws NotFoundException {
            assertTrue(recherche.Rechercher("*").contains("Paris"));
        }
    }
}
