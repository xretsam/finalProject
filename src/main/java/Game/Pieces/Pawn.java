package Game.Pieces;

import Game.Chess;
import Game.Color;
import Game.Type;

public class Pawn extends Piece {

    public Pawn(Color color, Chess chess, int x, int y) {
        type = Type.PAWN;
        this.color = color;
        this.chess = chess;
        this.x = x;
        this.y = y;
    }

    @Override
    public void evaluateMoves() {
        clear();
        int y0 = color == Color.WHITE ? 1 : 6;
        int dy = color == Color.WHITE ? 1 : -1;
        if (y == y0) {
            if (isTileFree(x, y + 2 * dy) && chess.turn == color) this.availableMoves[y + 2 * dy][x]++;
        }
        if (isTileFree(x, y + dy) && chess.turn == color) this.availableMoves[y + dy][x]++;
        if (isOpponentPiece(x - 1, y + dy) || chess.turn != color) addMove(x - 1, y + dy); //TODO: check the correctness
        if (isOpponentPiece(x + 1, y + dy) || chess.turn != color) addMove(x + 1, y + dy);
        if (y == y0 + 3 * dy && isOpponentPiece(x - 1, y))
            if (chess.getPieceAt(x - 1, y).type == Type.PAWN && chess.getPieceAt(x - 1, y).equals(chess.lastMovedPiece))
                availableMoves[y + dy][x - 1]++;
        if (y == y0 + 3 * dy && isOpponentPiece(x + 1, y))
            if (chess.getPieceAt(x + 1, y).type == Type.PAWN && chess.getPieceAt(x + 1, y).equals(chess.lastMovedPiece))
                availableMoves[y + dy][x + 1]++;
    }
}
