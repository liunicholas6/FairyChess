package org.cis120.chess;

import java.util.TreeMap;

public class MoveHolder extends TreeMap<Position, Move>{

    public void addMove(Move move) {
        put(move.getTarget(), move);
    }

    public boolean inThreatRange(Tile tile) {
        for (Move move : values()) {
            if (move.getTarget().equals(tile.getPos())) {
                return true;
            }
        }
        return false;
    }
}