// DiceSet.java
//
// By Sebastian Raaphorst, 2018.

package com.vorpal.toggle.dice;

import com.vorpal.toggle.board.Board;
import com.vorpal.utils.BigMath;

import java.math.BigInteger;

/**
 * Represents the set of dice making up a Toggle board, but not up to isomorphism as that would be nightmarish to
 * calculate and rank / unrank. Thus, every symmetry of each board will be represented, and boards will be weighted
 * in some sense, i.e. if a board B can be created by another identical rearrangement of the dice, say B', then
 * both B and B' will have rank numbers despite them being the same in terms of gameplay. They are not the same in
 * terms of actual representations, because the unviewable portion (i.e. the five sides of the dice you cannot see)
 * would differ.
 */
public final class DiceSet {
    // Name and description of the dice set.
    final String name;

    // The length and width of the board.
    private final int side;

    // The dice.
    private final Die[] dice;

    // The number of dice permutations.
    private final BigInteger dicePermutations;

    // The number of possible boards for a permutation.
    private final BigInteger boardsPerPermutation;

    // The number of possible boards.
    private final BigInteger numBoards;

    public DiceSet(final String name, final int side, final Die[] dice) {
        if (side < 3 || side > 7)
            throw new IllegalArgumentException("DiceSet must have side 3 <= s <= 7");
        if (dice.length != side * side)
            throw new IllegalArgumentException("DiceSet expected " + side * side + " dice, got " + dice.length);

        this.name = name;
        this.side = side;
        this.dice = dice;

        dicePermutations = BigMath.factorial(side  * side);
        boardsPerPermutation = BigMath.exponent(6, side * side);
        numBoards = dicePermutations.multiply(boardsPerPermutation);
    }

    public int getSide() {
        return side;
    }

    public int getNumberOfDice() {
        return side * side;
    }

    public Die getDie(int index) {
        return dice[index];
    }

    public String getName() {
        return name;
    }

    // Given a board rank, create the board.
    public Board unrankBoard(final BigInteger boardRank) {
        if (boardRank.compareTo(BigInteger.ZERO) < 0 || boardRank.compareTo(numBoards) >= 0)
            throw new IndexOutOfBoundsException("Illegal board rank: " + boardRank);

        // Get the permutation and the dice side values.
        // The boardRank is permutationIndex * boardsPerPermutation + diceSidesIndex.
        final BigInteger permutationIndex = boardRank.divide(boardsPerPermutation);
        final BigInteger diceSidesIndex   = boardRank.mod(boardsPerPermutation);

        // TODO: FINISH
        return null;
    }
}