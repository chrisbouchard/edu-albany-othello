package edu.albany.othello.bots;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.albany.othello.BoardState;
import edu.albany.othello.Move;
import edu.albany.othello.Piece;

public class AbsoluteWinLoseBot extends Bot {
    private boolean hasGloated;

    public AbsoluteWinLoseBot(Piece p) {
        super(p);
        hasGloated = false;
    }

    @Override
    public Map<Move, Double> getMoveConfidences(BoardState bs,
            Map<Piece, Map<Move, Set<BoardState>>> deepestBoardStates) {
        Map<Move, Double> moveConfidences = new HashMap<Move, Double>();

        Map<Move, Set<BoardState>> allDeepestBoardStates = deepestBoardStates
                .get(Piece.WHITE);
        allDeepestBoardStates.putAll(deepestBoardStates.get(Piece.BLACK));

        for (Move m : allDeepestBoardStates.keySet()) {

            // get the deep BoardStates for this move
            Set<BoardState> deepestBoardStatesSet = allDeepestBoardStates
                    .get(m);

            boolean isWin = true;
            boolean isLose = true;

            for (BoardState deepBS : deepestBoardStatesSet) {
                isWin &= deepBS.getWinningPiece() == this.piece;
                isLose &= deepBS.getWinningPiece() == this.piece.getAlternate();
            }

            moveConfidences.put(m, (isWin ? 1.0 : 0.0) + (isLose ? -1.0 : 0.0));

            if (isWin && !hasGloated) {
                System.err.println("You have no chance to survive "
                        + "make your time.");
                hasGloated = true;
            }
        }
        return moveConfidences;
    }
}
