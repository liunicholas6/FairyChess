package org.cis120.chess;

enum TileColor {
    TILE_WHITE,
    TILE_BLACK
}

/**
 * A mutable wrapper for piece that holds contextual information about it. The board is made up of tiles.
 */
public class Tile {
    private final TileColor color;
    private final Board board;
    private final Position pos;

    private Piece piece;

    public Tile (TileColor color, Board board, Position pos) {
        this.board = board;
        this.pos = pos;
        this.color = color;
    }

    public Position getPos() {
        return pos;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }
}