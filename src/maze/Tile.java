package maze;

/**
 * The Tile class, which handles each block on the maze.
 *
 * @author Aryan Agrawal
 * @version 0.1
 */
public class Tile implements java.io.Serializable {

    /**
     * Each of the four tile types.
     */
    public enum Type {
        CORRIDOR, ENTRANCE, EXIT, WALL;
    }

    /**
     * Stores the tile type enum.
     */
    private Type type;

    /**
     * The class constructor. Sets the tile type.
     *
     * @param typeIn
     */
    private Tile(Type typeIn) {
        type = typeIn;
    }

    /**
     * Converts an input character to the equivalent tile.
     *
     * @param charIn
     * @return Tile
     */
    protected static Tile fromChar(char charIn) {
        return charIn == '#' ? new Tile(Type.WALL)
                : charIn == '.' ? new Tile(Type.CORRIDOR)
                        : charIn == 'e' ? new Tile(Type.ENTRANCE) : charIn == 'x' ? new Tile(Type.EXIT) : null;
    }

    /**
     * Getter method for the tile type.
     *
     * @return Type
     */
    public Type getType() {
        return type;
    }

    /**
     * Checks if a tile can be traveled to.
     *
     * @return boolean
     */
    public boolean isNavigable() {
        return type != Type.WALL ? true : false;
    }

    /**
     * Converts the tile type to the equivalent character.
     *
     * @return String
     */
    public String toString() {
        return type == Type.WALL ? "#"
                : type == Type.CORRIDOR ? "." : type == Type.ENTRANCE ? "e" : type == Type.EXIT ? "x" : null;
    }

}
