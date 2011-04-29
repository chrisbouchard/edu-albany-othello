package edu.albany.othello;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*
 * TODO: We need to add a method to prompt for a move.
 * We may wind up tightly coupling the view to the controller for simplicity.
 */

public class OthelloSwingView implements OthelloView {
    private class ButtonActionListener implements ActionListener {
        private int r;
        private int c;

        public ButtonActionListener(int r, int c) {
            this.r = r;
            this.c = c;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // System.out.println(String.format("(%d, %d)", r, c));

            if (currentHuman != null) {
                Human h = currentHuman;
                currentHuman = null;
                h.makeMove(r, c);
            }
        }
    }

    private JFrame frame;
    private PieceButton[][] buttons;
    private JLabel messageLabel;
    private Human currentHuman;

    public OthelloSwingView() {
        currentHuman = null;

        frame = new JFrame("Play Othello!");
        LayoutManager frameLayout = new BorderLayout(2, 2);
        frame.setLayout(frameLayout);

        JPanel buttonPanel = new JPanel();
        LayoutManager buttonLayout = new GridLayout(BoardState.ROWS,
                BoardState.COLS);
        buttonPanel.setLayout(buttonLayout);
        frame.add(buttonPanel, BorderLayout.CENTER);
        buttonPanel.setBackground(new Color(0f, 0.7f, 0.3f));

        messageLabel = new JLabel(" ");
        frame.add(messageLabel, BorderLayout.SOUTH);

        buttons = new PieceButton[BoardState.ROWS][BoardState.COLS];

        for (int r = 0; r < BoardState.ROWS; ++r) {
            for (int c = 0; c < BoardState.COLS; ++c) {
                buttons[r][c] = new PieceButton(null);
                buttonPanel.add(buttons[r][c]);
                buttons[r][c].addActionListener(new ButtonActionListener(r, c));
            }
        }

        frame.setSize(500, 500);
        frame.setVisible(true);
    }

    public void displayMessage(String msg) {
        JOptionPane.showMessageDialog(frame, msg);
    }

    public void setCurrentHuman(Human h) {
        currentHuman = h;
    }

    public void update() {
        BoardState bs = OthelloApplication.model.getCurrentBoardState();
        Piece cp = OthelloApplication.model.getCurrentPiece();
        Set<Move> validMoves = bs.getValidMoves(cp);

        for (int r = 0; r < BoardState.ROWS; ++r) {
            for (int c = 0; c < BoardState.COLS; ++c) {
                buttons[r][c].setPiece(bs.getPieceAt(r, c));

                if (validMoves.contains(new Move(cp, r, c))) {
                    buttons[r][c].setSelected(true);
                }
                else {
                    buttons[r][c].setSelected(false);
                }

                buttons[r][c].repaint();
            }
        }

        String message = "";

        for (Piece p : Piece.values()) {
            message += String.format("[%c] %s: %d    ",
                    ((p == cp) ? '*' : ' '), p, bs.getNumPieces(p));
        }

        message += bs.isGameOver() ? "GAME OVER!" : "";

        messageLabel.setText(message);

        if (bs.isGameOver()) {
            displayMessage(bs.getWinningPiece() + " Wins!");
        }
    }
}