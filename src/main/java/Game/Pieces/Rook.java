package Game.Pieces;

import Game.Chess;
import Game.Color;
import Game.Type;


public class Rook extends Piece {
    public Rook(Color color, Chess chess, int x, int y) {
        type = Type.ROOK;
        this.color = color;
        this.chess = chess;
        this.x = x;
        this.y = y;
    }

    @Override
    public void evaluateMoves() {
        clear();
        for (int j = x + 1; j < 8; j++) if(!addMove(j,y)) break;
        for (int j = x - 1; j >= 0; j--) if(!addMove(j,y)) break;
        for (int i = y + 1; i < 8; i++) if(!addMove(x,i)) break;
        for (int i = y - 1; i >= 0; i--) if(!addMove(x,i)) break;
    }
}
