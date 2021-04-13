package maze;

import java.util.List;

public class Maze {

    private Tile entrance;

    private Tile exit;

    private List<List<Tile>> tiles;

    private Maze() {
    }

    public static Maze fromTxt(String stringIn) {
    }

    public Tile getAdjacentTile(Tile tileIn, Direction directionIn) {
    }

    public Tile getEntrance() {
        return entrance;
    }

    public Tile getExit() {
        return exit;
    }

    public Tile getTileAtLocation(Coordinate coordinateIn) {
    }

    public Coordinate getTileLocation(Tile tileIn) {
    }

    public List<List<Tile>> getTiles() {
        return tiles;
    }

    private void setEntrance(Tile tileIn) {
    }

    private void setExit(Tile tileIn) {
    }

    public String toString() {
    }

}