package maze;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Maze implements java.io.Serializable {

    public enum Direction {
        NORTH, SOUTH, EAST, WEST;
    }

    public class Coordinate {

        private int x, y;

        public Coordinate(int xIn, int yIn) {
            x = xIn;
            y = yIn;
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

    private Tile entrance;

    private Tile exit;

    private List<List<Tile>> tiles;

    private Maze() {
        tiles = new ArrayList<List<Tile>>();
    }

    public static Maze fromTxt(String filePath) throws IOException, NullPointerException, InvalidMazeException {
        Maze maze = new Maze();
        try (FileReader file = new FileReader(filePath)) {
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
        } catch (IOException e) {
            throw new IOException();
        } catch (NullPointerException e) {
            throw new NullPointerException();
        }

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

    public Tile getEntrance() {
        return entrance;
    }

    public Tile getExit() {
        return exit;
    }

    public Tile getTileAtLocation(Coordinate coordinateIn) {
        return tiles.get(tiles.size() - 1 - coordinateIn.getY()).get(coordinateIn.getX());
    }

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

    public List<List<Tile>> getTiles() {
        return tiles;
    }

    private void setEntrance(Tile tileIn) throws IllegalArgumentException, InvalidMazeException {
        if (getTileLocation(tileIn) == null) {
            throw new IllegalArgumentException("Invalid entrance in the maze.");
        } else if (entrance == null) {
            entrance = tileIn;
        } else {
            throw new MultipleEntranceException("Multiple entrances found in the maze.");
        }
    }

    private void setExit(Tile tileIn) throws IllegalArgumentException, InvalidMazeException {
        if (getTileLocation(tileIn) == null) {
            throw new IllegalArgumentException("Invalid exit in the maze.");
        } else if (exit == null) {
            exit = tileIn;
        } else {
            throw new MultipleExitException("Multiple exits found in the maze.");
        }
    }

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