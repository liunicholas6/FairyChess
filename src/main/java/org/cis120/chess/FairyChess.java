package org.cis120.chess;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class FairyChess {

    private Board board;
    private Map<Player, Set<Tile>> tilesWithPieces;
    private MoveHolder selectedTileMoves;

    public FairyChess (Board board, Collection<Tile> tiles) {
        this.board = board;
    }
}
