package org.example.exo5;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DiceScoreTest {

    @Mock
    private Ide die;

    @Test
    void getScore_identicalDice_shouldReturnValueTimes2Plus10() {
        when(die.getRoll()).thenReturn(4, 4);
        assertEquals(18, new DiceScore(die).getScore());
    }

    @Test
    void getScore_identicalDiceOf1_shouldReturn12() {
        when(die.getRoll()).thenReturn(1, 1);
        assertEquals(12, new DiceScore(die).getScore());
    }

    @Test
    void getScore_identicalDiceOf6_shouldReturn30() {
        when(die.getRoll()).thenReturn(6, 6);
        assertEquals(30, new DiceScore(die).getScore());
    }

    @Test
    void getScore_differentDice_shouldReturnHighest() {
        when(die.getRoll()).thenReturn(3, 5);
        assertEquals(5, new DiceScore(die).getScore());
    }

    @Test
    void getScore_differentDiceReversed_shouldReturnHighest() {
        when(die.getRoll()).thenReturn(6, 2);
        assertEquals(6, new DiceScore(die).getScore());
    }
}
