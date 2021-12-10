package org.cis120.chess;

import java.util.*;
import java.util.stream.Collectors;

class Chess{
    private final Board board;
    private final ArrayList<Move> moveHistory;

    private GameState gameState;
    private Piece selectedPiece;
    private MoveHolder selectedMoves;

    public Chess() {
        board = new Board(8, 8);
        this.moveHistory = new ArrayList<>();
        this.gameState = new GameState(GameStateType.RUNNING, Player.PLAYER1);

        for (int x = 0; x < 8; x++) {
            board.placePiece(PieceFactory.getPiece('P', Player.PLAYER1, x, 1));
            board.placePiece(PieceFactory.getPiece('P', Player.PLAYER2, x, 6));
        }
        constructorHelper(Player.PLAYER1);
        constructorHelper(Player.PLAYER2);
    }

    private void constructorHelper(Player player) {
        int y = player == Player.PLAYER1 ? 0 : 7;
        board.placePiece(PieceFactory.getPiece('R', player, 0, y));
        board.placePiece(PieceFactory.getPiece('N', player, 1, y));
        board.placePiece(PieceFactory.getPiece('B', player, 2, y));
        board.placePiece(PieceFactory.getPiece('Q', player, 3, y));
        board.placePiece(PieceFactory.getPiece('K', player, 4, y));
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

    public MoveHolder generateMoves(Piece piece) {
        Chess copy = new Chess();
        List<Move> moveList = piece.generateMoves(copy).values()
                .stream().parallel()
                .filter(move -> {
                    board.copyOnto(copy.board);
                    move.move(copy);
                    return !copy.inCheck(piece.getPlayer());
                })
                .collect(Collectors.toList());
        MoveHolder moves = new MoveHolder();
        for (Move move : moveList) {
            moves.addMove(move);
        }
        return moves;
    }

    public void handlePositionalInput(Position position) {
        if (selectedMoves != null && selectedMoves.containsKey(position)) {
            movePiece(position);
        }
        else {
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
    }

    public void movePiece(Position pos) {
        Move move = selectedMoves.get(pos);
        moveHistory.add(move);
        move.move(this);
        selectedPiece = null;
        selectedMoves = null;
        nextTurn();
    }

    public boolean inCheck(Player player) {
        List<Piece> pieceList = getPieces();
        Position kingPos = pieceList
                .stream().parallel()
                .filter(piece -> piece.getPlayer() == player && piece.getSymbol() == 'K')
                .findAny().get().getPosition();
        return pieceList.stream().parallel()
                .map(piece -> piece.generateMoves(this).values()
                        .stream().parallel()
                        .anyMatch(move -> move.getCapturePos().equals(kingPos)))
                .reduce(false, (hd, acc) -> hd || acc);
    }

    public void nextTurn() {
        gameState = new GameState(GameStateType.RUNNING, GameState.getOtherPlayer(gameState.getPlayer()));
        System.out.println(inCheck(gameState.getPlayer()));
    }

    public static void main(String[] args) {
        Chess chess = new Chess();
        chess.handlePositionalInput(new Position("a1"));
        Piece whiteRook = chess.selectedPiece;
        System.out.println(whiteRook);
        MoveHolder generatedMoves = chess.selectedMoves;
        System.out.println(generatedMoves);
        generatedMoves.get(new Position("a3")).move(chess);
        System.out.print(chess.board);
    }
}