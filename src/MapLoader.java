import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;



//this class is used to load maps
//no attributes, no getters or setters
//just map-loading methods that take a lot of args
//also can unload the current map from the screen and makes sure there are no memory leaks

public class MapLoader {

    //attributes===============================================

    Label currentMapLabel;

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
        currentMapLabel = new Label();
        currentMapLabel.setLayoutX(50);
        currentMapLabel.setLayoutY(50);

        currentMapLabel.setStyle("-fx-font: 20 arial; -fx-font-weight: bold; -fx-background-color: white;");
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

    //easy way to add a tile to the map with a single method call
    //overloaded to be more versatile

    //set bottom level with no collision, i.e. grass
    public void addTileToMap(String bottomLevelImageName, String tileSizeFileNamePart, WorldMap worldMap, int x, int y) {
        String bottomLevelImage = "file:assets/tiles/" + bottomLevelImageName + "_" + tileSizeFileNamePart + ".png";
        worldMap.tileArray[x][y] = new Tile(bottomLevelImage, x, y);
    }

    //set bottom level with collision
    public void addTileToMap(String bottomLevelImageName, String tileSizeFileNamePart, WorldMap worldMap, int x, int y, boolean collision) {
        String bottomLevelImage = "file:assets/tiles/" + bottomLevelImageName + "_" + tileSizeFileNamePart + ".png";
        worldMap.tileArray[x][y] = new Tile(bottomLevelImage, x, y, collision);
    }

    //set bottom level and mid level with collision
    public void addTileToMap(String bottomLevelImageName, String midLevelImageName, String tileSizeFileNamePart, WorldMap worldMap, int x, int y, boolean collision) {
        String bottomLevelImage = "file:assets/tiles/" + bottomLevelImageName + "_" + tileSizeFileNamePart + ".png";
        String midLevelImage = "file:assets/tiles/" + midLevelImageName + "_" + tileSizeFileNamePart + ".png";
        worldMap.tileArray[x][y] = new Tile(bottomLevelImage, midLevelImage, x, y, collision);
    }

    //set bottom level, mid level, and top level with collision
    public void addTileToMap(String bottomLevelImageName, String midLevelImageName, String topLevelImageName, String tileSizeFileNamePart, WorldMap worldMap, int x, int y, boolean collision) {
        String bottomLevelImage = "file:assets/tiles/" + bottomLevelImageName + "_" + tileSizeFileNamePart + ".png";
        String midLevelImage = "file:assets/tiles/" + midLevelImageName + "_" + tileSizeFileNamePart + ".png";
        String topLevelImage = "file:assets/tiles/" + topLevelImageName + "_" + tileSizeFileNamePart + ".png";
        worldMap.tileArray[x][y].setBottomLevelWithCollision(bottomLevelImageName, collision);
        worldMap.tileArray[x][y].setMidLevel(midLevelImage);
        worldMap.tileArray[x][y].setTopLevel(topLevelImage);
        worldMap.tileArray[x][y] = new Tile(bottomLevelImage, midLevelImage, topLevelImage, x, y, collision);
    }

