package edu.albany.othello;

public abstract class Player {
	private Piece p;

	protected BoardState getBoardState() {
		return OthelloApplication.model.getCurrentBoardState();
	}

	public Piece getPiece() {
		return p;
	}

}
