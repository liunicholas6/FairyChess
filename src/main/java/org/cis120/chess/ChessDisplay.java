package org.cis120.chess;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChessDisplay extends JPanel {

    private Chess chess;
    private JLabel status;
    private BufferedImage WHITE_TILE;
    private BufferedImage BLACK_TILE;
    private Map<String, BufferedImage> piece_images = new HashMap<>();

    // Game constants
    public static final int BOARD_WIDTH = 400;
    public static final int BOARD_HEIGHT = 400;
    public static final int TILE_SIZE = 50;


    /**
     * Initializes the game board.
     */
    public ChessDisplay(JLabel statusInit) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setFocusable(true);

        chess = new Chess();
        status = statusInit;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();
                Position position = new Position(7 - (400 - (p.x)) / 50, 7 - p.y / 50);
                chess.handlePositionalInput(position);

                updateStatus();
                repaint();
            }
        });

        try {
            WHITE_TILE = ImageIO.read(new File("files/ChessSprites/tile_white.png"));
            BLACK_TILE = ImageIO.read(new File("files/ChessSprites/tile_black.png"));
            for (Piece piece : chess.getPieces()) {
                String filePath = piece.getFilePath();
                if (!piece_images.containsKey(filePath)) {
                    piece_images.put(filePath, ImageIO.read(new File(filePath)));
                }
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        chess = new Chess();
        updateStatus();
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        status.setText(chess.getGameState().toString());
    }

    private void drawAtPosition(Graphics g, BufferedImage image, int row, int column) {
        g.drawImage(image, row * 50, (7 - column) * 50, TILE_SIZE, TILE_SIZE, null);
    }

    /**
     * Draws the game board.
     * <p>
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        for (int r = 0; r <= chess.getBoard().getRows(); r++) {
            for (int c = 0; c <= chess.getBoard().getCols(); c++) {
                BufferedImage tileSprite = ((r + c) % 2 == 0) ? BLACK_TILE : WHITE_TILE;
                drawAtPosition(g, tileSprite, r, c);
            }
        }
        Color myColour = new Color(0, 255, 0, 100);
        g.setColor(myColour);
        MoveHolder selectedMoves = chess.getSelectedMoves();
        if (selectedMoves != null) {
            for (Position position : chess.getSelectedMoves().keySet()) {
                g.fillRect(position.getX() * 50, (7 - position.getY()) * 50, 50, 50);
            }
        }
        for (Piece piece : chess.getPieces()) {
            Position position = piece.getPosition();
            if (position != null) {
                drawAtPosition(g, piece_images.get(piece.getFilePath()), position.getX(), position.getY());
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
