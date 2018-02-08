/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auth.agents.confour.game.ai;

import auth.agents.confour.game.Board;

/**
 *
 * @author Anastatsia
 */
public class EvaluatorByQuads extends Evaluator {
    
    public EvaluatorByQuads() {
        score = 0;
    }
    
    /**
     * Gives an evaluation of the state of the board based on the amount of Ps and Os in every segment
     * a win by P has a value of +1000,
     * a win by O has a value of -1000,
     * a draw has a value of 0,
     * otherwise, take all possible straight segments on the grid (defined as a set of four slots in a line horizontal, vertical, or diagonal),
     * evaluate each of them according to the rules below, and return the sum of the values over all segments.
     * The rules for evaluating segments are as follows:
     * -50 for three Os, no Ps,
     * -10 for two Os, no Ps,
     * - 1 for one O, no Ps,
     * 0 for no tokens, or mixed Ps and Os,
     * 1 for one P, no Os,
     * 10 for two Ps, no Os,
     * 50 for three Ps, no Os.

     * @param board the current state of the board
     * @param x the players choice
     * @return the evaluation score
     */
    @Override
    public int evaluate(Board board, int x) {
        if (board.canAdd(x)) {
            Board tBoard = board.clone();
            tBoard.add(x, Board.P);
            //checking for win or loss scenario
            if (tBoard.hasWinner()) {
                score = checkWinner(tBoard);
            } else {
                //evaluate move by counting the Ps and Os in every segment of the board

                //horizontal segments
                for (int h=0;h<Board.H;h++){
                    for (int w=0;w<Board.W-3;w++) {
                        score += evaluateSegment(tBoard,w,h,'R');
                    }
                }
                //vertical segments
                for (int w=0;w<Board.W;w++){
                    for (int h=0; h<Board.H-3;h++) {
                        score += evaluateSegment(tBoard,w,h,'U');
                    }
                }

                //counter diagonal segments starting from the bottom left corner
                for (int w=0;w<Board.W-3;w++){
                    for (int h=0; h<Board.H-3;h++) {
                        score += evaluateSegment(tBoard,w,h,'C');
                    }
                }

                //main diagonal segments starting from the bottom right corner
                for (int w=Board.W-1;w>2;w--){
                    for (int h=0; h<Board.H-3;h++) {
                        score += evaluateSegment(tBoard,w,h,'D');
                    }
                }
            }
        }
        return score;
    }
    
    /**
     *
     * @param board the current board
     * @param posX the horizontal position of the first cell of the segment
     * @param posY the vertical position of the first cell of the segment
     * @param direction the direction of the other cells of the segment compared to the first
     *                  R for right (horizontal segments), U for up (vertical segments),
     *                  C for counter-diagonal (counter diagonal segments) and D for diagonal (main diagonal segments)
     * @return the score of the segment (0, 1, 10 or 50)
     */
    private int evaluateSegment(Board board,int posX, int posY, char direction){
        int segmentScore;
        int[] points = {0, 1, 10, 50};
        int countP = 0;
        int countO = 0;
        for (int i=0;i<4;i++){
            int x = posX;
            int y = posY;
            switch (direction) {
                case 'R':
                    x += i;
                    break;
                case 'U':
                    y += i;
                    break;
                case 'C':
                    x += i;
                    y += i;
                    break;
                case 'D':
                    x -= i;
                    y += i;
                    break;
                default:
                    break;
            }
            if (x >= Board.W || y >= Board.H){
                return 0;
            }
            char token = board.get(x,y);
            if (token == Board.P) {
                countP++;
            }
            else if (token == Board.O){
                countO++;
            }
        }
        if (countP == 0){
            segmentScore = - points[countO];
        }
        else if (countO == 0){
            segmentScore = points[countP];
        }
        else{
            //mixed Ps and Os
            segmentScore = 0;
        }
        return segmentScore;
    }
}
