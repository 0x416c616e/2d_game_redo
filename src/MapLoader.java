import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

//this class is used to load maps
//no attributes, no getters or setters
//just map-loading methods that take a lot of args
//also can unload the current map from the screen and makes sure there are no memory leaks

public class MapLoader {

    //attributes===============================================



    boolean controlsAreOnTheMap;

    boolean downPressed;
    boolean upPressed;
    boolean leftPressed;
    boolean rightPressed;
    long timeBetween;
    long THRESHOLD; //switch for either keyboard or touchscreen

    Font buttonFont;
    //constructor===============================================

    long now;
    long swap;
    //current time in nanoseconds
    //maploader deals with movement, and for smooth movement, you should only be able to move every now and then
    //for a consistent movement rate, unlike before due to how OS-specific keyboard speed polling/character repeating works
    //so only run the movement stuff if it's been a certain amount of time
    final long KEYBOARD_THRESHOLD = 100_000_000L; //100 milliseconds
    //touchscreen movement feels better with no limitations on how fast you can tap to move
    //to avoid "dead clicks"
    final long TOUCHSCREEN_THRESHOLD = 1L;
    long lastMove;
    String controls;


    Button downButton;
    Button rightButton;
    Button upButton;
    Button leftButton;

    public MapLoader(String controls) {
        //doesn't have much aside from just methods for loading stuff
        //in order to de-clutter the Main class
        buttonFont = new Font("Arial", 30.0);
        switch (controls) {
            case "keyboard":
            case "touchscreen":
                this.controls = controls;
                break;
            default:
                System.out.println("MapLoader constructor error with controls initialization");
                break;
        }
        now = System.nanoTime();
        setAllTouchscreenButtonsNotPressed();
        controlsAreOnTheMap = false;
        downButton = new Button("v");
        rightButton = new Button(">");
        upButton = new Button("^");
        leftButton = new Button("<");
    }



    public void setDownPressed(boolean downPressed) {
        this.downPressed = downPressed;
    }

    public boolean getDownPressed() {
        return this.downPressed;
    }

    public void setUpPressed(boolean upPressed) {
        this.upPressed = upPressed;
    }

    public boolean getUpPressed() {
        return this.upPressed;
    }

    public void setRightPressed(boolean rightPressed) {
        this.rightPressed = rightPressed;
    }

    public boolean getRightPressed() {
        return this.rightPressed;
    }

