package auth.agents.confour.game;


/**
 * 2 . . .
 * 1 . . .
 * 0 . . .
 *   0 1 2 ...
 *
 *
 *
 */
public class Board {

    // board sizes
    private static final int W = 7;
    private static final int H = 6;

    private static final char E = ' '; // empty
    private static final char P = 'P'; // player
    private static final char O = 'O'; // opponent

    private final char[][] cells;

    public Board() {

        cells = new char[W][H];

        for (int x = 0; x < W; x++) {
            for (int y = 0; y < H; y++) {
                cells[x][y] = E;
            }
        }
    }

    public void add(int x, char who) {

        cells[x][empty(x)] = who;
    }

    public char get(int x, int y) {

        return cells[x][y];
    }

    public int empty(int x) {

        int y = 0;
        while (cells[x][y] != E) y += 1;

        return y;
    }
}
