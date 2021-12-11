package org.cis120.chess;

import org.cis120.tictactoe.GameBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RunChess implements Runnable {
    public void run() {

        final JFrame frame = new JFrame("Chess");
        frame.setLocation(0, 0);

        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        final ChessDisplay chessDisplay = new ChessDisplay(status);
        frame.add(chessDisplay, BorderLayout.CENTER);

        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> chessDisplay.reset());
        control_panel.add(reset);

        JOptionPane.showMessageDialog(null,
                "Hello! This is an implementation of Chess. Click a piece to see where it can move " +
                        "and click again on a highlighted square to move the piece.");

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        chessDisplay.reset();
    }
}