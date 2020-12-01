import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.security.cert.Extension;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

//if you want to see where most stuff happens, scroll down to the init() method
public class Main extends Application {
    //this prints out helpful information if enabled
    boolean debugMode = false;

    //if debug mode is enabled, dbgAlert will print a message
    //containing info about the code that either is about to run
    //or just finished running
    //calling it dbgAlert instead of debugAlert means my IDE won't
    //auto-suggest other vars with debug at the beginning
    public void dbgAlert(String message) {
        if (debugMode) {
            System.out.println(message);
            message = null;
            System.gc();
        }
    }
    //use dbgAlert for the following:
    //-start of an event handler
    //-end of an event handler
    //-use of the new keyword (for analyzing memory usage and possible leaks/dangling references/bad iteration
    //-close to where something can go wrong
    //-displaying the value of something that could potentially be problematic

    //debugMode is only useful for developing and seeing additional info printed out to the console
    public void enableDebugMode() {
        debugMode = true;
        System.out.println("Debug mode has been enabled.");
        System.out.println("To turn it off, just restart the game.");
        //why not just directly change the debugMode variable? because of lambda expressions requiring
        //variables that are final or effectively final.
        //a workaround for this limitation is by calling methods rather than having the lambda expression
        //itself change anything
    }

    //methods getting or setting controls status (keyboard or touchscreen)

