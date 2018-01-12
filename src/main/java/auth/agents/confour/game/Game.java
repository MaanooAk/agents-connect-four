package auth.agents.confour.game;

public class Game {

    private final boolean first; // if the player started

    private Board board;
    private boolean playing; // if its players turn

    public Game(boolean first) {
        this.first = first;

        board = new Board();
        playing = first;

    }

    public boolean isPlaying() {
        return playing;
    }

    public void addPlayerDisk(int x) {
        board.add(x, Board.P);
    }

    public void addOpponentDisk(int x) {
        board.add(x, Board.P);
    }

}
