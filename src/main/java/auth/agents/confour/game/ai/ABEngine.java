package auth.agents.confour.game.ai;

import auth.agents.confour.game.Board;

/**
 *
 * @author peron
 */
public class ABEngine implements IEngine {

    private final int alpha = Integer.MIN_VALUE;
    private final int beta = Integer.MAX_VALUE;

    private final Evaluator evaluator;
    private final int depth;

    public ABEngine(Evaluator evaluator) {
        this(evaluator, 3);
    }
    public ABEngine(Evaluator evaluator, int depth) {
        this.evaluator = evaluator;
        this.depth = depth;
    }

    @Override
    public int suggest(Board board) {

        return alphaBeta(board, depth);
    }

    private int alphaBeta(Board board, int d) {

        if (board.hasWinner()) {
            return evaluator.evaluate(board);
        }

        char player = Board.P;

        int maxI = 0;
        int maxScore = Integer.MIN_VALUE;

        for (int i = 0; i < Board.W; i++) {
            if (board.canAdd(i)) {

                Board b = board.clone();
                b.add(i, player);

                int score = -alphaBeta(b, d-1, false);

                if (score > maxScore) {
                    maxScore = score;
                    maxI = i;
                }
            }
        }

        return maxI;
    }

    public int alphaBeta(Board board, int d, boolean maxing) {

        if (d == 0 || board.hasWinner()) {
            return evaluator.evaluate(board);
        }

        char player = maxing ? Board.P : Board.O;

        int maxI = 0;
        int maxScore = Integer.MIN_VALUE;

        for (int i = 0; i < Board.W; i++) {
            if (board.canAdd(i)) {

                Board b = board.clone();
                b.add(i, player);

                int score = -alphaBeta(b, d-1, !maxing);

                if (score > maxScore) {
                    maxScore = score;
                    maxI = i;
                }
            }
        }

        return maxScore;
    }

//    private int alphaBeta(Board currentBoard, int d, int a, int b, boolean maxPlayer) {
//
//        if (d == 0 || currentBoard.hasWinner()) {
//            return evaluator.evaluate(currentBoard);
//        }
//
//        Board tempBoard;
//        char player = maxPlayer ? Board.P : Board.O;
//
//        if (maxPlayer) {
//
//            int val = Integer.MIN_VALUE;
//            for (int i = 0; i < Board.W; i++) {
//                tempBoard = currentBoard.clone();
//                if (tempBoard.canAdd(i)) {
//                    tempBoard.add(i, player);
//                    val = Math.max(val, alphaBeta(tempBoard, d - 1, a, b, !maxPlayer));
//                    a = Math.max(a, val);
//                    if (b <= a) {
//                        break;
//                    }
//                }
//            }
//            return val;
//        } else {
//
//            int val = Integer.MAX_VALUE;
//            for (int i = 0; i < Board.W; i++) {
//                tempBoard = currentBoard.clone();
//                if (tempBoard.canAdd(i)) {
//                    tempBoard.add(i, player);
//                    val = Math.min(val, alphaBeta(tempBoard, d - 1, a, b, !maxPlayer));
//                    b = Math.min(b, val);
//                    if (b <= a) {
//                        break;
//                    }
//                }
//            }
//            return val;
//        }
//
//    }

}
