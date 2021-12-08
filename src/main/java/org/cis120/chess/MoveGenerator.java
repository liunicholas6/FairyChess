package org.cis120.chess;

import java.util.*;

public interface MoveGenerator {

    MoveHolder generate(Board board, Tile source);

    MoveGenerator ROOK = new Rider(0, 1);
    MoveGenerator KNIGHT = new Leaper(1, 2);
    MoveGenerator BISHOP = new Rider(1, 1);
    MoveGenerator QUEEN = new Compound(ROOK, BISHOP);
    MoveGenerator KING = new Crown();
}

class Leaper implements MoveGenerator {
    private final Position[] directions;
    public Leaper(int x, int y) {
        directions = new Position(x, y).allDirections();
    }

    @Override
    public MoveHolder generate(Board board, Tile source) {
        Position sourcePos = source.getPos();
        MoveHolder moves = new MoveHolder();
        for (Position direction: directions) {
            Position targetPosition = sourcePos.plus(direction);
            Tile targetTile = board.getTile(targetPosition);
            if (targetTile != null) {
                Piece targetPiece = targetTile.getPiece();
                if (targetPiece != null) {
                    if (targetPiece.getPlayer() != source.getPiece().getPlayer()) {
                        moves.addMove(new StandardMove(source, targetPosition));
                    }
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
    public MoveHolder generate(Board board, Tile source) {
        Position sourcePos = source.getPos();
        MoveHolder moves = new MoveHolder();
        for (Position direction : directions) {
            for (int i = 1; i < Integer.MAX_VALUE; i++) {
                Position targetPosition = sourcePos.plus(direction.times(i));
                Tile targetTile = board.getTile(targetPosition);
                if (targetTile == null) {
                    break;
                }
                Piece pieceAtTarget = targetTile.getPiece();
                if (pieceAtTarget == null) {
                    moves.addMove(new StandardMove(source, targetPosition));
                }
                else {
                    if (pieceAtTarget.getPlayer() != source.getPiece().getPlayer()) {
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
    public MoveHolder generate(Board board, Tile source) {
        MoveHolder moves = new MoveHolder();
        for (MoveGenerator generator : generators) {
            moves.putAll(generator.generate(board, source));
        }
        return moves;
    }
}

class Crown implements MoveGenerator {
    private final Position[] directions =
            {new Position (-1, -1), new Position(-1, 0), new Position(-1, 1),
                    new Position (0, -1), new Position(0, 1),
                    new Position (1, -1), new Position(1, 0), new Position(1, 1)};

    @Override
    public MoveHolder generate(Board board, Tile source) {
        MoveHolder moves = new MoveHolder();
        Position sourcePos = source.getPos();
        for (Position direction : directions) {
            moves.addMove(new StandardMove(source, sourcePos.plus(direction)));
        }
        return moves;
    }
}

