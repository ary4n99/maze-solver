package maze.routing;

import maze.Tile;
import maze.Maze;
import java.util.List;
import java.util.Stack;

public class RouteFinder {

    private Maze maze;

    private Stack<Tile> route;

    private boolean finished;

    private List<Tile> visited;

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
        RouteFinder routeFinder;
        try (FileInputStream file = new FileInputStream(stringIn);
                ObjectInputStream stream = new ObjectInputStream(file);) {
            return (RouteFinder) stream.readObject();
        } catch (IOException e) {
            throw new IOException("Problem loading file.");
        }
    }

    public void save(String stringIn) {
        try (FileOutputStream file = new FileOutputStream(stringIn);
                ObjectOutputStream stream = new ObjectOutputStream(file);) {
            stream.writeObject(this);
        } catch (IOException e) {
            throw new IOException("Problem saving file.");
        }
    }

    public boolean step() {
        if (!finished) {
            if (!route.contains(maze.getEntrance()) && !visited.contains(maze.getEntrance())) {
                route.push(maze.getEntrance());
                visited.add(maze.getEntrance());
            } else {
                if (route.size() > 0) {
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
            }
        } else if (!visited.contains(maze.getExit())) {
            visited.add(maze.getExit());
        }
        return finished;
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