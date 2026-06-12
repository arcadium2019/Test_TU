package org.example.exo9;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FrameTest {

    @Mock
    private IGenerateur generateur;

    @Test
    void shouldIncreaseScoreWhenFirstRollIsMadeInStandardFrame() {
        when(generateur.randomPin(10)).thenReturn(5);
        Frame frame = new Frame(generateur, false);
        frame.makeRoll();
        assertEquals(5, frame.getScore());
    }

    @Test
    void shouldIncreaseScoreWhenSecondRollIsMadeInStandardFrame() {
        when(generateur.randomPin(10)).thenReturn(3, 4);
        Frame frame = new Frame(generateur, false);
        frame.makeRoll();
        frame.makeRoll();
        assertEquals(7, frame.getScore());
    }

    @Test
    void shouldRejectSecondRollWhenStandardFrameStartsWithStrike() {
        when(generateur.randomPin(10)).thenReturn(10);
        Frame frame = new Frame(generateur, false);
        assertTrue(frame.makeRoll());
        assertFalse(frame.makeRoll());
    }

    @Test
    void shouldRejectThirdRollWhenStandardFrameAlreadyHasTwoRolls() {
        when(generateur.randomPin(10)).thenReturn(3, 4);
        Frame frame = new Frame(generateur, false);
        frame.makeRoll();
        frame.makeRoll();
        assertFalse(frame.makeRoll());
    }

    @Test
    void shouldIncreaseScoreWhenSecondRollIsMadeAfterStrikeInLastFrame() {
        when(generateur.randomPin(10)).thenReturn(10, 5);
        Frame frame = new Frame(generateur, true);
        frame.makeRoll();
        int scoreBefore = frame.getScore();
        frame.makeRoll();
        assertTrue(frame.getScore() > scoreBefore);
    }

    @Test
    void shouldAcceptSecondRollWhenLastFrameStartsWithStrike() {
        when(generateur.randomPin(10)).thenReturn(10, 5);
        Frame frame = new Frame(generateur, true);
        frame.makeRoll();
        assertTrue(frame.makeRoll());
    }

    @Test
    void shouldAcceptThirdRollWhenLastFrameStartsWithStrike() {
        when(generateur.randomPin(10)).thenReturn(10, 5, 3);
        Frame frame = new Frame(generateur, true);
        frame.makeRoll();
        frame.makeRoll();
        assertTrue(frame.makeRoll());
    }

    @Test
    void shouldIncreaseScoreWhenThirdRollIsMadeAfterStrikeInLastFrame() {
        when(generateur.randomPin(10)).thenReturn(10, 5, 3);
        Frame frame = new Frame(generateur, true);
        frame.makeRoll();
        frame.makeRoll();
        int scoreBefore = frame.getScore();
        frame.makeRoll();
        assertTrue(frame.getScore() > scoreBefore);
    }

    @Test
    void shouldAcceptThirdRollWhenLastFrameStartsWithSpare() {
        when(generateur.randomPin(10)).thenReturn(5, 5, 3);
        Frame frame = new Frame(generateur, true);
        frame.makeRoll();
        frame.makeRoll();
        assertTrue(frame.makeRoll());
    }

    @Test
    void shouldIncreaseScoreWhenThirdRollIsMadeAfterSpareInLastFrame() {
        when(generateur.randomPin(10)).thenReturn(5, 5, 3);
        Frame frame = new Frame(generateur, true);
        frame.makeRoll();
        frame.makeRoll();
        int scoreBefore = frame.getScore();
        frame.makeRoll();
        assertTrue(frame.getScore() > scoreBefore);
    }

    @Test
    void shouldRejectThirdRollWhenLastFrameHasNoStrikeOrSpare() {
        when(generateur.randomPin(10)).thenReturn(3, 4);
        Frame frame = new Frame(generateur, true);
        frame.makeRoll();
        frame.makeRoll();
        assertFalse(frame.makeRoll());
    }

    @Test
    void shouldRejectFourthRollInLastFrame() {
        when(generateur.randomPin(10)).thenReturn(10, 5, 3);
        Frame frame = new Frame(generateur, true);
        frame.makeRoll();
        frame.makeRoll();
        frame.makeRoll();
        assertFalse(frame.makeRoll());
    }
}
