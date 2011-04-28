package edu.albany.othello;

import java.util.HashMap;
import java.util.Map;

import edu.albany.othello.bots.Bot;
import edu.albany.othello.bots.RandomBot;

public class DemoOthelloApplication {
	public static OthelloModel model;
	public static OthelloView view;
	public static OthelloController controller;

	public static void main(String[] args) {
		Map<Piece, Player> players = new HashMap<Piece, Player>();

		players.put(Piece.WHITE, new Human(Piece.WHITE));
		players.put(Piece.WHITE, new AIBrain(Piece.WHITE));

		model = new OthelloModel();
		view = new OthelloSwingView();
		controller = new OthelloController(players);

		Piece winningPiece = controller.playGame();
	}
}
