package Client.Model;

public class Piece {
    private String type;
    private String color;
    private int x;
    private int y;
    private int[] availableMoves;

    public void setType(String type) {
        this.type = type;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setAvailableMoves(int[] availableMoves) {
        this.availableMoves = availableMoves;
    }

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int[] getAvailableMoves() {
        return availableMoves;
    }
}
