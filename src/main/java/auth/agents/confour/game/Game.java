package auth.agents.confour.game;

/**
 * A connect four game state
 * <p>
 * Suggested creation is using the GameMaker module
 *
 * @see GameMaker
 */
public final class Game {

    private final boolean first; // if the player started

    private Board board;
    private boolean playing; // if its players turn

    public Game(boolean first) {
        this.first = first;

        board = new Board();
        playing = first;

    }

    public void addPlayerDisk(int x) {

        board.add(x, Board.P);
        nextPlayer();
    }

    public void addOpponentDisk(int x) {

        board.add(x, Board.P);
        nextPlayer();
    }

    private void nextPlayer() {

        playing = !playing;
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean isOver() {
        return board.hasWinner();
    }

    public boolean isPlayerWinner() {
        return board.getWinner() == Board.P;
    }

    public boolean isOpponentWinner() {
        return board.getWinner() == Board.O;
    }

    @Override
    public String toString() {
        return board.toString();
    }
}
