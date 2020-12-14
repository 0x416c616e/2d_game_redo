import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class Player {

    //attributes//attributes============================================

    private String name;
    int x;
    int y;
    String position; //facing up, down, left, or right
    String currentMapName;
    boolean isBusy; //denotes when the player is busy with an interface open, such as a menu, inventory, in combat, etc
                    //some things like WASD/touchscreen movement can't be done when the player is busy

    //busy status does NOT get saved to a file
    //you will not be able to save except in situations where you aren't busy with something else anyway, i.e. having a dialogue window open

    Image player_up_60x60Image;
    Image player_up_40x40Image;
    ImageView player_up_60x60ImageView;
    ImageView player_up_40x40ImageView;
    Image player_right_60x60Image;
    Image player_right_40x40Image;
    ImageView player_right_60x60ImageView;
    ImageView player_right_40x40ImageView;
    Image player_left_60x60Image;
    Image player_left_40x40Image;
    ImageView player_left_60x60ImageView;
    ImageView player_left_40x40ImageView;
    Image player_down_60x60Image;
    Image player_down_40x40Image;
    ImageView player_down_60x60ImageView;
    ImageView player_down_40x40ImageView;




    //constructors//attributes============================================

    //full constructor
    public Player(String name, int x, int y, String position, String currentMapName) {
        setName(name);
        setX(x);
        setY(y);
        setPosition(position);
        setIsBusy(false);
        this.loadImages();
    }

    //only use the following two constructors for the "new game" stuff
    public Player(String name){
        setName(name);
        //default starting position is 60, 60 for a new game save
        setX(11);
        setY(11);
        setPosition("down");
        setCurrentMapName("map_0_0");
        setIsBusy(false);
        this.loadImages();
    }

    public Player() {
        setName("noNameYet");
        setX(5);
        setY(5);
        setPosition("down");
        setCurrentMapName("map_0_0");
        setIsBusy(false);
        this.loadImages();
    }

    //getters and setters============================================


    public void setIsBusy(boolean busy) {
        isBusy = busy;
    }

    public boolean getIsBusy() {
        return isBusy;
    }


    public void setName(String name) {
        if (name.length() > 0 && name.length() < 11) {
            this.name = name;
        }
    }

    public String getName(){
        return this.name;
    }


    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        //I guess I'm just arbitrarily setting a map limit of 1000x1000
        if ((x >= 0) && (x <= 1000)) {
            this.x = x;
        }
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        if ((y >= 0) && (y <= 1000)) {
            this.y = y;
        }
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public String getCurrentMapName() {
        return currentMapName;
    }

    public void setCurrentMapName(String currentMapName) {
        this.currentMapName = currentMapName;
    }



    //other methods============================================

    public void loadImages() {
        //filename stuff
        String firstPart = "file:assets/player/";
        String lastPart = ".png";

        //images

        player_down_40x40Image = new Image(firstPart + "player_down_40x40" + lastPart);
        player_down_60x60Image = new Image(firstPart + "player_down_60x60" + lastPart);

        player_up_40x40Image = new Image(firstPart + "player_up_40x40" + lastPart);
        player_up_60x60Image = new Image(firstPart + "player_up_60x60" + lastPart);

        player_right_40x40Image = new Image(firstPart + "player_right_40x40" + lastPart);
        player_right_60x60Image = new Image(firstPart + "player_right_60x60" + lastPart);

        player_left_40x40Image = new Image(firstPart + "player_left_40x40" + lastPart);
        player_left_60x60Image = new Image(firstPart + "player_left_60x60" + lastPart);


        //imageviews

        player_down_40x40ImageView = new ImageView(player_down_40x40Image);
        player_down_60x60ImageView = new ImageView(player_down_60x60Image);

        player_up_40x40ImageView = new ImageView(player_up_40x40Image);
        player_up_60x60ImageView = new ImageView(player_up_60x60Image);

        player_right_40x40ImageView = new ImageView(player_right_40x40Image);
        player_right_60x60ImageView = new ImageView(player_right_60x60Image);

        player_left_40x40ImageView = new ImageView(player_left_40x40Image);
        player_left_60x60ImageView = new ImageView(player_left_60x60Image);
    }

    //gets a uniquely-named field from an XML file
    public String getUniqueXMLField(String fieldToGet, String filename) {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        try {
            String saveFileName = filename;
            DocumentBuilder b = f.newDocumentBuilder();
            Document doc = b.parse(new File(saveFileName));
            String valueOfXMLField = doc.getElementsByTagName(fieldToGet).item(0).getTextContent();
            System.gc();
            return valueOfXMLField;
        } catch(ParserConfigurationException pc) {
            System.gc();
            pc.printStackTrace();
        } catch (SAXException se) {
            System.gc();
            se.printStackTrace();
        } catch (IOException ioe) {
            System.gc();
            ioe.printStackTrace();
        }
        System.gc();
        return "errorGUPXML from Player class";
    }

    public void loadPlayerFromFile() {
        //getting all XML fields from the save file
        String playerSaveFileName = "saves/" + this.getName() + ".save";
        String playerNameFromFile = getUniqueXMLField("playerName", playerSaveFileName);
        int playerXFromFile = Integer.parseInt(getUniqueXMLField("playerXpositionOnMap", playerSaveFileName));
        int playerYFromFile = Integer.parseInt(getUniqueXMLField("playerYpositionOnMap", playerSaveFileName));
        String positionFromFile = getUniqueXMLField("playerPosition", playerSaveFileName);
        String currentMapNameFromFile = getUniqueXMLField("playerCurrentMapName", playerSaveFileName);

        //setting all the attributes of the player using the data from the save file
        this.setName(playerNameFromFile);
        this.setX(playerXFromFile);
        this.setY(playerYFromFile);
        this.setPosition(positionFromFile);
        this.setCurrentMapName(currentMapNameFromFile);

        playerSaveFileName = null;
        playerNameFromFile = null;
        positionFromFile = null;
        currentMapNameFromFile = null;


    }

    public String toString(){
        String playerString = "**********\n";
        playerString += "[PLAYER toString]\nName: " + this.getName() + "\n";
        playerString += "x: " + this.getX() + "\n";
        playerString += "y: " + this.getY() + "\n";
        playerString += "Position: " + this.getPosition() + "\n";
        playerString += "Current map name: " + this.getCurrentMapName() + "\n";
        playerString += "**********\n";
        return playerString;
    }

    public void setAllImageViewLocationX(int newX) {
        this.player_up_60x60ImageView.setLayoutX(newX);
        this.player_up_40x40ImageView.setLayoutX(newX);
        this.player_right_60x60ImageView.setLayoutX(newX);
        this.player_right_40x40ImageView.setLayoutX(newX);
        this.player_left_60x60ImageView.setLayoutX(newX);
        this.player_left_40x40ImageView.setLayoutX(newX);
        this.player_down_60x60ImageView.setLayoutX(newX);
        this.player_down_40x40ImageView.setLayoutX(newX);
    }

    public void setAllImageViewLocationY(int newY) {
        this.player_up_60x60ImageView.setLayoutY(newY);
        this.player_up_40x40ImageView.setLayoutY(newY);
        this.player_right_60x60ImageView.setLayoutY(newY);
        this.player_right_40x40ImageView.setLayoutY(newY);
        this.player_left_60x60ImageView.setLayoutY(newY);
        this.player_left_40x40ImageView.setLayoutY(newY);
        this.player_down_60x60ImageView.setLayoutY(newY);
        this.player_down_40x40ImageView.setLayoutY(newY);
    }
}
