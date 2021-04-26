package maze;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the main functionality of the maze.
 *
 * @author Aryan Agrawal
 * @version 0.1
 */
public class Maze implements java.io.Serializable {

    /**
     * The four directions.
     */
    public enum Direction {
        NORTH, SOUTH, EAST, WEST;
    }

    /**
     * An inner class to store tile coordinates.
     */
    public class Coordinate {

        /**
         * The respective x & y locations for the tile.
         */
        private int x, y;

        /**
         * The class constructor. Sets the input coordinates.
         *
         * @param xIn
         * @param yIn
         */
        public Coordinate(int xIn, int yIn) {
            x = xIn;
            y = yIn;
        }

        /**
         * Getter method for the x coordinate.
         *
         * @return int
         */
        public int getX() {
            return x;
        }

        /**
         * Getter method for the y coordinate.
         *
         * @return int
         */
        public int getY() {
            return y;
        }

        /**
         * Converts the coordinate to a string with proper formatting.
         *
         * @return String
         */
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    /**
     * Stores the entrance tile.
     */
    private Tile entrance;

    /**
     * Stores the exit tile.
     */
    private Tile exit;

    /**
     * A 2D array storing all of the maze tiles.
     */
    private List<List<Tile>> tiles;

    /**
     * The class constructor. Instantiates the 2D tile array.
     */
    private Maze() {
        tiles = new ArrayList<List<Tile>>();
    }

    /**
     * Reads the maze from an input .txt file.
     *
     * @param filePath
     * @return Maze
     * @throws IOException
     * @throws NullPointerException
     */
    public static Maze fromTxt(String filePath) throws IOException, NullPointerException {
        Maze maze = new Maze();
        FileReader file = new FileReader(filePath);
        int fileInt = file.read();
        maze.tiles.add(new ArrayList<Tile>());
        while (fileInt != -1) {
            char fileChar = (char) fileInt;
            if (fileChar == '\n') {
                maze.tiles.add(new ArrayList<Tile>());
            } else if (fileChar == 'e' || fileChar == 'x' || fileChar == '#' || fileChar == '.') {
                Tile newTile = Tile.fromChar(fileChar);
                maze.tiles.get(maze.tiles.size() - 1).add(newTile);
                if (fileChar == 'e') {
                    try {
                        maze.setEntrance(newTile);
                    } catch (MultipleEntranceException e) {
                        throw new MultipleEntranceException("Multiple entrances in the maze.");
                    }
                } else if (fileChar == 'x') {
                    try {
                        maze.setExit(newTile);
                    } catch (MultipleExitException e) {
                        throw new MultipleExitException("Multiple exits in the maze.");
                    }
                }
            } else {
                throw new InvalidMazeException("Invalid character in the maze.");
            }
            fileInt = file.read();
        }
        file.close();

        int rowSize = maze.tiles.get(0).size();
        for (List<Tile> row : maze.tiles) {
            if (rowSize != row.size())
                throw new RaggedMazeException("Row sizes in the maze don't match.");
        }
        if (maze.getEntrance() == null) {
            throw new NoEntranceException("No entrance found in the maze.");
        } else if (maze.getExit() == null) {
            throw new NoExitException("No exit found in the maze.");
        }
        return maze;
    }

    /**
     * Gets the adjacent tile given a direction and base tile.
     *
     * @param tileIn
     * @param direction
     * @return Tile
     */
    public Tile getAdjacentTile(Tile tileIn, Direction direction) {
        int oldTileX = getTileLocation(tileIn).getX();
        int oldTileY = getTileLocation(tileIn).getY();
        int newTileX = direction == Direction.WEST ? oldTileX - 1
                : direction == Direction.EAST ? oldTileX + 1 : oldTileX;
        int newTileY = direction == Direction.SOUTH ? oldTileY - 1
                : direction == Direction.NORTH ? oldTileY + 1 : oldTileY;
        if (newTileY > tiles.size() - 1 || newTileX > tiles.get(0).size() - 1 || newTileY < 0 || newTileX < 0) {
            return null;
        } else {
            return getTileAtLocation(new Coordinate(newTileX, newTileY));
        }
    }

    /**
     * Getter method for the tile entrance.
     *
     * @return Tile
     */
    public Tile getEntrance() {
        return entrance;
    }

    /**
     * Getter method for the tile exit.
     *
     * @return Tile
     */
    public Tile getExit() {
        return exit;
    }

    /**
     * Getter method for a tile given the coordinates.
     *
     * @param coordinateIn
     * @return Tile
     */
    public Tile getTileAtLocation(Coordinate coordinateIn) {
        return tiles.get(tiles.size() - 1 - coordinateIn.getY()).get(coordinateIn.getX());
    }

    /**
     * Getter method for the coordinates of a tile given the tile.
     *
     * @param tileIn
     * @return Coordinate
     */
    public Coordinate getTileLocation(Tile tileIn) {
        for (int col = 0; col < tiles.size(); col++) {
            for (int row = 0; row < tiles.get(col).size(); row++) {
                if (tiles.get(col).get(row).equals(tileIn)) {
                    return new Coordinate(row, (tiles.size() - 1 - col));
                }
            }
        }
        return null;
    }

    /**
     * Getter method for the 2D tile maze array.
     *
     * @return 2D tile array
     */
    public List<List<Tile>> getTiles() {
        return tiles;
    }

    /**
     * Setter method for the tile entrance.
     *
     * @param tileIn
     */
    private void setEntrance(Tile tileIn) {
        if (getTileLocation(tileIn) == null) {
            throw new IllegalArgumentException("Invalid entrance in the maze.");
        } else if (entrance == null) {
            entrance = tileIn;
        } else {
            throw new MultipleEntranceException("Multiple entrances found in the maze.");
        }
    }

    /**
     * Setter method for the tile exit.
     *
     * @param tileIn
     */
    private void setExit(Tile tileIn) {
        if (getTileLocation(tileIn) == null) {
            throw new IllegalArgumentException("Invalid exit in the maze.");
        } else if (exit == null) {
            exit = tileIn;
        } else {
            throw new MultipleExitException("Multiple exits found in the maze.");
        }
    }

    /**
     * A method to convert the maze to a string.
     *
     * @return String
     */
    public String toString() {
        String string = "";
        for (List<Tile> row : tiles) {
            for (Tile tile : row) {
                string += tile.toString();
            }
            string += "\n";
        }
        return string;
    }

}