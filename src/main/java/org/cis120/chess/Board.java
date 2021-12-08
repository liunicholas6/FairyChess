package org.cis120.chess;

enum Player {
    PLAYER1,
    PLAYER2
}

/**
 * Represents the chessboard, where every element of the 2D array representation is a
 * tile on the board is a Tile. A tile with a null value is an invalid tile.
 */
public class Board {
    private final static int MAX_DIM = 100;
    private final Tile[][] representation;
    private final int rows;
    private final int cols;

    /**
     * Generates an empty board with the given number of rows and columns.
     * @param rows number of rows
     * @param cols number of columns
     */
    public Board (int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.representation = new Tile[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c ++) {
                TileColor color = ((rows + cols) % 2 == 0)? TileColor.TILE_BLACK : TileColor.TILE_WHITE;
                representation[r][c] = new Tile(color, this, new Position(r, c));
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    /**
     * @param pos Position to check
     * @return Tile at the position, or null if there is none
     */
    public Tile getTile(Position pos) {
        int x = pos.getX();
        int y = pos.getY();
        if (x < 0 || x >= rows || y < 0 || y >= cols) {
            return null;
        }
        return representation[pos.getX()][pos.getY()];
    }

    /**
     * @param pos Position to place piece
     * @param piece Piece to place
     */
    public void setPiece(Position pos, Piece piece) {
        representation[pos.getX()][pos.getY()].setPiece(piece);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            sb.append("----");
        }
        sb.append("\n");
        for (int c = cols - 1; c >= 0; c--) {
            sb.append("| ");
            for (int r = 0; r < rows; r++) {
                Piece piece = representation[r][c].getPiece();
                sb.append(piece == null? " " : piece.getSymbol()).append(" | ");
            }
            sb.append("\n");
            for (int r = 0; r < rows; r++) {
                sb.append("----");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
