import maze.Maze;
import maze.routing.RouteFinder;
import maze.InvalidMazeException;
import maze.routing.NoRouteFoundException;
import java.io.File;
import java.util.Hashtable;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.GridPane;

// FIX LOADING EMPTY ROUTE
// ADD JAVADOCS

public class MazeApplication extends Application {

    public Maze maze;
    public RouteFinder routeFinder;
    public GridPane mazeGrid;
    public Insets insets = new Insets(20, 20, 20, 20);

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        Button loadMapButton = new Button();
        loadMapButton.setText("Load map");
        loadMapButton.setPrefSize(100, 50);
        Button loadRouteButton = new Button();
        loadRouteButton.setText("Load route");
        loadRouteButton.setPrefSize(100, 50);
        Button saveRouteButton = new Button();
        saveRouteButton.setText("Save route");
        saveRouteButton.setPrefSize(100, 50);
        Button stepButton = new Button();
        stepButton.setText("Next step");
        stepButton.setPrefSize(100, 50);
        Text welcomeText = new Text();
        welcomeText.setFont(new Font(20));
        welcomeText.setText("Welcome to the Maze Solver!");
        HBox topHBox = new HBox();
        topHBox.setAlignment(Pos.CENTER);
        topHBox.setPadding(insets);
        topHBox.setSpacing(20);
        topHBox.getChildren().addAll(loadMapButton, loadRouteButton);
        HBox middleHBox = new HBox();
        middleHBox.setAlignment(Pos.CENTER);
        middleHBox.setPadding(new Insets(20, 20, 0, 20));
        middleHBox.getChildren().add(welcomeText);
        HBox bottomHBox = new HBox();
        bottomHBox.setAlignment(Pos.CENTER);
        FileChooser fileChooserMaze = new FileChooser();
        fileChooserMaze.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text file", "*.txt"));
        FileChooser fileChooserRoute = new FileChooser();
        fileChooserRoute.getExtensionFilters().add(new FileChooser.ExtensionFilter("Route file", "*.route"));
        VBox root = new VBox();
        root.getChildren().addAll(middleHBox, topHBox);
        loadMapButton.setOnAction(e -> {
            File file = fileChooserMaze.showOpenDialog(stage);
            try {
                maze = Maze.fromTxt(file.getPath());
                routeFinder = new RouteFinder(maze);
            } catch (InvalidMazeException ex) {
                showErrorMessage(ex.toString());
            } catch (IllegalArgumentException ex) {
                showErrorMessage(ex.toString());
            }
            renderScreen(stage, root, topHBox, middleHBox, bottomHBox, loadMapButton, loadRouteButton, saveRouteButton,
                    stepButton, false);
        });
        loadRouteButton.setOnAction(e -> {
            try {
                routeFinder = RouteFinder.load(fileChooserRoute.showOpenDialog(stage).getPath());
                maze = routeFinder.getMaze();
            } catch (NoRouteFoundException ex) {
                showErrorMessage(ex.toString());
            } catch (NullPointerException ex) {
                showErrorMessage(ex.toString());
            }
            boolean finished = routeFinder.isFinished();
            renderScreen(stage, root, topHBox, middleHBox, bottomHBox, loadMapButton, loadRouteButton, saveRouteButton,
                    stepButton, finished);
        });
        saveRouteButton.setOnAction(e -> {
            FileChooser fileSave = new FileChooser();
            fileSave.setTitle("Save route");
            File file = fileSave.showSaveDialog(stage);
            if (file != null) {
                if (!file.getName().endsWith(".route")) {
                    file = new File(file.getPath() + ".route");
                }
                routeFinder.save(file.getPath());
            }
            renderScreen(stage, root, topHBox, middleHBox, bottomHBox, loadMapButton, loadRouteButton, saveRouteButton,
                    stepButton, false);
        });
        stepButton.setOnAction(e -> {
            boolean finished;
            try {
                finished = routeFinder.step();
            } catch (NoRouteFoundException ex) {
                finished = true;
                showErrorMessage(ex.toString());
            }
            renderScreen(stage, root, topHBox, middleHBox, bottomHBox, loadMapButton, loadRouteButton, saveRouteButton,
                    stepButton, finished);
        });
        stage.setScene(new Scene(root));
        stage.sizeToScene();
        stage.setTitle("Maze");
        stage.show();
    }

    public void MazeRouteStep() {
        mazeGrid = new GridPane();
        int x = 0;
        int y = 0;
        String mazeString = routeFinder.toString();
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
                        Rectangle rectangle = new Rectangle(50, 50);
                        rectangle.setFill(characterColours.get(key));
                        mazeGrid.add(rectangle, x, y);
                        break;
                    }
                }
            }
        }
    }

    public void showErrorMessage(String errorMessage) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error has occurred.");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    public void renderScreen(Stage stage, VBox root, HBox topHBox, HBox middleHBox, HBox bottomHBox,
            Button loadMapButton, Button loadRouteButton, Button saveRouteButton, Button stepButton, boolean finished) {
        MazeRouteStep();
        topHBox.getChildren().clear();
        topHBox.getChildren().addAll(loadMapButton, loadRouteButton, saveRouteButton);
        topHBox.setPadding(insets);
        middleHBox.getChildren().clear();
        middleHBox.getChildren().add(mazeGrid);
        middleHBox.setPadding(new Insets(0, 50, 0, 50));
        bottomHBox.getChildren().clear();
        if (!finished) {
            bottomHBox.getChildren().add(stepButton);
            bottomHBox.setPadding(insets);
        }
        root.getChildren().clear();
        root.getChildren().addAll(topHBox, middleHBox, bottomHBox);
        stage.sizeToScene();
        if (finished) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Message");
            alert.setHeaderText("The maze has been solved!");
            alert.setContentText("Click the button below to return to the maze solver.");
            alert.showAndWait();
        }
    }
}