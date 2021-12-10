package org.cis120.chess;

public class Move{
    protected Position source;
    protected Position target;
    protected Position capturePos;

    public Move(Position source, Position target) {
        this.source = source;
        this.target = target;
        this.capturePos = target;
    }
    public Position getSource() {
        return source;
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
        board.movePiece(target, source);
        return captured;
    }
}