package org.example.exo3;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FibTest {

    private final List<Integer> range1Result = new Fib(1).getFibSeries();
    private final List<Integer> range6Result = new Fib(6).getFibSeries();

    @Test
    void getFibSeries_range1_shouldNotBeEmpty() {
        assertFalse(range1Result.isEmpty());
    }

    @Test
    void getFibSeries_range1_shouldReturnListContainingZero() {
        assertEquals(List.of(0), range1Result);
    }

    @Test
    void getFibSeries_range6_shouldContain3() {
        assertTrue(range6Result.contains(3));
    }

    @Test
    void getFibSeries_range6_shouldHave6Elements() {
        assertEquals(6, range6Result.size());
    }

    @Test
    void getFibSeries_range6_shouldNotContain4() {
        assertFalse(range6Result.contains(4));
    }

    @Test
    void getFibSeries_range6_shouldReturnExpectedSequence() {
        assertEquals(List.of(0, 1, 1, 2, 3, 5), range6Result);
    }

    @Test
    void getFibSeries_range6_shouldBeSortedAscending() {
        for (int i = 0; i < range6Result.size() - 1; i++) {
            assertTrue(range6Result.get(i) <= range6Result.get(i + 1));
        }
    }
}