/**
 * Trie.java
 *
 * By Sebastian Raaphorst, 2018.
 *
 * Interface for a trie, used for fast word lookups.
 */

package com.vorpal.toggle.trie;

import java.util.function.Consumer;

public interface Trie {
    boolean isWord(final String s);

    /**
     * Dumps the words in the trie through a traversal.
     * They will be alphabetic.
     */
    void dump(final Consumer<String> consumer);
}