    public void setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
    }

    public boolean getLeftPressed() {
        return this.leftPressed;
    }

    public void setAllTouchscreenButtonsNotPressed() {
        setUpPressed(false);
        setDownPressed(false);
        setLeftPressed(false);
        setRightPressed(false);
    }

    //update current time in milliseconds
    public void updateTime() {
        now = System.nanoTime();
    }

    //you will only be able to move again after a certain amount of time
    //this method checks how long it's been since the last time you moved
    public boolean enoughTimeHasPassed() {
        //System.out.println("got here enoughTimeHasPassed");
        updateTime();
        timeBetween = now - lastMove;
        THRESHOLD = (this.controls.equals("keyboard")) ? KEYBOARD_THRESHOLD : TOUCHSCREEN_THRESHOLD;
        if (timeBetween >= THRESHOLD) {
            //System.out.println("timeBetween = " + timeBetween);
            swap = now;
            lastMove = swap;
            updateTime();
            return true;
        } else {
            //too soon to move again
            return false;
        }
    }


    //map-loading methods===============================================

    //maps are now hard-coded for performance reasons (XML parsing was too slow even though separating data from code sounds like a good idea in theory)
    //IMPORTANT INFO ABOUT MAPS AND WINDOWED MODE:
    //IMPORTANT TILES CANNOT BE ON THE BOTTOM OR RIGHTHAND EDGES BECAUSE THEY CAN BE CUT OFF
    //IF YOU NEED TO PUT MAPMOVE TILES ON THE EDGES, MAKE SURE THERE ARE TWO, IN CASE ONE GETS CUT OFF
    public void loadMap_0_0(WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        //testing loading bottom level
        String tileSizeFileNamePart = "";
        if (resolution.equals("1280x720")) {
            //System.out.println("loading map 720p");
            tileSizeFileNamePart = "40x40";
        } else if (resolution.equals("1280x800")) {
            //System.out.println("loading map 800p");
            tileSizeFileNamePart = "40x40";

        } else if (resolution.equals("1920x1080")) {
            //System.out.println("loading map 1080p");
            tileSizeFileNamePart = "60x60";
        } else {
            System.err.println("Error with loadMap0_0 for resolution part");
            System.exit(999234);
        }

        for (int xMap = 0; xMap < worldMap.getxDimension(); xMap++) {
            for (int yMap = 0; yMap < worldMap.getyDimension(); yMap++) {
                //default tile with grass and no event or collision
                //only has a bottom level image by default, no mid level or top level
                String tileGrassFileName = "file:assets/tiles/grass_" + tileSizeFileNamePart + ".png";
                worldMap.tileArray[xMap][yMap] = new Tile(tileGrassFileName);
            }
        }

        //adding only bottom level stuff, in this case, just two arrows and some water
        String downArrowFileName = "file:assets/tiles/down_arrow_" + tileSizeFileNamePart + ".png";
        int downArrowToNextMapY;
        if (!resolution.equals("1280x800")) {
            downArrowToNextMapY = 16;
        } else {
            System.out.println("800p has a different number of tiles, so moving to another map is different (down a couple rows)");
            //16:9 is 32x18 tiles, 16:10 is 32x20 tiles
            downArrowToNextMapY = 18;
        }

        //adding tiles to the map that will eventually have events that will take you to the next map if you move over it
        //there are two of each arrow (with a mapmove event) because windowed mode can get slightly cut off

        worldMap.tileArray[11][downArrowToNextMapY] = new Tile(downArrowFileName);
        worldMap.tileArray[11][downArrowToNextMapY].setEvent(new MapMove("walk", "map_0_1"));
        //System.out.println(worldMap.tileArray[11][downArrowToNextMapY].getEvent().getEventType());
        worldMap.tileArray[11][downArrowToNextMapY + 1] = new Tile(downArrowFileName);
        worldMap.tileArray[11][downArrowToNextMapY + 1].setEvent(new MapMove("walk", "map_0_1"));
        String rightArrowFileName = "file:assets/tiles/right_arrow_" + tileSizeFileNamePart + ".png";
        worldMap.tileArray[30][11] = new Tile(rightArrowFileName);
        worldMap.tileArray[30][11].setEvent(new MapMove("walk", "map_1_0"));
        worldMap.tileArray[31][11] = new Tile(rightArrowFileName);
        worldMap.tileArray[31][11].setEvent(new MapMove("walk", "map_1_0"));



        //tile data is used to figure out which images to add to the map
        worldMap.setAllBottomLevelImage(worldPane);
        worldMap.setAllMidLevelImage(worldPane);
        worldMap.setAllTopLevelImage(worldPane);
        mainMenu.getChildren().addAll(worldPane);
        player.loadPlayerFromFile();
        //System.out.print(player.toString() + "\n");

        //now that the map is loaded, time to add the player and controls
        putPlayerAndControlsOnMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
        System.gc();


    }

    public void loadMap_0_1(WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_0_1");
    }

    public void loadMap_1_1(WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet");
    }

    //other methods===============================================

    //1280x800 special case
    //maybe make the "save" function just save them above so that there won't be issues
    //because of the extra 2 rows at the bottom of the screen due to 16:10 aspect ratio instead of 16:9
    //or maybe just disallow saving there, and say this:
        //"error: can't save so close to the edge on 16:10 aspect ratio, please move your character higher up and then save"
    public void putPlayerOnMap(WorldMap worldMap, Pane worldPane, Player player) {
        int sizeOfTile = -1;
        switch (worldMap.getTileSize()) {
            case "40x40":
                sizeOfTile = 40;
                break;
            case "60x60":
                sizeOfTile = 60;
                break;
            default:
                System.err.println("Error with putPlayerOnMap");
                System.exit(234227);
        }
        if (! (sizeOfTile == -1)) {
            int xLocation = player.getX() * sizeOfTile;
            int yLocation = player.getY() * sizeOfTile;


            String nameOfImageToUse = "player_" + player.getPosition() + "_" + worldMap.getTileSize();
            boolean succeeded = false;
            switch (nameOfImageToUse) {
                case "player_down_40x40":
                    player.player_down_40x40ImageView.setLayoutX(xLocation);
                    player.player_down_40x40ImageView.setLayoutY(yLocation);
                    worldPane.getChildren().add(player.player_down_40x40ImageView);
                    succeeded = true;
                    break;
                case "player_down_60x60":
                    player.player_down_60x60ImageView.setLayoutX(xLocation);
                    player.player_down_60x60ImageView.setLayoutY(yLocation);
                    worldPane.getChildren().add(player.player_down_60x60ImageView);
                    succeeded = true;
                    break;

                case "player_right_40x40":
                    player.player_right_40x40ImageView.setLayoutX(xLocation);
                    player.player_right_40x40ImageView.setLayoutY(yLocation);
                    worldPane.getChildren().add(player.player_right_40x40ImageView);
                    succeeded = true;
                    break;
                case "player_right_60x60":
                    player.player_right_60x60ImageView.setLayoutX(xLocation);
                    player.player_right_60x60ImageView.setLayoutY(yLocation);
                    worldPane.getChildren().add(player.player_right_60x60ImageView);
                    succeeded = true;
                    break;

                case "player_up_40x40":
                    player.player_up_40x40ImageView.setLayoutX(xLocation);
                    player.player_up_40x40ImageView.setLayoutY(yLocation);
                    worldPane.getChildren().add(player.player_up_40x40ImageView);
                    succeeded = true;
                    break;
                case "player_up_60x60":
                    player.player_up_60x60ImageView.setLayoutX(xLocation);
                    player.player_up_60x60ImageView.setLayoutY(yLocation);
                    worldPane.getChildren().add(player.player_up_60x60ImageView);
                    succeeded = true;
                    break;

                case "player_left_40x40":
                    player.player_left_40x40ImageView.setLayoutX(xLocation);
                    player.player_left_40x40ImageView.setLayoutY(yLocation);
                    worldPane.getChildren().add(player.player_left_40x40ImageView);
                    succeeded = true;
                    break;
                case "player_left_60x60":
                    player.player_left_60x60ImageView.setLayoutX(xLocation);
                    player.player_left_60x60ImageView.setLayoutY(yLocation);
                    worldPane.getChildren().add(player.player_left_60x60ImageView);
                    succeeded = true;
                    break;

            }

        } else {
            System.err.println("errpr 2 with putPlayerOnMap");
            System.exit(111133);
        }

    }




    //controls methods for keyboard and touchscreen, adds ability to move on the map

    public void addControls(WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        if (!controlsAreOnTheMap) {
            switch (controls) {
                case "keyboard":
                    //System.out.println("Adding keyboard controls");
                    addKeyBoardControls(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                    System.out.println("adding keyboard controls");
                    break;
                case "touchscreen":
                    //System.out.println("Adding touchscreen controls");
                    addTouchscreenControls(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                    System.out.println("adding touchscreen controls");
                    break;
                default:
                    System.err.println("Error with addControls switch(controls)");
                    System.exit(45332746);

            }
            controlsAreOnTheMap = true;
        } else {
            //System.out.println("Controls are already on the map, no need to add the again");
            //all event handlers exist for keyboard and mouse still, HOWEVER: edge case for touchscreen controls
            //they get removed when the map is unloaded
            //but they can just be put back, no need to make them again
            if (!worldPane.getChildren().contains(downButton) && (controls.equals("touchscreen"))){
                worldPane.getChildren().addAll(downButton, rightButton, upButton, leftButton);
                controlsAreOnTheMap = true;
            }

        }

    }

    public void addKeyBoardControls(WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        //System.out.println("addKeyboardControls not implemented yet (work in progress)");
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W:
                    genericControlsMapMoveUp(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                    break;
                case A:
                    genericControlsMapMoveLeft(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                    break;
                case S:
                    genericControlsMapMoveDown(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                    break;
                case D:
                    genericControlsMapMoveRight(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                    break;
                default:
                    System.out.println("that key isn't handled at the moment");
                    break;
            }
        });

    }

    public void removeKeyBoardControls(Scene scene) {
        scene.setOnKeyPressed(null);
        System.out.println("removing keyboard controls");
        //still need to change boolean for controlsAreOnTheMap to false
    }

    public void removeTouchscreenControls(){
        downButton.setOnAction(null);
        rightButton.setOnAction(null);
        upButton.setOnAction(null);
        leftButton.setOnAction(null);
        System.out.println("removing touchscreen controls");
    }


    public void addTouchscreenControls(WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {



        int intendedHeight = -1; //means not initialized
        int intendedWidth = -1;
        switch (resolution) {
            case "1280x720":
                intendedWidth = 1280;
                intendedHeight = 720;
                break;
            case "1280x800":
                intendedWidth = 1280;
                intendedHeight = 800;
                break;
            case "1920x1080":
                intendedWidth = 1920;
                intendedHeight = 1080;
                break;
            default:
                System.err.println("switch resolution error in addTouchscreenControls");
                break;
        }

        if (intendedHeight == -1) {
            System.err.println("height and widht initialization error in addtouchscreenControls");
            System.exit(288231);
        }

        downButton.setLayoutY(intendedHeight * 0.8);
        downButton.setLayoutX(intendedWidth * 0.85);
        downButton.setFont(buttonFont);

        rightButton.setLayoutY(intendedHeight * 0.8);
        rightButton.setLayoutX(intendedWidth * 0.9);
        rightButton.setFont(buttonFont);

        upButton.setLayoutY(intendedHeight * 0.7);
        upButton.setLayoutX(intendedWidth * 0.85);
        upButton.setFont(buttonFont);

        leftButton.setLayoutY(intendedHeight * 0.8);
        leftButton.setLayoutX(intendedWidth * 0.8);
        leftButton.setFont(buttonFont);


        worldPane.getChildren().addAll(downButton, rightButton, upButton, leftButton);



        downButton.setOnAction(e -> {
            genericControlsMapMoveDown(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
            //without toFront(), the player's character could be on top of the touchscreen control buttons!
            downButton.toFront();
            rightButton.toFront();
            upButton.toFront();
            leftButton.toFront();
        });

        //for all of the Xbutton event handlers, I replaced MouseEvent.MOUSE_PRESSED with MouseEvent.ANY


        /*
        downButton.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                genericControlsMapMoveDown(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                //without toFront(), the player's character could be on top of the touchscreen control buttons!
                downButton.toFront();
                rightButton.toFront();
                upButton.toFront();
                leftButton.toFront();

                event.consume();
            }
        });*/



        rightButton.setOnAction(e -> {
            genericControlsMapMoveRight(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
            downButton.toFront();
            rightButton.toFront();
            upButton.toFront();
            leftButton.toFront();
        });

        /*
        rightButton.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                genericControlsMapMoveRight(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                downButton.toFront();
                rightButton.toFront();
                upButton.toFront();
                leftButton.toFront();

                event.consume();
            }
        });*/


        upButton.setOnAction(e -> {
            genericControlsMapMoveUp(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
            downButton.toFront();
            rightButton.toFront();
            upButton.toFront();
            leftButton.toFront();
        });

        /*
        upButton.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                genericControlsMapMoveUp(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                downButton.toFront();
                rightButton.toFront();
                upButton.toFront();
                leftButton.toFront();

                event.consume();
            }
        });*/


        leftButton.setOnAction(e -> {
            genericControlsMapMoveLeft(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
            downButton.toFront();
            rightButton.toFront();
            upButton.toFront();
            leftButton.toFront();
        });

        /*
        leftButton.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                genericControlsMapMoveLeft(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                downButton.toFront();
                rightButton.toFront();
                upButton.toFront();
                leftButton.toFront();

                event.consume();
            }
        });*/


    }

    //GENERIC CONTROLS
    //this should be run by both the keyboard controls and touchscreen controls
    //what triggers it depends on the type of controls, but the same logic applies to both of them

    public void genericControlsMapMoveDown(WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        if (player.getIsBusy() == true || (this.enoughTimeHasPassed() == false)) {
            //System.out.println("can't move due to being busy or not waiting long enough");
        } else {
            //System.out.println("proceeding with movement because the player isn't busy");
            //System.out.println("player x,y before moving down: " + player.getX() + ", " + player.getY());
            //down increases Y
            //counterintuitive but 0,0 is in the upper lefthand corner
            //and bottom right is the highest x,y position
            int newY = player.getY() +1;

            //to-do: handle events (events aren't implemented yet so I can't do that here)

            //check if there even is a bottom tile before you can move there
            //-1 because arrays start at 0, not 1, so yDimension is +1 more than the last array element's index
            int tileSize = worldMap.getTileSize().equals("40x40")? 40 : 60;

            if (player.getY() >= (worldMap.yDimension - 1)) {
                //already at the edge of the map, can't go further
                //moving will be handled with mapmove events, not implemented yet
                //System.out.println("can't move down, at the edge of the map");
            } else {
                //the below tile can possibly be moved to

                //check if below tile has collision or not
                if (worldMap.tileArray[player.getX()][newY].collision == false) {
                    //here, the player can move because it's not something with collision
                    player.setY(newY);
                    player.setAllImageViewLocationX(player.getX() * tileSize);
                    player.setAllImageViewLocationY(newY * tileSize);
                    //player is now on the new tile, now time to see if there's an event there
                    String tileEventTrigger = worldMap.tileArray[player.getX()][player.getY()].getEvent().getTrigger();
                    //System.out.println("got here before tileEventTrigger");
                    //System.out.println("tileEventTrigger for tile " + player.getX() + ", " + player.getY() + ": " + tileEventTrigger);
                    if (tileEventTrigger.equals("walk")) {
                        //System.out.println("got here in tileEventTrigger");
                        String tileEventType = worldMap.tileArray[player.getX()][player.getY()].getEvent().getEventType();
                        switch (tileEventType) {
                            case "MapMove":
                                String mapToLoadFromMoveEvent = worldMap.tileArray[player.getX()][player.getY()].getEvent().getMapToLoad();
                                switch (mapToLoadFromMoveEvent) {
                                    case "map_0_0":
                                        System.out.println("need to unload current map and then load map_0_0 (loading/unloading not implemented yet)");
                                        break;
                                    case "map_0_1":
                                        //System.out.println("need to unload current map and then load map_0_1 (loading/unloading not implemented yet)");
                                        //to figure out what needs to be unloaded, you first need to know what currently IS loaded
                                        switch (player.getCurrentMapName()) {
                                            case "map_0_0":
                                                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                                                loadMap_0_0(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                                                return;
                                            default:
                                                System.out.println("trying to load map_0_1 from a map other than map_0_0");
                                                break;
                                            //when I make more maps, put them here, but only that connect to map_0_1
                                        }
                                        return; //return gets out of entire method
                                    case "map_1_0":
                                        System.out.println("need to unload current map and then load map_1_0 (loading/unloading not implemented yet)");
                                        break;
                                    case "none":
                                        System.err.println("error with MapMove Event, in switch mapToLoadFromMoveEvent");
                                        break;
                                    default:
                                        System.out.println("I don't know, or it isn't implemented yet");
                                        break;
                                }
                                break;
                            case "Shop":
                                System.out.println("move-triggered Shop event not implemented yet");
                                break;
                            case "Dialogue":
                                System.out.println("move-triggered Dialogue event not implemented yet");
                                break;
                            case "Quest":
                                System.out.println("move-triggered Quest event not implemented yet");
                                break;
                            case "Combat":
                                System.out.println("move-triggered Combat event not implemented yet");
                                break;
                            case "Treasure":
                                System.out.println("move-triggered Treasure event not implemented yet");
                                break;
                            case "none":
                                System.out.println("this is a none type event, therefore nothing happens.");
                                break;
                            default:
                                System.err.println("move-triggered event error");
                                System.exit(67686);
                                break;
                            //MapMove, Shop, Dialogue, Quest, Combat, Treasure, or none
                        }
                    } //else there is no move-triggered event on this tile

                } else {
                    //System.out.println("can't move down due to collision");
                    //but still need to update position and graphics


                }






            }


            String oldPosition = player.getPosition();
            //if new position is different, need to change the imageview
            player.setPosition("down");
            //System.out.println("facing down");

            if (!(oldPosition.equals(player.getPosition()))) {
                switch (tileSize) {
                    case 40:
                        //remove the old imageview
                        switch (oldPosition) {
                            case "down":
                                //nothing for this one because it's the down event handler
                                //worldPane.getChildren().remove(player.player_down_40x40ImageView);
                                //worldPane.getChildren().add(player.player_down_40x40ImageView);
                                break;
                            case "right":
                                worldPane.getChildren().remove(player.player_right_40x40ImageView);
                                worldPane.getChildren().add(player.player_down_40x40ImageView);
                                break;
                            case "up":
                                worldPane.getChildren().remove(player.player_up_40x40ImageView);
                                worldPane.getChildren().add(player.player_down_40x40ImageView);
                                break;
                            case "left":
                                worldPane.getChildren().remove(player.player_left_40x40ImageView);
                                worldPane.getChildren().add(player.player_down_40x40ImageView);
                                break;
                            default:
                                break;
                        }
                        break;
                    case 60:
                        switch (oldPosition) {
                            case "down":
                                //nothing for this one because it's the down event handler
                                //worldPane.getChildren().remove(player.player_down_60x60ImageView);
                                //worldPane.getChildren().add(player.player_down_60x60ImageView);
                                break;
                            case "right":
                                worldPane.getChildren().remove(player.player_right_60x60ImageView);
                                worldPane.getChildren().add(player.player_down_60x60ImageView);
                                break;
                            case "up":
                                worldPane.getChildren().remove(player.player_up_60x60ImageView);
                                worldPane.getChildren().add(player.player_down_60x60ImageView);
                                break;
                            case "left":
                                worldPane.getChildren().remove(player.player_left_60x60ImageView);
                                worldPane.getChildren().add(player.player_down_60x60ImageView);
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        System.err.println("move down switch error");
                        break;
                }
            }  //


            System.gc();
            //System.out.println("player x,y after moving down: " + player.getX() + ", " + player.getY());
            //boombox.playSound(3);
        }
    }

    public void genericControlsMapMoveRight(WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        if (player.getIsBusy() == true || (this.enoughTimeHasPassed() == false)) {
            //System.out.println("can't move due to being busy or not waiting long enough");
        } else {
            //System.out.println("proceeding with movement because the player isn't busy");
            //System.out.println("player x,y before moving right: " + player.getX() + ", " + player.getY());
            //right increases X
            int newX = player.getX() +1;

            //to-do: handle events (events aren't implemented yet so I can't do that here)

            //check if there even is a right tile before you can move there
            //-1 because arrays start at 0, not 1, so xDimension is +1 more than the last array element's index
            int tileSize = worldMap.getTileSize().equals("40x40")? 40 : 60;
            if (player.getX() >= (worldMap.xDimension - 1)) {
                //already at the edge of the map, can't go further
                //moving will be handled with mapmove events, not implemented yet
                //System.out.println("can't move right, at the edge of the map");
            } else {
                //the below tile can possibly be moved to

                //check if below tile has collision or not
                if (worldMap.tileArray[newX][player.getY()].collision == false) {
                    //here, the player can move because it's not something with collision
                    player.setX(newX);

                    player.setAllImageViewLocationX(newX * tileSize);
                    player.setAllImageViewLocationY(player.getY() * tileSize);
                    //player is now on the new tile, now time to see if there's an event there
                    String tileEventTrigger = worldMap.tileArray[player.getX()][player.getY()].getEvent().getTrigger();
                    //System.out.println("got here before tileEventTrigger");
                    //System.out.println("tileEventTrigger for tile " + player.getX() + ", " + player.getY() + ": " + tileEventTrigger);
                    if (tileEventTrigger.equals("walk")) {
                        //System.out.println("got here in tileEventTrigger");
                        String tileEventType = worldMap.tileArray[player.getX()][player.getY()].getEvent().getEventType();
                        switch (tileEventType) {
                            case "MapMove":
                                String mapToLoadFromMoveEvent = worldMap.tileArray[player.getX()][player.getY()].getEvent().getMapToLoad();
                                switch (mapToLoadFromMoveEvent) {
                                    case "map_0_0":
                                        System.out.println("need to unload current map and then load map_0_0 (loading/unloading not implemented yet)");
                                        break;
                                    case "map_0_1":
                                        //System.out.println("need to unload current map and then load map_0_1 (loading/unloading not implemented yet)");
                                        //to figure out what needs to be unloaded, you first need to know what currently IS loaded
                                        switch (player.getCurrentMapName()) {
                                            case "map_0_0":
                                                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                                                loadMap_0_1(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                                                return;
                                            default:
                                                System.out.println("trying to load map_0_1 from a map other than map_0_0");
                                                break;
                                            //when I make more maps, put them here, but only that connect to map_0_1
                                        }
                                        return; //return gets out of entire method
                                    case "map_1_0":
                                        System.out.println("need to unload current map and then load map_1_0 (loading/unloading not implemented yet)");
                                        break;
                                    case "none":
                                        System.err.println("error with MapMove Event, in switch mapToLoadFromMoveEvent");
                                        break;
                                    default:
                                        System.out.println("I don't know, or it isn't implemented yet");
                                        break;
                                }
                                break;
                            case "Shop":
                                System.out.println("move-triggered Shop event not implemented yet");
                                break;
                            case "Dialogue":
                                System.out.println("move-triggered Dialogue event not implemented yet");
                                break;
                            case "Quest":
                                System.out.println("move-triggered Quest event not implemented yet");
                                break;
                            case "Combat":
                                System.out.println("move-triggered Combat event not implemented yet");
                                break;
                            case "Treasure":
                                System.out.println("move-triggered Treasure event not implemented yet");
                                break;
                            case "none":
                                System.out.println("this is a none type event, therefore nothing happens.");
                                break;
                            default:
                                System.err.println("move-triggered event error");
                                System.exit(67686);
                                break;
                            //MapMove, Shop, Dialogue, Quest, Combat, Treasure, or none
                        }
                    } //else there is no move-triggered event on this tile

                } else {
                    //System.out.println("can't move right due to collision");
                }








            }


            String oldPosition = player.getPosition();
            //if new position is different, need to change the imageview
            player.setPosition("right");
            //System.out.println("facing right");
            //

            if (!(oldPosition.equals(player.getPosition()))) {
                switch (tileSize) {
                    case 40:
                        //remove the old imageview
                        switch (oldPosition) {
                            case "down":
                                worldPane.getChildren().remove(player.player_down_40x40ImageView);
                                worldPane.getChildren().add(player.player_right_40x40ImageView);
                                break;
                            case "right":
                                //nothing for this one because it's the right event handler
                                //worldPane.getChildren().remove(player.player_right_40x40ImageView);
                                //worldPane.getChildren().add(player.player_down_40x40ImageView);
                                break;
                            case "up":
                                worldPane.getChildren().remove(player.player_up_40x40ImageView);
                                worldPane.getChildren().add(player.player_right_40x40ImageView);
                                break;
                            case "left":
                                worldPane.getChildren().remove(player.player_left_40x40ImageView);
                                worldPane.getChildren().add(player.player_right_40x40ImageView);
                                break;
                            default:
                                break;
                        }
                        break;
                    case 60:
                        switch (oldPosition) {
                            case "down":
                                worldPane.getChildren().remove(player.player_down_60x60ImageView);
                                worldPane.getChildren().add(player.player_right_60x60ImageView);
                                break;
                            case "right":
                                //nothing for this one because it's the down event handler
                                //worldPane.getChildren().remove(player.player_right_60x60ImageView);
                                //worldPane.getChildren().add(player.player_down_60x60ImageView);
                                break;
                            case "up":
                                worldPane.getChildren().remove(player.player_up_60x60ImageView);
                                worldPane.getChildren().add(player.player_right_60x60ImageView);
                                break;
                            case "left":
                                worldPane.getChildren().remove(player.player_left_60x60ImageView);
                                worldPane.getChildren().add(player.player_right_60x60ImageView);
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        System.err.println("move right switch error");
                        break;
                }
            }  //


            System.gc();
            //System.out.println("player x,y after moving right: " + player.getX() + ", " + player.getY());
            //boombox.playSound(3);
        }
    }

    public void genericControlsMapMoveUp(WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        if (player.getIsBusy() == true || (this.enoughTimeHasPassed() == false)) {
            //System.out.println("can't move due to being busy or not waiting long enough");
        } else {
            //System.out.println("proceeding with movement because the player isn't busy");
            //System.out.println("player x,y before moving up: " + player.getX() + ", " + player.getY());
            //up decreases Y
            //counterintuitive but 0,0 is in the upper lefthand corner
            //and bottom right is the highest x,y position
            int newY = player.getY() -1;

            //to-do: handle events (events aren't implemented yet so I can't do that here)

            //check if there even is an above tile before you can move there
            //-1 because arrays start at 0, not 1, so yDimension is +1 more than the last array element's index
            int tileSize = worldMap.getTileSize().equals("40x40")? 40 : 60;

            if (player.getY() <= 0) {
                //already at the edge of the map, can't go further
                //moving will be handled with mapmove events, not implemented yet
                //System.out.println("can't move up, at the edge of the map");
            } else {
                //the below tile can possibly be moved to

                //check if above tile has collision or not
                if (worldMap.tileArray[player.getX()][newY].collision == false) {
                    //here, the player can move because it's not something with collision
                    player.setY(newY);
                    player.setAllImageViewLocationX(player.getX() * tileSize);
                    player.setAllImageViewLocationY(newY * tileSize);
                    //player is now on the new tile, now time to see if there's an event there
                    String tileEventTrigger = worldMap.tileArray[player.getX()][player.getY()].getEvent().getTrigger();
                    //System.out.println("got here before tileEventTrigger");
                    //System.out.println("tileEventTrigger for tile " + player.getX() + ", " + player.getY() + ": " + tileEventTrigger);
                    if (tileEventTrigger.equals("walk")) {
                        //System.out.println("got here in tileEventTrigger");
                        String tileEventType = worldMap.tileArray[player.getX()][player.getY()].getEvent().getEventType();
                        switch (tileEventType) {
                            case "MapMove":
                                String mapToLoadFromMoveEvent = worldMap.tileArray[player.getX()][player.getY()].getEvent().getMapToLoad();
                                switch (mapToLoadFromMoveEvent) {
                                    case "map_0_0":
                                        System.out.println("need to unload current map and then load map_0_0 (loading/unloading not implemented yet)");
                                        break;
                                    case "map_0_1":
                                        //System.out.println("need to unload current map and then load map_0_1 (loading/unloading not implemented yet)");
                                        //to figure out what needs to be unloaded, you first need to know what currently IS loaded
                                        switch (player.getCurrentMapName()) {
                                            case "map_0_0":
                                                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                                                loadMap_0_1(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                                                return;
                                            default:
                                                System.out.println("trying to load map_0_1 from a map other than map_0_0");
                                                break;
                                            //when I make more maps, put them here, but only that connect to map_0_1
                                        }
                                        return; //return gets out of entire method
                                    case "map_1_0":
                                        System.out.println("need to unload current map and then load map_1_0 (loading/unloading not implemented yet)");
                                        break;
                                    case "none":
                                        System.err.println("error with MapMove Event, in switch mapToLoadFromMoveEvent");
                                        break;
                                    default:
                                        System.out.println("I don't know, or it isn't implemented yet");
                                        break;
                                }
                                break;
                            case "Shop":
                                System.out.println("move-triggered Shop event not implemented yet");
                                break;
                            case "Dialogue":
                                System.out.println("move-triggered Dialogue event not implemented yet");
                                break;
                            case "Quest":
                                System.out.println("move-triggered Quest event not implemented yet");
                                break;
                            case "Combat":
                                System.out.println("move-triggered Combat event not implemented yet");
                                break;
                            case "Treasure":
                                System.out.println("move-triggered Treasure event not implemented yet");
                                break;
                            case "none":
                                System.out.println("this is a none type event, therefore nothing happens.");
                                break;
                            default:
                                System.err.println("move-triggered event error");
                                System.exit(67686);
                                break;
                            //MapMove, Shop, Dialogue, Quest, Combat, Treasure, or none
                        }
                    } //else there is no move-triggered event on this tile

                } else {
                    //System.out.println("can't move down due to collision");
                    //but still need to update position and graphics
                    //i.e. you are next to an obstacle but not facing it, then you want to move that way, and then your position (facing) changes
                    //this is important for being able to interact with things with the interact key

                }






            }


            String oldPosition = player.getPosition();
            //if new position is different, need to change the imageview
            player.setPosition("up");
            //System.out.println("facing up");

            if (!(oldPosition.equals(player.getPosition()))) {
                switch (tileSize) {
                    case 40:
                        //remove the old imageview
                        switch (oldPosition) {
                            case "down":
                                worldPane.getChildren().remove(player.player_down_40x40ImageView);
                                worldPane.getChildren().add(player.player_up_40x40ImageView);
                                break;
                            case "right":
                                worldPane.getChildren().remove(player.player_right_40x40ImageView);
                                worldPane.getChildren().add(player.player_up_40x40ImageView);
                                break;
                            case "up":
                                //nothing for this one because it's the down event handler
                                //worldPane.getChildren().remove(player.player_up_40x40ImageView);
                                //worldPane.getChildren().add(player.player_up_40x40ImageView);
                                break;
                            case "left":
                                worldPane.getChildren().remove(player.player_left_40x40ImageView);
                                worldPane.getChildren().add(player.player_up_40x40ImageView);
                                break;
                            default:
                                break;
                        }
                        break;
                    case 60:
                        switch (oldPosition) {
                            case "down":
                                worldPane.getChildren().remove(player.player_down_60x60ImageView);
                                worldPane.getChildren().add(player.player_up_60x60ImageView);
                                break;
                            case "right":
                                worldPane.getChildren().remove(player.player_right_60x60ImageView);
                                worldPane.getChildren().add(player.player_up_60x60ImageView);
                                break;
                            case "up":
                                //nothing for this one because it's the down event handler
                                //worldPane.getChildren().remove(player.player_up_60x60ImageView);
                                //worldPane.getChildren().add(player.player_up_60x60ImageView);
                                break;
                            case "left":
                                worldPane.getChildren().remove(player.player_left_60x60ImageView);
                                worldPane.getChildren().add(player.player_up_60x60ImageView);
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        System.err.println("move down switch error");
                        break;
                }
            }  //


            System.gc();
            //System.out.println("player x,y after moving up: " + player.getX() + ", " + player.getY());
            //boombox.playSound(3);
        }
    }

    public void genericControlsMapMoveLeft(WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        if (player.getIsBusy() == true || (this.enoughTimeHasPassed() == false)) {
            //System.out.println("can't move due to being busy or not waiting long enough");
        } else {
            //System.out.println("proceeding with movement because the player isn't busy");
            //System.out.println("player x,y before moving left: " + player.getX() + ", " + player.getY());
            //left decreases X
            int newX = player.getX() -1;

            //to-do: handle events (events aren't implemented yet so I can't do that here)

            //check if there even is a left tile before you can move there
            //-1 because arrays start at 0, not 1, so xDimension is +1 more than the last array element's index
            int tileSize = worldMap.getTileSize().equals("40x40")? 40 : 60;
            if (player.getX() <= 0) {
                //already at the edge of the map, can't go further
                //moving will be handled with mapmove events, not implemented yet
                //System.out.println("can't move right, at the edge of the map");
            } else {
                //the below left can possibly be moved to

                //check if left tile has collision or not
                if (worldMap.tileArray[newX][player.getY()].collision == false) {
                    //here, the player can move because it's not something with collision
                    player.setX(newX);

                    player.setAllImageViewLocationX(newX * tileSize);
                    player.setAllImageViewLocationY(player.getY() * tileSize);
                    //player is now on the new tile, now time to see if there's an event there
                    String tileEventTrigger = worldMap.tileArray[player.getX()][player.getY()].getEvent().getTrigger();
                    //System.out.println("got here before tileEventTrigger");
                    //System.out.println("tileEventTrigger for tile " + player.getX() + ", " + player.getY() + ": " + tileEventTrigger);
                    if (tileEventTrigger.equals("walk")) {
                        //System.out.println("got here in tileEventTrigger");
                        String tileEventType = worldMap.tileArray[player.getX()][player.getY()].getEvent().getEventType();
                        switch (tileEventType) {
                            case "MapMove":
                                String mapToLoadFromMoveEvent = worldMap.tileArray[player.getX()][player.getY()].getEvent().getMapToLoad();
                                switch (mapToLoadFromMoveEvent) {
                                    case "map_0_0":
                                        System.out.println("need to unload current map and then load map_0_0 (loading/unloading not implemented yet)");
                                        break;
                                    case "map_0_1":
                                        //System.out.println("need to unload current map and then load map_0_1 (loading/unloading not implemented yet)");
                                        //to figure out what needs to be unloaded, you first need to know what currently IS loaded
                                        switch (player.getCurrentMapName()) {
                                            case "map_0_0":
                                                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                                                loadMap_0_1(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                                                return;
                                            default:
                                                System.out.println("trying to load map_0_1 from a map other than map_0_0");
                                                break;
                                            //when I make more maps, put them here, but only that connect to map_0_1
                                        }
                                        return; //return gets out of entire method
                                    case "map_1_0":
                                        System.out.println("need to unload current map and then load map_1_0 (loading/unloading not implemented yet)");
                                        break;
                                    case "none":
                                        System.err.println("error with MapMove Event, in switch mapToLoadFromMoveEvent");
                                        break;
                                    default:
                                        System.out.println("I don't know, or it isn't implemented yet");
                                        break;
                                }
                                break;
                            case "Shop":
                                System.out.println("move-triggered Shop event not implemented yet");
                                break;
                            case "Dialogue":
                                System.out.println("move-triggered Dialogue event not implemented yet");
                                break;
                            case "Quest":
                                System.out.println("move-triggered Quest event not implemented yet");
                                break;
                            case "Combat":
                                System.out.println("move-triggered Combat event not implemented yet");
                                break;
                            case "Treasure":
                                System.out.println("move-triggered Treasure event not implemented yet");
                                break;
                            case "none":
                                System.out.println("this is a none type event, therefore nothing happens.");
                                break;
                            default:
                                System.err.println("move-triggered event error");
                                System.exit(67686);
                                break;
                            //MapMove, Shop, Dialogue, Quest, Combat, Treasure, or none
                        }
                    } //else there is no move-triggered event on this tile

                } else {
                    System.out.println("can't move left due to collision");
                }







            }



            String oldPosition = player.getPosition();
            //if new position is different, need to change the imageview
            player.setPosition("left");
            //System.out.println("facing left");
            //

            if (!(oldPosition.equals(player.getPosition()))) {
                switch (tileSize) {
                    case 40:
                        //remove the old imageview
                        switch (oldPosition) {
                            case "down":
                                worldPane.getChildren().remove(player.player_down_40x40ImageView);
                                worldPane.getChildren().add(player.player_left_40x40ImageView);
                                break;
                            case "right":
                                worldPane.getChildren().remove(player.player_right_40x40ImageView);
                                worldPane.getChildren().add(player.player_left_40x40ImageView);
                                break;
                            case "up":

                                worldPane.getChildren().remove(player.player_up_40x40ImageView);
                                worldPane.getChildren().add(player.player_left_40x40ImageView);
                                break;
                            case "left":
                                //nothing for this one because it's the right event handler
                                //worldPane.getChildren().remove(player.player_left_40x40ImageView);
                                //worldPane.getChildren().add(player.player_left_40x40ImageView);
                                break;
                            default:
                                break;
                        }
                        break;
                    case 60:
                        switch (oldPosition) {
                            case "down":
                                worldPane.getChildren().remove(player.player_down_60x60ImageView);
                                worldPane.getChildren().add(player.player_left_60x60ImageView);
                                break;
                            case "right":
                                worldPane.getChildren().remove(player.player_right_60x60ImageView);
                                worldPane.getChildren().add(player.player_left_60x60ImageView);
                                break;
                            case "up":
                                worldPane.getChildren().remove(player.player_up_60x60ImageView);
                                worldPane.getChildren().add(player.player_left_60x60ImageView);
                                break;
                            case "left":
                                //nothing for this one because it's the down event handler
                                //worldPane.getChildren().remove(player.player_left_60x60ImageView);
                                //worldPane.getChildren().add(player.player_left_60x60ImageView);
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        System.err.println("move left switch error");
                        break;
                }
            }  //



            System.gc();
            //System.out.println("player x,y after moving left: " + player.getX() + ", " + player.getY());
            //boombox.playSound(3);
        }
    }

    public void genericControlsOpenInventory() {
        System.out.println("genericControlsMapMoveOpenInventory not yet implemented");
    }

    public void genericControlsCloseInventory() {
        System.out.println("genericControls not yet implemented");
    }

    public void genericControlsOpenMenu() {
        System.out.println("genericControlsOpenMenu not yet implemented");
    }

    public void genericControlsCloseMenu() {
        System.out.println("genericControlsCloseMenu not yet implemented");
    }

    public void genericControlsInteract() {
        System.out.println("genericControlsInteract not yet implemented");
    }




    //put this at the end of every loadMap_X_Y method
    public void putPlayerAndControlsOnMap(WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        putPlayerOnMap(worldMap, worldPane, player);
        addControls(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
    }

    public void takeControlsOffMap() {
        System.out.println("takeControlsOffMap not yet implemented");
    }

    //UNLOAD MAP METHODS!!!!!!!!!!!!!!!!!!!!!!!

    //unloading map!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public void unloadMap(WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        //System.out.println("where i left off!!!!!");
        worldPane.getChildren().clear(); //gets everything OUT of the worldPane, but it hasn't been destroyed yet
        mainMenu.getChildren().remove(worldPane);
        worldMap.destroyWorldMap(mainMenu, worldPane); //destroys Events, Tiles, and WorldMap, but there's still a couple other things that were added here in MapLoader
        //!!!!!!!!!!!!!!destroyWorldMap is too slow! I need to make separate destroyMap events for each level, no loops, only destroying what I know is in there!!!
        //update: I think I made it faster, so it's better after all
        worldPane = null;
        worldPane = new Pane();




        /*
        for (int xMap = 0; xMap < worldMap.getxDimension(); xMap++) {
            for (int yMap = 0; yMap < worldMap.getyDimension(); yMap++) {
                worldMap.tileArray[xMap][yMap].destroyTile();
                worldMap.tileArray[xMap][yMap] = null;
            }
        }
        int downArrowToNextMapY;
        if (!resolution.equals("1280x800")) {
            downArrowToNextMapY = 16;
        } else {
            System.out.println("800p has a different number of tiles, so moving to another map is different (down a couple rows)");
            //16:9 is 32x18 tiles, 16:10 is 32x20 tiles
            downArrowToNextMapY = 18;
        }*/


        removeKeyBoardControls(scene);
        removeTouchscreenControls();
        this.controlsAreOnTheMap = false;

        System.gc();

    }




}
