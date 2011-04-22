package edu.albany.othello;

import java.util.Random;

public class RandomBot extends Bot {

	public RandomBot(Piece p) {
		super(p);
	}

	@Override
	public Move nextMove(BoardState bs) {
		Random r = new Random();
		Move[] moves = (Move[]) bs.getValidMoves(p).toArray();
		return moves[r.nextInt(moves.length)];
	}

}
