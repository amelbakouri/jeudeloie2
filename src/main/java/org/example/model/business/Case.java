package org.example.model.business;

import java.util.Optional;

/**
 * A case of the board
 */
public final class Case {

    /**
     * The position on the board
     */
    private final int position;

    /**
     * The effect of the case
     */
    private CaseEffect effect;

    /**
     * Constructor
     * @param position the position of the case
     */
    public Case(int position) {
        this.position = position;
        this.effect = null;
    }

    /**
     * Constructor with effect
     * @param position the position of the case
     * @param effect the effect of the case
     */
    public Case(int position, CaseEffect effect) {
        this.position = position;
        this.effect = effect;
    }

    /**
     * Apply an effect on a player
     * @param p a player
     * @return not empty for complex effects
     */
    public Optional<String> effect(Pawn p) {
        if (effect != null) {
            return effect.effect(p);
        }
        return Optional.empty();
    }

    /**
     * Change the case effect
     * @param effect an effect
     */
    public void setCaseEffect(CaseEffect effect) {
        this.effect = effect;
    }

    /**
     * Get the effect of the case
     * @return the effect
     */
    public CaseEffect getCaseEffect() {
        return effect;
    }

    /**
     * Get the position of the case
     * @return the case position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Get the symbol associated with the case effect
     * @return the symbol as a char
     */
    public char getSymbol() {
        if (effect == null) {
            return 'E';  // Default symbol for an empty case (no effect)
        }
        if (effect instanceof BackEffect) {
            return 'B';
        } else if (effect instanceof QuestionEffect) {
            return 'Q';
        } else if (effect instanceof ReturnEffect) {
            return 'R';
        }
        return 'E';  // Default case
    }
}