    //set bottom level, mid level, top level, event, and collision
    public void addTileToMap(String bottomLevelImageName, String midLevelImageName, String topLevelImageName, Event tileEvent, String tileSizeFileNamePart, WorldMap worldMap, int x, int y, boolean collision) {
        String bottomLevelImage = "file:assets/tiles/" + bottomLevelImageName + "_" + tileSizeFileNamePart + ".png";
        String midLevelImage = "file:assets/tiles/" + midLevelImageName + "_" + tileSizeFileNamePart + ".png";
        String topLevelImage = "file:assets/tiles/" + topLevelImageName + "_" + tileSizeFileNamePart + ".png";
        worldMap.tileArray[x][y].setBottomLevelWithCollision(bottomLevelImageName, collision);
        worldMap.tileArray[x][y].setMidLevel(midLevelImage);
        worldMap.tileArray[x][y].setTopLevel(topLevelImage);
        worldMap.tileArray[x][y].setEvent(tileEvent);

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

    public void addMapLabel(Player player, Pane worldPane) {
        currentMapLabel.setText(null);
        currentMapLabel.setText(player.getCurrentMapName());
        worldPane.getChildren().add(currentMapLabel);
        System.out.println("addMapLabel: " + currentMapLabel.getText());
    }

    public void pushMapLabelToTop() {
        currentMapLabel.toFront();
    }


    //map-loading methods===============================================

    //maps are now hard-coded for performance reasons (XML parsing was too slow even though separating data from code sounds like a good idea in theory)
    //IMPORTANT INFO ABOUT MAPS AND WINDOWED MODE:
    //IMPORTANT TILES CANNOT BE ON THE BOTTOM OR RIGHTHAND EDGES BECAUSE THEY CAN BE CUT OFF
    //IF YOU NEED TO PUT MAPMOVE TILES ON THE EDGES, MAKE SURE THERE ARE TWO, IN CASE ONE GETS CUT OFF
    public void loadMap_0_0(boolean firstLoadOfCurrentPlay, int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {

        //firstLoadOfCurrentPlay means if this is the first map being loaded while the game window has been open
        //which means it needs to load the player from a file rather than being able to use getters

        System.out.println("player x,y for loadMap_0_0: " + player.getX() + ", " + player.getY());

        player.setCurrentMapName("map_0_0");

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
                //String tileGrassFileName = "file:assets/tiles/grass_" + tileSizeFileNamePart + ".png";
                //worldMap.tileArray[xMap][yMap] = new Tile(tileGrassFileName);
                addTileToMap("grass", tileSizeFileNamePart, worldMap, xMap, yMap);
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

        //==============================================================================

        //middle stuff

        //arrows and MapMove events

        worldMap.tileArray[15][downArrowToNextMapY] = new Tile(downArrowFileName, 15, downArrowToNextMapY);
        worldMap.tileArray[15][downArrowToNextMapY].setEvent(new MapMove("walk", "map_0_1", 15, 2));
        //System.out.println(worldMap.tileArray[15][downArrowToNextMapY].getEvent().getEventType());
        worldMap.tileArray[15][downArrowToNextMapY + 1] = new Tile(downArrowFileName, 15, (downArrowToNextMapY+1));
        worldMap.tileArray[15][downArrowToNextMapY + 1].setEvent(new MapMove("walk", "map_0_1", 15, 2));
        String rightArrowFileName = "file:assets/tiles/right_arrow_" + tileSizeFileNamePart + ".png";
        worldMap.tileArray[30][9] = new Tile(rightArrowFileName, 30, 9);
        worldMap.tileArray[30][9].setEvent(new MapMove("walk", "map_1_0", 2, 9));
        worldMap.tileArray[31][9] = new Tile(rightArrowFileName, 31, 9);
        worldMap.tileArray[31][9].setEvent(new MapMove("walk", "map_1_0", 2, 9));

        //content of the map
        String rockFileName = "file:assets/tiles/rock_" + tileSizeFileNamePart + ".png";
        worldMap.tileArray[4][4].setMidLevelWithCollision(rockFileName, true);
        String bushFileName = "file:assets/tiles/bush_" + tileSizeFileNamePart + ".png";
        worldMap.tileArray[20][8].setMidLevelWithCollision(bushFileName, true);
        String exclamationPointFileName = "file:assets/tiles/exclamation_point_" + tileSizeFileNamePart + ".png";
        worldMap.tileArray[20][8].setTopLevel(exclamationPointFileName);


        //==============================================================================

        //tile data is used to figure out which images to add to the map
        worldMap.setAllBottomLevelImage(worldPane);
        worldMap.setAllMidLevelImage(worldPane);
        worldMap.setAllTopLevelImage(worldPane);
        mainMenu.getChildren().addAll(worldPane);

        if (firstLoadOfCurrentPlay) {
            player.loadPlayerFromFile();
        } else {
            player.setX(newX);
            player.setY(newY);
            System.out.println("player newX and newY for map_0_0: " + player.getX() + ", " + player.getY());
        }

        //now that the map is loaded, time to add the player and controls
        putPlayerAndControlsOnMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
        addMapLabel(player, worldPane);
        System.gc();


    }

    public void loadMap_0_1(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        //standard beginning map code and putting grass on the bottom level

        System.out.println("player x,y:" + player.getX() + ", " + player.getY());
        System.out.println("in progress map_0_1");

        player.setCurrentMapName("map_0_1");
        System.out.println("mapName for loadMap_0_1 here: " + player.getCurrentMapName());
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
                worldMap.tileArray[xMap][yMap] = new Tile(tileGrassFileName, xMap, yMap);
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



        //middle stuff (specific to map_0_1====================================

        //arrows + MapMove events to different maps
        worldMap.tileArray[15][downArrowToNextMapY] = new Tile(downArrowFileName, 15, downArrowToNextMapY);
        worldMap.tileArray[15][downArrowToNextMapY].setEvent(new MapMove("walk", "map_0_2", 15, 2));
        worldMap.tileArray[15][downArrowToNextMapY + 1] = new Tile(downArrowFileName, 15, (downArrowToNextMapY + 1));
        worldMap.tileArray[15][downArrowToNextMapY + 1].setEvent(new MapMove("walk", "map_0_2", 15, 2));
        String rightArrowFileName = "file:assets/tiles/right_arrow_" + tileSizeFileNamePart + ".png";
        worldMap.tileArray[30][9] = new Tile(rightArrowFileName, 30, 9);
        worldMap.tileArray[30][9].setEvent(new MapMove("walk", "map_1_1", 2, 9));
        worldMap.tileArray[31][9] = new Tile(rightArrowFileName, 31, 9);
        worldMap.tileArray[31][9].setEvent(new MapMove("walk", "map_1_1", 2, 9));
        String upArrowFileName = "file:assets/tiles/up_arrow_" + tileSizeFileNamePart + ".png";
        worldMap.tileArray[15][0] = new Tile(upArrowFileName, 15, 0);
        worldMap.tileArray[15][0].setEvent(new MapMove("walk", "map_0_0", 15, 15));
        worldMap.tileArray[15][1] = new Tile(upArrowFileName, 15, 1);
        worldMap.tileArray[15][1].setEvent(new MapMove("walk", "map_0_0", 15, 15));


        //actual content of the map
        String treeFileName = "file:assets/tiles/tree_" + tileSizeFileNamePart + ".png";
        worldMap.tileArray[10][10].setMidLevelWithCollision(treeFileName, true);

        //end of middle stuff==================================================



        //finishing up stuff
        //tile data is used to figure out which images to add to the map
        worldMap.setAllBottomLevelImage(worldPane);
        worldMap.setAllMidLevelImage(worldPane);
        worldMap.setAllTopLevelImage(worldPane);
        mainMenu.getChildren().addAll(worldPane);



        if (firstLoadOfCurrentPlay) {
            player.loadPlayerFromFile();
        } else {
            player.setX(newX);
            player.setY(newY);
            System.out.println("player newX and newY for map_0_1: " + player.getX() + ", " + player.getY());
        }

        //now that the map is loaded, time to add the player and controls
        putPlayerAndControlsOnMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
        System.out.println("right here 123 player currentMapName: " + player.getCurrentMapName());
        addMapLabel(player, worldPane);
        System.gc();

    }

    public void loadMap_0_2(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_0_2");
    }

    public void loadMap_0_3(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_0_3");
    }

    public void loadMap_0_4(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_0_4");
    }

    public void loadMap_0_5(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_0_5");
    }

    //==================================

    public void loadMap_1_0(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_1_0");
    }

    public void loadMap_1_1(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_1_1");
    }

    public void loadMap_1_2(boolean firstLoadOfCurrentPlay, int newX, int newY,  WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_1_2");
    }

    public void loadMap_1_3(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_1_3");
    }

    public void loadMap_1_4(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_1_4");
    }

    public void loadMap_1_5(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_1_5");
    }

    //==================================

    public void loadMap_2_0(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_2_0");
    }

    public void loadMap_2_1(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_2_1");
    }

    public void loadMap_2_2(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_2_2");
    }

    public void loadMap_2_3(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_2_3");
    }

    public void loadMap_2_4(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_2_4");
    }

    public void loadMap_2_5(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_2_5");
    }

    //==================================

    public void loadMap_3_0(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_3_0");
    }

    public void loadMap_3_1(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_3_1");
    }

    public void loadMap_3_2(boolean firstLoadOfCurrentPlay, int newX, int newY,  WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_3_2");
    }

    public void loadMap_3_3(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_3_3");
    }

    public void loadMap_3_4(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_3_4");
    }

    public void loadMap_3_5(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_3_5");
    }

    //==================================

    public void loadMap_4_0(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_4_0");
    }

    public void loadMap_4_1(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_4_1");
    }

    public void loadMap_4_2(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_4_2");
    }

    public void loadMap_4_3(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_4_3");
    }

    public void loadMap_4_4(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_4_4");
    }

    public void loadMap_4_5(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_4_5");
    }

    //==================================

    public void loadMap_5_0(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_5_0");
    }

    public void loadMap_5_1(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_5_1");
    }

    public void loadMap_5_2(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_5_2");
    }

    public void loadMap_5_3(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_5_3");
    }

    public void loadMap_5_4(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_5_4");
    }

    public void loadMap_5_5(boolean firstLoadOfCurrentPlay,  int newX, int newY, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        System.out.println("not implemented yet map_5_5");
    }

    public void loadCorrectMap(boolean firstLoadOfCurrentPlay, int newX, int newY, String mapName, WorldMap worldMap, Pane worldPane, Pane mainMenu, String resolution, Player player, String controls, Scene scene, AudioPlayer boombox) {
        switch (mapName) {
            //====================
            case "map_0_0":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_0_0(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_0_1":
                System.out.println("player x,y coordinates before running unloadMap: " + player.getX() + ", " + player.getY());
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                System.out.println("player x,y coordinates after running unloadMap: " + player.getX() + ", " + player.getY());
                loadMap_0_1(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                System.out.println("player x,y coordinates after running loadMap_0_1: " + player.getX() + ", " + player.getY());
                break;
            case "map_0_2":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_0_2(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_0_3":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_0_3(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_0_4":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_0_4(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_0_5":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_0_5(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;

            //====================
            case "map_1_0":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_1_0(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_1_1":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_1_1(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_1_2":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_1_2(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_1_3":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_1_3(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_1_4":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_1_4(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_1_5":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_1_5(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            //====================
            case "map_2_0":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_2_0(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_2_1":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_2_1(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_2_2":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_2_2(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_2_3":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_2_3(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_2_4":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_2_4(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_2_5":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_2_5(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            //====================
            case "map_3_0":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_3_0(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_3_1":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_3_1(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_3_2":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_3_2(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_3_3":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_3_3(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_3_4":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_3_4(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_3_5":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_3_5(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            //====================
            case "map_4_0":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_4_0(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_4_1":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_4_1(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_4_2":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_4_2(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_4_3":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_4_3(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_4_4":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_4_4(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_4_5":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_4_5(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;

            //====================
            case "map_5_0":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_5_0(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_5_1":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_5_1(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_5_2":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_5_2(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_5_3":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_5_3(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_5_4":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_5_4(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;
            case "map_5_5":
                unloadMap(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                loadMap_5_5(firstLoadOfCurrentPlay, newX, newY, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                break;

        }
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
                    //System.out.println("adding keyboard controls");
                    break;
                case "touchscreen":
                    //System.out.println("Adding touchscreen controls");
                    addTouchscreenControls(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                    //System.out.println("adding touchscreen controls");
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






        rightButton.setOnAction(e -> {
            genericControlsMapMoveRight(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
            downButton.toFront();
            rightButton.toFront();
            upButton.toFront();
            leftButton.toFront();
        });




        upButton.setOnAction(e -> {
            genericControlsMapMoveUp(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
            downButton.toFront();
            rightButton.toFront();
            upButton.toFront();
            leftButton.toFront();
        });




        leftButton.setOnAction(e -> {
            genericControlsMapMoveLeft(worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
            downButton.toFront();
            rightButton.toFront();
            upButton.toFront();
            leftButton.toFront();
        });



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
                                int newXfromMapMoveEvent = worldMap.tileArray[player.getX()][player.getY()].getEvent().getNewXPositionForPlayer();
                                int newYfromMapMoveEvent = worldMap.tileArray[player.getX()][player.getY()].getEvent().getNewYPositionForPlayer();
                                loadCorrectMap(false, newXfromMapMoveEvent, newYfromMapMoveEvent, mapToLoadFromMoveEvent, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                                return;
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
            System.out.println("[" + player.getCurrentMapName() + "] player x,y after moving down: " + player.getX() + ", " + player.getY());
            pushMapLabelToTop();
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
                                int newXfromMapMoveEvent = worldMap.tileArray[player.getX()][player.getY()].getEvent().getNewXPositionForPlayer();
                                int newYfromMapMoveEvent = worldMap.tileArray[player.getX()][player.getY()].getEvent().getNewYPositionForPlayer();
                                loadCorrectMap(false, newXfromMapMoveEvent, newYfromMapMoveEvent, mapToLoadFromMoveEvent, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                                return;
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
            System.out.println("[" + player.getCurrentMapName() + "] player x,y after moving right: " + player.getX() + ", " + player.getY());
            pushMapLabelToTop();
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
                                int newXfromMapMoveEvent = worldMap.tileArray[player.getX()][player.getY()].getEvent().getNewXPositionForPlayer();
                                int newYfromMapMoveEvent = worldMap.tileArray[player.getX()][player.getY()].getEvent().getNewYPositionForPlayer();
                                loadCorrectMap(false, newXfromMapMoveEvent, newYfromMapMoveEvent, mapToLoadFromMoveEvent, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                                return;
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
            System.out.println("[" + player.getCurrentMapName() + "] player x,y after moving up: " + player.getX() + ", " + player.getY());
            pushMapLabelToTop();
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
                //the left tile can possibly be moved to

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
                                int newXfromMapMoveEvent = worldMap.tileArray[player.getX()][player.getY()].getEvent().getNewXPositionForPlayer();
                                int newYfromMapMoveEvent = worldMap.tileArray[player.getX()][player.getY()].getEvent().getNewYPositionForPlayer();
                                loadCorrectMap(false, newXfromMapMoveEvent, newYfromMapMoveEvent, mapToLoadFromMoveEvent, worldMap, worldPane, mainMenu, resolution, player, controls, scene, boombox);
                                return;
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
            System.out.println("[" + player.getCurrentMapName() + "] player x,y after moving left: " + player.getX() + ", " + player.getY());
            pushMapLabelToTop();
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
