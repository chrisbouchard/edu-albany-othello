package edu.albany.othello;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

//import java.util.Map.Entry;

public class AIBrain {
	// holds the bot and weight pair
	Map<Bot, Double> botList = new HashMap<Bot, Double>();

	public Move getBestestMove() {
		HashSet<HashMap<Move, Double>> moveConfidenceSet = new HashSet<HashMap<Move, Double>>();
		// for each bot
		for (Bot b : botList.keySet()) {
			// all (move, confidence) pairs for b
			HashMap<Move, Double> moveConfidences = b
					.getMoveConfidences(getBoardState());
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
		for (Move m : ((HashMap<Move, Double>) moveConfidenceSet.toArray()[0])
				.keySet()) {
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

	private BoardState getBoardState() {
		// TODO get this info from othello model
		return null;
	}

	/*
	 * bot1: (move1, w1*c1) (move2, w1*c2) (move3, w1*c3)
	 * bot2: (move1, w2*c1) (move2, w2*c2) (move3, w2*c3)
	 * bot3: (move1, w3*c1) (move2, w3*c2) (move3, w3*c3)
	 */

	/**
	 * 
	 * @return Set of HashMaps of Move, confidence pairs for all bots
	 */
	// return (move1, weightedconf_1,1) (move1, weightedconf_1,2) (move2,
	// weightedconf_2,1) (move2, weightedconf_2,2)
	// input:
	// for each bot, get the hashmap (move1, conf1) (move2, conf2) ...
	// then add each hashmap as an element in a set
	// set looks like b1:((m1, c1) (m2, c2)) b2:((m1, c1) (m2, c2)) b3:((m1, c1)
	// (m2, c2))
	private Set<HashMap<Move, Double>> getMoveConfidenceSet() {
		HashSet<HashMap<Move, Double>> moveConfidenceSet = new HashSet<HashMap<Move, Double>>();
		for (Bot b : botList.keySet()) {
			// all move, confidence pairs for b
			moveConfidenceSet.addAll(b.getMoveConfidences(getBoardState()));
		}

		return moveConfidenceSet;
	}

	/**
	 * 
	 * @return HashMap of Move, total confidence pairs
	 */
	// return (move1, sum1) (move2, sum2) (move3, sum3)
	// input: (move1, weightedconf_1,1) (move1, weightedconf_1,2) (move2,
	// weightedconf_2,1) (move2, weightedconf_2,2)
	private HashMap<Move, Double> getMoveWeightedConfidenceSums(
			Set<HashMap<Move, Double>> moveConfidenceSet) {
		//Set<HashMap<Move, Double>> moveConfidenceSet = getMoveConfidenceSet();
		HashMap<Move, Double> moveTotalConfidence = new HashMap<Move, Double>();
		for (HashMap<Move, Double> moveConfidence : moveConfidenceSet){
			moveTotalConfidence.add
		}
		return null;
	}

	/*
	 * 
	 * movetotalconfidence
	 * (move, conf) (move, conf) (move, conf)
	 */

	/**
	 * 
	 * @return Move with the highest total confidence
	 */
	// takes in the (Moves, sum of weighted confidences weighted) and returns
	// the Move with highest weightedconfsum
	// input: (move1, sum1) (move2, sum2) (move3, sum3)
	public Move geBestMove(HashMap<Move, Double> moveWeightedConfidenceSums) {
		// set of all entries
		// Set<Map.Entry<Move, Double>> mtc = moveTotalConfidence.entrySet();
		// the current highest element, starting with 0
		Map.Entry<Move, Double> currentHighestPair = null;// = new
		// HashMap.SimpleEntry<Move,
		// Double>(null,
		// 0.0);
		for (Map.Entry<Move, Double> entry : moveWeightedConfidenceSums
				.entrySet()) {
			if (currentHighestPair == null
					|| entry.getValue() > currentHighestPair.getValue()) {
				currentHighestPair = entry;
			}
		}
		return currentHighestPair.getKey();
	}

}
