package edu.albany.othello;

import java.util.HashSet;
import java.util.Set;

public class BoardState {
    private Piece[][] board = new Piece[8][8];
    
    public Set<Move> getValidMoves() {
        Set<Move> s = new HashSet<Move>();
        
        for (int r = 0; r < board.length; ++r) {
            for (int c = 0; c < board[r].length; ++c) {
                if (false) {
                    s.add(new Move(board[r][c], r, c));
                }
            }
        }
        
        return s;
    }
    
    public BoardState getBoardFromMove(Move m) {
        return null;
    }
}
