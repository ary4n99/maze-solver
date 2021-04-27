import maze.*;
import maze.routing.*;
import maze.visualisation.*;
import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.GridPane;

/**
 * The main JavaFX class that renders the maze.
 *
 * @author Aryan Agrawal
 * @version 0.1
 */
public class MazeApplication extends Application {

    /**
     * Stores the maze being solved.
     */
    public Maze maze;

    /**
     * Stores the RouteFinder being used.
     */
    public RouteFinder routeFinder;

    /**
     * Stores general padding values for rendering the program.
     */
    public Insets insets = new Insets(20, 20, 20, 20);

    /**
     * Stores the load map button.
     */
    public Button loadMapButton = new Button("Load map");

    /**
     * Stores the load route button.
     */
    public Button loadRouteButton = new Button("Load route");

    /**
     * Stores the save route button.
     */
    public Button saveRouteButton = new Button("Save route");

    /**
     * Stores the step button.
     */
    public Button stepButton = new Button("Next step");

    /**
     * Stores the solve button.
     */
    public Button solveButton = new Button("Solve maze");

    /**
     * Stores left VBox.
     */
    public VBox leftVBox = new VBox();

    /**
     * Stores middle VBox.
     */
    public VBox middleVBox = new VBox();

    /**
     * Stores right VBox.
     */
    public VBox rightVBox = new VBox();

    /**
     * Stores the root HBox.
     */
    public HBox root = new HBox();

    /**
     * The main method for launching the JavaFX program.
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The method that renders the program using JavaFX and sets the button
     * functions.
     *
     * @param stage
     */
    public void start(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        loadMapButton.setPrefSize(100, 50);
        loadRouteButton.setPrefSize(100, 50);
        saveRouteButton.setPrefSize(100, 50);
        stepButton.setPrefSize(100, 50);
        solveButton.setPrefSize(100, 50);
        leftVBox.setAlignment(Pos.CENTER);
        leftVBox.setPadding(insets);
        leftVBox.setSpacing(20);
        middleVBox.setAlignment(Pos.CENTER);
        middleVBox.setPadding(insets);
        middleVBox.setSpacing(20);
        middleVBox.getChildren().addAll(loadMapButton, loadRouteButton);
        rightVBox.setAlignment(Pos.CENTER);
        rightVBox.setPadding(insets);
        rightVBox.setSpacing(20);
        root.getChildren().addAll(middleVBox);
        loadMapButton.setOnAction(e -> {
            try {
                maze = Maze.fromTxt(fileChooser.showOpenDialog(stage).getPath());
                routeFinder = new RouteFinder(maze);
                renderScreen(stage, false);
            } catch (IOException ex) {
                showErrorMessage("Error loading maze file.");
            } catch (InvalidMazeException ex) {
                showErrorMessage(ex.getMessage());
            } catch (NullPointerException ex) {
            }

        });
        loadRouteButton.setOnAction(e -> {
            try {
                routeFinder = RouteFinder.load(fileChooser.showOpenDialog(stage).getPath());
                maze = routeFinder.getMaze();
                renderScreen(stage, routeFinder.isFinished());
            } catch (IOException ex) {
                showErrorMessage("Error loading route file.");
            } catch (ClassNotFoundException | NoRouteFoundException ex) {
                showErrorMessage(ex.getMessage());
            } catch (NullPointerException ex) {
            }

        });
        saveRouteButton.setOnAction(e -> {
            try {
                File file = fileChooser.showSaveDialog(stage);
                if (file != null) {
                    if (!file.getName().toLowerCase().endsWith(".route")) {
                        file = new File(file.getPath() + ".route");
                    }
                    routeFinder.save(file.getPath());
                }
            } catch (IOException ex) {
                showErrorMessage("Error saving route file.");
            } catch (NullPointerException ex) {
            }

        });
        stepButton.setOnAction(e -> {
            try {
                renderScreen(stage, routeFinder.step());
            } catch (NoRouteFoundException ex) {
                showErrorMessage(ex.getMessage());
            }
        });
        solveButton.setOnAction(e -> {
            try {
                while (!routeFinder.step()) {
                    renderScreen(stage, routeFinder.step());
                }
                renderScreen(stage, routeFinder.step());
            } catch (NoRouteFoundException ex) {
                showErrorMessage(ex.getMessage());
            }
        });
        stage.setScene(new Scene(root));
        stage.sizeToScene();
        stage.show();
    }

    /**
     * A helper method to launch error popups.
     *
     * @param errorMessage
     */
    public void showErrorMessage(String errorMessage) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error has occurred.");
        alert.setContentText(errorMessage);
        alert.setResizable(true);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    /**
     * The method that updates the program after calling the MazeRender.Render
     * method.
     *
     * @param stage
     * @param finished
     */
    public void renderScreen(Stage stage, boolean finished) {
        GridPane mazeGrid = MazeRender.Render(routeFinder.toString());
        stage.setTitle("Maze Solver - Aryan Agrawal");
        leftVBox.getChildren().clear();
        leftVBox.getChildren().addAll(loadMapButton, loadRouteButton, saveRouteButton);
        middleVBox.getChildren().clear();
        middleVBox.getChildren().add(mazeGrid);
        rightVBox.getChildren().clear();
        if (!finished) {
            rightVBox.getChildren().addAll(stepButton, solveButton);
        }
        root.getChildren().clear();
        root.getChildren().addAll(leftVBox, middleVBox, rightVBox);
        stage.sizeToScene();
    }
}