package edu.albany.othello;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.albany.othello.bots.*;

//import java.util.Map.Entry;

public class AIBrain extends Player {
	// holds the bot and weight pair
	Map<Bot, Double> botList;
	static final int maxDepth = 3;

	public Move getBestMove() {
		Set<Map<Move, Double>> moveConfidenceSet = new HashSet<Map<Move, Double>>();
		// for each bot
		for (Bot b : botList.keySet()) {
			// all (move, confidence) pairs for b
			Map<Move, Double> moveConfidences = b.getMoveConfidences(
					getBoardState(), getDeepestBoardStates());
			// for each (move, confidence) pair for b
			for (Entry<Move, Double> value : moveConfidences.entrySet()) {
				// compute the weighted conf
				value.setValue(value.getValue() * botList.get(b));
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
			for (Map<Move, Double> botHashMap : moveConfidenceSet) {
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

	// Creates an AIBrain that uses all bots at predetermined weights
	public AIBrain(Piece p) {
		// TODO Auto-generated constructor stub
		// TODO fill in botList
		super(p);
		botList = new HashMap<Bot, Double>();
		botList.put(new RandomBot(p), 2.0);
		botList.put(new MobilityBot(p), 10.0);
		botList.put(new AntiMobilityBot(p), 10.0);
		botList.put(new MaxPieceBot(p), 10.0);
		botList.put(new AbsoluteWinBot(p), Double.MAX_VALUE);
	}

	// Creates an AIBrain that uses the given bots with their given weights
	public AIBrain(Piece p, Map<Bot, Double> botWeight) {
		super(p);
		botList = botWeight;
	}

	public Map<Piece, Map<Move, Set<BoardState>>> getDeepestBoardStates() {
		Map<Piece, Map<Move, Set<BoardState>>> ans = new HashMap<Piece, Map<Move, Set<BoardState>>>();
		Map<Move, Set<BoardState>> ansBlack = new HashMap<Move, Set<BoardState>>();
		Map<Move, Set<BoardState>> ansWhite = new HashMap<Move, Set<BoardState>>();

		BoardState currentBoardState = getBoardState();

		Set<Move> moves = currentBoardState.getValidMoves(getPiece());
		// for every possible move
		for (Move m : moves) {
			// get the board for the move
			BoardState childBoardState = getBoardState().getBoardFromMove(m);
			Map<Piece, Set<BoardState>> childBoardStateTree = getChildBoardStateTree(
					childBoardState, getPiece().getAlternate(), maxDepth);
			ansBlack.put(m, childBoardStateTree.get(Piece.BLACK));
			ansWhite.put(m, childBoardStateTree.get(Piece.WHITE));
		}
		// add the black and white maps to the final answer
		ans.put(Piece.BLACK, ansBlack);
		ans.put(Piece.WHITE, ansWhite);

		return ans;
	}

	// TODO make this function NOT RECURSIVE - it uses too much memory
	// return the set of deepest BoardStates for each Piece that are no more
	// than depth levels deep
	// turn says whos turn it is to move on the BoardState bs
	private Map<Piece, Set<BoardState>> getChildBoardStateTree(BoardState bs,
			Piece turn, int depth) {
		Map<Piece, Set<BoardState>> ans = new HashMap<Piece, Set<BoardState>>();
		Set<BoardState> blackSet = new HashSet<BoardState>();
		Set<BoardState> whiteSet = new HashSet<BoardState>();

		// if the current player must pass their turn and the game is not over
		// yet, switch turns
		if (!bs.hasValidMove(turn) && !bs.isGameOver()) {
			turn = turn.getAlternate();
		}

		Set<BoardState> rootBlackSet = new HashSet<BoardState>();
		Set<BoardState> rootWhiteSet = new HashSet<BoardState>();

		if (turn == Piece.BLACK)
			rootBlackSet.add(bs);
		else
			rootWhiteSet.add(bs);

		if (depth == 0) {
			blackSet.addAll(rootBlackSet);
			whiteSet.addAll(rootWhiteSet);
		} else {
			Map<Piece, Set<BoardState>> childrenBoardStateTree = new HashMap<Piece, Set<BoardState>>();
			Set<BoardState> childrenBSTreeBlack = new HashSet<BoardState>();
			Set<BoardState> childrenBSTreeWhite = new HashSet<BoardState>();

			Set<Move> moves = bs.getValidMoves(turn);
			// for each move
			for (Move m : moves) {
				BoardState childBoardState = bs.getBoardFromMove(m);
				Map<Piece, Set<BoardState>> childBoardStateTree = getChildBoardStateTree(
						childBoardState, turn.getAlternate(), depth - 1);

				childrenBSTreeBlack
						.addAll(childBoardStateTree.get(Piece.BLACK));
				childrenBSTreeWhite
						.addAll(childBoardStateTree.get(Piece.WHITE));
			}
			childrenBoardStateTree.put(Piece.BLACK, childrenBSTreeBlack);
			childrenBoardStateTree.put(Piece.WHITE, childrenBSTreeWhite);
			// at this point, childrenBoardStateTree should contain the set of
			// all black/white boards at the deepest level

			// check if the black set is empty, if it is, then no children were
			// black so use the root
			if (childrenBoardStateTree.get(Piece.BLACK).isEmpty())
				blackSet.addAll(rootBlackSet);
			else
				blackSet.addAll(childrenBoardStateTree.get(Piece.BLACK));
			if (childrenBoardStateTree.get(Piece.WHITE).isEmpty())
				whiteSet.addAll(rootWhiteSet);
			else
				whiteSet.addAll(childrenBoardStateTree.get(Piece.WHITE));
		}

		ans.put(Piece.BLACK, blackSet);
		ans.put(Piece.WHITE, whiteSet);
		return ans;
	}

	@Override
	public void thinkOfMove() {
		Move m = getBestMove();
		OthelloApplication.controller.makeMove(m);
	}

}