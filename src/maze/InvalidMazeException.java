package maze;

/**
 * The InvalidMazeException class, the parent class for the other maze exception
 * types.
 *
 * @author Aryan Agrawal
 * @version 0.1
 */
public class InvalidMazeException extends RuntimeException {

    /**
     * The class constructor.
     *
     * @param message
     */
    public InvalidMazeException(String message) {
        super(message);
    }
}