package Game.Pieces;

import Game.Chess;
import Game.Color;
import Game.Type;

public class Queen extends Piece {

    private final Piece[] behaviorContainer;
    public Queen(Color color, Chess chess, int x, int y) {
        type = Type.QUEEN;
        this.color = color;
        this.chess = chess;
        this.x = x;
        this.y = y;
        behaviorContainer = new Piece[]{new Bishop(color, chess, this.x, this.y), new Rook(color, chess, this.x, this.y)};
    }

    @Override
    public void evaluateMoves() {
        clear();
        for(Piece piece : behaviorContainer) {
            piece.x = x;
            piece.y = y;
            piece.evaluateMoves();
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    this.availableMoves[i][j] += piece.availableMoves[i][j];
                }
            }
        }
    }
}
