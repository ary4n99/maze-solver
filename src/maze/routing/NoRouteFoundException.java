package maze.routing;

public class NoRouteFoundException extends RuntimeException {
    public NoRouteFoundException(String message) {
        super(message);
    }
}