package auth.agents.confour.game;

public class Statistics {

    private int games;
    private int wins;
    private int moves;

    public Statistics() {
    }

    public void add(Game game) {

        games += 1;
        if (game.isPlayerWinner()) wins += 1;

        moves += game.numberOfMoves();
    }

    public int getGames() {
        return games;
    }

    public float getWinRate() {
        return wins * 1f / games;
    }
}
