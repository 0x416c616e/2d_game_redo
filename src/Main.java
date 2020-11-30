import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

//if you want to see where most stuff happens, scroll down to the init() method
public class Main extends Application {

    //methods getting or setting controls status (keyboard or touchscreen)
    public String getControlStatus(){
        File controlSettingsFile = new File("settings/controls.txt");
        Scanner controlSettingsIn;
        try {
            controlSettingsIn = new Scanner(controlSettingsFile);
            String status = controlSettingsIn.next();
            controlSettingsIn.close();
            return status;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return "error";
    }

    public void setControlStatus(String status) {
        File controlsFile = new File("settings/controls.txt");
        try {
            PrintWriter fileOut = new PrintWriter(controlsFile);

            fileOut.write(status);

            fileOut.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public String getWindowModeStatus() {
        File windowModeSettingsFile = new File("settings/window_mode.txt");
        Scanner windowModeSettingsIn;
        try {
            windowModeSettingsIn = new Scanner(windowModeSettingsFile);
            String status = windowModeSettingsIn.next();
            windowModeSettingsIn.close();
            return status;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return "error";
    }

    public void setWindowModeStatus(String status) {
        File windowModeSettingsFile = new File("settings/window_mode.txt");
        try {
            PrintWriter fileOut = new PrintWriter(windowModeSettingsFile);

            fileOut.write(status);

            fileOut.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }


    //what happens in JavaFX before the window is opened
    @Override
    public void init() throws Exception {
        super.init();
    }

    //what happens during window opening etc.
    @Override
    public void start(Stage stage) throws Exception {
        //size for fonts (this is intended for small tablets with high DPI so big text is necessary)
        final double FONT_SIZE = 30.0;

        //getting settings for controls and window mode from config files
        String controls = getControlStatus();
        String windowMode = getWindowModeStatus();
        if (windowMode.equals("fullscreen")) {
            stage.setFullScreen(true);
        }
        //Basic window stuff
        stage.setTitle("2D RPG written in Java");
        stage.setWidth(1280);
        stage.setHeight(800);
        stage.setResizable(false);
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

        Button controlsButton = new Button("Controls");
        controlsButton.setLayoutX(50);
        controlsButton.setLayoutY(250);

        Button quitButton = new Button();
        quitButton.setText("Quit");
        quitButton.setLayoutX(50);
        quitButton.setLayoutY(300);

        //event handlers for main menu buttons

        //overlay to cover up other stuff on the screen
        Image tempBackground = new Image("file:assets/blank.png");
        ImageView tempBackgroundView = new ImageView(tempBackground);



        //New game
        Label enterNameLabel = new Label("Enter name");
        enterNameLabel.setFont(new Font(FONT_SIZE));
        enterNameLabel.setLayoutX(100);
        enterNameLabel.setLayoutY(100);

        newButton.setOnAction(e -> {
            mainMenu.getChildren().addAll(tempBackgroundView, enterNameLabel);
        });

        //^end of new game stuff



        //Continue an existing game
        continueButton.setOnAction(e -> {

        });
        //^end of continue game stuff


        //about button stuff

        //about button adds stuff on top of the screen
        //getOutOfAboutMenu gets rid of it
        Button getOutOfAboutMenu = new Button("Back to main menu");
        getOutOfAboutMenu.setLayoutX(50);
        getOutOfAboutMenu.setLayoutY(100);


        Text aboutText = new Text("About\nThis game was made by a computer science student.\nThis game is not finished yet!");
        aboutText.setLayoutX(50);
        aboutText.setLayoutY(50);

        //lambdas for opening and closing the about menu
        aboutButton.setOnAction(e -> {
            mainMenu.getChildren().addAll(tempBackgroundView, aboutText, getOutOfAboutMenu);
        });

        getOutOfAboutMenu.setOnAction(e -> {
            mainMenu.getChildren().removeAll(tempBackgroundView, aboutText, getOutOfAboutMenu);
        });

        //^end of about button events

        //settings menu stuff

        Text settingsText = new Text("Settings\nRestart the game in order for the changes to take effect.");
        settingsText.setLayoutX(50);
        settingsText.setLayoutY(50);
        Button getOutOfSettingsMenu = new Button("Back to main menu");
        getOutOfSettingsMenu.setLayoutX(50);
        getOutOfSettingsMenu.setLayoutY(400);

        //controls settings
        Text controlsText = new Text("Controls:");
        controlsText.setLayoutX(50);
        controlsText.setLayoutY(100);
        Button keyboardControlsButton = new Button("Keyboard");
        keyboardControlsButton.setLayoutX(100);
        keyboardControlsButton.setLayoutY(100);
        Button touchscreenControlsButton = new Button("Touchscreen");
        touchscreenControlsButton.setLayoutX(200);
        touchscreenControlsButton.setLayoutY(100);
        Text currentControlsText = new Text("Current controls: ");
        currentControlsText.setLayoutX(50);
        currentControlsText.setLayoutY(150);
        String controlsStatus = getControlStatus();
        Text controlsStatusFromFile = new Text(controlsStatus);
        controlsStatusFromFile.setLayoutX(150);
        controlsStatusFromFile.setLayoutY(150);

        //control buttons event handlers
        keyboardControlsButton.setOnAction(e -> {
            setControlStatus("keyboard");
        });
        touchscreenControlsButton.setOnAction(e -> {
            setControlStatus("touchscreen");
        });

        //window mode settings
        Text windowModeText = new Text("Window mode: ");
        windowModeText.setLayoutX(50);
        windowModeText.setLayoutY(200);
        Button fullscreenWindowModeButton = new Button("Fullscreen");
        fullscreenWindowModeButton.setLayoutX(100);
        fullscreenWindowModeButton.setLayoutY(250);
        Button windowedWindowModeButton = new Button("Windowed");
        windowedWindowModeButton.setLayoutX(200);
        windowedWindowModeButton.setLayoutY(250);
        Text currentWindowModeText = new Text("Current window mode: ");
        currentWindowModeText.setLayoutX(50);
        currentWindowModeText.setLayoutY(300);
        String windowModeStatus = getWindowModeStatus();
        Text windowModeStatusFromFile = new Text(windowModeStatus);
        windowModeStatusFromFile.setLayoutX(200);
        windowModeStatusFromFile.setLayoutY(300);

        //window mode button event handlers
        fullscreenWindowModeButton.setOnAction(e -> {
            setWindowModeStatus("fullscreen");
        });
        windowedWindowModeButton.setOnAction(e -> {
            setWindowModeStatus("windowed");
        });



        settingsButton.setOnAction(e -> {
            mainMenu.getChildren().addAll(tempBackgroundView, settingsText, getOutOfSettingsMenu);
            mainMenu.getChildren().addAll(controlsText, keyboardControlsButton, touchscreenControlsButton, currentControlsText, controlsStatusFromFile);
            mainMenu.getChildren().addAll(windowModeText, fullscreenWindowModeButton, windowedWindowModeButton, currentWindowModeText, windowModeStatusFromFile);
        });

        getOutOfSettingsMenu.setOnAction(e -> {
            mainMenu.getChildren().removeAll(tempBackgroundView, settingsText, getOutOfSettingsMenu);
            mainMenu.getChildren().removeAll(controlsText, keyboardControlsButton, touchscreenControlsButton, currentControlsText, controlsStatusFromFile);
            mainMenu.getChildren().removeAll(windowModeText, fullscreenWindowModeButton, windowedWindowModeButton, currentWindowModeText, windowModeStatusFromFile);
        });

        //^end of settings events

        //controls info menu
        Text controlsMenuText = new Text("Controls\nThis is where the controls will be listed once the game is developed more.");
        controlsMenuText.setLayoutX(50);
        controlsMenuText.setLayoutY(50);
        Button getOutOfControlsMenu = new Button("Return to main menu");
        getOutOfControlsMenu.setLayoutX(50);
        getOutOfControlsMenu.setLayoutY(300);

        //displays controls (not yet implemented)
        controlsButton.setOnAction(e -> {
            mainMenu.getChildren().addAll(tempBackgroundView, controlsMenuText, getOutOfControlsMenu);
        });
        getOutOfControlsMenu.setOnAction(e -> {
            mainMenu.getChildren().removeAll(tempBackgroundView, controlsMenuText, getOutOfControlsMenu);
        });

        //^end of controls info menu

        //quit button, very simple
        quitButton.setOnAction(e -> {
            //return code 0 means no errors, any other number means something went wrong
            System.exit(0);
        });

        //adding menu items to the menu
        mainMenu.getChildren().addAll(label1, newButton, continueButton, aboutButton, settingsButton, controlsButton, quitButton);
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
