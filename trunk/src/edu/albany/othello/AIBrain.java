package edu.albany.othello;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.albany.othello.bots.Bot;

//import java.util.Map.Entry;

public class AIBrain extends Player {
	// holds the bot and weight pair
	Map<Bot, Double> botList = new HashMap<Bot, Double>();
	static final int maxDepth = 5;

	public Move getBestMove() {
		HashSet<HashMap<Move, Double>> moveConfidenceSet = new HashSet<HashMap<Move, Double>>();
		// for each bot
		for (Bot b : botList.keySet()) {
			// all (move, confidence) pairs for b
			HashMap<Move, Double> moveConfidences = b.getMoveConfidences(
					getBoardState(), getDeepestBoardStates());
			// for each (move, confidence) pair for b
			for (Entry<Move, Double> value : moveConfidences.entrySet()) {
				// compute the weighted conf
				value.setValue(value.getValue() * botList.get(b));
				// HashSet<Move, Double> moveWeightedConfidence;
			}
			// by this point, moveConfidences holds the weighted confs for b
			// moveConfidences: (m1, wc1) (m2, wc2)

			// add moveConfidences to the set, where each element in the set
			// represents one bot
			moveConfidenceSet.add(moveConfidences);
		}
		// by this point, all bots added their hashmaps to the set
		// moveConfidenceSet: ((m1, w1c1) (m2, w1c2)) ((m1, w2c1) (m2, w2c2))
		// ((m1, w3c1) (m2, w3c2))

		// we need to find the total weights

		// create the new HashMap
		// (move1, sum1) (move2, sum2) (move3, sum3)
		HashMap<Move, Double> moveWeightedConfidenceSums = new HashMap<Move, Double>();

		// for each key in the first HashMap
		for (Move m : (moveConfidenceSet.iterator().next()).keySet()) {
			// calculate the totalValue for the current key
			Double totalValue = 0.0;
			// for each HashMap in moveConfidenceSet
			for (HashMap<Move, Double> botHashMap : moveConfidenceSet) {
				// add the value to the running total
				totalValue += botHashMap.get(m);
			}
			// add the (move, totalValue) to the new sum set
			moveWeightedConfidenceSums.put(m, totalValue);

		}
		// by this point moveWeightedConfidenceSums should contain each move
		// once with the weighted sum

		// computes the best move given all moves and their total
		// holds the highest (move, total) pair
		Map.Entry<Move, Double> currentHighestPair = null;
		for (Map.Entry<Move, Double> entry : moveWeightedConfidenceSums
				.entrySet()) {
			if (currentHighestPair == null
					|| entry.getValue() > currentHighestPair.getValue()) {
				currentHighestPair = entry;
			}
		}
		return currentHighestPair.getKey();
	}

	public AIBrain() {
		// TODO Auto-generated constructor stub
		// TODO fill in botList
		botList = new HashMap<Bot, Double>();
	}

	//return the list of board states
	private Map<Move, Set<BoardState>> getDeepestBoardStatesRecursive(){
		
		return null;
	}
	
	private Map<Piece, Map<Move, Set<BoardState>>> getDeepestBoardStates() {
		BoardState currentBoardState = getBoardState();
		Map<Piece, Map<Move, Set<BoardState>>> deepestBoardState;
		for (int i = 0; i < maxDepth; i++) {
			Set<Move> moves = currentBoardState.getValidMoves(getPiece());
			// for every possible move
			for (Move m : moves) {
				// get the board for the move
				getBoardState().getBoardFromMove(m);
			}
		}
		return null;
	}

}
