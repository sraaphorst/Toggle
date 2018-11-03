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

import java.util.*;

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

    // The word list for this, sorted by length and then alphabetically.
    private final List<String> words;

    // The minimum length of a valid word.
    private final int minimumWordLength;

    public Board(final BoardType boardType,
                 final DiceSet diceSet,
                 final List<Integer> permutation,
                 final List<Integer> diceSides,
                 final Trie trie,
                 int minimumWordLength) {
        this.boardType = Objects.requireNonNull(boardType);
        this.diceSet = Objects.requireNonNull(diceSet);
        this.permutation = Objects.requireNonNull(permutation);
        this.diceSides = Objects.requireNonNull(diceSides);
        this.boardSize = new Dimensions(diceSet.getSide(), diceSet.getSide());
        this.trie = Objects.requireNonNull(trie);
        this.minimumWordLength = minimumWordLength;

        // Check that everything is compatible.
        if (!(permutation.size() == diceSet.getNumberOfDice() && BigMath.isPermutation(permutation)))
            throw new IllegalArgumentException("list is not a permutation");
        diceSides.forEach(s -> {
            if (s < 0 || s > 5)
                throw new IllegalArgumentException("illegal die side specified: " + s);
        });

        // Find the words in this board.
        final List<String> wordList = new ArrayList<>();
        // Starting in the upper left corner, iterate over every tile and then perform a backtracking using a stack
        // to find all the words in the board.
        for (int x = 0; x < boardSize.first; ++x)
            for (int y = 0; y < boardSize.second; ++y) {
                final Stack<Coordinates> stack = new Stack<>();
                stack.push(new Coordinates(x, y));
                setupWordListRecursive(stack, getValueAt(x, y), wordList);
                stack.pop();
            }

        // Now sort wordList and store immutably in words.
        // We sort first based on length, and then alphabetically.
        wordList.sort((o1, o2) -> {
            if (o1.length() < o2.length()) return -1;
            if (o1.length() > o2.length()) return 1;
            return o1.compareTo(o2);
        });
        words = Collections.unmodifiableList(wordList);
    }

    /**
     * This backtracking algorithm, given the stack of dice chosen so far and the word represented,
     * determines what words can be generated from this choice and adds them to this.words.
     * @param stack the coordinates of the dice chosen so far
     * @param word the word represented by that choice so far
     * @param wordList the list into which to collect words
     */
    private void setupWordListRecursive(final Stack<Coordinates> stack, final String word, final List<String> wordList) {
        // If we are not a prefix, backtrack.
        if (!trie.isPrefix(word))
            return;

        // Determine if word is a word.
        if (word.length() >= minimumWordLength && trie.isWord(word) && !wordList.contains(word))
            wordList.add(word);

        // Get all unvisited neighbours of the top coordinate and traverse over them.
        final Coordinates c = stack.peek();
        final Set<Coordinates> adjacencies = getAdjacencies(c);
        adjacencies.removeAll(stack);

        for (final Coordinates cNext: adjacencies) {
            stack.push(cNext);
            setupWordListRecursive(stack, word + getValueAt(cNext), wordList);
            stack.pop();
        }
    }

    public Die getDieAt(final int x, final int y) {
        checkCoordinates(x, y);
        final int index = BigMath.pairToIndex(diceSet.getSide(), x, y);
        final Die die = diceSet.getDie(permutation.get(index));
        return die;
    }

    public String getValueAt(final Coordinates c) {
        return getValueAt(c.first, c.second);
    }

    public String getValueAt(final int x, final int y) {
        final int index = BigMath.pairToIndex(diceSet.getSide(), x, y);
        return getDieAt(x, y).getChar(diceSides.get(index));
    }

    public Set<Coordinates> getAdjacencies(final Coordinates c) {
        checkCoordinates(c);
        return boardType.adjacencies(boardSize, c);
    }

    public List<String> getWords() {
        return words;
    }

    public Set<Coordinates> getAdjacencies(final int x, final int y) {
        return getAdjacencies(new Coordinates(x, y));
    }

    private void checkCoordinates(final Coordinates c) {
        checkCoordinates(c.first, c.second);
    }

    private void checkCoordinates(final int x, final int y) {
        if (x < 0 || x >= diceSet.getSide())
            throw new IllegalArgumentException("illegal x coordinate: " + x);
        if (y < 0 || y >= diceSet.getSide())
            throw new IllegalArgumentException("illegal y coordinate: " + y);
    }
}
