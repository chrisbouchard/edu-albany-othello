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
		HashMap<Move, Double> moveConfidences = new HashMap<Move, Double>();
		// return moves[r.nextInt(moves.length)];
		int random = r.nextInt(moves.length);
		for (int i = 0; i < moves.length; i++) {
			if (i == random)
				moveConfidences.put(moves[i], 1.0);
			else
				moveConfidences.put(moves[i], 0.0);
		}
		return moveConfidences;
	}

}
