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
        VBox leftVBox = new VBox();
        leftVBox.setAlignment(Pos.CENTER);
        leftVBox.setPadding(insets);
        leftVBox.setSpacing(20);
        leftVBox.getChildren().addAll(loadMapButton, loadRouteButton);
        VBox middleVBox = new VBox();
        middleVBox.setAlignment(Pos.CENTER);
        middleVBox.setPadding(new Insets(20, 0, 20, 20));
        middleVBox.getChildren().add(welcomeText);
        VBox rightVBox = new VBox();
        rightVBox.setAlignment(Pos.CENTER);
        FileChooser fileChooser = new FileChooser();
        HBox root = new HBox();
        root.getChildren().addAll(middleVBox, leftVBox);
        loadMapButton.setOnAction(e -> {
            try {
                maze = Maze.fromTxt(fileChooser.showOpenDialog(stage).getPath());
                routeFinder = new RouteFinder(maze);
                renderScreen(stage, root, leftVBox, middleVBox, rightVBox, loadMapButton, loadRouteButton,
                        saveRouteButton, stepButton, false);
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
                renderScreen(stage, root, leftVBox, middleVBox, rightVBox, loadMapButton, loadRouteButton,
                        saveRouteButton, stepButton, routeFinder.isFinished());
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
                renderScreen(stage, root, leftVBox, middleVBox, rightVBox, loadMapButton, loadRouteButton,
                        saveRouteButton, stepButton, routeFinder.step());
            } catch (NoRouteFoundException ex) {
                showErrorMessage(ex.getMessage());
            }
        });
        stage.setScene(new Scene(root));
        stage.sizeToScene();
        stage.setTitle("Maze Solver - Aryan Agrawal");
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
     * @param root
     * @param leftVBox
     * @param middleVBox
     * @param rightVBox
     * @param loadMapButton
     * @param loadRouteButton
     * @param saveRouteButton
     * @param stepButton
     * @param finished
     */
    public void renderScreen(Stage stage, HBox root, VBox leftVBox, VBox middleVBox, VBox rightVBox,
            Button loadMapButton, Button loadRouteButton, Button saveRouteButton, Button stepButton, boolean finished) {
        GridPane mazeGrid = MazeRender.Render(routeFinder.toString());
        leftVBox.getChildren().clear();
        leftVBox.getChildren().addAll(loadMapButton, loadRouteButton, saveRouteButton);
        leftVBox.setPadding(insets);
        middleVBox.getChildren().clear();
        middleVBox.getChildren().add(mazeGrid);
        middleVBox.setPadding(new Insets(50, 20, 50, 20));
        rightVBox.getChildren().clear();
        if (!finished) {
            rightVBox.getChildren().add(stepButton);
            rightVBox.setPadding(insets);
        }
        root.getChildren().clear();
        root.getChildren().addAll(leftVBox, middleVBox, rightVBox);
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