package edu.albany.othello;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;

public class PieceButton extends JButton {

    /**
     * 
     */
    private static final long serialVersionUID = -3573867997835593775L;

    private Piece p;

    public PieceButton(Piece p) {
        this.p = p;
        setSize(50, 50);
    }

    public Piece getPiece() {
        return p;
    }

    public void setPiece(Piece p) {
        this.p = p;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        
        if (p == null) {
            g2.setColor(Color.GRAY);
            g2.drawOval(2, 2, getWidth() - 2, getHeight() - 2);
        }
        else {
            switch (p) {
            case BLACK:
                g2.setColor(Color.BLACK);
                break;

            case WHITE:
                g2.setColor(Color.WHITE);
                break;
            }
            
            g2.fillOval(0, 0, getWidth(), getHeight());
        }
        
        
    }

}
