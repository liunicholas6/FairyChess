package org.cis120.chess;

public class Move{
    protected Piece piece;
    protected Position target;
    protected Position capturePos;

    public Move(Piece piece, Position target) {
        this.piece = piece;
        this.target = target;
        this.capturePos = target;
    }
    public Piece getPiece() {
        return piece;
    }

    public Position getTarget() {
        return target;
    }

    public Position getCapturePos() {
        return capturePos;
    }

    public Piece move(Chess chess) {
        Board board = chess.getBoard();
        Piece captured = board.capturePiece(target);
        board.movePiece(target, piece);
        return captured;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(piece.getSymbol()).append(" ").append(target);
        return sb.toString();
    }
}