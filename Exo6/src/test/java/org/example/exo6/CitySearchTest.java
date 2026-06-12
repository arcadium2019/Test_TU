package org.example.exo6;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CitySearchTest {

    private RechercheVille search;

    @BeforeEach
    void setUp() {
        search = new RechercheVille();
    }

    // Step 1 — text < 2 characters → NotFoundException
    @Nested
    class Step1_TextTooShort {

        @Test
        void search_emptyText_shouldThrowNotFoundException() {
            assertThrows(NotFoundException.class, () -> search.Rechercher(""));
        }

        @ParameterizedTest
        @ValueSource(strings = {"a", "P", "R"})
        void search_singleCharacter_shouldThrowNotFoundException(String word) {
            assertThrows(NotFoundException.class, () -> search.Rechercher(word));
        }
    }

    // Step 2 — exact prefix (≥ 2 chars)
    @Nested
    class Step2_PrefixSearch {

        @Test
        void search_Va_shouldReturnValenceAndVancouver() throws NotFoundException {
            List<String> result = search.Rechercher("Va");
            assertTrue(result.contains("Valence"));
            assertTrue(result.contains("Vancouver"));
            assertEquals(2, result.size());
        }

        @Test
        void search_Pa_shouldReturnParis() throws NotFoundException {
            List<String> result = search.Rechercher("Pa");
            assertTrue(result.contains("Paris"));
        }

        @Test
        void search_nonExistentText_shouldReturnEmptyList() throws NotFoundException {
            List<String> result = search.Rechercher("Zz");
            assertTrue(result.isEmpty());
        }
    }

    // Step 3 — case insensitive
    @Nested
    class Step3_CaseInsensitive {

        @Test
        void search_lowercase_shouldFindCities() throws NotFoundException {
            List<String> result = search.Rechercher("pa");
            assertTrue(result.contains("Paris"));
        }

        @Test
        void search_uppercase_shouldFindCities() throws NotFoundException {
            List<String> result = search.Rechercher("PARIS");
            assertTrue(result.contains("Paris"));
        }

        @Test
        void search_mixedCase_shouldFindCities() throws NotFoundException {
            List<String> result = search.Rechercher("pArIs");
            assertTrue(result.contains("Paris"));
        }
    }

    // Step 4 — substring in the middle of the name
    @Nested
    class Step4_SubstringSearch {

        @Test
        void search_ape_shouldReturnBudapest() throws NotFoundException {
            List<String> result = search.Rechercher("ape");
            assertTrue(result.contains("Budapest"));
        }

        @Test
        void search_ong_shouldReturnHongKong() throws NotFoundException {
            List<String> result = search.Rechercher("ong");
            assertTrue(result.contains("Hong Kong"));
        }

        @Test
        void search_ang_shouldReturnBangkok() throws NotFoundException {
            List<String> result = search.Rechercher("ang");
            assertTrue(result.contains("Bangkok"));
        }
    }

    // Step 5 — asterisk → all cities
    @Nested
    class Step5_Asterisk {

        @Test
        void search_asterisk_shouldReturnAllCities() throws NotFoundException {
            List<String> result = search.Rechercher("*");
            assertEquals(16, result.size());
        }

        @Test
        void search_asterisk_shouldContainParis() throws NotFoundException {
            assertTrue(search.Rechercher("*").contains("Paris"));
        }
    }
}
