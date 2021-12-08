package org.cis120.chess;

import java.util.Arrays;

enum Player {
    PLAYER1,
    PLAYER2
}

/**
 * Class that represents a chess piece. Every piece has a sprite, a symbol (used for move notation),
 * a moveGenerator that creates all the moves for a piece, and a player.
 */

public class Piece {
    private final char symbol;
    private final MoveGenerator moveGenerator;
    private final Player player;

    public Piece (char symbol, MoveGenerator moveGenerator, Player player) {
        this.symbol = symbol;
        this.moveGenerator = moveGenerator;
        this.player = player;
    }

    public char getSymbol() {
        return symbol;
    }

    public Player getPlayer() {
        return player;
    }

    public MoveGenerator getMoveGenerator() {
        return moveGenerator;
    }

    @Override
    public String toString() {
        return "Piece{" +
                "symbol=" + symbol +
                ", moveGenerator=" + moveGenerator +
                ", player=" + player +
                '}';
    }

    public static Piece Rook(Player player) {
        return new Piece('R', MoveGenerator.ROOK, player);
    }

    public static Piece Knight(Player player) {
        return new Piece('N', MoveGenerator.KNIGHT, player);
    }

    public static Piece Bishop(Player player) {
        return new Piece('B', MoveGenerator.BISHOP, player);
    }

    public static Piece Queen(Player player) {
        return new Piece ('Q', MoveGenerator.QUEEN, player);
    }
}
