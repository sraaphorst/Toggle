package com.vorpal.toggle.trie;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

final class LinkedTrieTest {
    private static LinkedTrie trie;

    @BeforeAll
    static void setUp() {
        final InputStream res = LinkedTrieTest.class.getResourceAsStream("/dictionary.txt");
        trie = new LinkedTrie(res);
    }

    @Test
    void findApple() {
        assertTrue(trie.isWord("apple"));
    }

    @Test
    void findApples() {
        assertTrue(trie.isWord("apples"));
    }

    @Test
    void findApplesauce() {
        assertTrue(trie.isWord("applesauce"));
    }

    @Test
    void doNotFindAppleSau() {
        assertFalse(trie.isWord("applesau"));
    }

    @Test
    void caseDoesntMatter() {
        assertTrue(trie.isWord("APPLE"));
        assertTrue(trie.isWord("Apple"));
        assertTrue(trie.isWord("aPpLe"));
    }

    @Test
    void testPrefix() {
        final String applesauce = "applesauce";

        for (int i = 0; i <= applesauce.length(); ++i)
            assertTrue(trie.isPrefix(applesauce.substring(0, i)));
        assertFalse(trie.isPrefix(applesauce + "a"));
    }

}