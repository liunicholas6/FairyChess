package org.cis120.chess;

public class Piece {
    private final String filePath;
    private final char symbol;
    private final IMoveGenerator moveGenerator;
    private final Player player;
    private Position position;
    private boolean moved = false;

    public Piece (String filePath, char symbol, IMoveGenerator IMoveGenerator, Player player, Position position) {
        this.filePath = filePath;
        this.symbol = symbol;
        this.moveGenerator = IMoveGenerator;
        this.player = player;
        this.position = position;
    }

    public String getFilePath() {
        return filePath;
    }

    public char getSymbol() {
        return symbol;
    }

    public MoveHolder generateMoves(Chess chess) {
        return moveGenerator.generate(chess, this);
    }

    public Player getPlayer() {
        return player;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void markMoved() {
        this.moved = true;
    }

    public boolean isMoved() {
        return this.moved;
    }

    @Override
    public String toString() {
        return "Piece{" +
                "symbol=" + symbol +
                ", moveGenerator=" + moveGenerator +
                ", player=" + player +
                '}';
    }

    public static Piece ChessPiece(String name, char symbol, IMoveGenerator moveGenerator, Player player, Position position) {
        StringBuilder filePathBuilder = new StringBuilder();
        filePathBuilder.append("files/ChessSprites/")
                .append((player == Player.PLAYER1) ? "w" : "b")
                .append("_")
                .append(name)
                .append(".png");
        return new Piece (filePathBuilder.toString(), symbol, moveGenerator, player, position);
    }

    public static Piece Rook(Player player, Position position) {
        return ChessPiece("rook",'R', IMoveGenerator.ROOK, player, position);
    }

    public static Piece Knight(Player player, Position position) {
        return ChessPiece("knight", 'N', IMoveGenerator.KNIGHT, player, position);
    }

    public static Piece Bishop(Player player, Position position) {
        return ChessPiece("bishop", 'B', IMoveGenerator.BISHOP, player, position);
    }

    public static Piece Queen(Player player, Position position) {
        return ChessPiece("queen", 'Q', IMoveGenerator.QUEEN, player, position);
    }
}