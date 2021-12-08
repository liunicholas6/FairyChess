package org.cis120.chess;

import java.util.Collection;
import java.util.TreeMap;

public class MoveHolder extends TreeMap<Position, Move>{
    public void addMove(Move move) {
        put(move.getTarget(), move);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < 8; r++) {
            sb.append("----");
        }
        sb.append("\n");
        for (int c = 8 - 1; c >= 0; c--) {
            sb.append("| ");
            for (int r = 0; r < 8; r++) {
                sb.append((containsKey(new Position(r, c)))? 'T' : ' ');
                sb.append(" | ");
            }
            sb.append("\n");
            for (int r = 0; r < 8; r++) {
                sb.append("----");
            }
            sb.append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }
}