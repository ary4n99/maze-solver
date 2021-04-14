package maze;

public class Coordinate {

    private int x;

    private int y;

    public Coordinate(int xIn, int yIn) {
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}