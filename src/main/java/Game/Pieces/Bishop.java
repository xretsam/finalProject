package Game.Pieces;

import Game.Chess;
import Game.Color;
import Game.Type;

public class Bishop extends Piece {
    public Bishop(Color color, Chess chess, int x, int y) {
        type = Type.BISHOP;
        this.color = color;
        this.chess = chess;
        this.x = x;
        this.y = y;
    }

    @Override
    public void evaluateMoves() {
        clear();
        for (int i = y + 1, j = x + 1; i < 8 && j < 8; i++, j++) if (!addMove(j,i)) break;
        for (int i = y - 1, j = x + 1; i >= 0 && j < 8; i--, j++) if (!addMove(j,i)) break;
        for (int i = y + 1, j = x - 1; i < 8 && j >= 0; i++, j--) if (!addMove(j,i)) break;
        for (int i = y - 1, j = x - 1; i >= 0 && j >= 0; i--, j--)  if (!addMove(j,i)) break;
    }
}
