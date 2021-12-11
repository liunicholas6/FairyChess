package org.cis120.chess;

enum MoveType {
    STANDARD,
    PROMOTION,
    PAWN_JUMP,
    EN_PASSANT,
}


public class Move {
    protected final MoveType type;
    protected Position source;
    protected Position target;
    protected Position capturePos;

    public Move(Position source, Position target, Position capturePos, MoveType type) {
        this.type = type;
        this.source = source;
        this.target = target;
        this.capturePos = capturePos;
    }

    public Move(Position source, Position target, MoveType type) {
        this(source, target, target, type);
    }

    public Move(Position source, Position target) {
        this(source, target, MoveType.STANDARD);
    }

    public MoveType getType() {
        return type;
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

    public void move(Chess chess) {
        Board board = chess.getBoard();
        board.capturePiece(capturePos);
        board.movePiece(target, source);
    }
}