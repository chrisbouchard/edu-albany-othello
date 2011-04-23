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

			// get the deep boardstates for this move
			Set<BoardState> deepestBoardStatesSet = deepestBoardStates.get(
					this.piece).get(m);

			double avgConfidence = 0;

			for (BoardState deepBS : deepestBoardStatesSet) {
				avgConfidence += ((double) deepBS.getValidMoves(this.piece)
						.size())
						/ deepBS.getNumPieces(null);
			}
			avgConfidence /= deepestBoardStatesSet.size();

			moveConfidences.put(m, avgConfidence);

		}
		return moveConfidences;
	}
}
