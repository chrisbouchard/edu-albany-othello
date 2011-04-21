package edu.albany.othello;

public class MoveConfidence {
	private double confidence;
	private Move move;

	public MoveConfidence(double confidence, double move){
		this.confidence = confidence;
		this.move = move;
	}
}
