package org.cis120.chess;

import java.util.*;

public interface IMoveGenerator {

    MoveHolder generate(Chess chess, Piece source);

    IMoveGenerator ROOK = new Rider(0, 1);
    IMoveGenerator KNIGHT = new Leaper(1, 2);
    IMoveGenerator BISHOP = new Rider(1, 1);
    IMoveGenerator QUEEN = new Compound(ROOK, BISHOP);
}

class Leaper implements IMoveGenerator {
    private final Position[] directions;
    public Leaper(int x, int y) {
        directions = new Position(x, y).allDirections();
    }

    @Override
    public MoveHolder generate(Chess chess, Piece source) {
        Board board = chess.getBoard();
        Position sourcePos = source.getPosition();
        MoveHolder moves = new MoveHolder();
        for (Position direction: directions) {
            Position targetPosition = sourcePos.plus(direction);
            if (board.isValidPosition(targetPosition)) {
                Piece targetPiece = board.getPiece(targetPosition);
                if (targetPiece == null || targetPiece.getPlayer() != source.getPlayer()) {
                        moves.addMove(new StandardMove(source, targetPosition));
                }
            }
        }
        return moves;
    }
}

class Rider implements IMoveGenerator {
    private final Position[] directions;

    public Rider(int x, int y) {
        directions = new Position(x, y).allDirections();
    }

    @Override
    public MoveHolder generate(Chess chess, Piece source) {
        Board board = chess.getBoard();
        Position sourcePos = source.getPosition();
        MoveHolder moves = new MoveHolder();
        for (Position direction : directions) {
            for (int i = 1; i < Integer.MAX_VALUE; i++) {
                Position targetPosition = sourcePos.plus(direction.times(i));
                if (!board.isValidPosition(targetPosition)) {
                    break;
                }
                Piece targetPiece = board.getPiece(targetPosition);
                if (targetPiece == null) {
                    moves.addMove(new StandardMove(source, targetPosition));
                }
                else {
                    if (targetPiece.getPlayer() != source.getPlayer()) {
                        moves.addMove(new StandardMove(source, targetPosition));
                    }
                    break;
                }
            }
        }
        return moves;
    }
}

class Compound implements IMoveGenerator {
    private final Collection<IMoveGenerator> generators;

    public Compound (IMoveGenerator generator1, IMoveGenerator generator2) {
        this.generators = Collections.unmodifiableList(Arrays.asList(generator1, generator2));
    }

    public Compound(Collection<IMoveGenerator> generators) {
        this.generators = generators;
    }

    @Override
    public MoveHolder generate(Chess chess, Piece source) {
        MoveHolder moves = new MoveHolder();
        for (IMoveGenerator generator : generators) {
            moves.putAll(generator.generate(chess, source));
        }
        return moves;
    }
}

class PawnMoveGenerator implements IMoveGenerator {
    @Override
    public MoveHolder generate(Chess chess, Piece source) {
        Position forwards;
        switch (source.getPlayer()) {
            case PLAYER1:
                forwards = new Position(0, 1);
                break;
            case PLAYER2:
                forwards = new Position(0, -1);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + source.getPlayer());
        }
        Board board = chess.getBoard();
        MoveHolder moves = new MoveHolder();
        Position positionAhead = source.getPosition().plus(forwards);
        if (board.isValidPosition(positionAhead)) {
            Piece pieceInFront = board.getPiece(positionAhead);
            Position position2Ahead = positionAhead.plus(forwards);
            if (pieceInFront == null) {
                if (board.isValidPosition(position2Ahead)){
                    moves.addMove(new StandardMove(source, positionAhead));
                }
                else {

                }
            }
        }
        return null;
    }
}

