package auth.agents.confour.game;


import java.awt.event.HierarchyBoundsAdapter;

/**
 * A connect four board state.
 * <p>
 * The indexes are show bellow:
 * <pre>
 *   y
 *   2 . . .
 *   1 . . .
 *   0 . . .
 *     0 1 2 x
 * </pre>
 * <p>
 * The values are represented with char values: E, P, O.
 * The winner is represented with char values: N, E, P, D.
 *
 *
 */
public final class Board {

    // board sizes
    public static final int W = 7;
    public static final int H = 6;

    // board cells
    public static final char E = '.'; // empty
    public static final char P = 'P'; // player
    public static final char O = 'O'; // opponent

    // winner state
    public static final char N = 'N'; // none
    public static final char D = 'D'; // draw

    private final char[][] cells;
    private int left;

    private char winner;

    public Board() {

        cells = new char[W][H];

        clear();
    }

    /**
     * Used by the clone method.
     */
    private Board(Board other) {
        this(); // TODO improve

        for (int x = 0; x < W; x++) {
            for (int y = 0; y < H; y++) {
                cells[x][y] = other.get(x, y);
            }
        }

        left = other.left;
        winner = other.winner;
    }

    /**
     * Creates a clone of the object. The new objects shares none
     * of the variables with the old one.
     *
     * @return the new board
     */
    public Board clone() {
        return new Board(this);
    }

    /**
     * Changes the board in order to become an clone of a given board.
     *
     * @param other the given board
     */
    public void set(Board other) {

        for (int x = 0; x < W; x++) {
            for (int y = 0; y < H; y++) {
                cells[x][y] = other.get(x, y);
            }
        }

        left = other.left;
        winner = other.winner;
    }

    /**
     * Resets the board
     */
    public void clear() {

        for (int x = 0; x < W; x++) {
            for (int y = 0; y < H; y++) {
                cells[x][y] = E;
            }
        }
        left = W*H;
        winner = N;

    }

    public boolean canAdd(int x) {
        return cells[x][H-1] == E;
    }

    public void add(int x, char who) {
        int y = empty(x);

        cells[x][y] = who;
        left -= 1;

        check(x, y);
    }

    public char get(int x, int y) {

        return cells[x][y];
    }

    public int empty(int x) {

        int y = 0;
        while (cells[x][y] != E) y += 1;

        return y;
    }

    private void check(int x, int y) {
        char c = get(x, y);

        if (check(c, x, y)) {
            winner = c;
        } else if (left == 0) {
            winner = D;
        }

    }

    private boolean check(char c, int x, int y) {
        return check(c, x, y, 0, 1) ||
                check(c, x, y, 1, 1) ||
                check(c, x, y, 1, 0);
    }

    private boolean check(char c, int x, int y, int dx, int dy) {
        int count = 0;

        for (int i = -4; i <= 4; i++) {

            int ix = x + i*dx;
            int iy = y + i*dy;

            if (!inside(ix, iy)) continue;

            if (get(ix, iy) != c) {
                count = 0;
            } else {
                count += 1;
            }
        }

        return count >= 4;
    }

    private boolean inside(int x, int y) {
        return 0 <= x && x < W && 0 <= y && y < H;
    }

    public boolean hasWinner() {
        return winner != N;
    }

    public char getWinner() {
        return winner;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int y = H - 1; y >= 0; y--) {
            sb.append("|");
            for (int x = 0; x < W; x++) {
                sb.append(cells[x][y]);
            }
            sb.append("|\n");
        }
        sb.append("\\-------/");

        return sb.toString();
    }

}
