package auth.agents.confour.game;

import auth.agents.confour.game.ai.IEngine;

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
    private boolean playing; // if its player's turn

    public Game(boolean first) {
        this.first = first;

        board = new Board();
        playing = first;

    }

    public void addPlayerDisk(int x) {

        board.add(x, Board.P);
        nextPlayer();
    }

    /**
     * @return the move played
     */
    public int addPlayerDisk(IEngine engine) {

        int x = engine.suggest(board);
        addPlayerDisk(x);

        return x;
    }

    public void addOpponentDisk(int x) {

        board.add(x, Board.O);
        nextPlayer();
    }

    private void nextPlayer() {

        playing = !playing;
    }

    public boolean isPlaying() {
        return !isOver() && playing;
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

    public boolean isPlayerFirst() {
        return first;
    }

    public int numberOfMoves() {return (board.W*board.H) - board.getLeft();}

    @Override
    public String toString() {
        return board.toString();
    }
}
