package maze;

public class Tile implements java.io.Serializable {

    public enum Type {
        CORRIDOR, ENTRANCE, EXIT, WALL;
    }

    private Type type;

    private Tile(Type typeIn) {
        type = typeIn;
    }

    protected static Tile fromChar(char charIn) {
        return charIn == '#' ? new Tile(Type.WALL)
                : charIn == '.' ? new Tile(Type.CORRIDOR)
                        : charIn == 'e' ? new Tile(Type.ENTRANCE) : charIn == 'x' ? new Tile(Type.EXIT) : null;
    }

    public Type getType() {
        return type;
    }

    public boolean isNavigable() {
        return type != Type.WALL ? true : false;
    }

    public String toString() {
        return type == Type.WALL ? "#"
                : type == Type.CORRIDOR ? "." : type == Type.ENTRANCE ? "e" : type == Type.EXIT ? "x" : null;
    }

}
