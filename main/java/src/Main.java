import com.vorpal.toggle.trie.LinkedTrie;

public class Main {
    public static void main(String[] args) {
        System.out.println("Reading trie...");
        final var trie = new LinkedTrie(Main.class.getResourceAsStream("/dictionary.txt"));
        System.out.println("Done.");

//        System.out.println("--- CONTENTS: ---");
//        trie.dump(System.out::println);
//        System.out.println("----- DONE -----");

        System.out.println(trie.isWord("apple"));
    }
}
