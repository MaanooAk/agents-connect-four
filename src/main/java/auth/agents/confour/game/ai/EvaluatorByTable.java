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
public class EvaluatorByTable extends Evaluator {
    
    /**
      *
      * The evaluation table appoints a value to each position of the board,
      * depending on the amount of possible 4 in rows that position can take part in.
      *
      * i.e. the 3 in the upper left corner is for one each of horizontal, vertical, and diagonal lines of four which can be made with it
      * i.e. the 4 beside it is for two horizontal (one including starting in the corner, one starting on it), one vertical, and one diagonal
      *
      * The following is a visual depiction of the evaluation table.
      * evalTable = {{3,  4,  5,  7,  5, 4, 3},
      *              {4,  6,  8, 10,  8, 6, 4},
      *              {5,  8, 11, 13, 11, 8, 5},
      *              {5,  8, 11, 13, 11, 8, 5},
      *              {4,  6,  8, 10,  8, 6, 4},
      *              {3,  4,  5,  7,  5, 4, 3}}
      *
      * The sum of the values in the table is 276.
      *
      *
      * As for the evalTable variable below,
      * the first array level is the x axis, the second level is the y axis
      * [WIDTH][HEIGHT}
      */
    private final char[][] evalTable = {{3,  4,  5,  5,  4, 3},
                                        {4,  6,  8,  8,  6, 4},
                                        {5,  8, 11, 11,  8, 5},
                                        {7, 10, 13, 13, 10, 7},
                                        {5,  8, 11, 11,  8, 5},
                                        {4,  6,  8,  8,  6, 4},
                                        {3,  4,  5,  5,  4, 3}};

    
    public EvaluatorByTable() {
        score = 0;
    }
    
    /**
     * Gives an evaluation of the state of the board based on the evalTable.
     * This method only takes into account the position of each players discs on the board.
     * for more info see the comment section above the evalTable.
     *
     * @param board the current state of the board
     * @return the evaluation score
     */
    @Override
    public int evaluate(Board board) {

        Board tBoard = board.clone();
        //checking for win or loss scenario
        if (tBoard.hasWinner()) {
            score = checkWinner(tBoard);
        } else {
            //evaluating by using the evalTable
            for (int w=0;w<Board.W;w++) {
                for (int h=0;h<Board.H;h++) {
                    if (tBoard.get(w,h) == Board.P)
                        score += evalTable[w][h];
                    else if (tBoard.get(w,h) == Board.O)
                        score -= evalTable[w][h];
                }
            }
        }
        return score;
    }
    
}
