package edu.albany.othello;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class OthelloModel {
    private BoardState currentBoardState;
    private Piece currentPiece;
    private List<Move> moveList;

    public OthelloModel() {
        initialize();
    }

    public BoardState getCurrentBoardState() {
        return currentBoardState;
    }

    public Piece getCurrentPiece() {
        return currentPiece;
    }

    public List<Move> getMoveList() {
        return Collections.unmodifiableList(moveList);
    }

    public int getTurnCounter() {
        return moveList.size();
    }
    
    public void initialize() {
        currentBoardState = new BoardState();
        currentPiece = Piece.BLACK;
        moveList = new LinkedList<Move>();
    }

    public void makeMove(int r, int c) {
        Move m = new Move(currentPiece, r, c);
        currentBoardState = currentBoardState.getBoardFromMove(m);
        moveList.add(m);
        currentPiece = currentPiece.getAlternate();
    }
}
