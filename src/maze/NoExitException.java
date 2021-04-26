package maze;

/**
 * The NoExitException class, thrown when the maze has no exit. Extends
 * InvalidMazeException.
 *
 * @author Aryan Agrawal
 * @version 0.1
 */
public class NoExitException extends InvalidMazeException {

    /**
     * The class constructor.
     *
     * @param message
     */
    public NoExitException(String message) {
        super(message);
    }
}