import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class WorldMap {

    //attributes============================================

    int xDimension;
    int yDimension;
    Tile tileArray[][];
    String name;
    String tileSize;
    Player player;

    //Tiles contain data, but the WorldMap class contains the images

    Image topLevelImageArray[][];
    ImageView topLevelImageViewArray[][];
    Image midLevelImageArray[][];
    ImageView midLevelImageViewArray[][];
    Image bottomLevelImageArray[][];
    ImageView bottomLevelImageViewArray[][];

    //constructors============================================

    public WorldMap(int xDimension, int yDimension, String tileSize, Player player) {
        setyDimension(yDimension);
        setxDimension(xDimension);
        setTileArray(new Tile[xDimension][yDimension]);
        setTileSize(tileSize);

        topLevelImageArray = new Image[xDimension][yDimension];
        topLevelImageViewArray = new ImageView[xDimension][yDimension];

        midLevelImageArray = new Image[xDimension][yDimension];
        midLevelImageViewArray = new ImageView[xDimension][yDimension];

        bottomLevelImageArray = new Image[xDimension][yDimension];
        bottomLevelImageViewArray = new ImageView[xDimension][yDimension];

        System.out.println("Created a new WorldMap. Not finished yet.");
    }

    //getters and setters============================================

    public void setyDimension(int yDimension) {
        this.yDimension = yDimension;
    }

    public void setTileArray(Tile[][] tileArray) {
        this.tileArray = tileArray;
    }

    public void setxDimension(int xDimension) {
        this.xDimension = xDimension;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTileSize(String tileSize) {
        this.tileSize = tileSize;
    }



    public int getyDimension() {
        return yDimension;
    }

    public int getxDimension() {
        return xDimension;
    }


    public Tile[][] getTileArray() {
        return tileArray;
    }

    public String getName() {
        return name;
    }

    public String getTileSize() {
        return tileSize;
    }


    //graphics setters-----


    //set one bottom level image
    public void setBottomLevelImage(int x, int y) {
        String bottomLevelFileName = this.tileArray[x][y].getBottomLevel();
        //only proceed if the bottomLevel String is not blank
        if (!bottomLevelFileName.equals("")) {
            bottomLevelImageArray[x][y] = new Image(bottomLevelFileName);
            bottomLevelImageViewArray[x][y] = new ImageView(bottomLevelImageArray[x][y]);
            int tileSizeInt = -1;
            if (this.getTileSize().equals("40x40")) {
                tileSizeInt = 40;
            } else if (this.getTileSize().equals("60x60")) {
                tileSizeInt = 60;
            }
            if (! (tileSizeInt == -1)) {
                int newXLayout = x * tileSizeInt;
                int newYLayout = y * tileSizeInt;
                bottomLevelImageViewArray[x][y].setLayoutX(newXLayout);
                bottomLevelImageViewArray[x][y].setLayoutY(newYLayout);
            }

        } else {
            System.out.println("Error with setBottomLevelImage for " + x + ", " + y);
        }
    }

    //set all bottom level images
    public void setAllBottomLevelImage(Pane pane) {
        for (int xLoop = 0; xLoop < this.getxDimension(); xLoop++) {
            for (int yLoop = 0; yLoop < this.getyDimension(); yLoop++) {
                setBottomLevelImage(xLoop, yLoop);
                pane.getChildren().add(bottomLevelImageViewArray[xLoop][yLoop]);
            }
        }
    }


    //set one mid level image
    public void setMidLevelImage(int x, int y) {
        String midLevelFileName = this.tileArray[x][y].getMidLevel();
        //only proceed if the midLevel String is not blank
        if (!midLevelFileName.equals("")) {
            midLevelImageArray[x][y] = new Image(midLevelFileName);
            midLevelImageViewArray[x][y] = new ImageView(midLevelImageArray[x][y]);
            int tileSizeInt = -1;
            if (this.getTileSize().equals("40x40")) {
                tileSizeInt = 40;
            } else if (this.getTileSize().equals("60x60")) {
                tileSizeInt = 60;
            }
            if (! (tileSizeInt == -1)) {
                int newXLayout = x * tileSizeInt;
                int newYLayout = y * tileSizeInt;
                midLevelImageViewArray[x][y].setLayoutX(newXLayout);
                midLevelImageViewArray[x][y].setLayoutY(newYLayout);
            }

        } else {
            System.out.println("Error with setMidLevelImage for " + x + ", " + y);
        }
    }

    //set all mid level images
    public void setAllMidLevelImage(Pane pane) {
        for (int xLoop = 0; xLoop < this.getxDimension(); xLoop++) {
            for (int yLoop = 0; yLoop < this.getyDimension(); yLoop++) {
                setBottomLevelImage(xLoop, yLoop);
                pane.getChildren().add(midLevelImageViewArray[xLoop][yLoop]);
            }
        }
    }








    //other methods============================================

    public void loadMap() {
        System.out.println("not implemented yet");
    }

    public void unloadMap() {
        System.out.println("not implemented yet");
    }



}
