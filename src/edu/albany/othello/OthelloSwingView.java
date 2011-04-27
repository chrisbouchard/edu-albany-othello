package edu.albany.othello;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
            System.out.println(String.format("(%d, %d)", r, c));

            if (currentHuman != null) {
                Human h = currentHuman;
                currentHuman = null;
                h.makeMove(r, c);
            }
        }
    }

    private JButton[][] buttons;
    private JLabel messageLabel;
    private Human currentHuman;

    public OthelloSwingView() {
        currentHuman = null;

        JFrame frame = new JFrame("Play Othello!");
        LayoutManager frameLayout = new BorderLayout(2, 2);
        frame.setLayout(frameLayout);

        JPanel buttonPanel = new JPanel();
        LayoutManager buttonLayout = new GridLayout(BoardState.ROWS,
                BoardState.COLS);
        buttonPanel.setLayout(buttonLayout);
        frame.add(buttonPanel, BorderLayout.CENTER);

        messageLabel = new JLabel(" ");
        frame.add(messageLabel, BorderLayout.SOUTH);

        buttons = new JButton[BoardState.ROWS][BoardState.COLS];

        for (int r = 0; r < BoardState.ROWS; ++r) {
            for (int c = 0; c < BoardState.COLS; ++c) {
                buttons[r][c] = new JButton();
                buttonPanel.add(buttons[r][c]);
                buttons[r][c].addActionListener(new ButtonActionListener(r, c));
            }
        }

        frame.pack();
        frame.setVisible(true);
    }

    public void setCurrentHuman(Human h) {
        currentHuman = h;
    }

    public void update() {
        BoardState bs = OthelloApplication.model.getCurrentBoardState();

        for (int r = 0; r < BoardState.ROWS; ++r) {
            for (int c = 0; c < BoardState.COLS; ++c) {
                if (bs.getPieceAt(r, c) == null) {
                    buttons[r][c].setText(" ");
                }
                else {
                    switch (bs.getPieceAt(r, c)) {
                    case WHITE:
                        buttons[r][c].setText("W");
                        break;

                    case BLACK:
                        buttons[r][c].setText("B");
                        break;

                    default:
                        buttons[r][c].setText(" ");
                        break;
                    }
                }
            }
        }

        String message = "";

        for (Piece p : Piece.values()) {
            message += String.format("[%c] %s: %d    ",
                    ((p == OthelloApplication.model.getCurrentPiece()) ? '*'
                            : ' '), p, OthelloApplication.model
                            .getCurrentBoardState().getNumPieces(p));
        }

        message += OthelloApplication.model.getCurrentBoardState().isGameOver() ? "GAME OVER!"
                : "";

        messageLabel.setText(message);
    }
}
