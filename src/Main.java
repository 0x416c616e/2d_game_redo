import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

//if you want to see where most stuff happens, scroll down to the init() method
public class Main extends Application {
    //this prints out helpful information if enabled
    boolean debugMode = false;
    //if this is true, it will log debugging info to a file called debug_log.txt
    boolean logDebugging = false;
    //both of the above booleans should be false, unless you want to see debug info about the STARTUP of the program

    //if debug mode is enabled, dbgAlert will print a message
    //containing info about the code that either is about to run
    //or just finished running
    //calling it dbgAlert instead of debugAlert means my IDE won't
    //auto-suggest other vars with debug at the beginning
    public void dbgAlert(String message) {
        if (debugMode) {
            System.out.println(message);
            System.gc();
        }
        //logging debug info to file
        if (logDebugging == true) {
            File debugLogFile = new File("debug_log/log.txt");
            FileWriter myfileWriter = null;
            try {
                FileWriter myFileWriter = new FileWriter(debugLogFile, true);
                //the second boolean argument means appending mode is true
                PrintWriter debugFileOut = new PrintWriter(myFileWriter);
                debugFileOut.write(message + "\n");
                debugFileOut.close();
                myFileWriter.close();
            } catch (FileNotFoundException ex) {
                System.out.println("FileNotFoundException for dbgAlert");
                ex.printStackTrace();
            } catch (IOException ioex) {
                System.out.println("IOException for dbgAlert");
                ioex.printStackTrace();
            }
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
        dbgAlert("ran enableDebugMode");
    }

    //this method is used for either enabling or disabling the debug logging
    //arg should be either "true" or "false" Strings, as per the text file
    //this is used with the event handlers for the buttons on the main menu
    //for enabling or disabling debug logging
    //note: debug LOGGING, not debug MODE, which is a different setting
    public void toggleDebugLogMode(String trueOrFalse) {
        dbgAlert("running toggleDebugLogMode method");
        String enabledOrDisabled = "";
        if (trueOrFalse.equals("true")) {
            logDebugging = true;
            enabledOrDisabled = "enabled";
        } else {
            logDebugging = false;
            enabledOrDisabled = "disabled";
        }

        File debugLogFile = new File("settings/debug_logging_option.txt");
        dbgAlert("new File debugLogFile");
        FileWriter debugFileWriter = null;
        try {
            debugFileWriter = new FileWriter(debugLogFile);
            dbgAlert("new FileWriter debugFileWriter");
            //the second boolean argument means appending mode is true
            PrintWriter debugFileOut = new PrintWriter(debugFileWriter);
            dbgAlert("new PrintWriter debugFileOut");
            debugFileOut.write(trueOrFalse);
            debugFileOut.close();
            debugFileWriter.close();

            dbgAlert(enabledOrDisabled + " debug logging in debug_logging_option.txt");
        } catch (FileNotFoundException ex) {
            dbgAlert("FileNotFoundException for toggleDebugLogMode");
            ex.printStackTrace();
        } catch (IOException ioex) {
            dbgAlert("IOException for toggleDebugLogMode");
            ioex.printStackTrace();
        }
        dbgAlert("ran toggleDebugLogMode method");
    }

    //pseudo-breakpoint functionality to pause program for user-specified duration
    //because otherwise you'll see the debug feed go by super fast
    public void setBreakpoint(int duration){
        dbgAlert("running getBreakpoint() method");
        try {
            Thread.sleep(duration);
        } catch (InterruptedException ex) {
            dbgAlert("InterruptedException for setBreakpoint()");
            ex.printStackTrace();
        }
        dbgAlert("ran getBreakpoint() method");
    }

    //methods getting or setting controls status (keyboard or touchscreen)

    //get whether the controls are touchscreen or keyboard
    public String getControlStatus(){
        //when debug mode is enabled, dbgAlert displays the message, otherwise nothing happens
        dbgAlert("running getControlStatus()");

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
            dbgAlert("FileNotFoundException for getControlStatus");
            ex.printStackTrace();
        }
        controlSettingsIn = null;
        controlSettingsFile = null;
        dbgAlert("Problem with getControlStatus() method");
        return "error";
    }

