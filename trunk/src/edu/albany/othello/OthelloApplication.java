package edu.albany.othello;

import java.util.HashMap;
import java.util.Map;

import edu.albany.othello.bots.Bot;
import edu.albany.othello.bots.RandomBot;

public class OthelloApplication {
    public static OthelloModel model;
    public static OthelloView view;
    public static OthelloController controller;
    
    public static void main(String[] args) {
        Map<Piece, Player> players = new HashMap<Piece, Player>();
        
        Map<Bot, Double> randomBot = new HashMap<Bot, Double>();
        randomBot.put(new RandomBot(Piece.BLACK), Double.MAX_VALUE);
        
        players.put(Piece.BLACK, new AIBrain(Piece.BLACK, randomBot));
        //players.put(Piece.WHITE, new Human(Piece.WHITE));
        players.put(Piece.WHITE, new AIBrain(Piece.WHITE));
        
        model = new OthelloModel();
        //view = new OthelloSwingView();
        view = new OthelloTextView(true);
        controller = new OthelloController(players);
        
        Piece winningPiece = controller.playGame();
        System.out.println(winningPiece + " wins!");
    }
}
