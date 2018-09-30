/**
 * LinkedTrie.java
 *
 * By Sebastian Raaphorst, 2018.
 *
 * Collection of linked nodes, each representing a single letter or a sequence of letters.
 * Note that lookups are CASE INSENSITIVE.
 */

package com.vorpal.toggle.trie;

import com.vorpal.utils.Pair;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class LinkedTrie implements Trie {
    /************
     * TrieNode *
     ************/
    private final class LinkedTrieNode implements TrieNode {
        // The characters represented by this node.
        // For example, if this node has "t", the parent has "i", and the grandparent is the root, then this node
        // represents the word "it".
        private final String contents;

        // Boolean flag to represent if the string represented by this node comprises a valid word.
        private final boolean isValidWord;

        // The children of this node.
        private final Map<Character, LinkedTrieNode> children;

        // The parent of this node in the trie: the root has an empty value.
        private final Optional<LinkedTrieNode> parent;

        // Creates an empty root node.
        LinkedTrieNode() {
            this(Optional.empty(), "", false);
        }

        LinkedTrieNode(final Optional<LinkedTrieNode> parent, final String contents, final boolean isValidWord) {
            this.parent = parent;
            this.contents = contents.toLowerCase();
            this.isValidWord = isValidWord;
            children = new HashMap<>();
        }

        LinkedTrieNode(final LinkedTrieNode parent, final String contents, final boolean isWord) {
            this(Optional.ofNullable(parent), contents, isWord);
        }

        // Used for packing only.
        private LinkedTrieNode(final Optional<LinkedTrieNode> parent, final String contents,
                               final boolean isValidWord, final Map<Character, LinkedTrieNode> children) {
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
            children.values().forEach(LinkedTrieNode::pack);

            // If this is a valid word, we can't pack, because we would lose that information.
            if (!isValidWord && children.size() == 1) {
                children.forEach(($, childValue) -> {
                    final LinkedTrieNode packed = new LinkedTrieNode(parent, contents + childValue.contents,
                            childValue.isValidWord, childValue.children);

                    // If this node has a parent, set it to be equal to the new node.
                    parent.ifPresent(p -> p.children.put(contents.charAt(0), packed));
                });

                // Clear out the children here to eliminate all in pointers to this node, since we want it to be
                // garbage collected along with its children.
                children.clear();
            }
        }

        // Add a word, or a partially digested word fragment, to this node.
        public void add(final String sm) {
            final String s = sm.toLowerCase();
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
                final LinkedTrieNode node = new LinkedTrieNode(parent, contents, true, children);
                parent.ifPresent(p -> p.children.put(contents.charAt(0), node));
                return;
            } else if (!s2.isEmpty()) {
                var first = s2.charAt(0);
                var node = children.computeIfAbsent(first,
                        ($) -> new LinkedTrieNode(this, String.valueOf(first), s2.length() == 1));

                // Now invoke with the rest of s2 on the child node.
                node.add(s2);
            }
        }

        // Determine if the fragment here is a word.
        public boolean isWord(final String s) {
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

        public String getContents() {
            return contents;
        }
    }
    /*************/

    // Create a Trie from a stream of words.
    // Diactrics are removed, and strings are converted to lowercase.
    public LinkedTrie(final Stream<String> words) {
        root = new LinkedTrieNode();
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
        final Stack<Pair<LinkedTrieNode, String>> nodes = new Stack<>();
        nodes.push(new Pair<>(root, ""));
        while (!nodes.empty()) {
            final var np   = nodes.pop();
            final var node = np.first;
            final var str  = np.second + node.contents;

            if (node.isValidWord)
                consumer.accept(str);

            node.children.values().forEach(c -> nodes.push(new Pair<>(c, str)));
        }
    }

    /******************
     * TrieStatistics *
     ******************/
    // Some statistics about the size and height of the trie, in order to determine the effect that packing has.
    public class TrieStatistics {
        public int height  = -1;
        public long nodes  =  0;
        public long words  =  0;

        // In the regular trie case, it should be the case that each node minus the root contains one character.
        // We want to track, for each number of characters, how many nodes there in the packed tree.
        public final Map<Integer, Integer> nodesByCharCount = new TreeMap<>();

        // Because there are a surprising number of nodes that contain very large strings, keep track:
        public final Map<Integer, ArrayList<String>> stringCompressionsByCharCount = new TreeMap<>();

        // We would also like to know the highest level of compression at each depth.
        // TODO: Find the compressed nodes with the longest paths.
        public final Map<Integer, Integer> highestCompressionByDepth = new TreeMap<>();
    }

    /**
     * Calculate statistics based on the trie.
     */
    public TrieStatistics analyze() {
        final TrieStatistics stats = new TrieStatistics();

        // We keep track of the height of the tree.
        final Stack<Pair<LinkedTrieNode, Integer>> nodes = new Stack<>();
        nodes.push(new Pair<>(root, 0));
        while (!nodes.empty()) {
            final var pr     = nodes.pop();
            final var node   = pr.first;
            final var height = pr.second;

            ++stats.nodes;
            stats.nodesByCharCount.put(node.contents.length(), stats.nodesByCharCount.getOrDefault(node.contents.length(), 0) + 1);

            var array = stats.stringCompressionsByCharCount.getOrDefault(node.contents.length(), new ArrayList<>());
            array.add(node.contents);
            stats.stringCompressionsByCharCount.put(node.contents.length(), array);

            // Check if we are a valid word.
            if (node.isValidWord)
                ++stats.words;

            // If there are no children, we are a contender for the height of the tree.
            if (node.children.isEmpty())
                if (height > stats.height)
                    stats.height = height;

            // If we have any compression, check if it beats out the compression for this depth.
            if (node.contents.length() > stats.highestCompressionByDepth.getOrDefault(height, 1)) {
                stats.highestCompressionByDepth.put(height, node.contents.length());
            }

            // Recurse over the children.
            node.children.values().forEach(c -> nodes.push(new Pair<>(c, height + 1)));
        }

        return stats;
    }

    private final LinkedTrieNode root;
}
