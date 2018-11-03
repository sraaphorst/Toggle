package com.vorpal.utils;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

final class BigMathTest {
    @Test
    void checkIndexing() {
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                final Pair<Integer, Integer> p1 = new Pair<>(i, j);
                final Pair<Integer, Integer> p2 = BigMath.indexToPair(4, BigMath.pairToIndex(4, i, j));
                assertEquals(p1, p2);
            }
        }

        for (int i1 = 0; i1 < 16; ++i1) {
            final Pair<Integer, Integer> p  =  BigMath.indexToPair(4, i1);
            final int i2 =  BigMath.pairToIndex(4, p.first, p.second);
            assertEquals(i1, i2);
        }
    }

    @Test
    void permutationRanking() {
        // Using 16 positions takes way too long.
        final int numPositions = 8;
        final BigInteger n = BigMath.factorial(numPositions);
        for (BigInteger index = BigInteger.ZERO; index.compareTo(n) < 0; index = index.add(BigInteger.ONE)) {
            final int[] permutation = BigMath.unrankPermutation(numPositions, index);
            final BigInteger permRank    = BigMath.rankPermutation(permutation);
            assertEquals(permRank, index);
        }
    }

    @Test
    void diceRanking() {
        // Using 16 dice takes way too long.
        final int numDice = 8;
        final BigInteger n = BigMath.exponent(6, numDice);
        for (BigInteger index = BigInteger.ZERO; index.compareTo(n) < 0; index = index.add(BigInteger.ONE)) {
            final int[] faces = BigMath.unrankDiceFaces(numDice, index);
            final BigInteger facesRank = BigMath.rankDiceFaces(faces);
            assertEquals(facesRank, index);
        }
    }
}
