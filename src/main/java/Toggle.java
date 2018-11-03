import com.vorpal.toggle.board.Board;
import com.vorpal.toggle.board.BoardType;
import com.vorpal.toggle.dice.DefaultDiceSets;
import com.vorpal.toggle.trie.LinkedTrie;
import com.vorpal.toggle.trie.Trie;
import com.vorpal.utils.BigMath;

import java.io.InputStream;
import java.math.BigInteger;

public class Toggle {
    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    public static void main(String[] args) {
        final InputStream res = Trie.class.getResourceAsStream("/dictionary.txt");
        LinkedTrie trie = new LinkedTrie(res);

//        final Board board = new Board(BoardType.GRID,
//                DefaultDiceSets.DEFAULT_16_DICE_SET,
//                BigMath.unrankPermutationAsList(16, BigInteger.ZERO),
//                BigMath.unrankDiceFacesAsList(16, BigInteger.ZERO),
//                trie, 4);

        final Board board = new Board(BoardType.TORUS,
                DefaultDiceSets.DEFAULT_16_DICE_SET,
                BigMath.unrankPermutationAsList(16, BigInteger.valueOf(9223372036854775807L)),
                BigMath.unrankDiceFacesAsList(16, BigInteger.valueOf(839283)),
                trie, 4);

        System.out.println("\n");
        for (int x = 0; x < 4; ++x) {
            System.out.print("   ");
            for (int y = 0; y < 4; ++y) {
                final String val = board.getValueAt(x, y);
                //final int padding = 3 - val.length();
                System.out.print(padRight(val,3));
            }
            System.out.println();
        }
        System.out.println("\n");

        for (final String s: board.getWords())
            System.out.println(s);
        System.out.println("*** " + board.getWords().size() + " words.");
    }
}