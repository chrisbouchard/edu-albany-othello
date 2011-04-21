package edu.albany.othello;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BoardState {
    private Piece[][] board;
    
    public BoardState() {
        board = new Piece[8][8];
    }
    
    private BoardState(Piece[][] board, Move m) {
        //TODO: Add logic to check piece placement
        this.board = Arrays.copyOf(board, board.length);
        this.board[m.getR()][m.getC()] = m.getPiece();
    }
    
    public Set<Move> getValidMoves() {
        Set<Move> s = new HashSet<Move>();
        
        for (int r = 0; r < board.length; ++r) {
            for (int c = 0; c < board[r].length; ++c) {
                //TODO: Add proper condition here
                if (false) {
                    s.add(new Move(board[r][c], r, c));
                }
            }
        }
        
        return s;
    }
    
    public BoardState getBoardFromMove(Move m) {
        return new BoardState(board, m);
    }
}
