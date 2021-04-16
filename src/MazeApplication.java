import javafx.application.Application;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MazeApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        stage.setTitle("Maze Solver");
        BorderPane root = new BorderPane();
        Label title = new Label("Maze Solver");
        Button button1 = new Button("Button 1");
        Button button2 = new Button("Button 2");

        VBox vbox = new VBox(title, button1, button2);
        vbox.setAlignment(Pos.CENTER);
        root.setCenter(vbox);

        Scene scene = new Scene(root, 600, 600);
        stage.setScene(scene);
        stage.show();
    }
}
