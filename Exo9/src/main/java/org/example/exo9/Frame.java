package org.example.exo9;

import java.util.ArrayList;
import java.util.List;

public class Frame {
    private int score;
    private boolean lastFrame;
    private IGenerateur generateur;
    private List<Roll> rolls;

    public Frame(IGenerateur generateur, boolean lastFrame) {
        this.lastFrame = lastFrame;
        this.generateur = generateur;
        this.rolls = new ArrayList<>();
    }

    public boolean makeRoll() {
        if (!canRoll()) {
            return false;
        }
        int pins = generateur.randomPin(10);
        rolls.add(new Roll(pins));
        score += pins;
        return true;
    }

    public int getScore() {
        return score;
    }

    private boolean canRoll() {
        int rollCount = rolls.size();

        if (!lastFrame) {
            if (rollCount >= 2) return false;
            if (rollCount == 1 && rolls.get(0).getPins() == 10) return false;
            return true;
        } else {
            if (rollCount >= 3) return false;
            if (rollCount == 2) {
                boolean firstWasStrike = rolls.get(0).getPins() == 10;
                boolean isSpare = !firstWasStrike && (rolls.get(0).getPins() + rolls.get(1).getPins() == 10);
                return firstWasStrike || isSpare;
            }
            return true;
        }
    }
}
