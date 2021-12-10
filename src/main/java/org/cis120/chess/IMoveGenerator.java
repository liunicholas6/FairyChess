package org.cis120.chess;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public interface IMoveGenerator {

    MoveHolder generate(Chess chess, Position source);

    IMoveGenerator WHITE_PAWN = new PromotionMoveGenerator(
            new PawnMoveGenerator(Player.PLAYER1),
            p -> PieceFactory.getPiece('Q', Player.PLAYER1, p),
            move -> move.getTarget().getY() == 7);
    IMoveGenerator BLACK_PAWN = new PromotionMoveGenerator(
            new PawnMoveGenerator(Player.PLAYER2),
            p -> PieceFactory.getPiece('Q', Player.PLAYER2, p),
            move -> move.getTarget().getY() == 0);

    IMoveGenerator ROOK = new Rider(0, 1);
    IMoveGenerator KNIGHT = new Leaper(1, 2);
    IMoveGenerator BISHOP = new Rider(1, 1);
    IMoveGenerator QUEEN = new Compound(ROOK, BISHOP);
    IMoveGenerator KING = new Crown();
}

class Leaper implements IMoveGenerator {
    private final Position[] directions;
    public Leaper(int x, int y) {
        directions = new Position(x, y).allDirections();
    }

    @Override
    public MoveHolder generate(Chess chess, Position source) {
        Board board = chess.getBoard();
        Piece sourcePiece = board.getPiece(source);
        MoveHolder moves = new MoveHolder();
        for (Position direction: directions) {
            Position targetPosition = source.plus(direction);
            if (board.isValidPosition(targetPosition)) {
                Piece targetPiece = board.getPiece(targetPosition);
                if (targetPiece == null || targetPiece.getPlayer() != sourcePiece.getPlayer()) {
                        moves.addMove(new Move(sourcePiece, targetPosition));
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
    public MoveHolder generate(Chess chess, Position source) {
        Board board = chess.getBoard();
        Piece sourcePiece = board.getPiece(source);
        MoveHolder moves = new MoveHolder();
        for (Position direction : directions) {
            for (int i = 1; i < Integer.MAX_VALUE; i++) {
                Position targetPosition = source.plus(direction.times(i));
                if (!board.isValidPosition(targetPosition)) {
                    break;
                }
                Piece targetPiece = board.getPiece(targetPosition);
                if (targetPiece == null) {
                    moves.addMove(new Move(sourcePiece, targetPosition));
                }
                else {
                    if (targetPiece.getPlayer() != sourcePiece.getPlayer()) {
                        moves.addMove(new Move(sourcePiece, targetPosition));
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
    public MoveHolder generate(Chess chess, Position source) {
        MoveHolder moves = new MoveHolder();
        for (IMoveGenerator generator : generators) {
            moves.putAll(generator.generate(chess, source));
        }
        return moves;
    }
}

class PromotionMoveGenerator implements IMoveGenerator {
    private final IMoveGenerator originalMoveGenerator;
    private final Function<Position, Piece> makePiece;
    private final Predicate<Move> condition;

    public PromotionMoveGenerator(IMoveGenerator originalMoveGenerator, Function<Position, Piece> makePiece,
                                   Predicate<Move> condition) {
        this.makePiece = makePiece;
        this.originalMoveGenerator = originalMoveGenerator;
        this.condition = condition;
    }

    @Override
    public MoveHolder generate(Chess chess, Position source) {
        MoveHolder moves = originalMoveGenerator.generate(chess, source);
        for (Move move : moves.values()) {
            if (condition.test(move)) {
                moves.addMove(new PromotionMove(move));
            }
        }

        return moves;
    }

    class PromotionMove extends Move {
        private final Move originalMove;
        public PromotionMove(Move originalMove) {
            super(originalMove.getPiece(), originalMove.getTarget());
            this.originalMove = originalMove;
        }
        @Override
        public Piece move(Chess chess) {
            Piece captured = originalMove.move(chess);
            Piece promoted = makePiece.apply(originalMove.target);
            chess.getBoard().setPiece(target, promoted);
            return captured;
        }
    }
}

class PawnMoveGenerator implements IMoveGenerator {
    private final Position forwards;
    public PawnMoveGenerator (Player player) {
        switch (player) {
            case PLAYER1:
                forwards = new Position(0, 1);
                break;
            case PLAYER2:
                forwards = new Position(0, -1);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public MoveHolder generate(Chess chess, Position source) {
        Board board = chess.getBoard();
        Piece sourcePiece = board.getPiece(source);
        MoveHolder moves = new MoveHolder();

        Position posAhead = source.plus(forwards);
        Piece pieceAhead = board.getPiece(posAhead);

        if (pieceAhead == null) {
            moves.addMove(new Move(sourcePiece, posAhead));
            Position pos2Ahead = posAhead.plus(forwards);
            if (!sourcePiece.isMoved() && board.getPiece(pos2Ahead) == null) {
                moves.addMove(new Move(sourcePiece, pos2Ahead));
            }
        }

        Position[] diagonals = new Position[]{
                posAhead.plus(new Position(-1, 0)),
                posAhead.plus(new Position(1, 0))};

        for (Position diagonal : diagonals) {
            if (board.isValidPosition(diagonal)) {
                Piece atDiagonal = board.getPiece(diagonal);
                if (atDiagonal != null && atDiagonal.getPlayer() != sourcePiece.getPlayer()) {
                    moves.addMove(new Move(sourcePiece, diagonal));
                }
            }
        }
        return moves;
    }
}

class Crown implements IMoveGenerator {
    private Position[] directions = new Position[] {
            new Position(-1, -1),
            new Position (-1, 0),
            new Position (-1, 1),
            new Position (0, -1),
            new Position (0, 1),
            new Position (1, -1),
            new Position (1, 0),
            new Position (1, 1)
    };

    @Override
    public MoveHolder generate(Chess chess, Position source) {
        Board board = chess.getBoard();
        Piece sourcePiece = board.getPiece(source);
        MoveHolder moves = new MoveHolder();
        for (Position direction : directions) {
            Position target = source.plus(direction);
            if (board.isValidPosition(target)) {
                Piece atDirection = board.getPiece(target);
                if (atDirection == null || atDirection.getPlayer() != sourcePiece.getPlayer()) {
                    moves.addMove(new Move(sourcePiece, target));
                }
            }
        }
        return moves;
    }
}
