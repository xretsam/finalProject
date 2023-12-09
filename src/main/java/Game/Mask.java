package Game;

import Game.Pieces.Piece;

public class Mask {
    int[][] moves;

    public Mask() {
        moves = new int[8][8];
    }

    public void clear() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                moves[i][j] = 0;
            }
        }
    }

    public void andMask(Mask mask) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (mask.moves[i][j] == 0) moves[i][j] = 0;
            }
        }
    }

    public void addMask(Mask mask) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                moves[i][j] += mask.moves[i][j];
            }
        }
    }

    public void inc(int x, int y) {
        moves[y][x]++;
    }

}
