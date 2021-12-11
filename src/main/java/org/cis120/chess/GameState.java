package org.cis120.chess;

enum GameStateType {
    RUNNING,
    STALEMATE,
    CHECKMATE,
}

enum Player {
    PLAYER1,
    PLAYER2
}

public class GameState {
    private final Player player;
    private final GameStateType type;

    public GameState(GameStateType type, Player player) {
        this.type = type;
        this.player = player;
    }

    public GameState(GameStateType type) {
        this.type = type;
        this.player = null;
    }

    public GameStateType getType() {
        return type;
    }

    public Player getPlayer() {
        return player;
    }

    public static Player getOtherPlayer(Player player) {
        switch (player) {
            case PLAYER1:
                return Player.PLAYER2;
            case PLAYER2:
                return Player.PLAYER1;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return "GameState{" +
                "player=" + player +
                ", type=" + type +
                '}';
    }
}