    //set controls to either touchscreen or keyboard
    public void setControlStatus(String status) {
        dbgAlert("running setControlStatus() method with status=" + status);
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
            dbgAlert("FileNotFoundException for setControlStatus");
            ex.printStackTrace();
        }
    }

    //get whether the game is windowed or fullscreen
    public String getWindowModeStatus() {
        dbgAlert("running getWindowModeStatus() method");
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
            dbgAlert("FileNotFoundException for getWindowModeStatus");
            ex.printStackTrace();
        }
        dbgAlert("getWindowModeStatus error");
        return "error";
    }

    //set the window mode to windowed or fullscreen
    public void setWindowModeStatus(String status) {
        dbgAlert("running setWindowModeStatus() method with status=" + status);
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
            dbgAlert("FileNotFoundException for setWindowModeStatus");
            ex.printStackTrace();
        }
    }

    //this is a helper function for setResolution to make its body less repetitive
    public void writeResolution(String res) {
        dbgAlert("running writeResolution method");
        File resolutionModeSettingsFile = new File("settings/resolution.txt");
        dbgAlert("new File resolutionModeSettingsFile");
        try {
            PrintWriter resolutionFileOut = new PrintWriter(resolutionModeSettingsFile);
            dbgAlert("new PrintWriter resolutionFileOut");
            List<String> resolutionList = List.of("1280x720", "1280x800", "1920x1080");
            //check if provided resolution is one of the three valid ones
            if (resolutionList.contains(res)) {
                dbgAlert("valid resolution arg provided for writeResolution");
                resolutionFileOut.write(res);
            }
            resolutionFileOut.close();

            } catch (IOException ioex) {
            dbgAlert("IOException with writeResolution");
            ioex.printStackTrace();
        }
        dbgAlert("ran writeResolution method");
    }

    //change the resolution setting in settings/resolution.txt
    public void setResolution(String res) {
        dbgAlert("running setResolution method");
        switch (res) {
            case "1280x720":
            case "1280x800":
            case "1920x1080":
                writeResolution(res);
                break;
            default:
                dbgAlert("error with setResolution method with res arg: " + res);
                break;
        }
        dbgAlert("ran setResolution method");
    }

    //load the resolution setting from settings/resolution.txt
    public String getResolution() {
        dbgAlert("running getResolution() method");
        File resolutionSettingsfile = new File("settings/resolution.txt");
        dbgAlert("new File resolutionSettingsFile");
        Scanner resolutionSettingsIn;
        try {
            resolutionSettingsIn = new Scanner(resolutionSettingsfile);
            dbgAlert("new Scanner resolutionSettingsIn");
            String resolutionSetting = resolutionSettingsIn.next();
            return resolutionSetting;
        } catch (FileNotFoundException ex) {
            dbgAlert("FileNotFoundException for getResolution");
            ex.printStackTrace();
        }
        dbgAlert("error with getResolution()");
        return "error";
    }




    //what happens in JavaFX before the window is opened
    @Override
    public void init() throws Exception {
        dbgAlert("running init() method");
        super.init();
        dbgAlert("ran init() method");
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
        dbgAlert("ran settingsChange");
    }

    //what happens during window opening etc.
    @Override
    public void start(Stage stage) throws Exception {
        dbgAlert("running start() method");

        //check if debug logging is enabled or not
        Scanner loggingSettingsIn = null;
        dbgAlert("new Scanner loggingSettingsIn");
        File debugLoggingFile = new File("settings/debug_logging_option.txt");
        dbgAlert("new File debugLoggingFile");
        try {
            dbgAlert("opened debug_logging_option.txt");
            loggingSettingsIn = new Scanner(debugLoggingFile);
            dbgAlert("new Scanner controlSettingsIn");
            String debugLoggingOption = loggingSettingsIn.next();
            dbgAlert("value of debugLoggingOption: " + debugLoggingOption);
            if (debugLoggingOption.equals("true")) {
                dbgAlert("Logging debug info to debug_log/log.txt");
                logDebugging = true;
            }
            loggingSettingsIn.close();
        } catch (FileNotFoundException ex) {
            dbgAlert("FileNotFoundException for start() opening debug_logging_option.txt");
            ex.printStackTrace();
        }


        //size for fonts (this is intended for small tablets with high DPI so big text is necessary)
        final double FONT_SIZE = 30.0;
        //this font is used for menu text and whatnot
        Font standardFont = new Font("Arial", FONT_SIZE);
        dbgAlert("new Font standardFont");
        //getting settings for controls and window mode from config files
        String controls = getControlStatus();
        String windowMode = getWindowModeStatus();
        if (windowMode.equals("fullscreen")) {
            stage.setFullScreen(true);
        }
        //Basic window stuff
        stage.setTitle("2D RPG written in Java");
        String currentResolution = getResolution();
        switch (currentResolution) {
            case "1280x720":
                stage.setWidth(1280);
                stage.setHeight(720);
                dbgAlert("resolution is 1280x720");
                break;
            case "1280x800":
                stage.setWidth(1280);
                stage.setHeight(800);
                dbgAlert("resolution is 1280x800");
                break;
            case "1920x1080":
                stage.setWidth(1920);
                stage.setHeight(1080);
                dbgAlert("resolution is 1920x1080");
                break;
            default:
                dbgAlert("resolution configuration error with value: " + currentResolution);
                break;
        }

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

        //adding touchscreen buttons for player name input
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

        //pattern matching for name input
        //can be 1-10 characters long for player's name (which is also the name of the save
        //i.e. Joe.save
        Pattern namePattern = Pattern.compile("^[A-Za-z]{1,10}$");
        dbgAlert("new Pattern namePattern");

        //assets for the name error screen
        ImageView nameErrorBackground = new ImageView(tempBackground);
        dbgAlert("new ImageView nameErrorBackground");
        Text nameErrorText = new Text("The name you entered is not valid.\nNames can only contain numbers and must be 1-10 characters long.");
        dbgAlert("new Text nameErrorText");
        nameErrorText.setFont(standardFont);
        nameErrorText.setLayoutX(50);
        nameErrorText.setLayoutY(100);

        Button closeNameErrorButton = new Button("OK");
        dbgAlert("new Button closeNameErrorButton");
        closeNameErrorButton.setFont(standardFont);
        closeNameErrorButton.setLayoutX(50);
        closeNameErrorButton.setLayoutY(150);

        closeNameErrorButton.setOnAction(e -> {
            dbgAlert("Running closeNameErrorButton event handler");
            mainMenu.getChildren().removeAll(nameErrorBackground, nameErrorText, closeNameErrorButton);
            dbgAlert("nameErrorBackground, nameErrorText, and closeNameErrorButton removed from mainMenu");
            dbgAlert("Ran closeNameErrorButton event handler");
            System.gc();
        });





        //clicking button to submit new save game with desired name
        submitNameButton.setOnAction(e -> {
            dbgAlert("Running submitNameButton event handler");
            Matcher nameMatcher = namePattern.matcher(nameField.getText());
            dbgAlert("new Matcher nameMatcher");
            dbgAlert("nameField.getText():" + nameField.getText());
            dbgAlert("Does the user-entered name match the regex? " + nameMatcher.matches());
            if (!nameMatcher.matches()) {
                mainMenu.getChildren().addAll(nameErrorBackground, nameErrorText, closeNameErrorButton);
                dbgAlert("added nameErrorBackground, nameErrorText, and closeNameErrorButton to mainMenu");
            } else {
                //name matched regex, so here's where I can implement the additional save stuff and eventually loading a new game
                dbgAlert("Name matched regex, proceeding with further new game stuff");

                //!!!!!!!!!!!!!!!!THIS IS WHERE I LEFT OFF LAST TIME!!!!!!!!!!!!!!!
                //1. Change capitalization of name to first letter being uppercase and all other letters are lowercase
                String playerName = nameField.getText().toLowerCase();
                playerName = playerName.toUpperCase().charAt(0) + playerName.substring(1, playerName.length());
                dbgAlert("playerName: " + playerName);
                //2. check if a save with that name already exists
                File newSaveFile = new File("saves/" + playerName + ".save");
                dbgAlert("new File newSaveFile");
                Boolean saveAlreadyExists = newSaveFile.exists();
                if (saveAlreadyExists) {
                    dbgAlert("Save with that name already exists, therefore can't make it with that name");
                } else {
                    dbgAlert("Save with that name doesn't already exist, therefore it can be made");
                }
                //3. if not, try to make a new save
                //4. if able to write a new save file, save it as name.save, i.e. Joe.save
                //5. let player know where the save file is (saves/name.save)
                //6. the save file will eventually be structured as XML
                //7. think about making different java classes for the player, and for the world map
                //8. also need to make a world map XML file that can be loaded
                //9. and some basic images for grass (passable), rocks (can't be moved through), and the player
                //loading can't be hard-coded into the "new" menu feature because it also needs to apply to the "continue" menu feature
                //JAR build/artifact stuff might be more complicated with multiple classes but I'll have to sort that out

            }

            dbgAlert("Ran submitNameButton event handler");
            System.gc();
        });



        Button getOutOfNewMenu = new Button("Cancel");
        dbgAlert("new Button getOutOfNewMenu");
        getOutOfNewMenu.setFont(standardFont);
        getOutOfNewMenu.setLayoutX(600);
        getOutOfNewMenu.setLayoutY(150);

        Button touchscreenNameBackspaceButton = new Button("Backspace");
        dbgAlert("new Button touchscreenNameBackspacebutton");
        touchscreenNameBackspaceButton.setFont(standardFont);
        touchscreenNameBackspaceButton.setLayoutX(750);
        touchscreenNameBackspaceButton.setLayoutY(300);


        //touchscreen backspace button gets rid of last character in name textfield
        touchscreenNameBackspaceButton.setOnAction(e -> {
            dbgAlert("running touchscreenNameBackspaceButton event handler");
            //don't try to remove a character if there are no characters in the name field
            if (nameField.getText().length() > 0) {
                nameField.setText(nameField.getText().substring(0, nameField.getText().length() - 1));
                dbgAlert("nameField: " + nameField.getText());
            } else {
                dbgAlert("nameField is empty, therefore can't use backspace");
            }
            dbgAlert("ran touchscreenNameBackspaceButton event handler");
            System.gc();
        });

        getOutOfNewMenu.setOnAction(e -> {
            dbgAlert("running getOutOfNewMenu event handler");
            mainMenu.getChildren().removeAll(tempBackgroundView, enterNameLabel, nameField, submitNameButton, getOutOfNewMenu);
            dbgAlert("tempBackgroundView, enterNameLavel, nameField, submitNameButton, and getOutOfNewMenu removed from mainMenu");
            if (controls.equals("touchscreen")) {
                for (int i = 0; i < 26; i++) {
                    mainMenu.getChildren().remove(newSaveKeyboard[i]);
                    dbgAlert("newSaveKeyboard[" + i + "] removed from mainMenu");

                }
                mainMenu.getChildren().remove(touchscreenNameBackspaceButton);
                dbgAlert("touchscreenNameBackspcaeButton removed from mainMenu");
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
                mainMenu.getChildren().add(touchscreenNameBackspaceButton);
                dbgAlert("touchscreenNameBackspaceButton added to mainMenu");
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
        getOutOfAboutMenu.setLayoutY(400);


        Text aboutText = new Text("About\nThis game was made by a computer science student.\nThis game is not finished yet!" +
                "\nThis game has big text and the option to use touchscreen controls because it's intended\n" +
                "for small tablets with 1280x800 resolution screens, though you can also play with keyboard\ncontrols, " +
                "and the game also supports 720p and 1080p, all either windowed or fullscreen.\n\nThe creator's website is here:");
        dbgAlert("new Text aboutText");
        aboutText.setFont(standardFont);
        aboutText.setLayoutX(50);
        aboutText.setLayoutY(50);


        //the about page also has a link to my website
        Hyperlink websiteLink = new Hyperlink();
        dbgAlert("new Hyperlink websiteLink");
        websiteLink.setLayoutX(50);
        websiteLink.setLayoutY(300);
        websiteLink.setFont(standardFont);
        websiteLink.setText("https://saintlouissoftware.com");
        websiteLink.setOnAction(e -> {
            dbgAlert("running websiteLink event handler");
            getHostServices().showDocument(websiteLink.getText());
            dbgAlert("ran website:ink event handler");
        });

        //lambdas for opening and closing the about menu
        aboutButton.setOnAction(e -> {
            dbgAlert("Running aboutButton event handler");
            mainMenu.getChildren().addAll(tempBackgroundView, aboutText, websiteLink, getOutOfAboutMenu);
            dbgAlert("tempBackgroundView, aboutText, websiteLink, and getOutOfAboutMenu added to mainMenu");
            //try to force garbage collection because clicking menus increases memory usage
            System.gc();
            dbgAlert("Ran aboutButton event handler");
        });

        getOutOfAboutMenu.setOnAction(e -> {
            dbgAlert("Running getOutOfAboutMenu event handler");
            mainMenu.getChildren().removeAll(tempBackgroundView, aboutText, websiteLink, getOutOfAboutMenu);
            dbgAlert("tempBackgroundView, aboutText, websiteLink, and getOutOfAboutMenu removed from mainMenu");
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
            dbgAlert("running keyboardControlsButton event handler");
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


        //resolution options
        Label resolutionLabel = new Label("Resolution");
        dbgAlert("new Label resolutionLabel");
        resolutionLabel.setLayoutX(700);
        resolutionLabel.setLayoutY(150);
        resolutionLabel.setFont(standardFont);

        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "1280x720",
                        "1280x800",
                        "1920x1080"
                );
        final ComboBox resolutionComboBox = new ComboBox(options);
        resolutionComboBox.setStyle("-fx-font: 30px \"Arial\";");
        dbgAlert("new ComboBox resolutionComboBox");
        resolutionComboBox.setLayoutX(700);
        resolutionComboBox.setLayoutY(200);
        switch (currentResolution) {
            case "1280x720":
                resolutionComboBox.getSelectionModel().select(0);
                break;
            case "1280x800":
                resolutionComboBox.getSelectionModel().select(1);
                break;
            case "1920x1080":
                resolutionComboBox.getSelectionModel().select(2);
                break;
            default:
                dbgAlert("error with fetching resolution for combobox");
                break;
        }
        resolutionComboBox.setOnAction(e -> {
            //all this does is log that it got clicked, but for UI purposes, it lets the user change the active resolution choice
            //but the way the resolution is changed is through clicking the changeResolutionButton, which then gets the selected item in
            //the resolutionComboBox
            dbgAlert("running resolutionComboBox event handler");
            dbgAlert("ran resolutionComboBox event handler");
            System.gc();
        });

        Button changeResolutionButton = new Button("Change Resolution");
        dbgAlert("new button changeResolutionButton");
        changeResolutionButton.setFont(standardFont);
        changeResolutionButton.setLayoutX(700);
        changeResolutionButton.setLayoutY(300);
        changeResolutionButton.setOnAction(e -> {
            dbgAlert("running changeResolutionButton event handler");
            String newResolutionSelection = resolutionComboBox.getSelectionModel().getSelectedItem().toString();
            dbgAlert("Resolution string: " + newResolutionSelection);
            setResolution(newResolutionSelection);
            if (mainMenu.getChildren().contains(getOutOfSettingsMenu)) {
                mainMenu.getChildren().remove(getOutOfSettingsMenu);
                mainMenu.getChildren().add(restartToApplyChangesButton);
            }
            dbgAlert("ran changeResolutionButton event handler");
            System.gc();
        });



        settingsButton.setOnAction(e -> {
            dbgAlert("running settingsButton event handler");
            mainMenu.getChildren().addAll(tempBackgroundView, settingsText, settingsNoteText, getOutOfSettingsMenu);
            dbgAlert("tempBackgroundView, settingsText, settingsNoteText, and getOutOfSettingsMenu added to mainMenu");
            mainMenu.getChildren().addAll(controlsText, keyboardControlsButton, touchscreenControlsButton, currentControlsText, controlsStatusFromFile);
            dbgAlert("controlsText, keyboardControlsbutton, touchscreenControlsbutton, currentControlsText, and controlsStatusFromFile added to mainMenu");
            mainMenu.getChildren().addAll(windowModeText, fullscreenWindowModeButton, windowedWindowModeButton, currentWindowModeText, windowModeStatusFromFile);
            dbgAlert("windowModeText, fullscreenWindowModeButton, windowedWindowModeButton, currentWindowModeText, and windowModeStatusFromFile added to mainMenu");
            mainMenu.getChildren().addAll(resolutionLabel, resolutionComboBox, changeResolutionButton);
            dbgAlert("resolutionLabel, resolutionComboBox, and changeResolutionButton added to mainMenu");
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
            mainMenu.getChildren().removeAll(resolutionLabel, resolutionComboBox, changeResolutionButton);
            dbgAlert("resolutionLabel, resolutionComboBox, and changeResolutionButton removed from mainMenu");
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

        //displays controls
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

        //Label for info about debug mode
        Label debugModeLabel = new Label("To turn off debug mode,\njust restart the game.");
        dbgAlert("new Label debugModeLabel");
        debugModeLabel.setFont(standardFont);
        debugModeLabel.setLayoutX(900);
        debugModeLabel.setLayoutY(100);

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

            if (!mainMenu.getChildren().contains(debugModeLabel)) {
                mainMenu.getChildren().add(debugModeLabel);
                dbgAlert("debugModeLabel added to mainMenu");
            }
            dbgAlert("ran debugModeButton event handler");

        });

        //^end of debug mode button

        //clear debug log button
        Button clearDebugLogButton = new Button("Clear Debug Log");
        dbgAlert("new Button clearDebugLogButton");
        clearDebugLogButton.setFont(standardFont);
        clearDebugLogButton.setLayoutX(900);
        clearDebugLogButton.setLayoutY(275);
        clearDebugLogButton.setOnAction(e -> {
            dbgAlert("Running clearDebugLogButton event handler");
            try {
                dbgAlert("Clearing debug log");
                File debugFile = new File("debug_log/log.txt");

                PrintWriter debugOut = new PrintWriter(debugFile);
                //overwrites the contents of debug_log/log.txt but doesn't add anything new to it
                //effectively just clearing it out
                debugOut.write("");

                debugOut.close();
                dbgAlert("debug log has been cleared");
            } catch (FileNotFoundException ex) {
                dbgAlert("FileNotFoundException for clearDebugLogbutton event handler");
                ex.printStackTrace();
            }
            dbgAlert("ran clearDebugLogButton event handler");
            System.gc();
        });
        //^end of debug log button

        //enable debug log button for toggling debug logging on or off
        //logs debug info to debug_log/log.txt
        Button enableDebugLogButton = new Button("Enable Debug Logging");
        dbgAlert("new button enableDebugLogButton");
        enableDebugLogButton.setFont(standardFont);
        enableDebugLogButton.setLayoutX(900);
        enableDebugLogButton.setLayoutY(350);
        enableDebugLogButton.setOnAction(e -> {
            dbgAlert("Running enableDebugLogButton event handler");

            toggleDebugLogMode("true");


            dbgAlert("Ran enableDebugLogButton event handler");
            System.gc();
        });

        //^end of enable debug log button



        //disable debug log button
        Button disableDebugLogButton = new Button("Disable Debug Logging");
        dbgAlert("new button disableDebugLogButton");
        disableDebugLogButton.setFont(standardFont);
        disableDebugLogButton.setLayoutX(900);
        disableDebugLogButton.setLayoutY(425);
        disableDebugLogButton.setOnAction(e -> {
            dbgAlert("Running enableDebugLogButton event handler");

            toggleDebugLogMode("false");

            dbgAlert("Ran enableDebugLogButton event handler");
            System.gc();
        });
        //^end of disable debug log button

        //adding menu items to the menu
        mainMenu.getChildren().addAll(label1, newButton, continueButton, aboutButton, settingsButton, controlsButton, quitButton);
        dbgAlert("label1, newButton, continueButton, aboutButton, settingsButton, controlsButton, and quitButton added to mainMenu");

        //debug button, take this out before publishing the game
        mainMenu.getChildren().addAll(debugModeButton, clearDebugLogButton, enableDebugLogButton, disableDebugLogButton);
        dbgAlert("added debugModeButton clearDebugLogbutton, enableDebugLogButton, and disableDebugLogButton to mainMenu");

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
        dbgAlert("ran start()");

    }

    //what happens in JavaFX after the window is closed
    @Override
    public void stop() throws Exception {
        dbgAlert("running stop() method");
        super.stop();
        dbgAlert("ran stop() method");
    }

    //JavaFX boilerplate main
    public static void main(String[] args) {
        launch(args);
    }
}
