// DefaultDiceSets.java
//
// By Sebastian Raaphorst, 2018.

package com.vorpal.toggle.dice;

/**
 * A class containing the definitions of standard Boggle dice.
 */
public class DefaultDiceSets implements DiceConstants{
    private DefaultDiceSets() {}

    public final static DiceSet DEFAULT_16_DICE_SET = new DiceSet(4,
            new Die[]{
                    new Die(A, A, C, I, O, T),
                    new Die(A, B, I, L, T, Y),
                    new Die(A, B, J, M, O, QU),
                    new Die(A, C, D, E, M, P),
                    new Die(A, C, E, L, R, S),
                    new Die(A, D, E, N, V, Z),
                    new Die(A, H, M, O, R, S),
                    new Die(B, F, I, O, R, X),
                    new Die(D, E, N, O, S, W),
                    new Die(D, K, N, O, T, U),
                    new Die(E, E, F, H, I, Y),
                    new Die(E, G, I, N, T, V),
                    new Die(E, G, K, L, U, Y),
                    new Die(E, H, I, N, P, S),
                    new Die(E, L, P, S, T, U),
                    new Die(G, I, L, R, U, W),
            });

    public final static DiceSet DEFAULT_25_DICE_SET = new DiceSet(5,
            new Die[]{
                    new Die(A, A, A, F, R, S),
                    new Die(A, A, E, E, E, E),
                    new Die(A, A, F, I, R, S),
                    new Die(A, D, E, N, N, N),
                    new Die(A, E, E, E, E, M),
                    new Die(A, E, E, G, M, U),
                    new Die(A, E, G, M, N, N),
                    new Die(A, F, I, R, S, Y),
                    new Die(B, J, K, QU, X, Z),
                    new Die(C, C, N, S, T, W),
                    new Die(C, E, I, I, L, T),
                    new Die(C, E, I, L, P, T),
                    new Die(C, E, I, P, S, T),
                    new Die(D, D, L, N, O, R),
                    new Die(D, H, H, L, O, R),
                    new Die(D, H, H, N, O, T),
                    new Die(D, H, L, N, O, R),
                    new Die(E, I, I, I, T, T),
                    new Die(E, M, O, T, T, T),
                    new Die(E, N, S, S, S, U),
                    new Die(F, I, P, R, S, Y),
                    new Die(G, O, R, R, V, W),
                    new Die(H, I, P, R, S, Y),
                    new Die(O, O, O, T, T, U),
                    new Die(N, O, O, T, U, W),
            });
}
