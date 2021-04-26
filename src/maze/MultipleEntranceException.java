package maze;

/**
 * The MultipleEntranceException class, thrown when the maze has multiple
 * entrances. Extends InvalidMazeException.
 *
 * @author Aryan Agrawal
 * @version 0.1
 */
public class MultipleEntranceException extends InvalidMazeException {

    /**
     * The class constructor.
     *
     * @param message
     */
    public MultipleEntranceException(String message) {
        super(message);
    }
}