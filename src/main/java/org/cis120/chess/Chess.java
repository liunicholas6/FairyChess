package org.cis120.chess;

import java.util.*;
import java.util.stream.Collectors;

class Chess {
    private final Board board;
    private final ArrayList<Move> moveHistory;
    private final Map<Player, Piece> royals;

    private GameState gameState;
    private Piece selectedPiece;
    private MoveHolder selectedMoves;

    public Chess() {
        board = new Board(8, 8);
        this.moveHistory = new ArrayList<>();
        royals = new EnumMap<>(Player.class);
        this.gameState = new GameState(GameStateType.RUNNING, Player.PLAYER1);

        for (int x = 0; x < 8; x++) {
            board.placePiece(PieceFactory.getPiece('P', Player.PLAYER1, x, 1));
            board.placePiece(PieceFactory.getPiece('P', Player.PLAYER2, x, 6));
        }
        constructorHelper(Player.PLAYER1);
        constructorHelper(Player.PLAYER2);
    }

    public Chess(Chess other) {
        board = new Board(8, 8);
        other.board.copyOnto(board);
        this.royals = new EnumMap<>(Player.class);
        for (Piece oldRoyal : other.royals.values()) {
            Piece newRoyal = oldRoyal.copy();
            board.placePiece(newRoyal);
            royals.put(newRoyal.getPlayer(), newRoyal);
        }
        this.moveHistory = other.moveHistory;
        this.gameState = other.gameState;
    }

    private void constructorHelper(Player player) {
        int y = player == Player.PLAYER1 ? 0 : 7;
        board.placePiece(PieceFactory.getPiece('R', player, 0, y));
        board.placePiece(PieceFactory.getPiece('N', player, 1, y));
        board.placePiece(PieceFactory.getPiece('B', player, 2, y));
        board.placePiece(PieceFactory.getPiece('Q', player, 3, y));

        Piece king = PieceFactory.getPiece('K', player, 4, y);
        royals.put(king.getPlayer(), king);
        board.placePiece(king);

        board.placePiece(PieceFactory.getPiece('B', player, 5, y));
        board.placePiece(PieceFactory.getPiece('N', player, 6, y));
        board.placePiece(PieceFactory.getPiece('R', player, 7, y));
    }

    public Board getBoard() {
        return board;
    }

    public ArrayList<Piece> getPieces() {
        return board.getPieces();
    }

    public GameState getGameState() {
        return gameState;
    }

    public MoveHolder getSelectedMoves() {
        return selectedMoves;
    }

    public ArrayList<Move> getMoveHistory() {
        return moveHistory;
    }

    public Move getLastMove() {
        return (moveHistory.size() == 0) ? null : moveHistory.get(moveHistory.size() - 1);
    }

    public MoveHolder generateMoves(Piece piece) {
        List<Move> validMoves = piece.generateMoves(this).values()
                .stream().parallel()
                .filter(move -> {
                    Chess copy = new Chess(this);
                    move.move(copy);
                    return !copy.inCheck(piece.getPlayer());
                })
                .collect(Collectors.toList());

        MoveHolder moves = new MoveHolder();
        for (Move move : validMoves) {
            moves.addMove(move);
        }
        return moves;
    }

    public void handlePositionalInput(Position position) {
        if (selectedMoves != null && selectedMoves.containsKey(position)) {
            movePiece(position);
        } else {
            selectPiece(position);
        }
    }

    public void selectPiece(Position pos) {
        Piece pieceAtPosition = board.getPiece(pos);
        selectedPiece = (pieceAtPosition == null ||
                pieceAtPosition.getPlayer() == gameState.getPlayer()) ?
                pieceAtPosition : null;
        selectedMoves = (selectedPiece == null) ? null :
                generateMoves(selectedPiece);
        if (royals.containsValue(selectedPiece) && !selectedPiece.isMoved()) {
            makeKingSideCastle();
            makeQueenSideCastle();
        }
    }

    private void makeKingSideCastle() {
        int y = gameState.getPlayer() == Player.PLAYER1 ? 0 : 7;
        Piece rook = board.getPiece(new Position(7, y));
        if (rook == null || rook.isMoved()) {
            return;
        }
        for (int x = 5; x <= 6; x++) {
            if (board.getPiece(new Position(x, y)) != null) {
                return;
            }
        }
        for (int x = 4; x <= 6; x++) {
            if (isThreatened(gameState.getPlayer(), new Position(x, y))) {
                return;
            }
        }
        selectedMoves.addMove(new CastleMove(y, true));
    }

    private void makeQueenSideCastle() {
        int y = gameState.getPlayer() == Player.PLAYER1 ? 0 : 7;
        Piece rook = board.getPiece(new Position(0, y));
        if (rook == null || rook.isMoved()) {
            return;
        }
        for (int x = 1; x <= 3; x++) {
            if (board.getPiece(new Position(x, y)) != null) {
                return;
            }
        }
        for (int x = 2; x <= 4; x++) {
            if (isThreatened(gameState.getPlayer(), new Position(x, y))) {
                return;
            }
        }
        selectedMoves.addMove(new CastleMove(y, false));
    }

    public void movePiece(Position pos) {
        Move move = selectedMoves.get(pos);
        moveHistory.add(move);
        move.move(this);
        selectedPiece = null;
        selectedMoves = null;
        nextTurn();
    }

    public boolean isThreatened(Player player, Position position) {
        return getPieces().stream().parallel()
                .filter(piece -> piece.getPlayer() != player && !royals.containsValue(piece))
                .map(piece -> piece.generateMoves(this).values()
                        .stream().parallel()
                        .anyMatch(move -> move.getCapturePos().equals(position)))
                .reduce(false, (elem, acc) -> elem || acc);
    }

    public boolean inCheck(Player player) {
        return isThreatened(player, royals.get(player).getPosition());
    }

    public void nextTurn() {
        Player thisPlayer = gameState.getPlayer();
        Player nextPlayer = GameState.getOtherPlayer(thisPlayer);
        boolean noMoves = getPieces()
                .stream().parallel()
                .filter(piece -> piece.getPlayer() == nextPlayer)
                .map(piece -> generateMoves(piece).size() == 0)
                .reduce(true, (elem, acc) -> elem && acc);
        if (noMoves) {
            gameState = new GameState(inCheck(nextPlayer) ? GameStateType.CHECKMATE : GameStateType.STALEMATE, thisPlayer);
        }
        else {
            gameState = new GameState(GameStateType.RUNNING, nextPlayer);
        }
    }
}