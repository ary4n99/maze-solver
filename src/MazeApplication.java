import maze.Maze;
import maze.routing.RouteFinder;
import maze.InvalidMazeException;
import maze.routing.NoRouteFoundException;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
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
        FileChooser fileChooserMaze = new FileChooser();
        fileChooserMaze.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text file", "*.txt"));
        FileChooser fileChooserRoute = new FileChooser();
        fileChooserRoute.getExtensionFilters().add(new FileChooser.ExtensionFilter("Route file", "*.route"));
        HBox root = new HBox();
        root.getChildren().addAll(middleVBox, leftVBox);
        loadMapButton.setOnAction(e -> {
            File file = fileChooserMaze.showOpenDialog(stage);
            try {
                maze = Maze.fromTxt(file.getPath());
                routeFinder = new RouteFinder(maze);
                renderScreen(stage, root, leftVBox, middleVBox, rightVBox, loadMapButton, loadRouteButton,
                        saveRouteButton, stepButton, false);
            } catch (IOException ex) {
                showErrorMessage(ex.toString());
            } catch (InvalidMazeException ex) {
                System.out.println(ex.toString());
                showErrorMessage(ex.toString());
            } catch (NullPointerException ex) {
            }

        });
        loadRouteButton.setOnAction(e -> {
            try {
                routeFinder = RouteFinder.load(fileChooserRoute.showOpenDialog(stage).getPath());
                maze = routeFinder.getMaze();
                renderScreen(stage, root, leftVBox, middleVBox, rightVBox, loadMapButton, loadRouteButton,
                        saveRouteButton, stepButton, routeFinder.isFinished());
            } catch (IOException ex) {
                System.out.println(ex.toString());
                showErrorMessage(ex.toString());
            } catch (ClassNotFoundException ex) {
                showErrorMessage(ex.toString());
            } catch (NoRouteFoundException ex) {
                showErrorMessage(ex.toString());
            } catch (NullPointerException ex) {
            }

        });
        saveRouteButton.setOnAction(e -> {
            FileChooser fileSave = new FileChooser();
            fileSave.setTitle("Save route");
            File file = fileSave.showSaveDialog(stage);
            if (file != null) {
                if (!file.getName().endsWith(".route")) { // make case insensitive
                    file = new File(file.getPath() + ".route");
                }
                try {
                    routeFinder.save(file.getPath());
                } catch (IOException ex) {
                    showErrorMessage(ex.toString());
                } catch (NullPointerException ex) {
                    showErrorMessage(ex.toString());
                }
            }
        });
        stepButton.setOnAction(e -> {
            try {
                renderScreen(stage, root, leftVBox, middleVBox, rightVBox, loadMapButton, loadRouteButton,
                        saveRouteButton, stepButton, routeFinder.step());
            } catch (NoRouteFoundException ex) {
                showErrorMessage(ex.toString());
            }
        });
        stage.setScene(new Scene(root));
        stage.sizeToScene();
        stage.setTitle("Maze Solver - Aryan Agrawal");
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
        alert.setHeaderText("An error has occurred.");
        alert.setContentText(errorMessage);
        alert.setResizable(true);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    public void renderScreen(Stage stage, HBox root, VBox leftVBox, VBox middleVBox, VBox rightVBox,
            Button loadMapButton, Button loadRouteButton, Button saveRouteButton, Button stepButton, boolean finished) {
        MazeRouteStep();
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