package edu.albany.othello.bots;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.albany.othello.BoardState;
import edu.albany.othello.Move;
import edu.albany.othello.Piece;

public class AntiMobilityBot extends Bot {
	public AntiMobilityBot(Piece p) {
		super(p);
	}

	@Override
	public HashMap<Move, Double> getMoveConfidences(BoardState bs,
			Map<Piece, Map<Move, Set<BoardState>>> deepestBoardStates) {
		HashMap<Move, Double> moveConfidences = new HashMap<Move, Double>();
		for (Move m : deepestBoardStates.get(this.piece.getAlternate()).keySet()) {
			moveConfidences.put(m, 1-((double) deepestBoardStates.get(this.piece.getAlternate())
					.get(m).size() / bs.getNumPieces(null)));
		}
		return moveConfidences;
	}
	
}
