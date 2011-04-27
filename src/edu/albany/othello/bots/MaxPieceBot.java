package edu.albany.othello.bots;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.albany.othello.BoardState;
import edu.albany.othello.Move;
import edu.albany.othello.Piece;

public class MaxPieceBot extends Bot {

	public MaxPieceBot(Piece p) {
		super(p);
	}

	// confidence for a BoardState will be gameDuration * (#pieces owned by
	// player/#pieces owned)
	// by all players
	// gameDuration = #turn/60
	// confidence for a move will be the average confidence for a BoardState
	@Override
	public Map<Move, Double> getMoveConfidences(BoardState bs,
			Map<Piece, Map<Move, Set<BoardState>>> deepestBoardStates) {

		Map<Move, Double> moveConfidences = new HashMap<Move, Double>();
		double gameDuration = (bs.getNumPieces(this.piece) + bs
				.getNumPieces(this.piece.getAlternate())) / 60;

		for (Move m : deepestBoardStates.get(this.piece).keySet()) {

			// get the deep BoardStates for this move
			Set<BoardState> deepestBoardStatesSet = deepestBoardStates.get(
					this.piece).get(m);

			double avgConfidence = 0;

			for (BoardState deepBS : deepestBoardStatesSet) {
				avgConfidence += ((double) deepBS.getNumPieces(this.piece))
						/ (deepBS.getNumPieces(this.piece) + deepBS
								.getNumPieces(this.piece.getAlternate()));
			}
			avgConfidence /= deepestBoardStatesSet.size();
			avgConfidence *= gameDuration;

			moveConfidences.put(m, avgConfidence);
		}
		return moveConfidences;
	}

}
