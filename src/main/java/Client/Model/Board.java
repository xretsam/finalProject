package Client.Model;

public class Board {
    private Piece[][] pieces;
    private String currentTurn;

    private String color;

    private boolean end;

    private String cause;

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }
    public boolean isEnd() {
        return end;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
    public String getCause() {
        return cause;
    }
    public Board() {
        pieces = new Piece[8][8];
    }
    public void setPiece(Piece piece) {
        int x = piece.getX();
        int y = piece.getY();
        pieces[y][x] = piece;
    }

    public void setCurrentTurn(String currentTurn) {
        this.currentTurn = currentTurn;
    }

    public Piece[][] getPieces() {
        return pieces;
    }

    public Piece getPieceAt(int x, int y) {
        return pieces[7 - y][x];
    }
    public String getCurrentTurn() {
        return currentTurn;
    }
}
