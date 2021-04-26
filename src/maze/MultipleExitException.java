package maze;

/**
 * The MultipleExitException class, thrown when the maze has multiple exits.
 * Extends InvalidMazeException.
 *
 * @author Aryan Agrawal
 * @version 0.1
 */
public class MultipleExitException extends InvalidMazeException {

    /**
     * The class constructor.
     *
     * @param message
     */
    public MultipleExitException(String message) {
        super(message);
    }
}