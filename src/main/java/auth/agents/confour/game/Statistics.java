package auth.agents.confour.game;

public class Statistics {

    private int games;
    private int wins;
    private int moves;

    public Statistics(){
    }


    public void add(boolean isWinner, int moves){
        games += 1;
        if (isWinner){
            wins += 1;
        }
        this.moves += moves;
    }

    public void printStats(){
        double avgMoves = (double)moves/games;
        double winRate = (double)wins/games;
    }
}
