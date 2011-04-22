package edu.albany.othello;

import java.util.Map;

public class OthelloController {
    private Map<Piece, Player> players;
    
    public OthelloController(Map<Piece, Player> players) {
        this.players = players;
    }

    public void update() {
        Player pl = players.get(OthelloApplication.model.getCurrentPiece());
        Move m = pl.getBestMove();
        
        System.out.println(String.format("%d, %d", m.getR(), m.getC()));
        
        try {
            OthelloApplication.model.makeMove(m.getR(), m.getC());
        }
        catch (IndexOutOfBoundsException ex) {
            System.err.println("Out of bounds!");
        }
        catch (IllegalArgumentException ex) {
            System.err.println("Bad move!");
        }
    }
}
