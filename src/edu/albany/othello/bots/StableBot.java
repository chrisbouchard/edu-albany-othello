package edu.albany.othello.bots;

import java.util.Map;
import java.util.Set;

import edu.albany.othello.BoardState;
import edu.albany.othello.Move;
import edu.albany.othello.Piece;

public class StableBot extends Bot {

	public StableBot(Piece p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Map<Move, Double> getMoveConfidences(BoardState bs,
			Map<Piece, Map<Move, Set<BoardState>>> deepestBoardStates) {
		// TODO Auto-generated method stub
		return null;
	}

}
