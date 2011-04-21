package edu.albany.othello;

import java.util.Random;
import java.util.Set;

public class RandomBot extends Bot {

	public RandomBot() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Move nextMove(BoardState bs) {
		Random r = new Random();
		Move[] moves = (Move[]) bs.getValidMoves().toArray();
		return moves[r.nextInt(moves.length)];
	}

}
