package auth.agents.confour.game.ai;

import auth.agents.confour.game.Board;

import java.util.Random;

public class EngineRandom implements IEngine {

    private final Random ra = new Random();

    public EngineRandom() {

    }

    @Override
    public int suggest(Board board) {
        int move;

        do {
            move = ra.nextInt(Board.W);
        } while(!board.canAdd(move));

        return move;
    }
}
