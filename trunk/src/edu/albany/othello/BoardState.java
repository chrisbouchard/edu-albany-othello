package edu.albany.othello;

import java.util.HashSet;
import java.util.Set;

public class BoardState {
    // Size of the board
    public static int ROWS = 8;
    public static int COLS = 8;

    // Convenience method to check if a square is on the board
    public static Boolean isInBounds(int r, int c) {
        return r >= 0 && r < ROWS && c >= 0 && c < COLS;
    }

    public static void main(String[] args) {
        BoardState bs = new BoardState();
        System.out.println(bs);
        System.out.println(bs.getValidMoves(Piece.BLACK));
        System.out.println(bs.getBoardFromMove(new Move(Piece.BLACK, 2, 3))
                .getValidMoves(Piece.WHITE));
        System.out.println(bs.getBoardFromMove(new Move(Piece.BLACK, 2, 3))
                .getBoardFromMove(new Move(Piece.WHITE, 4, 2)));
    }

    private Piece[][] board;

    // Create a default board state
    public BoardState() {
        board = new Piece[ROWS][COLS];
        board[ROWS / 2 - 1][COLS / 2 - 1] = Piece.WHITE;
        board[ROWS / 2 - 1][COLS / 2] = Piece.BLACK;
        board[ROWS / 2][COLS / 2 - 1] = Piece.BLACK;
        board[ROWS / 2][COLS / 2] = Piece.WHITE;
    }

    // Create a new board state based on a given state
    private BoardState(BoardState parent, Move m) {
        // Check that the new move is in bounds and is legal
        if (!isInBounds(m.getR(), m.getC())) {
            throw new IndexOutOfBoundsException();
        }
        
        if (parent.board[m.getR()][m.getC()] != null) {
            throw new IllegalArgumentException();
        }

        board = new Piece[ROWS][COLS];

        for (int r = 0; r < ROWS; ++r) {
            for (int c = 0; c < COLS; ++c) {
                board[r][c] = parent.board[r][c];
            }
        }

        board[m.getR()][m.getC()] = m.getPiece();
    }

    // Check if a piece of the given color could capture a piece if placed here
    private Boolean canCapture(Piece p, int r, int c) {
        for (int dr = -1; dr <= 1; ++dr) {
            for (int dc = -1; dc <= 1; ++dc) {
                if (canCaptureDirected(p, r + dr, c + dc, dr, dc)) {
                    return true;
                }
            }
        }

        return false;
    }

    private Boolean canCaptureDirected(Piece p, int r, int c, int dr, int dc) {
        // Check if this square is in bounds
        if (!isInBounds(r, c) || board[r][c] == null) {
            return false;
        }

        // Check if the next square is in bounds
        if (!isInBounds(r + dr, c + dc) || board[r + dr][c + dc] == null) {
            return false;
        }

        if (board[r][c] != p && board[r + dr][c + dc] == p) {
            return true;
        }

        return canCaptureDirected(p, r + dr, c + dc, dr, dc);
    }

    public BoardState getBoardFromMove(Move m) {
        return new BoardState(this, m);
    }

    public Set<Move> getValidMoves(Piece p) {
        Set<Move> s = new HashSet<Move>();

        for (int r = 0; r < ROWS; ++r) {
            for (int c = 0; c < COLS; ++c) {
                if (board[r][c] == null && canCapture(p, r, c)) {
                    s.add(new Move(p, r, c));
                }
            }
        }

        return s;
    }

    @Override
    public String toString() {
        String str = " ";

        for (int c = 0; c < COLS; ++c) {
            str += " " + c;
        }
        for (int r = 0; r < ROWS; ++r) {
            str += "\n" + r;

            for (int c = 0; c < COLS; ++c) {
                if (board[r][c] == null) {
                    str += "  ";
                }
                else {
                    switch (board[r][c]) {
                    case WHITE:
                        str += " W";
                        break;

                    case BLACK:
                        str += " B";
                        break;

                    default:
                        str += "  ";
                        break;
                    }
                }
            }
        }

        return str;
    }
}
