package edu.albany.othello;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class OthelloModel {
    private BoardState currentBoardState;
    private Piece currentPiece;
    private List<Move> moveList;

    public OthelloModel() {
        currentBoardState = new BoardState();
        currentPiece = Piece.BLACK;
        moveList = new LinkedList<Move>();
    }

    public BoardState getCurrentBoardState() {
        return currentBoardState;
    }

    public Piece getCurrentPiece() {
        return currentPiece;
    }

    public void makeMove(Move m) {
        currentBoardState = currentBoardState.getBoardFromMove(m);
        moveList.add(m);
        currentPiece = currentPiece.getAlternate();
    }

    public int getTurnCounter() {
        return moveList.size();
    }

    public List<Move> getMoveList() {
        return Collections.unmodifiableList(moveList);
    }
}
