package edu.albany.othello.bots;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import edu.albany.othello.BoardState;
import edu.albany.othello.Move;
import edu.albany.othello.Piece;

public class RandomBot extends Bot {

	public RandomBot(Piece p) {
		super(p);
	}

	@Override
	public HashMap<Move, Double> getMoveConfidences(BoardState bs,
			Map<Piece, Map<Move, Set<BoardState>>> deepestBoardStates) {
		Random r = new Random();
		//Move[] moves = (Move[]) bs.getValidMoves(piece).toArray();
		Move[] moves = (Move[]) bs.getValidMoves(piece).toArray(new Move[0]);
		
		
		//Object[] moveso = (Move[]) bs.getValidMoves(piece).toArray();
		//System.out.println(moveso[0]);
		//System.out.flush();
		//Move[] moves = (Move[]) moveso;
		//Set<Move> moves = bs.getValidMoves(piece);
		HashMap<Move, Double> moveConfidences = new HashMap<Move, Double>();
		// return moves[r.nextInt(moves.length)];
		int random = r.nextInt(moves.length);
		for (int i = 0; i < moves.length; i++) {
			if (i == random)
				moveConfidences.put(moves[i], 1.0);
			else
				moveConfidences.put(moves[i], 0.0);
		}
		return moveConfidences;
	}

}
