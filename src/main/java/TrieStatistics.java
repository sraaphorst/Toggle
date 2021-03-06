// TrieStatistics.java
//
// By Sebastian Raaphorst, 2018.

import com.vorpal.toggle.trie.LinkedTrie;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * Take a dictionary, make it into a trie.
 * Then take the trie and perform a packing algorithm on it.
 * Compute some statistics on each of the two tries, and compare them.
 */
public class TrieStatistics {
    private static final int THRESHOLD = 25;

    private static void displayStats(LinkedTrie.TrieStatistics stats) {
        System.out.format("\t* Number of nodes: %8d\n", stats.nodes);
        System.out.format("\t* Height of trie:  %8d\n", stats.height);
        System.out.format("\t* Number of words: %8d\n\n", stats.words);
        System.out.println("Number of nodes containing strings of length:");
        stats.nodesByCharCount.forEach((k,v) ->
                System.out.format("%4d %7d\n", k, v)
        );

        System.out.println("\nHighest level of compression at depth:");
        stats.highestCompressionByDepth.forEach((k,v) ->
                System.out.format("%4d %4d\n", k, v)
        );

        // Find the content length where there are THRESHOLD or fewer strings, i.e. find the first index in
        // stats.nodesByCharCount that is at most THRESHOLD.
        // Remember to cut out key 0 because the root has no contents.
        System.out.println();
        final Optional<Integer> maybeIdx = stats.nodesByCharCount.entrySet().stream()
                .filter(e -> e.getKey() >= 1 && e.getValue() <= THRESHOLD)
                .findAny()
                .map(Map.Entry::getKey);
        final int upper_idx = Collections.max(stats.nodesByCharCount.keySet());

        if (maybeIdx.isPresent()) {
            maybeIdx.ifPresent(lower_idx -> {
                for (int i = lower_idx; i <= upper_idx; ++i) {
                    System.out.println("Contents of nodes with length " + i + ":");
                    for (final String c: stats.stringCompressionsByCharCount.get(i))
                        System.out.print("  " + c);
                    System.out.println();
                }
            });
        } else {
            System.out.println("No nodes with contents of length at least " + THRESHOLD + '.');
        }
    }

    public static void main(String[] args) {
        System.out.print("READING TRIE... ");
        final LinkedTrie trie = new LinkedTrie(TrieStatistics.class.getResourceAsStream("/dictionary.txt"));
        System.out.println("done.\n");
        final LinkedTrie.TrieStatistics stats1 = trie.analyze();
        System.out.println("Statistics:");
        displayStats(stats1);

        System.out.print("\n\nPACKING TRIE... ");
        trie.pack();
        System.out.println("done.\n");
        final LinkedTrie.TrieStatistics stats2 = trie.analyze();
        System.out.println("Statistics:");
        displayStats(stats2);
    }
}
