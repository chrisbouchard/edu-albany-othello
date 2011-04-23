package edu.albany.othello;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.omg.CORBA.portable.ApplicationException;

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

	public AIBrain(Piece p) {
		// TODO Auto-generated constructor stub
		// TODO fill in botList
		super(p);
		botList = new HashMap<Bot, Double>();
	}

	// new method:

	public Map<Piece, Map<Move, Set<BoardState>>> getDeepestBoard() {
		Map<Piece, Map<Move, Set<BoardState>>> ans = new HashMap<Piece, Map<Move, Set<BoardState>>>();
		Map<Move, Set<BoardState>> ansBlack = new HashMap<Move, Set<BoardState>>();
		Map<Move, Set<BoardState>> ansWhite = new HashMap<Move, Set<BoardState>>();

		BoardState currentBoardState = getBoardState();

		Set<Move> moves = currentBoardState.getValidMoves(getPiece());
		// for every possible move
		for (Move m : moves) {
			// get the board for the move
			BoardState childBoardState = getBoardState().getBoardFromMove(m);
			Map<Piece, Set<BoardState>> childBoardStateTree = derp(
					childBoardState, getPiece().getAlternate(), maxDepth);
			// ansBlack.get(Piece.BLACK).addAll(childBoardStateTree.get(Piece.BLACK));
			ansBlack.put(m, childBoardStateTree.get(Piece.BLACK));
			ansWhite.put(m, childBoardStateTree.get(Piece.WHITE));
		}
		// add the black and white maps to the final answer
		ans.put(Piece.BLACK, ansBlack);
		ans.put(Piece.WHITE, ansWhite);

		return ans;
	}

	// return the set of deepest BoardStates for each Piece that are no more
	// than depth levels deep
	// turn says whos turn it is to move on the BoardState bs
	private Map<Piece, Set<BoardState>> derp(BoardState bs, Piece turn,
			int depth) {
		Map<Piece, Set<BoardState>> ans = new HashMap<Piece, Set<BoardState>>();
		Set<BoardState> blackSet = new HashSet<BoardState>();
		Set<BoardState> whiteSet = new HashSet<BoardState>();

		// if the current player must pass their turn and the game is not over
		// yet, switch turns
		if (!bs.hasValidMove(turn) && bs.gameNotOver()) {
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

			// left off here //////////

			Map<Piece, Set<BoardState>> childrenBoardStateTree = new HashMap<Piece, Set<BoardState>>();
			Set<BoardState> childrenBSTreeBlack = new HashSet<BoardState>();
			Set<BoardState> childrenBSTreeWhite = new HashSet<BoardState>();

			Set<Move> moves = bs.getValidMoves(turn);
			// for each move
			for (Move m : moves) {
				BoardState childBoardState = bs.getBoardFromMove(m);
				Map<Piece, Set<BoardState>> childBoardStateTree = derp(
						childBoardState, turn.getAlternate(), depth - 1);

				childrenBSTreeBlack
						.addAll(childBoardStateTree.get(Piece.BLACK));
				childrenBSTreeWhite
						.addAll(childBoardStateTree.get(Piece.WHITE));
			}
			childrenBoardStateTree.put(Piece.WHITE, childrenBSTreeWhite);
			childrenBoardStateTree.put(Piece.BLACK, childrenBSTreeBlack);
			// at this point, childrenBoardStateTree should contain the set of
			// all black/white boards at the deepest level

			// check if the black set is empty, if it is, then no children were
			// black so use the root
			if (childrenBoardStateTree.get(Piece.BLACK).isEmpty()) {
				blackSet.addAll(rootBlackSet);
			}
			if (childrenBoardStateTree.get(Piece.BLACK).isEmpty()) {
				whiteSet.addAll(rootWhiteSet);
			}
		}

		ans.put(Piece.BLACK, blackSet);
		ans.put(Piece.WHITE, whiteSet);
		return ans;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////

	// returns 2 pairs as a map. the value is the set of boardstates.
	// (BLACK, (MOVE, {}))
	// (WHITE, (MOVE, {}))
	// where MOVE is the top level's move.
	// bs: the root board state.
	// depth: the remaining depth to reach, if 0, then only look at root.
	// firstMove: null if the first time this method is called. this holds the
	// top most branch taken. if bs has 5 valid moves the first time this method
	// is called, then there will be 5 different firstMove values used when
	// called recursively
	//
	// return: (BLACK, (move, boardstateset)) (WHITE, (move, boardstateset))
	// move is null if its the first time called AND boardstateset is the root
	// bs or maybe {}
	private Map<Piece, Map<Move, Set<BoardState>>> deep(BoardState bs,
			int depth, Move firstMove) {
		// rootSets is the returned states for the root node
		Map<Piece, Map<Move, Set<BoardState>>> rootBWSets = new HashMap<Piece, Map<Move, Set<BoardState>>>();
		rootBWSets.put(Piece.BLACK, new HashMap<Move, Set<BoardState>>());
		rootBWSets.put(Piece.WHITE, new HashMap<Move, Set<BoardState>>());

		// rootSet is a set containing the root node
		Set rootSet = new HashSet<BoardState>();
		rootSet.add(bs);

		if (bs.getTurn() == Piece.BLACK) {
			Map<Move, Set<BoardState>> rootBlack = new HashMap<Move, Set<BoardState>>();
			rootBlack.put(firstMove, rootSet);
			rootBWSets.put(Piece.BLACK, rootBlack);
			rootBWSets.put(Piece.WHITE, new HashMap<Move, Set<BoardState>>());
		}

		return null;
	}

	// given a board state, return the list of boardstates depth levels deep
	private Map<Move, Set<BoardState>> getDeepestBoardStatesRecursive(
			BoardState bs, int depth) {
		Set<BoardState> nextLevelBoardStates = new HashSet<BoardState>();
		Set<Move> moves = bs.getValidMoves(getPiece());
		// for each move
		for (Move m : moves) {
			nextLevelBoardStates.add(bs.getBoardFromMove(m));
		}
		// nextLevelBoardStates now holds the all the boardstates that are
		// children of bs

		// if we can't recurse any more, just return the current board state and
		// nextLevelBoardStates
		if (depth == 0) {
			// create the map
			Map<Move, Set<BoardState>> ans = new HashMap<Move, Set<BoardState>>();
			ans.put(bs.getTurn(), bs);
			ans.put(nextLevelBoardStates.getTurn(), nextLevelBoardStates);

		}

		for (BoardState BS : nextLevelBoardStates) {
			getDeepestBoardStatesRecursive(BS, depth - 1);
		}

		return null;
	}

	public Map<Piece, Map<Move, Set<BoardState>>> getDeepestBoardStates() {
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

	@Override
	public void thinkOfMove() {
		Move m = getBestMove();
		OthelloApplication.controller.makeMove(m);
	}

}

/*
 * /*******************************
 * 
 * 
 * //boardstate, depth, color
 * set deep(rootBS, d, w){
 * 
 * //if we can't go deeper, return empty set or rootBS
 * if (d == 0){
 * if (rootBS == w)
 * return rootBS;
 * else
 * return {};
 * }
 * 
 * set child = {}
 * for (each childBS c)
 * child.add( deep(c, d-1, w) )
 * 
 * //child could be empty
 * 
 * //if child contains no bs that are w, then return the root if it is w, if the
 * root isnt even w, then return {}
 * if (child == {}){
 * if (rootBS == w)
 * return rootBS;
 * else
 * return {};
 * }
 * else //if child has BSs
 * return child;
 * }
 */

/*
 * /*************************** with multiple pieces ***********
 * 
 * //boardstate, depth
 * 2sets deep(rootBS, d){
 * 
 * 2sets rootSet = <w:{}, b:{}>;
 * if (rootBS == w)
 * rootSet.w = {rootBS};
 * else
 * rootSet.b = {rootBS};
 * 
 * 
 * //if we can't go deeper, return empty set or rootBS
 * if (d == 0){
 * return rootSet;
 * }
 * 
 * 2sets child = <w:{}, b:{}>;
 * for (each childBS c){
 * 2sets childset = deep(c, d-1, w)
 * child.w.add(childset.w)
 * child.b.add(childset.b)
 * }
 * 
 * //child.w and/or child.b could be empty
 * 
 * //if child contains no bs that are w, then return the root if it is w, if the
 * root isnt even w, then return {}
 * //if child.w == {}, then there are no w children, so return the rootSet.w for
 * w. Same for b
 * if (child.w == {})
 * ans.w = rootSet.w;
 * else
 * ans.w = child.w;
 * 
 * if (child.b == {})
 * ans.b = rootSet.b;
 * else
 * ans.b = child.b;
 * 
 * return ans;
 */