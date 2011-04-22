package edu.albany.othello;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class RandomBot extends Bot {

	public RandomBot(Piece p) {
		super(p);
	}

	@Override
	public HashMap<Move, Double> getMoveConfidences(BoardState bs) {
		Random r = new Random();
		Move[] moves = (Move[]) bs.getValidMoves(p).toArray();
		// return moves[r.nextInt(moves.length)];
		return null;
	}

}
