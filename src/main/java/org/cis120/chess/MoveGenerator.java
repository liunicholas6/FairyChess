package org.cis120.chess;

import java.util.*;

public interface MoveGenerator {

    MoveHolder generate(Chess chess, Piece source);

    MoveGenerator ROOK = new Rider(0, 1);
    MoveGenerator KNIGHT = new Leaper(1, 2);
    MoveGenerator BISHOP = new Rider(1, 1);
    MoveGenerator QUEEN = new Compound(ROOK, BISHOP);
}

class Leaper implements MoveGenerator {
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

class Rider implements MoveGenerator {
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

class Compound implements MoveGenerator {
    private final Collection<MoveGenerator> generators;

    public Compound (MoveGenerator generator1, MoveGenerator generator2) {
        this.generators = Collections.unmodifiableList(Arrays.asList(generator1, generator2));
    }

    public Compound(Collection<MoveGenerator> generators) {
        this.generators = generators;
    }

    @Override
    public MoveHolder generate(Chess chess, Piece source) {
        MoveHolder moves = new MoveHolder();
        for (MoveGenerator generator : generators) {
            moves.putAll(generator.generate(chess, source));
        }
        return moves;
    }
}

