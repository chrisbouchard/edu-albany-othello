package edu.albany.othello;

public abstract class Bot {
	protected Piece p;

	public Bot(Piece p){
		this.p = p; 	
	}

	public abstract Set<MoveConfidence> getMoveConfidences(BoardState bs);
}