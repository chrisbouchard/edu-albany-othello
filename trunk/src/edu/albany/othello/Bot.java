package edu.albany.othello;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class Bot {
	protected Piece p;

	public Bot(Piece p) {
		this.p = p;
	}

	// returns set of all move and confidence pairs
	public abstract HashMap<Move, Double> getMoveConfidences(BoardState bs,
			Map<Piece, Map<Move, Set<BoardState>>> deepestBoardStates);
}
