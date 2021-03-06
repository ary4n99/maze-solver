package maze.visualisation;

import java.util.Hashtable;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * The MazeRender class, the helper class for MazeApplication.
 *
 * @author Aryan Agrawal
 * @version 0.1
 */
public class MazeRender {

    /**
     * Renders the maze using JavaFX.
     *
     * @param mazeString
     * @return GridPane
     */
    public static GridPane Render(String mazeString) {
        GridPane mazeGrid = new GridPane();
        int x = 0;
        int y = 0;
        Hashtable<Character, Color> characterColours = new Hashtable<Character, Color>();
        characterColours.put('#', Color.BLACK);
        characterColours.put('*', Color.PINK);
        characterColours.put('e', Color.GREEN);
        characterColours.put('x', Color.RED);
        characterColours.put('.', Color.GRAY);
        for (int i = 0; i < mazeString.length(); i++) {
            if (mazeString.charAt(i) == '\n') {
                x = 0;
                y += 1;
            } else {
                for (Character key : characterColours.keySet()) {
                    if (mazeString.charAt(i) == key) {
                        x += 1;
                        Rectangle rectangle = new Rectangle(30, 30);
                        rectangle.setFill(characterColours.get(key));
                        mazeGrid.add(rectangle, x, y);
                        break;
                    }
                }
            }
        }
        return mazeGrid;
    }
}
