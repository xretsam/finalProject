package Game.Pieces;


import Game.Chess;
import Game.Color;
import Game.Type;

public class Piece {
    public Type type;
    public Color color;

    public int x;
    public int y;

    protected Chess chess;

    public int moveCount = 0;
    public int[][] availableMoves = new int[8][8];

    protected PieceStrategy strategy;

    public void evaluateMoves() {
        strategy.evaluateMoves(this);
    }

    protected void clear() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                availableMoves[i][j] = 0;
            }
        }
    }

    protected boolean isTileFree(int x, int y) {
        if (x > 7 || x < 0 || y > 7 || y < 0) return false;
        return chess.getPieceAt(x, y) == null;
    }


    protected boolean isOpponentPiece(int x, int y) {
        if (chess.getPieceAt(x, y) != null) return chess.getPieceAt(x, y).color != color;
        return false;
    }
    protected boolean isAllyPiece(int x, int y) {
        if (chess.getPieceAt(x, y) != null) return chess.getPieceAt(x, y).color == color;
        return false;
    }
    protected boolean addMove(int x, int y) {
        if (x > 7 || x < 0 || y > 7 || y < 0) return false;
        if (isTileFree(x, y) || (isAllyPiece(x, y) && chess.turn != color) || isOpponentPiece(x, y)) {
            availableMoves[y][x]++;
            if(isTileFree(x,y)) return true;
            else if ((isAllyPiece(x, y) && chess.turn != color)) return false;
            return false;
        }
        return false;
    }
    public void printMoves() {
        StringBuilder sb = new StringBuilder();
        sb.append("  A B C D E F G H   \n");
        for (int i = 7; i >= 0; i--) {
            sb.append(i + 1).append(" ");
            for (int j = 0; j < 8; j++) {
                if(this.availableMoves[i][j] == 0) {
                    sb.append("- ");
                } else sb.append("* ");
            }
            sb.append(i + 1).append(" \n");
        }
        sb.append("  A B C D E F G H   \n");
        System.out.println(sb);
    }
    public String getType(){
        return switch (type) {
            case PAWN -> "pawn";
            case ROOK -> "rook";
            case KNIGHT -> "knight";
            case BISHOP -> "bishop";
            case QUEEN -> "queen";
            case KING -> "king";
        };
    }

    public String getColor(){
        return switch(color) {
            case WHITE -> "white";
            case BLACK -> "black";
        };
    }
    private Piece(Chess chess, int x, int y, Color color, PieceStrategy strategy, Type type){
        this.color = color;
        this.chess = chess;
        this.x = x;
        this.y = y;
        this.strategy = strategy;
        this.type = type;
    }
    public static Piece createPawn(Chess chess, int x, int y, Color color) {
        return new Piece(chess,x,y,color,new PawnStrategy(), Type.PAWN);
    }
    public static Piece createRook(Chess chess, int x, int y, Color color) {
        return new Piece(chess,x,y,color,new RookStrategy(), Type.ROOK);
    }
    public static Piece createKnight(Chess chess, int x, int y, Color color) {
        return new Piece(chess,x,y,color,new KnightStrategy(), Type.KNIGHT);
    }
    public static Piece createBishop(Chess chess, int x, int y, Color color) {
        return new Piece(chess,x,y,color,new BishopStrategy(), Type.BISHOP);
    }
    public static Piece createQueen(Chess chess, int x, int y, Color color) {
        return new Piece(chess,x,y,color,new QueenStrategy(), Type.QUEEN);
    }
    public static Piece createKing(Chess chess, int x, int y, Color color) {
        return new Piece(chess,x,y,color,new KingStrategy(), Type.KING);
    }
}
