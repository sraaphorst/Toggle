package com.vorpal.toggle.dice;

import com.vorpal.utils.BigMath;

import java.util.List;
import java.util.Objects;

/**
 * A board is a permutation of the dice as well as a side for each die.
 */
public class Board {
    private final DiceSet diceSet;

    // Note that the dice sides are independent of permutation, so while the die in
    // position (x,y) is permutation(x,y), the face of the die in position(x,y) is
    // diceSides(x,y).
    private final List<Integer> permutation;
    private final List<Integer> diceSides;

    public Board(final DiceSet diceSet, final List<Integer> permutation, final List<Integer> diceSides) {
        this.diceSet = Objects.requireNonNull(diceSet);
        this.permutation = Objects.requireNonNull(permutation);
        this.diceSides = Objects.requireNonNull(diceSides);

        // Check that everything is compatible.
        if (!(permutation.size() == diceSet.getNumberOfDice() && BigMath.isPermutation(permutation)))
            throw new IllegalArgumentException("list is not a permutation");
        diceSides.forEach(s -> {
            if (s < 0 || s > 5)
                throw new IllegalArgumentException("illegal die side specified: " + s);
        });
    }

    String getValueAt(final int x, final int y) {
        if (x < 0 || x >= diceSet.getSide())
            throw new IllegalArgumentException("illegal x coordinate: " + x);
        if (y < 0 || y >= diceSet.getSide())
            throw new IllegalArgumentException("illegal y coordinate: " + y);

        // Calculate the index of the position.
        final var index = BigMath.pairToIndex(diceSet.getSide(), x, y);
        final var die = diceSet.getDie(permutation.get(index));
        return die.getChar(diceSides.get(index));
    }
}
