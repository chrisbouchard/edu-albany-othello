package edu.albany.othello;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AIBrain {
	// holds the bot and weight pair
	Map<Bot, Double> botList = new HashMap<Bot, Double>();

	public AIBrain() {
		// TODO Auto-generated constructor stub
		// TODO fill in botList
		botList = new HashMap<Bot, Double>();
	}

	private BoardState getBoardState() {
		return null;
	}

	/**
	 * 
	 * @return Set of HashMaps of Move, confidence pairs for each bot
	 */
	private Set<HashMap<Move, Double>> getMoveConfidenceSet() {
		Set<HashMap<Move, Double>> moveConfidenceSet = new HashSet<HashMap<Move, Double>>();
		/*for (Bot b : botList.keySet()) {
			MoveConfidence mc = b.getMoveConfidences(getBoardState());
			moveConfidenceSet.add(mc);
		}
*/
		return moveConfidenceSet;
	}

	/**
	 * 
	 * @return HashMap of Move, total confidence pairs
	 */
	private HashMap<Move, Double> getTotalConfidence(
			Set<HashMap<Move, Double>> moveConfidenceSet) {
		// TODO
		return null;
	}

	/**
	 * 
	 * @return Move with the highest total confidence
	 */
	public Move getHighestConfidenceMove(
			HashMap<Move, Double> moveTotalConfidence) {
		// TODO
		return null;
	}

}
