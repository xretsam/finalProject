package Game.Pieces;

public class RookStrategy implements PieceStrategy{
    @Override
    public void evaluateMoves(Piece piece) {
        piece.clear();
        int x = piece.x;
        int y = piece.y;
        for (int j = x + 1; j < 8; j++) if(!piece.addMove(j,y)) break;
        for (int j = x - 1; j >= 0; j--) if(!piece.addMove(j,y)) break;
        for (int i = y + 1; i < 8; i++) if(!piece.addMove(x,i)) break;
        for (int i = y - 1; i >= 0; i--) if(!piece.addMove(x,i)) break;
    }
}
