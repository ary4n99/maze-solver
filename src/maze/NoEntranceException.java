package maze;

/**
 * The NoEntranceException class, thrown when the maze has no entrance. Extends
 * InvalidMazeException.
 *
 * @author Aryan Agrawal
 * @version 0.1
 */
public class NoEntranceException extends InvalidMazeException {

    /**
     * The class constructor.
     *
     * @param message
     */
    public NoEntranceException(String message) {
        super(message);
    }
}