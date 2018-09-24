/**
 * TrieNode.java
 *
 * By Sebastian Raaphorst, 2018.
 */

package com.vorpal.toggle.trie;


public interface TrieNode {
    /**
     * Determines if the specified string is a word in the trie rooted at this node.
     * @param s the string to consider
     * @return true if it is a word, and false otherwise
     */
    boolean isWord(String s);
}
