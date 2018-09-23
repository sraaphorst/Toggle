package com.vorpal.toggle.trie;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkedTrieTest {
    private static LinkedTrie trie;

    @BeforeAll
    static void setUp() {
        var res = LinkedTrieTest.class.getResourceAsStream("/dictionary.txt");
        trie = new LinkedTrie(res);
    }

    @Test
    void findApple() {
        assertTrue(trie.isWord("apple"));
    }
}