package auth.agents.confour.game.ai;

import auth.agents.confour.game.Board;

/**
 * Interface for a connect four ai engine suggesting the next move
 */
public interface IEngine {

    /**
     * Suggest the next move of the player.
     * The board is given from the perspective of the current player.
     *
     * @param board the current board
     * @return the column in which to drop the next disk
     */
    int suggest(Board board);

}
