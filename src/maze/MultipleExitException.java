package maze;

public class MultipleExitException extends InvalidMazeException {
    public MultipleExitException(String messege) {
        super(messege);
    }
}