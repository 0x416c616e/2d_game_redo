import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("test java15 jar");
        stage.setWidth(1280);
        stage.setHeight(800);
        StackPane root = new StackPane();
        Pane mainMenu = new Pane();
        Label label1 = new Label("I got the JAR to work on java15/openjfx15!");
        mainMenu.getChildren().add(label1);
        root.getChildren().add(mainMenu);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
