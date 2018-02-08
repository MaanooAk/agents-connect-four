package auth.agents.confour.game.ai;

import auth.agents.confour.game.Board;

/**
 *
 * @author peron
 */
public class ABEngine implements IEngine {
    
    private int depth = 3;
    private int alpha = Integer.MIN_VALUE;
    private int beta = Integer.MAX_VALUE;
    
    @Override
    public int suggest(Board board){
        int nextMove;
        nextMove = alphaBeta(board, depth, alpha, beta, true);
        return nextMove;
        
    }
    
    private int alphaBeta(Board currentBoard, int d, int a, int b, boolean maxPlayer ){
        
        
        for (int i=0 ; i<currentBoard.W ; i++){
            char player;
            Board tempBoard=currentBoard.clone();
            if(tempBoard.canAdd(i)){
                if(maxPlayer){
                    player='P';
                }else{
                    player='O';
                }
                tempBoard.add(i, player);
            }
        }
        return 0;
    }
    
}
