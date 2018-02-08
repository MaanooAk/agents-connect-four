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
    
    public EvaluatorByGroupCount() {
        score = 0;
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
        

        //weights for 0,1,2 and 3 in a row
        int[] weights = {0,0,1,4};

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
                    int countP = 0;
                    int countO = 0;
                    for (int h=0;h<Board.H;h++) {
                        if (tBoard.get(w,h) == Board.P) {
                            score -= weights[countO];
                            countO = 0;
                            countP += 1;
                        }
                        else if (tBoard.get(w,h) == Board.O) {
                            score += weights[countP];
                            countP = 0;
                            countO += 1;
                        }
                        else {
                            score += weights[countP];
                            countP = 0;
                            score -= weights[countO];
                            countO = 0;
                        }
                    }
                }
                //------------------------------------------------
                //checking the rows
                for (int h=0;h<Board.H;h++) {
                    int countP = 0;
                    int countO = 0;
                    for (int w=0;w<Board.W;w++) {
                        if (tBoard.get(w,h) == Board.P) {
                            score -= weights[countO];
                            countO = 0;
                            countP += 1;
                        }
                        else if (tBoard.get(w,h) == Board.O) {
                            score += weights[countP];
                            countP = 0;
                            countO += 1;
                        }
                        else {
                            score += weights[countP];
                            countP = 0;
                            score -= weights[countO];
                            countO = 0;
                        }
                    }
                }
                //------------------------------------------------
                //checking the counter diagonals
                //all the diagonals starting from the bottom row
                for (int w=0;w<Board.W-3;w++) {
                    int countP = 0;
                    int countO = 0;
                    int h = 0;

                    for (int d=0;d<Board.H-1;d++) {
                        if ((w+d > Board.W) || (h+d > Board.H))
                            continue;

                        if (tBoard.get(w+d,h+d) == Board.P) {
                            score -= weights[countO];
                            countO = 0;
                            countP += 1;
                        }
                        else if (tBoard.get(w+d,h+d) == Board.O) {
                            score += weights[countP];
                            countP = 0;
                            countO += 1;
                        }
                        else {
                            score += weights[countP];
                            countP = 0;
                            score -= weights[countO];
                            countO = 0;
                        }
                    }
                }
                //the rest of the counter diagonals, that start from the first column
                for (int h=1;h<Board.H-3;h++) {
                    int countP = 0;
                    int countO = 0;

                    for (int d=0;d<Board.H-2;d++) {
                        if ((0+d > Board.W) || (h+d > Board.H))
                            continue;

                        if (tBoard.get(0+d,h+d) == Board.P) {
                            score -= weights[countO];
                            countO = 0;
                            countP += 1;
                        }
                        else if (tBoard.get(0+d,h+d) == Board.O) {
                            score += weights[countP];
                            countP = 0;
                            countO += 1;
                        }
                        else {
                            score += weights[countP];
                            countP = 0;
                            score -= weights[countO];
                            countO = 0;
                        }
                    }
                }
                //------------------------------------------------
                //checking the main diagonals
                //all the diagonals starting from the bottom row
                for (int w=Board.W-1;w>2;w--) {
                    int countP = 0;
                    int countO = 0;
                    int h = 0;

                    for (int d=0;d<Board.H-1;d++) {
                        if ((w-d > Board.W) || (h+d > Board.H))
                            continue;

                        if (tBoard.get(w-d,h+d) == Board.P) {
                            score -= weights[countO];
                            countO = 0;
                            countP += 1;
                        }
                        else if (tBoard.get(w-d,h+d) == Board.O) {
                            score += weights[countP];
                            countP = 0;
                            countO += 1;
                        }
                        else {
                            score += weights[countP];
                            countP = 0;
                            score -= weights[countO];
                            countO = 0;
                        }
                    }
                }
                //the rest of the main diagonals, that start from the last column
                for (int h=1;h<Board.H-3;h++) {
                    int countP = 0;
                    int countO = 0;

                    for (int d=0;d<Board.H-2;d++) {
                        if ((Board.H - d > Board.W) || (h+d > Board.H))
                            continue;

                        if (tBoard.get(Board.H - d,h+d) == Board.P) {
                            score -= weights[countO];
                            countO = 0;
                            countP += 1;
                        }
                        else if (tBoard.get(Board.H - d,h+d) == Board.O) {
                            score += weights[countP];
                            countP = 0;
                            countO += 1;
                        }
                        else {
                            score += weights[countP];
                            countP = 0;
                            score -= weights[countO];
                            countO = 0;
                        }
                    }
                }
            }
        }

        return score;
    }
}
