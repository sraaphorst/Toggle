// Board.java
//
// By Sebastian Raaphorst, 2018.

package com.vorpal.toggle.board;

import com.vorpal.toggle.dice.DiceSet;
import com.vorpal.toggle.dice.Die;
import com.vorpal.toggle.trie.Trie;
import com.vorpal.utils.BigMath;
import com.vorpal.utils.Coordinates;
import com.vorpal.utils.Dimensions;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;

/**
 * A board is a permutation of the dice as well as a side for each die.
 */
public class Board {
    // The type of the board, which provides information about the adjacencies.
    private final BoardType boardType;

    // The dimension of the board.
    private final Dimensions boardSize;

    // The set of dice being used
    private final DiceSet diceSet;

    // Note that the dice sides are independent of permutation, so while the die in
    // position (x,y) is permutation(x,y), the face of the die in position(x,y) is
    // diceSides(x,y).
    private final List<Integer> permutation;
    private final List<Integer> diceSides;

    // The trie that we are using as our dictionary.
    private final Trie trie;

    public Board(final BoardType boardType,
                 final DiceSet diceSet,
                 final List<Integer> permutation,
                 final List<Integer> diceSides,
                 final Trie trie) {
        this.boardType = Objects.requireNonNull(boardType);
        this.diceSet = Objects.requireNonNull(diceSet);
        this.permutation = Objects.requireNonNull(permutation);
        this.diceSides = Objects.requireNonNull(diceSides);
        this.boardSize = new Dimensions(diceSet.getSide(), diceSet.getSide());
        this.trie = Objects.requireNonNull(trie);

        // Check that everything is compatible.
        if (!(permutation.size() == diceSet.getNumberOfDice() && BigMath.isPermutation(permutation)))
            throw new IllegalArgumentException("list is not a permutation");
        diceSides.forEach(s -> {
            if (s < 0 || s > 5)
                throw new IllegalArgumentException("illegal die side specified: " + s);
        });
    }

    /**
     * Find the words in the board.
     */
    private void setupWordList() {
        // Starting in the upper left corner, iterate over every tile and then perform a backtracking using a stack
        // to find all the words in the board.
        for (int x = 0; x < boardSize.first; ++x)
            for (int y = 0; y < boardSize.second; ++y) {
                final Stack<Die> stack = new Stack<>();
                stack.add(getDieAt(x, y));
                // TODO: Finish
            }
    }

    public Die getDieAt(final int x, final int y) {
        checkCoordinates(x, y);
        final int index = BigMath.pairToIndex(diceSet.getSide(), x, y);
        final Die die = diceSet.getDie(permutation.get(index));
        return die;
    }

    public String getValueAt(final int x, final int y) {
        final int index = BigMath.pairToIndex(diceSet.getSide(), x, y);
        return getDieAt(x, y).getChar(diceSides.get(index));
    }

    public Set<Coordinates> getAdjacencies(final int x, final int y) {
        checkCoordinates(x, y);
        return boardType.adjacencies(boardSize, new Coordinates(x, y));
    }

    private void checkCoordinates(final int x, final int y) {
        if (x < 0 || x >= diceSet.getSide())
            throw new IllegalArgumentException("illegal x coordinate: " + x);
        if (y < 0 || y >= diceSet.getSide())
            throw new IllegalArgumentException("illegal y coordinate: " + y);
    }
}
