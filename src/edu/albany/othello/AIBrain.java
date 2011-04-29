package edu.albany.othello;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.albany.othello.bots.*;

// import java.util.Map.Entry;

public class AIBrain extends Player {
    // holds the bot and weight pair
    private Map<Bot, Double> botList;
    private static final int maxDepth = 3;

    private static final int maxElements = 1500;

    private boolean beQuiet;

    public Move getBestMove() {
        Set<Map<Move, Double>> moveConfidenceSet = new HashSet<Map<Move, Double>>();

        BoardState currentBS = OthelloApplication.model.getCurrentBoardState();
        Piece currentPiece = OthelloApplication.model.getCurrentPiece();
        
        if (!beQuiet) {
            System.out.println("Thinking...");
        }

        Map<Piece, Map<Move, Set<BoardState>>> deepest = getDeepestBoardStates2(
                maxElements, currentBS, currentPiece);

        if (!beQuiet) {
            int numBlackBoards = 0;
            int numWhiteBoards = 0;

            for (Move m : deepest.get(Piece.BLACK).keySet()) {
                numBlackBoards += deepest.get(Piece.BLACK).get(m).size();
            }
            
            for (Move m : deepest.get(Piece.WHITE).keySet()) {
                numWhiteBoards += deepest.get(Piece.WHITE).get(m).size();
            }

            System.out.println(String.format("Considering %d black boards...",
                    numBlackBoards));
            System.out.println(String.format("Considering %d white boards...",
                    numWhiteBoards));
        }

        // for each bot
        for (Bot b : botList.keySet()) {
            // all (move, confidence) pairs for b
            /*
             * Map<Move, Double> moveConfidences = b.getMoveConfidences(
             * getBoardState(), getDeepestBoardStates());
             */

            Map<Move, Double> moveConfidences = b.getMoveConfidences(
                    getBoardState(), deepest);

            // DEBUGGING OUTPUT
            if (!beQuiet) {
                System.out.println(String.format("    %s: %s", b.getClass()
                        .getSimpleName(), moveConfidences));
            }

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

        if (!beQuiet) {
            System.out.println(moveWeightedConfidenceSums);
        }

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

        if (!beQuiet) {
            System.out.println("AI Choses: " + currentHighestPair);
        }

        return currentHighestPair.getKey();
    }

    // Creates an AIBrain that uses all bots at predetermined weights
    public AIBrain(Piece p, boolean beQuiet) {
        // TODO Auto-generated constructor stub
        // TODO fill in botList
        super(p);
        this.beQuiet = beQuiet;
        botList = new HashMap<Bot, Double>();
        botList.put(new RandomBot(p), 1.0);
        botList.put(new MobilityBot(p), 4.0);
        botList.put(new AntiMobilityBot(p), 6.0);
        botList.put(new MaxPieceBot(p), 6.0);
        botList.put(new ParityBot(p), 9.0);
        botList.put(new WinLossBot(p), 10.0);
        botList.put(new AbsoluteWinLoseBot(p), Double.MAX_VALUE);
    }

    // Creates an AIBrain that uses the given bots with their given weights
    public AIBrain(Piece p, boolean beQuiet, Map<Bot, Double> botWeight) {
        super(p);
        this.beQuiet = beQuiet;
        botList = new HashMap<Bot, Double>(botWeight);
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
    // turn says whose turn it is to move on the BoardState bs
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
        }
        else {
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

    public Map<Piece, Map<Move, Set<BoardState>>> getDeepestBoardStates2(
            int maxElements, BoardState currentBS, Piece currentPiece) {

        // An element in the set for the current level
        class Element {
            public BoardState bs;
            public Piece p;
            public Element parent;
            public Move rootMove;

            public Element(BoardState bs, Piece p, Element parent, Move rootMove) {
                this.bs = bs;
                this.p = p;
                this.parent = parent;
                this.rootMove = rootMove;
            }

            public boolean equals(Object o) {
                if (!(o instanceof Element)) {
                    return false;
                }

                Element e = (Element) o;
                return e.bs.equals(bs) && e.p == p && e.parent == parent
                        && e.rootMove.equals(rootMove);
            }

            public int hashCode() {
                int hash = 7;

                hash = hash * 31 + bs.hashCode();
                hash = hash * 31 + p.hashCode();
                hash = hash * 31
                        + ((rootMove == null) ? 0 : rootMove.hashCode());

                return hash;
            }
        }

        // Method logic starts here

        Map<Piece, Map<Move, Set<BoardState>>> ans = new HashMap<Piece, Map<Move, Set<BoardState>>>();

        for (Piece p : Piece.values()) {
            Map<Move, Set<BoardState>> pieceAns = new HashMap<Move, Set<BoardState>>();
            Set<Element> currentLevel = new HashSet<Element>();
            Set<Element> newElements = new HashSet<Element>();

            // Add the root to the current level
            // Root has no parent, and no move was made to get it
            // Assume the current board is the previous color just to get things
            // started
            Element rootElt = new Element(currentBS,
                    currentPiece.getAlternate(), null, null);
            currentLevel.add(rootElt);
            newElements.add(rootElt);

            int availableElements = maxElements - 1;

            while (!newElements.isEmpty()) {
                Set<Element> prevNewElements = newElements;
                newElements = new HashSet<Element>();

                for (Element newElt : prevNewElements) {
                    Piece nextP = newElt.p.getAlternate();
                    Set<Move> validMoves = newElt.bs.getValidMoves(nextP);

                    // Handle skips
                    if (validMoves.isEmpty()) {
                        nextP = newElt.p.getAlternate();
                        validMoves = newElt.bs.getValidMoves(nextP);
                    }

                    for (Move vm : validMoves) {
                        // Add the new elements if there's room
                        if (availableElements > 0) {
                            Move rootMove = (newElt.rootMove == null) ? vm
                                    : newElt.rootMove;
                            Element newE = new Element(
                                    newElt.bs.getBoardFromMove(vm), nextP,
                                    newElt, rootMove);
                            
                            // Try to add the element
                            if (currentLevel.add(newE)) {
                                // Ok, it must be new
                                newElements.add(newE);
                                --availableElements;

                                // Create a set for this move
                                if (newElt.rootMove == null) {
                                    pieceAns.put(vm, new HashSet<BoardState>());
                                }

                                // If this is the color we're looking for,
                                // remove
                                // its ancestors
                                if (newE.p == p) {
                                    while (newE.parent != null
                                            && currentLevel
                                                    .contains(newE.parent)) {
                                        Element parent = newE.parent;
                                        newE.parent = newE.parent.parent;
                                        currentLevel.remove(parent);
                                        ++availableElements;
                                    }
                                }
                            }
                        }

                        // Find elements whose parent was removed and set
                        // their parent to null
                        for (Element elt : currentLevel) {
                            if (elt.parent != null
                                    && !currentLevel.contains(elt.parent)) {
                                elt.parent = null;
                            }
                        }
                    }
                }

            }

            // Filter out the board states that don't match the color we
            // want and add to the proper key
            for (Element e : currentLevel) {
                if (e.p == p && e.rootMove != null) {
                    pieceAns.get(e.rootMove).add(e.bs);
                }
            }

            ans.put(p, pieceAns);
        }

        return ans;
    }

    @Override
    public void thinkOfMove() {
        Move m = getBestMove();
        OthelloApplication.controller.makeMove(m);
    }

    // For testing
    public static void main(String... args) {
        BoardState bs = new BoardState();
        AIBrain aib = new AIBrain(Piece.BLACK, true);
        System.out.println(aib.getDeepestBoardStates2(10, bs, Piece.BLACK));
    }

}
