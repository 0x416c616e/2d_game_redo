import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Text;

public class Main extends Application {
    //what happens in JavaFX before the window is opened
    @Override
    public void init() throws Exception {
        super.init();
    }

    //what happens during window opening etc.
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
        Image menuBackground = new Image("file:assets/background.png");
        ImageView menuBackgroundView = new ImageView(menuBackground);
        mainMenu.getChildren().add(menuBackgroundView);
        Label label1 = new Label("This game isn't finished yet!");

        //buttons
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

        //event handlers for main menu buttons

        newButton.setOnAction(e -> {

        });

        continueButton.setOnAction(e -> {

        });

        //about button stuff

        //about button adds stuff on top of the screen
        //getOutOfAboutMenu gets rid of it
        Button getOutOfAboutMenu = new Button("Back to main menu");
        getOutOfAboutMenu.setLayoutX(50);
        getOutOfAboutMenu.setLayoutY(100);

        Image tempBackground = new Image("file:assets/blank.png");
        ImageView tempBackgroundView = new ImageView(tempBackground);
        Text aboutText = new Text("This game was made by a computer science student.\nThis game is not finished yet!");
        aboutText.setLayoutX(50);
        aboutText.setLayoutY(50);

        aboutButton.setOnAction(e -> {
            System.out.println("Got here");

            mainMenu.getChildren().addAll(tempBackgroundView, aboutText, getOutOfAboutMenu);
        });

        getOutOfAboutMenu.setOnAction(e -> {
            System.out.println("test123");
            mainMenu.getChildren().removeAll(tempBackgroundView, aboutText, getOutOfAboutMenu);
        });

        //^end of about button events

        settingsButton.setOnAction(e -> {

        });


        //quit button, very simple
        quitButton.setOnAction(e -> {
            //return code 0 means no errors, any other number means something went wrong
            System.exit(0);
        });

        //adding menu items to the menu
        mainMenu.getChildren().addAll(label1, newButton, continueButton, aboutButton, settingsButton, quitButton);
        root.getChildren().add(mainMenu);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        //displaying the window
        stage.show();


    }

    //what happens in JavaFX after the window is closed
    @Override
    public void stop() throws Exception {
        super.stop();
    }

    //JavaFX boilerplate main
    public static void main(String[] args) {
        launch(args);
    }
}
