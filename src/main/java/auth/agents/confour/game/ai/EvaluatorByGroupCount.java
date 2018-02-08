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
public class EvaluatorByGroupCount extends Evaluator {
    
    //weights for 0,1,2 and 3 in a row
    private int[] weights = {0,0,1,4};
    private int countO;
    private int countP;
    
    public EvaluatorByGroupCount() {
        score = 0;
        countO = 0;
        countP = 0;
    }
    
    /**
     * Gives an evaluation of the state of the board based on the amount of X in a row you and your opponent have.
     *
     * Adding to longer rows is favored over shorter ones
     *
     * @param board the current state of the board
     * @param x the players choice
     * @return the evaluation score
     */
    @Override
    public int evaluate(Board board, int x){

        if (board.canAdd(x)) {
            Board tBoard = board.clone();
            tBoard.add(x, Board.P);
            //checking for win or loss scenario
            if (tBoard.hasWinner()) {
                score = checkWinner(tBoard);
            } else {
                //evaluating by counting the X in a row
                //------------------------------------------------
                //checking the columns
                for (int w=0;w<Board.W;w++) {
                    countP = 0;
                    countO = 0;
                    for (int h=0;h<Board.H;h++) {
                        calculateScore(tBoard, w, h, 0, false);
                    }
                }
                //------------------------------------------------
                //checking the rows
                for (int h=0;h<Board.H;h++) {
                    countP = 0;
                    countO = 0;
                    for (int w=0;w<Board.W;w++) {
                        calculateScore(tBoard, w, h, 0, false);
                    }
                }
                //------------------------------------------------
                //checking the counter diagonals
                //all the diagonals starting from the bottom row
                for (int w=0;w<Board.W-3;w++) {
                    countP = 0;
                    countO = 0;
                    int h = 0;

                    for (int d=0;d<Board.H-1;d++) {
                        calculateScore(tBoard, w, h, d, false);
                    }
                }
                //the rest of the counter diagonals, that start from the first column
                for (int h=1;h<Board.H-3;h++) {
                    countP = 0;
                    countO = 0;

                    for (int d=0;d<Board.H-2;d++) {
                        calculateScore(tBoard, 0, h, d, false);
                    }
                }
                //------------------------------------------------
                //checking the main diagonals
                //all the diagonals starting from the bottom row
                for (int w=Board.W-1;w>2;w--) {
                    countP = 0;
                    countO = 0;
                    int h = 0;

                    for (int d=0;d<Board.H-1;d++) {
                        calculateScore(tBoard, w, h, d, true);
                    }
                }
                //the rest of the main diagonals, that start from the last column
                for (int h=1;h<Board.H-3;h++) {
                    countP = 0;
                    countO = 0;

                    for (int d=0;d<Board.H-2;d++) {
                        calculateScore(tBoard, Board.W - 1, h, 0, true);
                    }
                }
            }
        }

        return score;
    }
    
    /**
     * 
     * @param tBoard current state of the board
     * @param w width of cell
     * @param h height of cell
     * @param dy distance from starting cell
     * @param flag if true, the horizontal distance is negative
     */
    private void calculateScore(Board tBoard, int w, int h, int dy, boolean flag) {
        int dx = dy;
        if (flag == true) {
            dx = -dy;
        }
        if ((w+dx > Board.W) || (h+dy > Board.H))
            return;

        switch (tBoard.get(w+dx,h+dy)) {
            case Board.P:
                score -= weights[countO];
                countO = 0;
                countP += 1;
                break;
            case Board.O:
                score += weights[countP];
                countP = 0;
                countO += 1;
                break;
            default:
                score += weights[countP];
                countP = 0;
                score -= weights[countO];
                countO = 0;
                break;
        }
    }
}
