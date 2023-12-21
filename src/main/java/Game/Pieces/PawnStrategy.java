package Game.Pieces;

import Game.Chess;
import Game.Color;
import Game.Type;

public class PawnStrategy implements PieceStrategy{

    @Override
    public void evaluateMoves(Piece piece) {
        Color color = piece.color;
        int x = piece.x;
        int y = piece.y;
        Chess chess = piece.chess;

        piece.clear();
        int y0 = piece.color == Color.WHITE ? 1 : 6;
        int dy = piece.color == Color.WHITE ? 1 : -1;
        int[][]availableMoves = piece.availableMoves;
        if (y == y0) {
            if (piece.isTileFree(x, y + 2 * dy) && chess.turn == color) availableMoves[y + 2 * dy][x]++;
        }
        if (piece.isTileFree(x, y + dy) && chess.turn == color) availableMoves[y + dy][x]++;
        if (piece.isOpponentPiece(x - 1, y + dy) || chess.turn != color) piece.addMove(x - 1, y + dy);
        if (piece.isOpponentPiece(x + 1, y + dy) || chess.turn != color) piece.addMove(x + 1, y + dy);
        if (y == y0 + 3 * dy && piece.isOpponentPiece(x - 1, y))
            if (chess.getPieceAt(x - 1, y).type == Type.PAWN && chess.getPieceAt(x - 1, y).equals(chess.lastMovedPiece))
                availableMoves[y + dy][x - 1]++;
        if (y == y0 + 3 * dy && piece.isOpponentPiece(x + 1, y))
            if (chess.getPieceAt(x + 1, y).type == Type.PAWN && chess.getPieceAt(x + 1, y).equals(chess.lastMovedPiece))
                availableMoves[y + dy][x + 1]++;
    }
}
