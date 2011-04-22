package edu.albany.othello;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MobilityBot extends Bot {
	public MobilityBot(Piece p) {
		super(p);
	}

	@Override
	public HashMap<Move, Double> getMoveConfidences(BoardState bs,
			Map<Piece, Map<Move, Set<BoardState>>> deepestBoardStates) {
		HashMap<Move, Double> moveConfidences = new HashMap<Move, Double>();
		for (Move m: deepestBoardStates.get(this.p).keySet()){
			moveConfidences.put(m, ((double)deepestBoardStates.get(this.p).get(m).size()/bs.getNumPieces(null)));
		}
		return moveConfidences;
	}
}
