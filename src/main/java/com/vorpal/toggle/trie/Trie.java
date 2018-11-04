// Trie.java
//
// By Sebastian Raaphorst, 2018.

package com.vorpal.toggle.trie;

import java.util.function.Consumer;

/**
 * Interface for a trie structure, used for fast word lookups.
 */
public interface Trie {
    /**
     * Given a string, check if it is contained, as a word or suffix of other words in the tree.
     * @param s the string to check
     * @return true is s is recognized as a suffix
     */
    boolean isPrefix(final String s);

    /**
     * Given a string, check if it is a word according to the trie.
     * @param s the string to check
     * @return true is s is recognized as a word
     */
    boolean isWord(final String s);

    /**
     * Performs a dump of the words in the trie to the consumer.
     * The words should be in alphabetic order.
     * @param consumer the consumer of the complete words.
     */
    void dump(final Consumer<String> consumer);

    static Trie createDefaultTrie() {
        return new LinkedTrie(Trie.class.getResourceAsStream("/dictionary.txt"));
    }
}