    //get whether the controls are touchscreen or keyboard
    public String getControlStatus(){
        //when debug mode is enabled, dbgAlert displays the message, otherwise nothing happens
        dbgAlert("ran getControlStatus()");

        File controlSettingsFile = new File("settings/controls.txt");
        dbgAlert("new File controlSettingsFile");
        dbgAlert("Opened File settings/controls.txt");
        Scanner controlSettingsIn;
        try {
            controlSettingsIn = new Scanner(controlSettingsFile);
            dbgAlert("new Scanner controlSettingsIn");
            String status = controlSettingsIn.next();
            controlSettingsIn.close();
            controlSettingsIn = null;
            controlSettingsFile = null;
            dbgAlert("Successfully get controlStatus with controlSettingsIn");
            dbgAlert("status value is: " + status);
            return status;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        controlSettingsIn = null;
        controlSettingsFile = null;
        dbgAlert("Problem with getControlStatus() method");
        return "error";
    }

    //set controls to either touchscreen or keyboard
    public void setControlStatus(String status) {
        dbgAlert("ran setControlStatus() method with status=" + status);
        File controlsFile = new File("settings/controls.txt");
        dbgAlert("new File controlsFile");
        dbgAlert("Opening settings/controls.txt");
        try {
            PrintWriter fileOut = new PrintWriter(controlsFile);
            dbgAlert("new PrintWriter fileOut");
            fileOut.write(status);
            fileOut.close();
            dbgAlert("Successfully updated controlStatus with fileOut/controlsFile");
            fileOut = null;
            controlsFile = null;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    //get whether the game is windowed or fullscreen
    public String getWindowModeStatus() {
        dbgAlert("ran getWindowModeStatus() method");
        File windowModeSettingsFile = new File("settings/window_mode.txt");
        dbgAlert("new File windowModeSettingsFile");
        dbgAlert("Opening file settings/window_mode.txt");
        Scanner windowModeSettingsIn;
        try {
            windowModeSettingsIn = new Scanner(windowModeSettingsFile);
            dbgAlert("new Scanner windowModeSettingsIn");
            String status = windowModeSettingsIn.next();
            windowModeSettingsIn.close();
            windowModeSettingsFile = null;
            windowModeSettingsIn = null;
            dbgAlert("successfully got windowModeStatus");
            dbgAlert("windowModeStatus is " + status);
            return status;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return "error";
    }

    //set the window mode to windowed or fullscreen
    public void setWindowModeStatus(String status) {
        dbgAlert("ran setWindowModeStatus() method with status=" + status);
        File windowModeSettingsFile = new File("settings/window_mode.txt");
        dbgAlert("new File windowModeSettingsFile");
        dbgAlert("Opening file settings/window_mode.txt");
        try {
            PrintWriter fileOut = new PrintWriter(windowModeSettingsFile);
            dbgAlert("new PrintWriter fileOut");
            fileOut.write(status);

            fileOut.close();
            fileOut = null;
            windowModeSettingsFile = null;
            dbgAlert("successfully updated WindowStatus with fileOut/windowModeSettingsFile");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }


    //what happens in JavaFX before the window is opened
    @Override
    public void init() throws Exception {
        dbgAlert("running init() method");
        super.init();
    }

    //if changes are made in the settings menu, the user has to restart the game
    public void settingsChange(Pane mainMenu, Button restartToApplyChangesButton, Button getOutOfSettingsMenu) {
        //if they make changes to settings, they have to restart the program
        dbgAlert("running settingsChange() method");
        if (!mainMenu.getChildren().contains(restartToApplyChangesButton)) {
            mainMenu.getChildren().add(restartToApplyChangesButton);
            dbgAlert("restartToApplyChangesButton added to mainMenu");
        }
        if (mainMenu.getChildren().contains(getOutOfSettingsMenu)) {
            mainMenu.getChildren().remove(getOutOfSettingsMenu);
            dbgAlert("getOutOfSettingsMenu removed from mainMenu");
        }
    }

    //what happens during window opening etc.
    @Override
    public void start(Stage stage) throws Exception {
        dbgAlert("running start() method");
        //size for fonts (this is intended for small tablets with high DPI so big text is necessary)
        final double FONT_SIZE = 30.0;
        //this font is used for menu text and whatnot
        Font standardFont = new Font(FONT_SIZE);
        dbgAlert("new Font standardFont");
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
        dbgAlert("new StackPane root");
        //main menu has the buttons for new game, continue, about, settings, and quit
        Pane mainMenu = new Pane();
        dbgAlert("new Pane mainMenu");
        Image menuBackground = new Image("file:assets/background.png");
        dbgAlert("new Image menuBackground");
        ImageView menuBackgroundView = new ImageView(menuBackground);
        dbgAlert("new ImageView menuBackgroundView");
        mainMenu.getChildren().add(menuBackgroundView);
        dbgAlert("menuBackgroundView added to mainMenu");
        Label label1 = new Label("This game isn't finished yet!");
        dbgAlert("new Label label1");
        label1.setFont(standardFont);
        //buttons
        Button newButton = new Button();
        dbgAlert("new Button newButton");
        newButton.setText("New Game");
        newButton.setFont(standardFont);
        newButton.setLayoutX(50);
        newButton.setLayoutY(50);

        Button continueButton = new Button();
        dbgAlert("new Button continueButton");
        continueButton.setText("Continue");
        continueButton.setFont(standardFont);
        continueButton.setLayoutX(50);
        continueButton.setLayoutY(150);

        Button aboutButton = new Button();
        dbgAlert("new button aboutButton");
        aboutButton.setText("About");
        aboutButton.setFont(standardFont);
        aboutButton.setLayoutX(50);
        aboutButton.setLayoutY(250);

        Button settingsButton = new Button();
        dbgAlert("new Button settingsButton");
        settingsButton.setText("Settings");
        settingsButton.setFont(standardFont);
        settingsButton.setLayoutX(50);
        settingsButton.setLayoutY(350);

        Button controlsButton = new Button("Controls");
        dbgAlert("new Button controlsButton");
        controlsButton.setFont(standardFont);
        controlsButton.setLayoutX(50);
        controlsButton.setLayoutY(450);

        Button quitButton = new Button();
        dbgAlert("new Button quitButton");
        quitButton.setText("Quit");
        quitButton.setFont(standardFont);
        quitButton.setLayoutX(50);
        quitButton.setLayoutY(550);

        dbgAlert("Moving on to event handler code");
        //event handlers for main menu buttons

        //overlay to cover up other stuff on the screen
        dbgAlert("trying to open assets/blank.png");
        Image tempBackground = new Image("file:assets/blank.png");
        dbgAlert("new Image tempBackground");
        ImageView tempBackgroundView = new ImageView(tempBackground);
        dbgAlert("new ImageView tempBackgroundView");
        dbgAlert("opened assets/blank.png");


        //New game
        //Get name for new save
        Label enterNameLabel = new Label("Enter name");
        dbgAlert("new Label enterNameLabel");
        enterNameLabel.setFont(standardFont);
        enterNameLabel.setLayoutX(100);
        enterNameLabel.setLayoutY(100);

        TextField nameField = new TextField();
        dbgAlert("new TextField nameField");
        nameField.setLayoutX(100);
        nameField.setLayoutY(150);
        nameField.setFont(standardFont);
        Button submitNameButton = new Button("OK");
        dbgAlert("new Button submitNameButton");
        submitNameButton.setFont(standardFont);
        submitNameButton.setLayoutX(500);
        submitNameButton.setLayoutY(150);

        Button[] newSaveKeyboard = new Button[26];
        dbgAlert("new Button[] newSaveKeyboard");

        if (controls.equals("touchscreen")) {
            dbgAlert("You are playing with touchscreen controls");
            //adding touchscreen keyboard to the screen
            String[] alphabet = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
                                 "A", "S", "D", "F", "G", "H", "J", "K", "L",
                                 "Z", "X", "C", "V", "B", "N", "M"};
            //making the buttons for the onscreen keyboard
            for (int i = 0; i < 26; i++) {
                newSaveKeyboard[i] = new Button(alphabet[i]);
                newSaveKeyboard[i].setFont(standardFont);
                dbgAlert("new Button newSaveKeyboard[" + i + "]");
            }
            //setting positions for first row of keys (qwerty row)
            for (int i = 0; i < 10; i++) {
                newSaveKeyboard[i].setLayoutY(300);
                newSaveKeyboard[i].setLayoutX((70 * i) + 50);
            }

            //setting positions for the second row of keys (asdf row)
            for (int i = 10; i < 19; i++) {
                newSaveKeyboard[i].setLayoutY(400);
                newSaveKeyboard[i].setLayoutX((70 * (i - 10)) + 85);
            }

            //setting positions for the third row of keys (zxcv row)
            for (int i = 19; i < 26; i++) {
                newSaveKeyboard[i].setLayoutY(500);
                newSaveKeyboard[i].setLayoutX((70 * (i - 19)) + 140);
            }

            //touchscreen keyboard event handlers
            for (int i = 0; i < 26; i++) {
                final int x = i;
                newSaveKeyboard[i].setOnAction(e -> {
                    dbgAlert("running event handler for newSaveKeyboard[" + x + "]");
                    if (nameField.getCharacters().length() < 10) {
                        nameField.appendText(alphabet[x]);
                    }

                    dbgAlert("ran event handler for newSaveKeyboard[" + x + "]");
                    System.gc();
                });


            }

        } else {
            dbgAlert("You are playing with keyboard controls");
        }

        Button getOutOfNewMenu = new Button("Cancel");
        dbgAlert("new Button getOutOfNewMenu");
        getOutOfNewMenu.setFont(standardFont);
        getOutOfNewMenu.setLayoutX(600);
        getOutOfNewMenu.setLayoutY(150);

        getOutOfNewMenu.setOnAction(e -> {
            dbgAlert("running getOutOfNewMenu event handler");
            mainMenu.getChildren().removeAll(tempBackgroundView, enterNameLabel, nameField, submitNameButton, getOutOfNewMenu);
            dbgAlert("tempBackgroundView, enterNameLavel, nameField, submitNameButton, and getOutOfNewMenu removed from mainMenu");
            if (controls.equals("touchscreen")) {
                for (int i = 0; i < 26; i++) {
                    mainMenu.getChildren().removeAll(newSaveKeyboard[i]);
                }
            }
            dbgAlert("ran getOutOfNewMenu event handler");
            System.gc();
        });

        newButton.setOnAction(e -> {
            dbgAlert("Running newButton event handler");
            mainMenu.getChildren().addAll(tempBackgroundView, enterNameLabel, nameField, submitNameButton, getOutOfNewMenu);
            dbgAlert("tempBackgroundView, enterNameLabel, nameField, submitNameButton, and getOutOfNewMenu added to mainMenu");
            if (controls.equals("touchscreen")) {
                for (int i = 0; i < 26; i++) {
                    mainMenu.getChildren().add(newSaveKeyboard[i]);
                    dbgAlert("newSaveKeyboard[" + i + "] added to mainMenu");
                }

            }
            //this method is not finished yet

            //try to force garbage collection because clicking menus increases memory usage
            System.gc();
            dbgAlert("Ran newButton event handler");
        });



        //^end of new game stuff



        //Continue an existing game
        dbgAlert("got to continueButton code");
        FileChooser saveChooser = new FileChooser();
        dbgAlert("new FileChooser saveChooser");
        FileChooser.ExtensionFilter saveFilter = new FileChooser.ExtensionFilter("Game saves", "*.save");
        dbgAlert("new FileChooser.ExtensionFilter saveFilter");

        continueButton.setOnAction(e -> {
            dbgAlert("Running continueButton event handler");

            //not yet implemented event handler
            saveChooser.setTitle("Open Existing Game Save");
            saveChooser.getExtensionFilters().add(saveFilter);
            File selectedGameSave = saveChooser.showOpenDialog(stage);
            dbgAlert("new File selectedGameSave");
            if (selectedGameSave == null) {
                dbgAlert("Player did not select a game save to load");
            } else {
                dbgAlert("Proceeding with loading game save");
                dbgAlert("Game save selected: " + selectedGameSave.getName());
                //loading game is not yet implemented
            }
            dbgAlert("This feature is not finished yet");


            System.gc();
            dbgAlert("Ran continueButton event handler");
        });
        //^end of continue game stuff

        dbgAlert("Got to about button code");
        //about button stuff

        //about button adds stuff on top of the screen
        //getOutOfAboutMenu gets rid of it
        Button getOutOfAboutMenu = new Button("Back to main menu");
        dbgAlert("new Button getOutOfAboutMenu");
        getOutOfAboutMenu.setFont(standardFont);
        getOutOfAboutMenu.setLayoutX(50);
        getOutOfAboutMenu.setLayoutY(300);


        Text aboutText = new Text("About\nThis game was made by a computer science student.\nThis game is not finished yet!" +
                "\nThis game has big text and the option to use touchscreen controls because it's intended\n" +
                "for small tablets with 1280x800 resolution screens, though you can also play in\nwindowed mode with keyboard controls.");
        dbgAlert("new Text aboutText");
        aboutText.setFont(standardFont);
        aboutText.setLayoutX(50);
        aboutText.setLayoutY(50);

        //lambdas for opening and closing the about menu
        aboutButton.setOnAction(e -> {
            dbgAlert("Running aboutButton event handler");
            mainMenu.getChildren().addAll(tempBackgroundView, aboutText, getOutOfAboutMenu);
            dbgAlert("tempBackgroundView, aboutText, and getOutOfAboutMenu added to mainMenu");
            //try to force garbage collection because clicking menus increases memory usage
            System.gc();
            dbgAlert("Ran aboutButton event handler");
        });

        getOutOfAboutMenu.setOnAction(e -> {
            dbgAlert("Running getOutOfAboutMenu event handler");
            mainMenu.getChildren().removeAll(tempBackgroundView, aboutText, getOutOfAboutMenu);
            dbgAlert("tempBackgroundView, aboutText, and getOutOfAboutMenu removed from mainMenu");
            //try to force garbage collection because clicking menus increases memory usage
            System.gc();
            dbgAlert("Ran getOutOfAboutMenu event handler");
        });

        //^end of about button events

        dbgAlert("Got to settings menu code");
        //settings menu stuff

        Text settingsText = new Text("Settings");
        dbgAlert("new Text settingsText");
        settingsText.setFont(standardFont);
        settingsText.setLayoutX(50);
        settingsText.setLayoutY(50);
        Text settingsNoteText = new Text("Restart the game in order for the changes to take effect.");
        dbgAlert("new Text settingsNoteText");
        settingsNoteText.setFont(standardFont);
        settingsNoteText.setFill(Color.RED);
        settingsNoteText.setLayoutX(50);
        settingsNoteText.setLayoutY(100);
        Button getOutOfSettingsMenu = new Button("Back to main menu");
        dbgAlert("new Button getOutOfSettingsMenu");
        getOutOfSettingsMenu.setFont(standardFont);
        getOutOfSettingsMenu.setLayoutX(50);
        getOutOfSettingsMenu.setLayoutY(600);

        //controls settings
        Text controlsText = new Text("Controls:");
        dbgAlert("new Text controlText");
        controlsText.setFont(standardFont);
        controlsText.setLayoutX(50);
        controlsText.setLayoutY(150);
        Button keyboardControlsButton = new Button("Keyboard");
        dbgAlert("new Button keyboardControlsButton");
        keyboardControlsButton.setFont(standardFont);
        Border activeBorder = new Border(new BorderStroke(Color.GREEN,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5.0)));
        dbgAlert("new Border activeBorder");
        keyboardControlsButton.setLayoutX(100);
        keyboardControlsButton.setLayoutY(200);
        Button touchscreenControlsButton = new Button("Touchscreen");
        dbgAlert("new Button touchscreenControlsButton");
        touchscreenControlsButton.setFont(standardFont);
        touchscreenControlsButton.setLayoutX(300);
        touchscreenControlsButton.setLayoutY(200);
        if (controls.equals("keyboard")) {
            keyboardControlsButton.setBorder(activeBorder);
        } else {
            touchscreenControlsButton.setBorder(activeBorder);
        }
        Text currentControlsText = new Text("Current controls: ");
        dbgAlert("new Text currentControlsText");
        currentControlsText.setFont(standardFont);
        currentControlsText.setLayoutX(50);
        currentControlsText.setLayoutY(350);
        String controlsStatus = getControlStatus();
        Text controlsStatusFromFile = new Text(controlsStatus);
        dbgAlert("new Text controlsStatusFromFile");
        controlsStatusFromFile.setFont(standardFont);
        controlsStatusFromFile.setLayoutX(300);
        controlsStatusFromFile.setLayoutY(350);

        //force the user to close the program and reopen it after they make changes
        Button restartToApplyChangesButton = new Button("Quit to apply changes");
        dbgAlert("new Button restartToApplyChangesButton");
        restartToApplyChangesButton.setFont(standardFont);
        restartToApplyChangesButton.setLayoutX(50);
        restartToApplyChangesButton.setLayoutY(600);

        restartToApplyChangesButton.setOnAction(e -> {
            dbgAlert("running restartToApplyChangesButton event handler");
            System.exit(0);
        });

        //control buttons event handlers
        keyboardControlsButton.setOnAction(e -> {
            dbgAlert("running keyboardControlsbutton event handler");
            setControlStatus("keyboard");

            //if they make changes to settings, they have to restart the program
            settingsChange(mainMenu, restartToApplyChangesButton, getOutOfSettingsMenu);

            //try to force garbage collection because clicking menus increases memory usage
            System.gc();
            dbgAlert("ran keyboardControlsButton event handler");

        });
        touchscreenControlsButton.setOnAction(e -> {
            dbgAlert("running touchscreenControlsButton event handler");

            setControlStatus("touchscreen");

            //if they make changes to settings, they have to restart the program
            settingsChange(mainMenu, restartToApplyChangesButton, getOutOfSettingsMenu);

            //try to force garbage collection because clicking menus increases memory usage
            System.gc();
            dbgAlert("ran touchscreenControlsButton event handler");

        });

        //window mode settings
        Text windowModeText = new Text("Window mode: ");
        dbgAlert("new Text windowModeText");
        windowModeText.setFont(standardFont);
        windowModeText.setLayoutX(50);
        windowModeText.setLayoutY(400);
        Button fullscreenWindowModeButton = new Button("Fullscreen");
        dbgAlert("new Button fulscreenWindowModeButton");
        fullscreenWindowModeButton.setFont(standardFont);
        fullscreenWindowModeButton.setLayoutX(100);
        fullscreenWindowModeButton.setLayoutY(450);
        Button windowedWindowModeButton = new Button("Windowed");
        dbgAlert("new Button windowedWindowModeButton");
        windowedWindowModeButton.setFont(standardFont);
        windowedWindowModeButton.setLayoutX(300);
        windowedWindowModeButton.setLayoutY(450);
        switch (windowMode) {
            case "fullscreen":
                fullscreenWindowModeButton.setBorder(activeBorder);
                break;
            case "windowed":
                windowedWindowModeButton.setBorder(activeBorder);
                break;
            default:
                break;
        }
        Text currentWindowModeText = new Text("Current window mode: ");
        dbgAlert("new Text currentWindowModeText");
        currentWindowModeText.setFont(standardFont);
        currentWindowModeText.setLayoutX(50);
        currentWindowModeText.setLayoutY(550);
        String windowModeStatus = getWindowModeStatus();
        Text windowModeStatusFromFile = new Text(windowModeStatus);
        dbgAlert("new Text windowModeStatusFromFile");
        windowModeStatusFromFile.setFont(standardFont);
        windowModeStatusFromFile.setLayoutX(400);
        windowModeStatusFromFile.setLayoutY(550);

        //window mode button event handlers
        fullscreenWindowModeButton.setOnAction(e -> {
            dbgAlert("running fullscreenWindowModeButton event handler");
            setWindowModeStatus("fullscreen");

            //if they make changes to settings, they have to restart the program
            settingsChange(mainMenu, restartToApplyChangesButton, getOutOfSettingsMenu);

            //try to force garbage collection because clicking menus increases memory usage
            System.gc();
            dbgAlert("ran fullscreenWindowModebutton event handler");
        });
        windowedWindowModeButton.setOnAction(e -> {
            dbgAlert("running windowedModebutton event handler");
            setWindowModeStatus("windowed");

            //if they make changes to settings, they have to restart the program
            settingsChange(mainMenu, restartToApplyChangesButton, getOutOfSettingsMenu);

            //try to force garbage collection because clicking menus increases memory usage
            System.gc();
            dbgAlert("ran windowedWindowModeButton event handler");
        });



        settingsButton.setOnAction(e -> {
            dbgAlert("running settingsButton event handler");
            mainMenu.getChildren().addAll(tempBackgroundView, settingsText, settingsNoteText, getOutOfSettingsMenu);
            dbgAlert("tempBackgroundView, settingsText, settingsNoteText, and getOutOfSettingsMenu added to mainMenu");
            mainMenu.getChildren().addAll(controlsText, keyboardControlsButton, touchscreenControlsButton, currentControlsText, controlsStatusFromFile);
            dbgAlert("controlsText, keyboardControlsbutton, touchscreenControlsbutton, currentControlsText, and controlsStatusFromFile added to mainMenu");
            mainMenu.getChildren().addAll(windowModeText, fullscreenWindowModeButton, windowedWindowModeButton, currentWindowModeText, windowModeStatusFromFile);
            dbgAlert("windowModeText, fullscreenWindowModeButton, windowedWindowModeButton, currentWindowModeText, and windowModeStatusFromFile added to mainMenu");
            //try to force garbage collection because clicking menus increases memory usage
            System.gc();
            dbgAlert("ran settingsButton event handler");
        });

        getOutOfSettingsMenu.setOnAction(e -> {
            dbgAlert("running getOutOfSettingsMenu event handler");
            mainMenu.getChildren().removeAll(tempBackgroundView, settingsText, settingsNoteText, getOutOfSettingsMenu);
            dbgAlert("tempBackgroundView, settingsText, settingsNoteText, and getOutOfSettingsMenu removed from mainMenu");
            mainMenu.getChildren().removeAll(controlsText, keyboardControlsButton, touchscreenControlsButton, currentControlsText, controlsStatusFromFile);
            dbgAlert("controlsText, keyboardControlsButton, touchscreenControlsButton, currentControlsText, and controlsStatusFromFile removed from mainMenu");
            mainMenu.getChildren().removeAll(windowModeText, fullscreenWindowModeButton, windowedWindowModeButton, currentWindowModeText, windowModeStatusFromFile);
            dbgAlert("windowModeText, fullscreenWindowModeButton, windowedWindowModeButton, currentWindowModeText, and windowModeStatusFromFile removed from mainMenu");
            //try to force garbage collection because clicking menus increases memory usage
            System.gc();
            dbgAlert("ran getOutOfSettingsMenu event handler");
        });

        //^end of settings events

        //controls info menu
        Text controlsMenuText = new Text("Controls\nThis is where the controls will be listed once the game is developed more.");
        dbgAlert("new Text controlsMenuText");
        controlsMenuText.setFont(standardFont);
        controlsMenuText.setLayoutX(50);
        controlsMenuText.setLayoutY(50);
        Button getOutOfControlsMenu = new Button("Return to main menu");
        dbgAlert("new Button getOutOfControlsMenu");
        getOutOfControlsMenu.setFont(standardFont);
        getOutOfControlsMenu.setLayoutX(50);
        getOutOfControlsMenu.setLayoutY(300);

        //displays controls (not yet implemented)
        controlsButton.setOnAction(e -> {
            dbgAlert("running controlsButton event handler");
            mainMenu.getChildren().addAll(tempBackgroundView, controlsMenuText, getOutOfControlsMenu);
            dbgAlert("tempBackgroundView, controlsMenuText, and getOutOfControlsmenu added to mainMenu");
            //try to force garbage collection because clicking menus increases memory usage
            System.gc();
            dbgAlert("ran controlsButton event handler");
        });
        getOutOfControlsMenu.setOnAction(e -> {
            dbgAlert("running getOutOfControlsMenu event handler");
            mainMenu.getChildren().removeAll(tempBackgroundView, controlsMenuText, getOutOfControlsMenu);
            dbgAlert("tempBackgroundView, controlsMenuText, and getOutOfControlsMenu removed from mainMenu");
            //try to force garbage collection because clicking menus increases memory usage
            System.gc();
            dbgAlert("ran getOutOfControlsMenu event handler");
        });

        //^end of controls info menu

        //quit button, very simple
        quitButton.setOnAction(e -> {
            dbgAlert("running quitButton event handler");
            //return code 0 means no errors, any other number means something went wrong
            System.exit(0);

        });

        //^end of quit button

        //debug mode button
        Button debugModeButton = new Button("Enable Debug Mode");
        dbgAlert("new Button debugModeButton");
        debugModeButton.setFont(standardFont);
        debugModeButton.setLayoutX(900);
        debugModeButton.setLayoutY(200);
        debugModeButton.setOnAction(e -> {
            dbgAlert("running debugModeButton event handler");
            enableDebugMode();
            System.gc();
            dbgAlert("ran debugModeButton event handler");
        });
        //^end of debug mode button

        //adding menu items to the menu
        mainMenu.getChildren().addAll(label1, newButton, continueButton, aboutButton, settingsButton, controlsButton, quitButton);
        dbgAlert("label1, newButton, continueButton, aboutButton, settingsButton, controlsButton, and quitButton added to mainMenu");

        //debug button, take this out before publishing the game
        mainMenu.getChildren().add(debugModeButton);
        dbgAlert("added debugModeButton to mainMenu");

        //adding stuff to the window
        root.getChildren().add(mainMenu);
        dbgAlert("added mainMenu to root");
        Scene scene = new Scene(root);
        dbgAlert("new Scene scene");
        dbgAlert("added root to scene");
        stage.setScene(scene);
        dbgAlert("set stage scene to scene");
        //displaying the window
        stage.show();
        dbgAlert("stage.show()");

    }

    //what happens in JavaFX after the window is closed
    @Override
    public void stop() throws Exception {
        dbgAlert("running stop() method");
        super.stop();
    }

    //JavaFX boilerplate main
    public static void main(String[] args) {
        launch(args);
    }
}
