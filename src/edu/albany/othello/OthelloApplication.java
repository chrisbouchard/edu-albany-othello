package edu.albany.othello;

import java.util.HashMap;
import java.util.Map;

public class OthelloApplication {
    public static OthelloModel model;
    public static OthelloView view;
    public static OthelloController controller;
    
    public static void main(String[] args) {
        Map<Piece, Player> players = new HashMap<Piece, Player>();
        
        players.put(Piece.BLACK, new Human(Piece.BLACK));
        //players.put(Piece.WHITE, new Human(Piece.WHITE));
        players.put(Piece.WHITE, new AIBrain(Piece.WHITE));
        
        model = new OthelloModel();
        //view = new OthelloSwingView();
        view = new OthelloTextView();
        controller = new OthelloController(players);
        
        model.initialize();
        controller.start();
    }
}
