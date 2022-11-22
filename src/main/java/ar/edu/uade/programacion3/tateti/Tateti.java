package ar.edu.uade.programacion3.tateti;


import java.util.ArrayList;
import java.util.List;

public class Tateti {
    private final int[][] board;
    private final int EMPTY_CELL = 0;
    private final int MAXIMIZER_CELL = 1;
    private final int MINIMIZER_CELL = 2;

    private Tateti() {
        board = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = EMPTY_CELL;
            }
        }
    }
    public static Tateti inicializar() {
        return new Tateti();
    }

    public void turno(Player player) {
        if (player.equals(Player.COMPUTER)) {
            computerTurn();
            printBoard();
        }
    }

    public boolean jugar(Move move) {
        addCell(move, MAXIMIZER_CELL);
        System.out.println("Movimiento Jugador");
        printBoard();
        boolean ended = gameEnded();
        if (ended) return true;
        computerTurn();
        System.out.println("Movimiento Computadora");
        printBoard();
        return gameEnded();
    }

    private void computerTurn() {
        Move move = computerMove();
        addCell(move, MINIMIZER_CELL);
    }

    private Move computerMove() {
        int bestScore = Integer.MAX_VALUE;
        Move bestMove = null;
        for (Move move : getPossibleMoves(board)) {
            addCell(move, MINIMIZER_CELL);
            int score = minMax(board, true);
            undoCell(move);
            if (score < bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        System.out.println("score " + bestScore);
        if (bestScore >= 100) {
            System.out.println("found winning move " + bestScore + " " + bestMove);
        }
        return bestMove;
    }

    private int minMax(int[][] board, boolean isMax) {
        if (isBoardFull(board)) return 0;
        int winner = evaluate();
        if (winner != 0) return winner;

        int best;
        if (isMax) {
            best = Integer.MIN_VALUE;
            for (Move move : getPossibleMoves(board)) {
                addCell(move, 1);
                best = Math.max(best, minMax(board, false));
                undoCell(move);
            }
        } else {
            best = Integer.MAX_VALUE;
            for (Move move : getPossibleMoves(board)) {
                addCell(move, 2);
                best = Math.min(best, minMax(board, true));
                undoCell(move);
            }
        }
        return best;
    }

    private boolean gameEnded() {
        int winner = getWinner();
        if (winner != 0) {
            String val = winner == MAXIMIZER_CELL ? "Jugador" : "Computadora";
            System.out.println("El ganador es: " + val);
            return true;
        }
        if (isBoardFull(board)) {
            System.out.println("Empate");
            return true;
        }
        return false;
    }

    private static boolean isBoardFull(int[][] board) {
        return getPossibleMoves(board).isEmpty();
    }

    private int getWinner() {
        if (board[1][1] != EMPTY_CELL
                && ((board[0][0] == board[1][1] && board[1][1] == board[2][2])
                || (board[0][2] == board[1][1] && board[1][1] == board[2][0]))) {
            return board[1][1];
        }
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != EMPTY_CELL && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {// mirar filas
                 return board[i][0];
            }
            if (board[0][i] != EMPTY_CELL && board[0][i] == board[1][i] && board[1][i] == board[2][i]) { // mirar columnas
                return board[0][i];
            }
        }
        return 0;
    }

    private void addCell(Move move, int value) {
        if (board[move.getRow()][move.getCol()] != 0) throw new RuntimeException("non empty cell");
        board[move.getRow()][move.getCol()] = value;
    }

    private void undoCell(Move move) {
        board[move.getRow()][move.getCol()] = 0;
    }

    private static List<Move> getPossibleMoves(int[][] board) {
       List<Move> moves = new ArrayList<>();
       for (int i = 0; i < 3; i++) {
           for (int j = 0; j < 3; j++) {
               if (board[i][j] == 0) {
                   moves.add(new Move(i, j));
               }
           }
       }
       return moves;
    }

    private void printBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (j == 0) System.out.print(" ");
                String value = board[i][j] == 0 ? "-" : board[i][j] == 1 ? "X" : "O";
                System.out.print(value);
                if (j != 2) {
                    System.out.print(" | ");
                }
            }
            if (i != 2) {
                System.out.print("\n-----------\n");
            }
        }
        System.out.println();
    }

    private int evaluate() {
        int winner = getWinner();
        return winner == 0 ? 0 : winner == MAXIMIZER_CELL ? 10 : -10;
    }
//    private int evaluate() {
//        int score = 0;
//        // Evaluate score for each of the 8 lines (3 rows, 3 columns, 2 diagonals)
//        for (int i = 0; i < 3; i++) {
//            score += evaluateLine(i, 0, i, 1, i, 2);  // row
//            score += evaluateLine(0, i, 1, i, 2, i);  // column
//        }
//        score += evaluateLine(0, 0, 1, 1, 2, 2);  // diagonal
//        score += evaluateLine(0, 2, 1, 1, 2, 0);  // alternate diagonal
//        return score;
//    }
//
//    /*
//     retorna +100, +10, +1 for 3-, 2-, 1-in-a-line for computer.
//     -100, -10, -1 for 3-, 2-, 1-in-a-line for opponent.
//     0 otherwise */
//    private int evaluateLine(int row1, int col1, int row2, int col2, int row3, int col3) {
//        int score;
//        switch (board[row1][col1]) { // First cell
//            case MAXIMIZER_CELL: score = 1; break;
//            case MINIMIZER_CELL: score = -1; break;
//            default: score = 0; break;
//        }
//        // Second cell
//        if (board[row2][col2] == MAXIMIZER_CELL) {
//            if (score == 1) {   // cell1 is MAXIMIZER_CELL
//                score = 10;
//            } else if (score == -1) {  // cell1 is MINIMIZER_CELL
//                return 0;
//            } else {  // cell1 is empty
//                score = 1;
//            }
//        } else if (board[row2][col2] == MINIMIZER_CELL) {
//            if (score == -1) { // cell1 is MINIMIZER_CELL
//                score = -10;
//            } else if (score == 1) { // cell1 is MAXIMIZER_CELL
//                return 0;
//            } else {  // cell1 is empty
//                score = -1;
//            }
//        }
//
//        // Third cell
//        if (board[row3][col3] == MAXIMIZER_CELL) {
//            if (score > 0) {  // cell1 and/or cell2 is mySeed
//                score *= 10;
//            } else if (score < 0) {  // cell1 and/or cell2 is MINIMIZER_CELL
//                return 0;
//            } else {  // cell1 and cell2 are empty
//                score = 1;
//            }
//        } else if (board[row3][col3] == MINIMIZER_CELL) {
//            if (score < 0) {  // cell1 and/or cell2 is MINIMIZER_CELL
//                score *= 10;
//            } else if (score > 0) {  // cell1 and/or cell2 is MAXIMIZER_CELL
//                return 0;
//            } else {  // cell1 and cell2 are empty
//                score = -1;
//            }
//        }
//        return score;
//    }
}
