package org.cis120.chess;

public class PieceFactory {
    private static Piece ChessPiece(String name, char symbol, IMoveGenerator moveGenerator, Player player, Position position) {
        StringBuilder filePathBuilder = new StringBuilder();
        filePathBuilder.append("files/ChessSprites/")
                .append((player == Player.PLAYER1) ? "w" : "b")
                .append("_")
                .append(name)
                .append(".png");
        return new Piece (filePathBuilder.toString(), symbol, moveGenerator, player, position);
    }

    public static Piece getPiece(char pieceSymbol, Player player, int x, int y) {
        return getPiece(pieceSymbol, player, new Position(x, y));
    }

    public static Piece getPiece(char pieceSymbol, Player player, Position position) {
        switch (pieceSymbol) {
            case 'P' :
                IMoveGenerator moveGenerator = (player == Player.PLAYER1) ?
                        IMoveGenerator.WHITE_PAWN : IMoveGenerator.BLACK_PAWN;
                return ChessPiece("pawn", 'P', moveGenerator, player, position);
            case 'R' :
                return ChessPiece("rook",'R', IMoveGenerator.ROOK, player, position);
            case 'N' :
                return ChessPiece("knight", 'N', IMoveGenerator.KNIGHT, player, position);
            case 'B' :
                return ChessPiece("bishop", 'B', IMoveGenerator.BISHOP, player, position);
            case 'Q' :
                return ChessPiece("queen", 'Q', IMoveGenerator.QUEEN, player, position);
            case 'K' :
                return ChessPiece("king", 'K', IMoveGenerator.KING, player, position);
            default:
                throw new IllegalArgumentException();
        }
    }
}
