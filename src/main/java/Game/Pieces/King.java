package Game.Pieces;

import Game.Chess;
import Game.Color;
import Game.Type;

public class King extends Piece {
    public King(Color color, Chess chess, int x, int y) {
        type = Type.KING;
        this.color = color;
        this.chess = chess;
        this.x = x;
        this.y = y;
    }

    @Override
    public void evaluateMoves() {
        clear();
        for (int i = y - 1; i <= y + 1; i++) {
            for (int j = x - 1; j <= x + 1; j++) {
                if(i == y && j == x) continue;
                addMove(j, i);
            }
        }
        if (chess.turn != color) return;

        for (int i = y - 1; i <= y + 1; i++) {
            for (int j = x - 1; j <= x + 1; j++) {
                if (i > 7 || i < 0 || j > 7 || j < 0) continue;
                if (chess.opponentMoves[i][j] != 0) availableMoves[i][j] = 0;
            }
        }

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                Type[] types;
                if (i == 0 || j == 0) types = new Type[]{Type.ROOK, Type.QUEEN};
                else types = new Type[]{Type.BISHOP, Type.QUEEN};
                checkLine(types, x, y, j, i);
            }
        }

        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (i == 0 || j == 0) continue;
                checkTile(Type.KNIGHT, j, i);
            }
        }
        int dy = color == Color.WHITE ? 1 : -1;
        checkTile(Type.PAWN, x - 1, y + dy);
        checkTile(Type.PAWN, x + 1, y + dy);
        checkCastling(1);
        checkCastling(-1);
    }

    private void checkCastling(int dx){
        if (moveCount == 0) {
            for (int j = x + dx; j < 8 && j >= 0; j += dx) {
                if (chess.opponentMoves[y][j] != 0) break;
                Piece piece = chess.getPieceAt(j, y);
                if (piece != null) {
                    if (piece.type == Type.ROOK && piece.moveCount == 0) {
                        addMove(x + 2 * dx, y);
                    }
                    break;
                }
            }
        }
    }
    private void applyMask(boolean[][] mask, Piece piece) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!mask[i][j]) piece.availableMoves[i][j] = 0;
            }
        }
    }

    private void checkLine(Type[] types, int x, int y, int dx, int dy) {
        Piece defender = null;
        boolean[][] mask = new boolean[8][8];
        for (int i = y + dy, j = x + dx; i < 8 && i >= 0 && j < 8 && j >= 0; i += dy, j += dx) {
            mask[i][j] = true;
            if (chess.getPieceAt(j, i) != null) {
                if (chess.getPieceAt(j, i).color == color) {
                    if (defender != null) return;
                    defender = chess.getPieceAt(j, i);
                } else {
                    for (Type type : types) {
                        if (chess.getPieceAt(j, i).type == type) {
                            if (defender == null) {
                                for (int k = 0; k < 8; k++) {
                                    for (int l = 0; l < 8; l++) {
                                        if (!isOpponentPiece(l, k) && !isTileFree(l, k))
                                            applyMask(mask, chess.getPieceAt(l, k));
                                    }
                                }
                            } else applyMask(mask, defender);
                            return;
                        }
                    }
                }
            }
        }
    }

    private void checkTile(Type type, int x, int y) {
        Piece piece = chess.getPieceAt(x, y);
        if (piece != null) {
            if (piece.color != color && piece.type == type) {
                boolean[][] mask = new boolean[8][8];
                mask[y][x] = true;
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (!isOpponentPiece(x, y) && !isTileFree(x, y)) applyMask(mask, chess.getPieceAt(x, y));
                    }
                }
            }
        }
    }

    private void truncate(boolean[][] mask) {
    }
}
