package edu.albany.othello;

public class Move {
	private Piece piece;
	private int r, c;
	
    public Move(Piece piece, int r, int c) {
        this.piece = piece;
        this.r = r;
        this.c = c;
    }
}
