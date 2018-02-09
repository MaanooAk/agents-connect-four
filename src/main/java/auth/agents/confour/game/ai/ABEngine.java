package auth.agents.confour.game.ai;

import auth.agents.confour.game.Board;

/**
 *
 * @author peron
 */
public class ABEngine implements IEngine {

    private final int depth = 3;
    private final int alpha = Integer.MIN_VALUE;
    private final int beta = Integer.MAX_VALUE;

    private final Evaluator evaluator;

    public ABEngine(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public int suggest(Board board) {
        int nextMove;
        nextMove = alphaBeta(board, depth, alpha, beta, true);
        return nextMove;

    }

    private int alphaBeta(Board currentBoard, int d, int a, int b, boolean maxPlayer) {

        Board tempBoard;
        char player;
        //creating the possible boards after a move

        if (d == 0 || currentBoard.hasWinner()) {
            return evaluator.evaluate(currentBoard);
        }

        if (maxPlayer) {

            player = 'P';
            int val = Integer.MIN_VALUE;
            for (int i = 0; i < Board.W; i++) {
                tempBoard = currentBoard.clone();
                if (tempBoard.canAdd(i)) {
                    tempBoard.add(i, player);
                    val = Math.max(val, alphaBeta(tempBoard, d - 1, a, b, !maxPlayer));
                    a = Math.max(a, val);
                    if (b <= a) {
                        break;
                    }
                }
            }
            return val;
        } else {
            player = 'O';
            int val = Integer.MAX_VALUE;
            for (int i = 0; i < Board.W; i++) {
                tempBoard = currentBoard.clone();
                if (tempBoard.canAdd(i)) {
                    tempBoard.add(i, player);
                    val = Math.min(val, alphaBeta(tempBoard, d - 1, a, b, !maxPlayer));
                    b = Math.min(b, val);
                    if (b <= a) {
                        break;
                    }
                }
            }
            return val;
        }

    }

}
