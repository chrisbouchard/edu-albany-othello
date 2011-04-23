package edu.albany.othello;

import java.util.Scanner;

public class Human extends Player {
    
    public Human(Piece p) {
        super(p);
    }
    
    @Override
    public void thinkOfMove() {
        System.out.println("Waiting for move...");
        OthelloApplication.view.setCurrentHuman(this);
    }
    
    public void makeMove(int r, int c) {
        OthelloApplication.controller.makeMove(new Move(getPiece(), r, c));
    }
}
