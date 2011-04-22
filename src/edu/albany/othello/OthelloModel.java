package edu.albany.othello;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.albany.othello.event.UpdateEvent;
import edu.albany.othello.event.UpdateListener;

public class OthelloModel {
    private BoardState currentBoardState;
    private Piece currentPiece;
    private List<Move> moveList;

    private Set<UpdateListener> updateListeners;

    public OthelloModel() {
        updateListeners = new HashSet<UpdateListener>();
        initialize();
    }

    public void addUpdateListener(UpdateListener ul) {
        updateListeners.add(ul);
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
        return moveList.size() + 1;
    }

    public void initialize() {
        currentBoardState = new BoardState();
        currentPiece = Piece.BLACK;
        moveList = new LinkedList<Move>();
        fireUpdateEvents();
    }

    public void makeMove(int r, int c) {
        Move m = new Move(currentPiece, r, c);
        currentBoardState = currentBoardState.getBoardFromMove(m);
        moveList.add(m);

        currentPiece = currentPiece.getAlternate();

        if (!currentBoardState.hasValidMove(currentPiece)) {
            currentPiece = currentPiece.getAlternate();

            if (!currentBoardState.hasValidMove(currentPiece)) {
                for (Piece p : Piece.values()) {
                    System.out.println(p + ": "
                            + currentBoardState.getNumPieces(p));
                }
                System.err.println("Game over!");
                System.exit(0);
            }
        }

        fireUpdateEvents();
    }

    private void fireUpdateEvents() {
        UpdateEvent e = new UpdateEvent(this);
        for (UpdateListener ul : updateListeners) {
            ul.updatePerformed(e);
        }
    }
}
