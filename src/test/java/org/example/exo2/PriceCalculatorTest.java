package org.example.exo2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PriceCalculatorTest {

    private final PriceCalculator calculator = new PriceCalculator();

    // Cas nominaux

    @Test
    void calculateTotalPrice_shouldReturn30() {
        assertEquals(30.0, calculator.calculateTotalPrice(10.0, 3));
    }

    @Test
    void applyDiscount_shouldReturn80() {
        assertEquals(80.0, calculator.applyDiscount(100.0, 0.20));
    }

    @Test
    void calculateVat_shouldReturn20() {
        assertEquals(20.0, calculator.calculateVat(100.0, 0.20));
    }

    @Test
    void calculatePriceWithVat_shouldReturn120() {
        assertEquals(120.0, calculator.calculatePriceWithVat(100.0, 0.20));
    }

    // Cas d'erreur

    @Test
    void calculateTotalPrice_negativeUnitPrice_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateTotalPrice(-1.0, 3));
    }

    @Test
    void calculateTotalPrice_negativeQuantity_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateTotalPrice(10.0, -1));
    }

    @Test
    void applyDiscount_negativeRate_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> calculator.applyDiscount(100.0, -0.20));
    }

    @Test
    void calculateVat_negativeRate_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateVat(100.0, -0.20));
    }

    @Test
    void calculatePriceWithVat_negativeRate_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> calculator.calculatePriceWithVat(100.0, -0.20));
    }
}
