package edu.albany.othello.event;

import java.util.EventObject;

import edu.albany.othello.BoardState;
import edu.albany.othello.Piece;

public class GameOverEvent extends EventObject {

    /**
     * 
     */
    private static final long serialVersionUID = 1666959127143037016L;
    
    private BoardState finalState;
    private Piece winnerPiece;

    public GameOverEvent(Object source, BoardState finalState, Piece winnerPiece) {
        super(source);
        this.finalState = finalState;
        this.winnerPiece = winnerPiece;
    }

    public BoardState getFinalState() {
        return finalState;
    }

    public Piece getWinnerPiece() {
        return winnerPiece;
    }

}
