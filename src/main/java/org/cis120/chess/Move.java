package org.cis120.chess;

public abstract class Move{
    protected Tile source;
    protected Position target;
    public Move(Tile source, Position target) {
        this.source = source;
        this.target = target;
    }

    public Position getTarget() {
        return target;
    }

    public abstract Piece move(Board board);

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(source.getPiece().getSymbol()).append(" ").append(target);
        return sb.toString();
    }
}


class StandardMove extends Move {

    public StandardMove(Tile source, Position target) {
        super(source, target);
    }

    @Override
    public Piece move(Board board) {
        Piece moved = source.getPiece();
        source.setPiece(null);
        Piece captured = board.getTile(target).getPiece();
        board.setPiece(target, moved);
        return captured;
    }
}