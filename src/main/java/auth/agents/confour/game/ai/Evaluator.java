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
public abstract class Evaluator {
    
    protected int score;
    
    /**
     *
     * @param board the current state of the board
     * @return the evaluation score
     */
    public abstract int evaluate(Board board);
    
    protected int checkWinner(Board board) {
        switch (board.getWinner()) {
            case Board.P:
                return 1000;
            case Board.O:
                return -1000;
            default:
                return 0;
        }
    }
            
    
}