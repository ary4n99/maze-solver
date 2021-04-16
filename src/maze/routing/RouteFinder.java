package maze.routing;

import maze.Tile;
import maze.Maze;
import java.util.List;
import java.util.Stack;

public class RouteFinder {

    private Maze maze;

    private Stack<Tile> route;

    private boolean finished;

    public RouteFinder(Maze mazeIn) {
        maze = mazeIn;
        route = new Stack<Tile>();
        finished = false;
    }

    public Maze getMaze() {
        return maze;
    }

    public List<Tile> getRoute() {
        return route;
    }

    public boolean isFinished() {
        return finished;

    }

    public static RouteFinder load(String stringIn) {
    }

    public void save(String stringIn) {
    }

    public boolean step() {
    }

    public String toString() {
        String string = "";
        List<List<Tile>> mazeTiles = maze.getTiles();
        for (List<Tile> row : mazeTiles) {
            for (Tile tile : row) {
                string += route.contains(tile) ? "*" : tile.toString();
            }
            string += "\n";
        }
        return string;
    }
}