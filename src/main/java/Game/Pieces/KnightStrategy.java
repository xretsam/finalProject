package Game.Pieces;

public class KnightStrategy implements PieceStrategy{

    @Override
    public void evaluateMoves(Piece piece) {
        int x = piece.x;
        int y = piece.y;
        piece.clear();
        piece.addMove(x+1,y+2);
        piece.addMove(x-1,y+2);
        piece.addMove(x+1,y-2);
        piece.addMove(x-1,y-2);
        piece.addMove(x+2,y+1);
        piece.addMove(x-2,y+1);
        piece.addMove(x+2,y-1);
        piece.addMove(x-2,y-1);
    }
}
