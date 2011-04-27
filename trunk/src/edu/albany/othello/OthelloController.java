package edu.albany.othello;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.albany.othello.event.GameOverEvent;
import edu.albany.othello.event.GameOverListener;

public class OthelloController {
    private Map<Piece, Player> players;
    private boolean waitingOnMove;

    private Set<GameOverListener> gameOverListeners;

    public OthelloController(Map<Piece, Player> players) {
        waitingOnMove = false;
        this.players = players;
        this.gameOverListeners = new HashSet<GameOverListener>();
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

    public void start() {
        OthelloApplication.model.initialize();
        
        while (!OthelloApplication.model.getCurrentBoardState().isGameOver()) {
            if (!waitingOnMove) {
                waitingOnMove = true;
                players.get(OthelloApplication.model.getCurrentPiece())
                        .thinkOfMove();
            }
        }

        for (GameOverListener listener : gameOverListeners) {
            BoardState finalBoard = OthelloApplication.model
                    .getCurrentBoardState();
            listener.gameOver(new GameOverEvent(this, finalBoard, finalBoard
                    .getWinningPiece()));
        }
    }

    public void addGameOverListener(GameOverListener listener) {
        gameOverListeners.add(listener);
    }

    public void removeGameOverListener(GameOverListener listener) {
        gameOverListeners.remove(listener);
    }
}
