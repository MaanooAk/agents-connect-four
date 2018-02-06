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
                        if (tBoard.get(w,h) == Board.P)
                            score += evalTable[w][h];
                        else if (tBoard.get(w,h) == Board.O)
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
    
    /**
     * Gives an evaluation of the state of the board based on the amount of Ps and Os in every segment
     * a win by P has a value of +512,
     * a win by O has a value of -512,
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
     * @param board the current board
     * @param x the column to add the token
     * @return the evaluation score
     */
    public int evaluate3(Board board, int x) {
        score = 0;
        if (board.canAdd(x)) {
            Board tBoard = board.clone();
            tBoard.add(x, Board.P);
            //checking for win or loss scenario
            if (tBoard.hasWinner()) {
                if (tBoard.getWinner() == Board.P) {
                    score = 512;
                } else if (tBoard.getWinner() == Board.O) {
                    score = -512;
                } //else it's a draw, score remains 0
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
            if (direction == 'R'){
                x += i;
            }
            else if(direction == 'U'){
                y += i;
            }
            else if (direction == 'C'){
                x += i;
                y += i;
            }
            else if (direction == 'D'){
                x -= i;
                y += i;
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
