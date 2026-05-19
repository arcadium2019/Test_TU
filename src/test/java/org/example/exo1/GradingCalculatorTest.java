package org.example.exo1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GradingCalculatorTest {

    @Test
    void score95Presence90_shouldReturnA() {
        assertEquals('A', new GradingCalculator(95, 90).getGrade());
    }

    @Test
    void score85Presence90_shouldReturnB() {
        assertEquals('B', new GradingCalculator(85, 90).getGrade());
    }

    @Test
    void score65Presence90_shouldReturnC() {
        assertEquals('C', new GradingCalculator(65, 90).getGrade());
    }

    @Test
    void score95Presence65_shouldReturnB() {
        assertEquals('B', new GradingCalculator(95, 65).getGrade());
    }

    @Test
    void score95Presence55_shouldReturnF() {
        assertEquals('F', new GradingCalculator(95, 55).getGrade());
    }

    @Test
    void score65Presence55_shouldReturnF() {
        assertEquals('F', new GradingCalculator(65, 55).getGrade());
    }

    @Test
    void score50Presence90_shouldReturnF() {
        assertEquals('F', new GradingCalculator(50, 90).getGrade());
    }
}