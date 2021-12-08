package org.cis120.chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Display extends JPanel{

    private Board board; // model for the game
    private JLabel status; // current status text

    // Game constants
    public static final int BOARD_WIDTH = 300;
    public static final int BOARD_HEIGHT = 300;

    /**
     * Initializes the game board.
     */
    public Display(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        board = new Board(8, 8); // initializes model for the game
        status = statusInit; // initializes the status JLabel

        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();

                // updates the model given the coordinates of the mouseclick
                ttt.playTurn(p.x / 100, p.y / 100);

                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        ttt.reset();
        status.setText("Player 1's Turn");
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (ttt.getCurrentPlayer()) {
            status.setText("Player 1's Turn");
        } else {
            status.setText("Player 2's Turn");
        }

        int winner = ttt.checkWinner();
        if (winner == 1) {
            status.setText("Player 1 wins!!!");
        } else if (winner == 2) {
            status.setText("Player 2 wins!!!");
        } else if (winner == 3) {
            status.setText("It's a tie.");
        }
    }

    /**
     * Draws the game board.
     *
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws board grid
        g.drawLine(100, 0, 100, 300);
        g.drawLine(200, 0, 200, 300);
        g.drawLine(0, 100, 300, 100);
        g.drawLine(0, 200, 300, 200);

        // Draws X's and O's
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int state = ttt.getCell(j, i);
                if (state == 1) {
                    g.drawOval(30 + 100 * j, 30 + 100 * i, 40, 40);
                } else if (state == 2) {
                    g.drawLine(30 + 100 * j, 30 + 100 * i, 70 + 100 * j, 70 + 100 * i);
                    g.drawLine(30 + 100 * j, 70 + 100 * i, 70 + 100 * j, 30 + 100 * i);
                }
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
