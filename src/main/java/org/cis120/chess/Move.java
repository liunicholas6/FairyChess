package org.cis120.chess;


abstract class Move{
    protected Piece piece;
    protected Position target;
    public Move(Piece piece, Position target) {
        this.piece = piece;
        this.target = target;
    }

    public Position getTarget() {
        return target;
    }

    public abstract Piece move(Board board);

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(piece.getSymbol()).append(" ").append(target);
        return sb.toString();
    }
}


class StandardMove extends Move {

    public StandardMove(Piece piece, Position target) {
        super(piece, target);
    }

    @Override
    public Piece move(Board board) {
        Piece captured = board.capturePiece(target);
        board.movePiece(target, piece);
        return captured;
    }
}