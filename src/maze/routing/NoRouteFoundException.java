package maze.routing;

/**
 * The NoRouteFoundException class, called when a route can't be found through
 * the maze.
 *
 * @author Aryan Agrawal
 * @version 0.1
 */
public class NoRouteFoundException extends RuntimeException {

    /**
     * The class constructor.
     *
     * @param message
     */
    public NoRouteFoundException(String message) {
        super(message);
    }
}