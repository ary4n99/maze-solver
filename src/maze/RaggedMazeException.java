package maze;

/**
 * The RaggedMazeException class, thrown when the maze is ragged. Extends
 * InvalidMazeException.
 *
 * @author Aryan Agrawal
 * @version 0.1
 */
public class RaggedMazeException extends InvalidMazeException {

    /**
     * The class constructor.
     *
     * @param message
     */
    public RaggedMazeException(String message) {
        super(message);
    }
}