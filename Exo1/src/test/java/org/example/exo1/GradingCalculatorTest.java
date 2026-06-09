package org.example.exo1;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GradingCalculatorTest {

    @ParameterizedTest
    @CsvSource({
        "95, 90, A",
        "85, 90, B",
        "65, 90, C",
        "95, 65, B",
        "95, 55, F",
        "65, 55, F",
        "50, 90, F"
    })
    void getGrade_shouldReturnExpectedGrade(int score, int attendance, char expectedGrade) {
        assertEquals(expectedGrade, new GradingCalculator(score, attendance).getGrade());
    }
}