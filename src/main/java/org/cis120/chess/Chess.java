package org.cis120.chess;

import org.cis120.Game;

import java.util.*;

enum Player {
    PLAYER1,
    PLAYER2
}

class Chess {
    private final Board board;
    private final Piece[] pieces;
    private final ArrayList<Move> moveHistory;

    private GameState gameState;
    private Piece selectedPiece;
    private MoveHolder selectedMoves;

    public Chess() {
        board = new Board(8, 8);
        this.moveHistory = new ArrayList<>();
        this.gameState = new GameState(GameStateType.RUNNING, Player.PLAYER1);

        pieces = new Piece[] {
                Piece.Rook(Player.PLAYER1, new Position("a1")),
                Piece.Knight(Player.PLAYER1, new Position("b1")),
                Piece.Bishop(Player.PLAYER1, new Position("c1")),
                Piece.Queen(Player.PLAYER1, new Position("d1")),

                Piece.Bishop(Player.PLAYER1, new Position("f1")),
                Piece.Knight(Player.PLAYER1, new Position("g1")),
                Piece.Rook(Player.PLAYER1, new Position("h1")),

                Piece.Rook(Player.PLAYER2, new Position("a8")),
                Piece.Knight(Player.PLAYER2, new Position("b8")),
                Piece.Bishop(Player.PLAYER2, new Position("c8")),
                Piece.Queen(Player.PLAYER2, new Position("d8")),

                Piece.Bishop(Player.PLAYER2, new Position("f8")),
                Piece.Knight(Player.PLAYER2, new Position("g8")),
                Piece.Rook(Player.PLAYER2, new Position("h8")),
        };
        for (Piece piece : pieces) {
            board.placePiece(piece);
        }
    }

    public Board getBoard() {
        return board;
    }

    public Piece[] getPieces() {
        return pieces;
    }

    public GameState getGameState() {
        return gameState;
    }

    public MoveHolder getSelectedMoves() {
        return selectedMoves;
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
        selectedMoves = (selectedPiece == null) ?
                null : selectedPiece.generateMoves(this);
    }

    public void movePiece(Position pos) {
        Move move = selectedMoves.get(pos);
        moveHistory.add(move);
        move.move(board);
        selectedPiece = null;
        selectedMoves = null;
        nextTurn();
    }

    public void nextTurn() {
        gameState = new GameState(GameStateType.RUNNING, gameState.getOtherPlayer());
    }

    public static void main(String[] args) {
        Chess chess = new Chess();
        chess.handlePositionalInput(new Position("a1"));
        Piece whiteRook = chess.selectedPiece;
        System.out.println(whiteRook);
        MoveHolder generatedMoves = chess.selectedMoves;
        System.out.println(generatedMoves);
        generatedMoves.get(new Position("a3")).move(chess.board);
        System.out.print(chess.board);
    }
}