package edu.albany.othello.bots;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.albany.othello.BoardState;
import edu.albany.othello.Move;
import edu.albany.othello.MoveConfidence;
import edu.albany.othello.Piece;

public class MaxPieceBot extends Bot {

	public MaxPieceBot(Piece p) {
		super(p);
	}

	// confidence for a BoardState will be #pieces owned by player/#pieces owned
	// by all players
	// confidence for a move will be the average confidence for a BoardState
	@Override
	public Map<Move, Double> getMoveConfidences(BoardState bs,
			Map<Piece, Map<Move, Set<BoardState>>> deepestBoardStates) {
		Map<Move, Double> moveConfidences = new HashMap<Move, Double>();
		for (Move m : deepestBoardStates.get(this.piece).keySet()) {

			Map<Move, Set<BoardState>> z = deepestBoardStates.get(this.piece);
			// get the deep BoardStates for this move
			Set<BoardState> deepestBoardStatesSet = deepestBoardStates.get(
					this.piece).get(m);

			System.out.println("BoardStates: " + deepestBoardStates.toString());

			double avgConfidence = 0;

			for (BoardState deepBS : deepestBoardStatesSet) {
				System.out.println("foobar");
				avgConfidence += ((double) deepBS.getNumPieces(this.piece))
						/ (deepBS.getNumPieces(this.piece) + deepBS
								.getNumPieces(this.piece.getAlternate()));
				System.out.println("this is " + avgConfidence);
			}
			avgConfidence /= deepestBoardStatesSet.size();
			System.out.println("maxpiece says move: " + m + " with confidence "
					+ avgConfidence);

			moveConfidences.put(m, avgConfidence);
		}
		// TODO Auto-generated method stub
		return moveConfidences;
	}

}
