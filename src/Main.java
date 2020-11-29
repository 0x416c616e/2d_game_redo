import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
        //Basic window stuff
        stage.setTitle("2D RPG written in Java");
        stage.setWidth(1280);
        stage.setHeight(800);
        //root stackpane used for stacking stuff on top i.e. a menu on top of something else in the window
        StackPane root = new StackPane();
        //main menu has the buttons for new game, continue, about, settings, and quit
        Pane mainMenu = new Pane();
        Label label1 = new Label("This game isn't finished yet!");
        Button newButton = new Button();
        newButton.setText("New Game");
        newButton.setLayoutX(50);
        newButton.setLayoutY(50);

        Button continueButton = new Button();
        continueButton.setText("Continue");
        continueButton.setLayoutX(50);
        continueButton.setLayoutY(100);

        Button aboutButton = new Button();
        aboutButton.setText("About");
        aboutButton.setLayoutX(50);
        aboutButton.setLayoutY(150);

        Button settingsButton = new Button();
        settingsButton.setText("Settings");
        settingsButton.setLayoutX(50);
        settingsButton.setLayoutY(200);

        Button quitButton = new Button();
        quitButton.setText("Quit");
        quitButton.setLayoutX(50);
        quitButton.setLayoutY(250);
        
        mainMenu.getChildren().addAll(label1, newButton, continueButton, aboutButton, settingsButton, quitButton);
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
