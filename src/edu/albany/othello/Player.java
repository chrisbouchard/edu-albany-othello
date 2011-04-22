package edu.albany.othello;

public abstract class Player {

	protected BoardState getBoardState() {
		return OthelloApplication.model.getCurrentBoardState();
	}

}
