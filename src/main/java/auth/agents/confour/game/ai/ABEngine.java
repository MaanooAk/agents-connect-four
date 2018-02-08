package auth.agents.confour.game.ai;

import auth.agents.confour.game.Board;
import java.lang.Math;

/**
 *
 * @author peron
 */
public class ABEngine implements IEngine {

    private int depth = 3;
    private int alpha = Integer.MIN_VALUE;
    private int beta = Integer.MAX_VALUE;

    private Evaluator evaluator;

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

        for (int i = 0; i < currentBoard.W; i++) {

            char player;
            //creating the possible boards after a move
            Board tempBoard = currentBoard.clone();
            
            if (tempBoard.canAdd(i)) {
                
                if(d == 0 || tempBoard.hasWinner())
                
                if (maxPlayer) {
                    player = 'P';
                    tempBoard.add(i, player);
                    
                    //???
                    a = Math.max(a, alphaBeta(tempBoard, d - 1, a, b, !maxPlayer));
                    if (b <= a) {
                        break;
                    }
                    //return ?1?!?
                } else {
                    player = 'O';
                    tempBoard.add(i, player);
                    
                    b = Math.min(b, alphaBeta(tempBoard, d - 1, a, b, !maxPlayer));
                    if (b <= a) {
                        break;
                    }
                }
            }

        }
        return 0;
    }

}
