package com.vorpal.toggle.dice;

/**
 * A single 6-sided die. We could generalize this to an n-sided die but I don't want to over-complicate
 * this project. Similarly, Q will represent Qu in all cases, as per regular Boggle.
 */
public class Die {
    private final String[] chars;

    public Die(String s1, String s2, String s3, String s4, String s5, String s6) {
        chars = new String[] {s1, s2, s3, s4, s5, s6};
    }

    public String getChar(int i) {
        return chars[i];
    }
}
