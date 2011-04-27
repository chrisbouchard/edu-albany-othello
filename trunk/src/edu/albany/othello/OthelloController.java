package edu.albany.othello;

import java.util.Map;

public class OthelloController {
    private Map<Piece, Player> players;
    private boolean waitingOnMove;

    public OthelloController(Map<Piece, Player> players) {
        waitingOnMove = false;
        this.players = players;
    }

    public void makeMove(Move m) {
        //System.out.println(String.format("%d, %d", m.getR(), m.getC()));

        try {
            OthelloApplication.model.makeMove(m.getR(), m.getC());
            //System.out.println("Ok!");
        }
        catch (IndexOutOfBoundsException ex) {
            //System.out.println("Out of bounds!");
        }
        catch (IllegalArgumentException ex) {
            //System.out.println("Bad move!");
        }
        finally {
            waitingOnMove = false;
        }
    }

    public Piece playGame() {
        OthelloApplication.model.initialize();
        
        while (!OthelloApplication.model.getCurrentBoardState().isGameOver()) {
            if (!waitingOnMove) {
                waitingOnMove = true;
                players.get(OthelloApplication.model.getCurrentPiece())
                        .thinkOfMove();
            }
        }
        
        BoardState finalBoard = OthelloApplication.model.getCurrentBoardState();
        return finalBoard.getWinningPiece();
    }
}
