package edu.albany.othello.bots;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.albany.othello.BoardState;
import edu.albany.othello.Move;
import edu.albany.othello.Piece;

public class MobilityBot extends Bot {
	public MobilityBot(Piece p) {
		super(p);
	}

	@Override
	public HashMap<Move, Double> getMoveConfidences(BoardState bs,
			Map<Piece, Map<Move, Set<BoardState>>> deepestBoardStates) {
		HashMap<Move, Double> moveConfidences = new HashMap<Move, Double>();
		for (Move m : deepestBoardStates.get(this.piece).keySet()) {
			moveConfidences.put(m, ((double) deepestBoardStates.get(this.piece)
					.get(m).size() / bs.getNumPieces(null)));
		}
		return moveConfidences;
	}
}
