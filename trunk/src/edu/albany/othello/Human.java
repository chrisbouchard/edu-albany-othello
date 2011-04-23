package edu.albany.othello;

import java.util.Scanner;

public class Human extends Player {
    
    public Human(Piece p) {
        super(p);
    }
    
    @Override
    public Move getBestMove() {
        Scanner s = new Scanner(System.in);
        
        //TODO: Get this from the View
        return new Move(getPiece(), s.nextInt(), s.nextInt());
    }

}
