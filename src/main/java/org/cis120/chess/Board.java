package org.cis120.chess;


import java.util.ArrayList;

public class Board{
    private final static int MAX_DIM = 100;
    private final Piece[][] representation;
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
        representation = new Piece[rows][cols];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Piece getPiece(Position pos) {
        return representation[pos.getX()][pos.getY()];
    }

    public boolean isValidPosition(Position pos) {
        int x = pos.getX();
        int y = pos.getY();
        return (x >= 0 && x < rows && y >= 0 && y < cols);
    }

    public void setPiece(Position pos, Piece piece) {
        representation[pos.getX()][pos.getY()] = piece;
    }

    public void placePiece(Piece piece) {
        setPiece(piece.getPosition(), piece);
    }

    public void movePiece(Position pos, Piece piece) {
        setPiece(piece.getPosition(), null);
        piece.setPosition(pos);
        placePiece(piece);
        piece.markMoved();
    }

    public ArrayList<Piece> getPieces() {
        ArrayList<Piece> result = new ArrayList<>(32);
        for (Piece[] row : representation) {
            for (Piece piece : row) {
                if (piece != null) {
                    result.add(piece);
                }
            }
        }
        return result;
    }



    public Piece capturePiece(Position pos) {
        Piece captured = getPiece(pos);
        setPiece(pos, null);
        if (captured != null) {
            captured.setPosition(null);
        }
        return captured;
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
                Piece piece = representation[r][c];
                sb.append(piece == null? " " : piece.getSymbol()).append(" | ");
            }
            sb.append("\n");
            for (int r = 0; r < rows; r++) {
                sb.append("----");
            }
            sb.append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }

    public void copyOnto (Board other) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Piece original = representation[r][c];
                Piece copy = (original == null) ? null : original.copy();
                other.representation[r][c] = copy;
            }
        }
    }
}
