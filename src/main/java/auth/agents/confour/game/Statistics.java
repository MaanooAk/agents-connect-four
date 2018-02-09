package auth.agents.confour.game;

public class Statistics {

    private int games;
    private int wins;
    private int moves;
    private long start;

    public Statistics() {
        games = wins = moves = 0;
        start = System.currentTimeMillis();
    }

    public void add(Game game) {

        games += 1;
        if (game.isPlayerWinner()) wins += 1;

        moves += game.numberOfMoves();
    }

    public long getDuration() {
        return System.currentTimeMillis() - start;
    }

    public float getWinRate() {
        return wins * 1f / games;
    }

    public float getMovesEfficiency() {
        return moves * 1f / games;
    }

    public int getGames() {
        return games;
    }

    public int getWins() {
        return wins;
    }

    @Override
    public String toString() {
        return String.format("WinRate %f MovesEfficiency %f | Games %d Wins %d | %d",
                getWinRate(), getMovesEfficiency(), getGames(), getWins(), 2 * getWins() - getGames());
    }
}
