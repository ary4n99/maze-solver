package maze.routing;

import maze.Tile;
import maze.Maze;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * The RouteFinder class, which finds a route through the maze and loads/stores
 * the current class.
 *
 * @author Aryan Agrawal
 * @version 0.1
 */
public class RouteFinder implements java.io.Serializable {

    /**
     * Stores the maze being solved.
     */
    private Maze maze;

    /**
     * Stores the current route.
     */
    private Stack<Tile> route;

    /**
     * Stores the status of the maze solving completion status.
     */
    private boolean finished;

    /**
     * Stores all the tiles visited in case a route isn't found on the first try.
     */
    private List<Tile> visited;

    /**
     * The class constructor. Sets the input maze and initializes the other class
     * variables.
     *
     * @param mazeIn
     */
    public RouteFinder(Maze mazeIn) {
        maze = mazeIn;
        route = new Stack<Tile>();
        visited = new ArrayList<Tile>();
        finished = false;
    }

    /**
     * Getter method for the maze.
     *
     * @return Maze
     */
    public Maze getMaze() {
        return maze;
    }

    /**
     * Getter method for the route.
     *
     * @return List<Tile>
     */
    public List<Tile> getRoute() {
        return route;
    }

    /**
     * Getter method for the finished status.
     *
     * @return boolean
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Reads the RouteFinder class from an input string.
     *
     * @param stringIn
     * @return RouteFinder
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static RouteFinder load(String stringIn) throws IOException, ClassNotFoundException {
        ObjectInputStream stream = new ObjectInputStream(new FileInputStream(stringIn));
        RouteFinder routeFinder = (RouteFinder) stream.readObject();
        return routeFinder;
    }

    /**
     * Saves the current RouteFinder class.
     *
     * @param stringIn
     * @throws IOException
     */
    public void save(String stringIn) throws IOException {
        ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(stringIn));
        stream.writeObject(this);
    }

    /**
     * DFS search algorithm to solve the maze.
     *
     * @return boolean
     * @throws NoRouteFoundException
     */
    public boolean step() throws NoRouteFoundException {
        if (!finished) {
            if (!route.contains(maze.getEntrance()) && !visited.contains(maze.getEntrance())) {
                route.push(maze.getEntrance());
                visited.add(maze.getEntrance());
            } else if (route.size() > 0) {
                for (Maze.Direction direction : Maze.Direction.values()) {
                    Tile tile = maze.getAdjacentTile(route.peek(), direction);
                    if (tile != null && tile.isNavigable() && !visited.contains(tile)) {
                        visited.add(tile);
                        route.push(tile);
                        return finished = tile.toString().equals("x");
                    }
                }
                route.pop();
            } else {
                throw new NoRouteFoundException("No route found.");
            }
        } else if (!visited.contains(maze.getExit())) {
            visited.add(maze.getExit());
        }
        return finished;
    }

    /**
     * Converts the current state of the route to a string.
     *
     * @return String
     */
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