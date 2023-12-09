package Game.Pieces;

import Game.Chess;
import Game.Color;
import Game.Type;

public class Knight extends Piece{
    public Knight(Color color, Chess chess, int x, int y) {
        type = Type.KNIGHT;
        this.color = color;
        this.chess = chess;
        this.x = x;
        this.y = y;
    }

    @Override
    public void evaluateMoves() {
        clear();
        addMove(x+1,y+2);
        addMove(x-1,y+2);
        addMove(x+1,y-2);
        addMove(x-1,y-2);
        addMove(x+2,y+1);
        addMove(x-2,y+1);
        addMove(x+2,y-1);
        addMove(x-2,y-1);
    }
}
