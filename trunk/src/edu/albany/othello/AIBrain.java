package edu.albany.othello;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AIBrain {
	//holds the bot and weight pair
	Map<Bot, Double> botList = new HashMap<Bot, Double>();

	public AIBrain() {
		// TODO Auto-generated constructor stub
		// TODO fill in botList
		botList = new HashMap<Bot, Double>();
	}

	private BoardState getBoardState() {
		return null;
	}

	// returns the set of confidences for each bot
	private Set<MoveConfidence> getMoveConfidenceSet() {
		Set<MoveConfidence> moveConfidenceSet = new Set<MoveConfidence>();
		for (Bot b : botList.keySet()){
			MoveConfidence mc = b.getMoveConfidences(getBoardState());
			moveConfidenceSet.add(mc);
		}
			
		return moveConfidenceSet;
	}

	private MoveConfidence getTotalConfidence() {
		return null;
	}

	public Move getHighestConfidenceMove() {

		return null;
	}

}
