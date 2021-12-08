package org.cis120.chess;

import java.util.Map;

/**
 * A mutable wrapper for piece that holds contextual information about it. The board is made up of tiles.
 */
public class Tile {
    private final Board board;
    private final Position pos;
    private Piece piece;
    private boolean hasChanged = false;

    public Tile (Board board, Position pos) {
        this.board = board;
        this.pos = pos;
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

    public void markChanged() {
        hasChanged = true;
    }

    public MoveHolder generateMoves() {
        return piece.getMoveGenerator().generate(board, this);
    }
}