package edu.albany.othello;

import java.util.Scanner;

public class OthelloTextView implements OthelloView {
    private class HumanInputThread extends Thread {
        @Override
        public void run() {
            Scanner sc = new Scanner(System.in);
            int r = sc.nextInt();
            int c = sc.nextInt();
            Human h = currentHuman;

            currentHuman = null;
            h.makeMove(r, c);
        }
    }

    private Human currentHuman;

    public OthelloTextView() {
        currentHuman = null;
    }

    public void setCurrentHuman(Human h) {
        currentHuman = h;
        new HumanInputThread().run();
    }

    public void update() {
        BoardState bs = OthelloApplication.model.getCurrentBoardState();
        String message = "";

        for (Piece p : Piece.values()) {
            message += String.format("[%c] %s: %d    ",
                    ((p == OthelloApplication.model.getCurrentPiece()) ? '*'
                            : ' '), p, OthelloApplication.model
                            .getCurrentBoardState().getNumPieces(p));
        }

        message += OthelloApplication.model.getCurrentBoardState().isGameOver() ? "GAME OVER!"
                : "";

        System.out.println(bs);
        System.out.println(message);
    }
}
