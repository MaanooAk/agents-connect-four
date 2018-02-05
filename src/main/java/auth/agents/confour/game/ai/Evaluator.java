package auth.agents.confour.game.ai;

import auth.agents.confour.game.Board;

/**
 * An evaluator class that contains some evaluation methods on the state of the board
 *
 */
public class Evaluator {

    private int score;

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

    public Evaluator() {
        score = 0;
    }

    /**
     * Gives an evaluation of the state of the board based on the evalTable.
     * This method only takes into account the position of each players discs on the board.
     * for more info see the comment section above the evalTable.
     *
     * @return the evaluation score
     */
    public int evaluate1(Board board, int x) {

        if (board.canAdd(x)) {
            Board tBoard = board.clone();
            tBoard.add(x, Board.P);
            //checking for win or loss scenario
            if (tBoard.hasWinner()) {
                if (tBoard.getWinner() == Board.P) {
                    score = 1000;
                } else if (tBoard.getWinner() == Board.O) {
                    score = -1000;
                } //else it's a draw, score=0
            } else {
                //evaluating by using the evalTable
                for (int w=0;w<Board.W;w++) {
                    for (int h=0;h<Board.H;h++) {
                        if (board.get(w,h) == Board.P)
                            score += evalTable[w][h];
                        else if (board.get(w,h) == Board.O)
                            score -= evalTable[w][h];
                    }
                }
            }
        }

        return score;
    }

    /**
     * Gives an evaluation of the state of the board based on the amount of X in a row you and your opponent have.
     *
     * Adding to longer rows is favored over shorter ones
     *
     * @return the evaluation score
     */
    public int evaluate2(Board board, int x) {

        //weights for 0,1,2 and 3 in a row
        int[] weights = {0,0,1,4};

        if (board.canAdd(x)) {
            Board tBoard = board.clone();
            tBoard.add(x, Board.P);
            //checking for win or loss scenario
            if (tBoard.hasWinner()) {
                if (tBoard.getWinner() == Board.P) {
                    score = 1000;
                } else if (tBoard.getWinner() == Board.O) {
                    score = -1000;
                } //else it's a draw, score=0
            } else {
                //evaluating by counting the X in a row
                //------------------------------------------------
                //checking the columns
                for (int w=0;w<Board.W;w++) {
                    int countP = 0;
                    int countO = 0;
                    for (int h=0;h<Board.H;h++) {
                        if (board.get(w,h) == Board.P) {
                            score -= countO * weights[countO];
                            countO = 0;
                            countP += 1;
                        }
                        else if (board.get(w,h) == Board.O) {
                            score += countP * weights[countP];
                            countP = 0;
                            countO += 1;
                        }
                        else {
                            score += countP * weights[countP];
                            countP = 0;
                            score -= countO * weights[countO];
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
                        if (board.get(w,h) == Board.P) {
                            score -= countO * weights[countO];
                            countO = 0;
                            countP += 1;
                        }
                        else if (board.get(w,h) == Board.O) {
                            score += countP * weights[countP];
                            countP = 0;
                            countO += 1;
                        }
                        else {
                            score += countP * weights[countP];
                            countP = 0;
                            score -= countO * weights[countO];
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

                        if (board.get(w+d,h+d) == Board.P) {
                            score -= countO * weights[countO];
                            countO = 0;
                            countP += 1;
                        }
                        else if (board.get(w+d,h+d) == Board.O) {
                            score += countP * weights[countP];
                            countP = 0;
                            countO += 1;
                        }
                        else {
                            score += countP * weights[countP];
                            countP = 0;
                            score -= countO * weights[countO];
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

                        if (board.get(0+d,h+d) == Board.P) {
                            score -= countO * weights[countO];
                            countO = 0;
                            countP += 1;
                        }
                        else if (board.get(0+d,h+d) == Board.O) {
                            score += countP * weights[countP];
                            countP = 0;
                            countO += 1;
                        }
                        else {
                            score += countP * weights[countP];
                            countP = 0;
                            score -= countO * weights[countO];
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

                        if (board.get(w-d,h+d) == Board.P) {
                            score -= countO * weights[countO];
                            countO = 0;
                            countP += 1;
                        }
                        else if (board.get(w-d,h+d) == Board.O) {
                            score += countP * weights[countP];
                            countP = 0;
                            countO += 1;
                        }
                        else {
                            score += countP * weights[countP];
                            countP = 0;
                            score -= countO * weights[countO];
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

                        if (board.get(Board.H - d,h+d) == Board.P) {
                            score -= countO * weights[countO];
                            countO = 0;
                            countP += 1;
                        }
                        else if (board.get(Board.H - d,h+d) == Board.O) {
                            score += countP * weights[countP];
                            countP = 0;
                            countO += 1;
                        }
                        else {
                            score += countP * weights[countP];
                            countP = 0;
                            score -= countO * weights[countO];
                            countO = 0;
                        }
                    }
                }
            }
        }

        return score;
    }

}
