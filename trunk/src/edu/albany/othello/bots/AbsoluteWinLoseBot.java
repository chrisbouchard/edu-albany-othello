package edu.albany.othello.bots;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.albany.othello.BoardState;
import edu.albany.othello.Move;
import edu.albany.othello.Piece;

public class AbsoluteWinLoseBot extends Bot {
    public AbsoluteWinLoseBot(Piece p) {
        super(p);
    }

    @Override
    public Map<Move, Double> getMoveConfidences(BoardState bs,
            Map<Piece, Map<Move, Set<BoardState>>> deepestBoardStates) {
        Map<Move, Double> moveConfidences = new HashMap<Move, Double>();

        for (Move m : deepestBoardStates.get(this.piece).keySet()) {

            // get the deep BoardStates for this move
            Set<BoardState> deepestBoardStatesSet = deepestBoardStates.get(
                    this.piece).get(m);

            boolean isWin = true;
            boolean isLose = true;

            for (BoardState deepBS : deepestBoardStatesSet) {
                isWin &= deepBS.isWinForPiece(this.piece);
                isLose &= deepBS.isWinForPiece(this.piece.getAlternate());
            }

            moveConfidences.put(m, (isWin ? 1.0 : 0.0) + (isLose ? -1.0 : 0.0));

        }
        return moveConfidences;
    }
}
