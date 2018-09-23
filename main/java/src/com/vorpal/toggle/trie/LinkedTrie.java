/**
 * LinkedTrie.java
 *
 * By Sebastian Raaphorst, 2018.
 *
 * Collection of linked nodes, each representing a single letter or a sequence of letters.
 * Note that lookups are CASE INSENSITIVE.
 */

package com.vorpal.toggle.trie;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class LinkedTrie implements Trie {

    /************
     * TrieNode *
     ************/
    private final class TrieNode {
        // The characters represented by this node.
        // For example, if this node has "t", the parent has "i", and the grandparent is the root, then this node
        // represents the word "it".
        private final String contents;

        // Boolean flag to represent if the string represented by this node comprises a valid word.
        private final boolean isValidWord;

        // The children of this node.
        private final Map<Character, TrieNode> children;

        // The parent of this node in the trie: the root has an empty value.
        private final Optional<TrieNode> parent;

        // Creates an empty root node.
        TrieNode() {
            this(Optional.empty(), "", false);
        }

        TrieNode(final Optional<TrieNode> parent, final String contents, final boolean isValidWord) {
            this.parent = parent;
            this.contents = contents.toLowerCase();
            this.isValidWord = isValidWord;
            children = new HashMap<>();
        }

        TrieNode(final TrieNode parent, final String contents, final boolean isWord) {
            this(Optional.ofNullable(parent), contents, isWord);
        }

        // Used for packing only.
        private TrieNode(final Optional<TrieNode> parent, final String contents,
                         final boolean isValidWord, final Map<Character, TrieNode> children) {
            this.parent = parent;
            this.contents = contents.toLowerCase();
            this.isValidWord = isValidWord;
            this.children = children;
        }
        /**
         * The pack method tries to reduce the number of nodes in the trie.
         * A node can be packed if the conditions hold:
         *
         * 1. This node is not a valid word.
         * 2. This node only has one child.
         *
         * Then we "pull" the child data up into this node.
         *
         * NOTE: pack should never be invoked until all words have been added; otherwise, essential intermediate
         * nodes may not exist during an addWord operation.
         */
        public void pack() {
            // Pack the children.
            children.values().forEach(TrieNode::pack);

            if (!isValidWord && children.size() == 1) {
                children.forEach(($, childValue) -> {
                    final TrieNode packed = new TrieNode(parent, contents + childValue.contents,
                            childValue.isValidWord, childValue.children);

                    // If this node hsa a parent, set it to be equal to the new node.
                    parent.ifPresent(p -> p.children.put(contents.charAt(0), packed));

                    // Clear out the children to get rid of all in-pointers, so this node will be GCed.
                    children.clear();
                });
            }
        }

        // Add a word, or a partially digested word fragment, to this node.
        public void add(final String s) {
            if (!s.startsWith(contents))
                // Something has gone wrong. This call means that the trie is invalid.
                // This could have resulted in packing before adding all words.
                throw new IllegalStateException("Cannot add \"" + contents + "\" to the trie. " +
                        "Did you pack before adding?");

            // Remaining characters in the word.
            final String s2 = s.substring(contents.length());

            // If s2 is empty, that means that we have reached a node that is a valid word.
            // If it is not currently a valid word, we must switch it to be one and change the parent to point
            // to this one instead. If words are being added in alphabetical order, this should never happen.
            if (s2.isEmpty() && !isValidWord) {
                final TrieNode node = new TrieNode(parent, contents, true, children);
                parent.ifPresent(p -> p.children.put(contents.charAt(0), node));
                return;
            } else if (!s2.isEmpty()) {
                var first = s2.charAt(0);
                var node = children.computeIfAbsent(first,
                        ($) -> new TrieNode(this, String.valueOf(first), s2.length() == 1));

                // Now invoke with the rest of s2 on the child node.
                node.add(s2);
            }
        }

        // Determine if the fragment here is a word.
        boolean isWord(final String s) {
            // The first condition shouldn't happen as we only iterate if we have letters left.
            // Secondly, if s doesn't start with the contents of this node, it is not a word.
            if (s.isEmpty() || !s.startsWith(contents))
                return false;

            // Chop off contents, and if we are empty, then we are a word if this node represents a word.
            final String s2 = s.substring(contents.length());
            if (s2.isEmpty())
                return isValidWord;

            // Otherwise, we recurse via children if there is an entry for the next character.
            return children.containsKey(s2.charAt(0)) && children.get(s2.charAt(0)).isWord(s2);
        }
    }
    /*************/

    // Create a Trie from a stream of words.
    // Diactrics are removed, and strings are converted to lowercase.
    public LinkedTrie(final Stream<String> words) {
        root = new TrieNode();
        words.map(w -> Normalizer.normalize(w, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", ""))
                .forEach(root::add);
    }

    // Create a Trie from a file.
    public LinkedTrie(final String filename) throws java.io.IOException {
        this(Files.lines(Paths.get(filename)));
    }

    public LinkedTrie(final File file) throws java.io.FileNotFoundException {
        this(new BufferedReader(new FileReader(file)).lines());
    }

    public LinkedTrie(final InputStream is) {
        this(new BufferedReader(new InputStreamReader(is)).lines());
    }

    /**
     * Remove unnecessary nodes and condense the tree, which should end up speeding up searches and reducing memory.
     * Calling it is, however, unnecessary, and it is idempotent.
     */
    public void pack() {
        root.pack();
    }

    /**
     * Determine if, according to this trie, the specified string is a valid word.
     * @param s the string to check
     * @return true if it is a valid word, and false otherwise
     */
    @Override
    public boolean isWord(final String s) {
        return root.isWord(s.toLowerCase());
    }

    /**
     * Attempts to add a word to the trie. Note that if the trie has been packed, then this may result in
     * an exception.
     * @param s the word to add
     * @throws IllegalStateException
     */
    public void addWord(final String s) throws IllegalStateException {
        root.add(s);
    }

    /**
     * Dumps the tree by visiting each node.
     */
    public void dump(final Consumer<String> consumer) {
        class NodePair {
            NodePair(final String s, final TrieNode n) {
                this.s = s;
                this.n = n;
            }
            String s;
            TrieNode n;
        }

        final Stack<NodePair> nodes = new Stack<>();
        nodes.push(new NodePair("", root));
        while (!nodes.empty()) {
            final var np   = nodes.pop();
            final var node = np.n;
            final var str  = np.s + node.contents;

            if (node.isValidWord)
                consumer.accept(str);

            node.children.values().forEach(c -> nodes.push(new NodePair(str, c)));
        }
    }

    private final TrieNode root;
}
