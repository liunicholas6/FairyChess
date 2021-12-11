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

    public Move() {
        type = null;
    }

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

class CastleMove extends Move {
    private final int x;
    private final boolean kingSide;

    public CastleMove(int x, boolean kingSide) {
        this.source = new Position(4, x);
        if (kingSide) {
            target = new Position(6, x);
        } else {
            target = new Position(2, x);
        }
        this.x = x;
        this.kingSide = kingSide;
    }

    @Override
    public void move(Chess chess) {
        Board board = chess.getBoard();

        if (kingSide) {
            board.movePiece(new Position(6, x), source);
            board.movePiece(new Position(5, x), new Position(7, x));
        } else {
            board.movePiece(new Position(2, x), source);
            board.movePiece(new Position(3, x), new Position(0, x));
        }
    }
}