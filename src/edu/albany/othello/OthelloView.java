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

import edu.albany.othello.event.UpdateEvent;
import edu.albany.othello.event.UpdateListener;

public class OthelloView implements UpdateListener {
    private class ButtonActionListener implements ActionListener {
        private int r;
        private int c;
        
        public ButtonActionListener(int r, int c) {
            this.r = r;
            this.c = c;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(r + ", " + c);
            
            try {
                OthelloApplication.model.makeMove(r, c);
            }
            catch (IndexOutOfBoundsException ex) {
                System.err.println("Out of bounds!");
            }
            catch (IllegalArgumentException ex) {
                System.err.println("Bad move!");
            }
        }
    }

    private JButton[][] buttons;
    private JLabel messageLabel;

    @Override
    public void updatePerformed(UpdateEvent e) {
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
        
        messageLabel.setText("Current player: " + OthelloApplication.model.getCurrentPiece());
    }
    
    public OthelloView() {
        JFrame frame = new JFrame("Play Othello!");
        LayoutManager frameLayout = new BorderLayout(2, 2);
        frame.setLayout(frameLayout);
        
        JPanel buttonPanel = new JPanel();
        LayoutManager buttonLayout = new GridLayout(BoardState.ROWS, BoardState.COLS);
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
}
