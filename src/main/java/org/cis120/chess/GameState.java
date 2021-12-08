package org.cis120.chess;

enum GameStateType {
    NO_PIECE_SELECTED,
    PIECE_SELECTED,
    STALEMATE,
    WIN,
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

    @Override
    public String toString() {
        return "GameState{" +
                "player=" + player +
                ", type=" + type +
                '}';
    }
}
