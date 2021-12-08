package org.cis120.chess;

import java.util.Arrays;
import java.util.Collection;

/**
 * Represents the chessboard, where every element of the 2D array representation is a
 * tile on the board is a Tile. A tile with a null value is an invalid tile.
 */
public class Board {
    private final Tile[][] representation;
    private final int rows;
    private final int cols;

    public static Board chessBoard() {
        Board board = new Board(8, 8);
        board.setPiece(new Position("a1"), Piece.Rook(Player.PLAYER1));
        board.setPiece(new Position("b1"), Piece.Knight(Player.PLAYER1));
        board.setPiece(new Position("c1"), Piece.Bishop(Player.PLAYER1));
        board.setPiece(new Position("d1"), Piece.Queen(Player.PLAYER1));

        board.setPiece(new Position("f1"), Piece.Bishop(Player.PLAYER1));
        board.setPiece(new Position("g1"), Piece.Knight(Player.PLAYER1));
        board.setPiece(new Position("h1"), Piece.Rook(Player.PLAYER1));

        board.setPiece(new Position("a8"), Piece.Rook(Player.PLAYER2));
        board.setPiece(new Position("b8"), Piece.Knight(Player.PLAYER2));
        board.setPiece(new Position("c8"), Piece.Bishop(Player.PLAYER2));
        board.setPiece(new Position("d8"), Piece.Queen(Player.PLAYER2));

        board.setPiece(new Position("f8"), Piece.Bishop(Player.PLAYER2));
        board.setPiece(new Position("g8"), Piece.Knight(Player.PLAYER2));
        board.setPiece(new Position("h8"), Piece.Rook(Player.PLAYER2));

        return board;
    }

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
                representation[r][c] = new Tile(this, new Position(r, c));
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

    public <T> String GridString (T[][] arr2D) {
        StringBuilder sb = new StringBuilder();
        for (T[] arr : arr2D) {
            sb.append(Arrays.toString(arr)).append("\n");
        }
        return sb.toString();
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

    public static void main(String[] args) {
        Board board = chessBoard();
        System.out.println(board);
        Tile whiteRook1Tile = board.getTile(new Position ("A1"));
        Piece blackRook1 = board.getTile(new Position("A8")).getPiece();
        whiteRook1Tile.generateMoves().get(new Position("A4")).move(board);
        Piece blackRook1Alias = board.getTile(new Position ("A4")).generateMoves().get(new Position("A8")).move(board);
        System.out.println("Black rook was captured: " + blackRook1.equals(blackRook1Alias));
        System.out.println(board);
    }
}
