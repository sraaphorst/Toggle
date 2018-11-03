// BigMath.java
//
// By Sebastian Raaphorst, 2018.

package com.vorpal.utils;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.List;

/**
 * Operations requiring BigInteger.
 *
 * Note that ranking / unranking modified from:
 * https://rosettacode.org/wiki/Permutations/Rank_of_a_permutation#Java
 */
public final class BigMath {
    private BigMath() {}

    public static BigInteger factorial(int n) {
        if (n < 0) throw new IllegalArgumentException("Attempted to calculate a negative factorial.");

        // Java doesn't care about tail recursion so we'll loop.
        var f = BigInteger.ONE;
        final var upper = BigInteger.valueOf(n);
        for (var ctr = BigInteger.ONE; ctr.compareTo(upper) <= 0; ctr = ctr.add(BigInteger.ONE))
            f = f.multiply(ctr);

        return f;
    }

    public static BigInteger exponent(int x, int n) {
        var e = BigInteger.ONE;
        final var xb = BigInteger.valueOf(x);
        for (var i = 0; i < n; ++i)
            e = e.multiply(xb);
        return e;
    }

    public static BigInteger rankPermutation(final int[] permutation) {
        final var n = permutation.length;
        final var usedDigits = new BitSet();
        var rank = BigInteger.ZERO;
        for (var i = 0; i < n; i++) {
            rank = rank.multiply(BigInteger.valueOf(n - i));
            int digit = 0;
            int v = -1;
            while ((v = usedDigits.nextClearBit(v + 1)) < permutation[i])
                digit++;
            usedDigits.set(v);
            rank = rank.add(BigInteger.valueOf(digit));
        }
        return rank;
    }

    public static int[] unrankPermutation(final int n, BigInteger rank) {
        final int[] digits = new int[n];
        for (var digit = 2; digit <= n; digit++) {
            var divisor = BigInteger.valueOf(digit);
            digits[n - digit] = rank.mod(divisor).intValue();
            if (digit < n)
                rank = rank.divide(divisor);
        }

        var usedDigits = new BitSet();
        final int[] permutation = new int[n];
        for (var i = 0; i < n; i++) {
            var v = usedDigits.nextClearBit(0);
            for (var j = 0; j < digits[i]; j++)
                v = usedDigits.nextClearBit(v + 1);
            permutation[i] = v;
            usedDigits.set(v);
        }
        return permutation;
    }
    
    public static BigInteger rankDiceFaces(final int[] faces) {
        BigInteger rank = BigInteger.ZERO;
        final var numFaces = BigInteger.valueOf(6);
        for (var f: faces)
            rank = rank.multiply(numFaces).add(BigInteger.valueOf(f));
        return rank;
    }

    public static int[] unrankDiceFaces(final int n, BigInteger rank) {
        int[] faces = new int[n];
        final var numFaces = BigInteger.valueOf(6);
        for (var idx = n - 1; idx >= 0; --idx) {
            faces[idx] = rank.mod(numFaces).intValue();
            rank = rank.divide(numFaces);
        }
        return faces;
    }

    /**
     * Check if a list of integers is a valid permutation.
     * @param candidate the list of integers
     * @return true if a permutation, and false otherwise
     */
    public static boolean isPermutation(final List<Integer> candidate) {
        final BitSet bs = new BitSet(candidate.size());
        candidate.forEach(bs::set);
        return bs.nextClearBit(0) == candidate.size();
    }

    public static int pairToIndex(final int side, final int x, final int y) {
        if (x < 0 || x >= side)
            throw new IllegalArgumentException("illegal x coordinate: " + x);
        if (y < 0 || y >= side)
            throw new IllegalArgumentException("illegal y coordinate: " + y);
        return x * side + y;
    }

    public static Pair<Integer, Integer> indexToPair(final int side, final int index) {
        if (index < 0 || index >= side * side)
            throw new IllegalArgumentException("illegal index: " + index);
        return new Pair<>(index / side, index % side);
    }
}
