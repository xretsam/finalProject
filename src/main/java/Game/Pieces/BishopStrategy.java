package Game.Pieces;

public class BishopStrategy implements PieceStrategy{

    @Override
    public void evaluateMoves(Piece piece) {
        int x = piece.x;
        int y = piece.y;
        piece.clear();
        for (int i = y + 1, j = x + 1; i < 8 && j < 8; i++, j++) if (!piece.addMove(j,i)) break;
        for (int i = y - 1, j = x + 1; i >= 0 && j < 8; i--, j++) if (!piece.addMove(j,i)) break;
        for (int i = y + 1, j = x - 1; i < 8 && j >= 0; i++, j--) if (!piece.addMove(j,i)) break;
        for (int i = y - 1, j = x - 1; i >= 0 && j >= 0; i--, j--)  if (!piece.addMove(j,i)) break;
    }
}
