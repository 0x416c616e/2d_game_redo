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

    //Tiles contain data, but the WorldMap class contains the images

    Image topLevelImageArray[][];
    ImageView topLevelImageViewArray[][];
    Image midLevelImageArray[][];
    ImageView midLevelImageViewArray[][];
    Image bottomLevelImageArray[][];
    ImageView bottomLevelImageViewArray[][];

    //constructors============================================

    public WorldMap(int xDimension, int yDimension, String tileSize) {
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
        if (bottomLevelFileName.length() > 0) {
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
                String bottomLevelName = this.tileArray[xLoop][yLoop].getBottomLevel();
                if (bottomLevelName.length() > 0) {
                    setBottomLevelImage(xLoop, yLoop);
                    pane.getChildren().add(bottomLevelImageViewArray[xLoop][yLoop]);
                }
            }
        }
    }


    //set one mid level image
    public void setMidLevelImage(int x, int y) {
        String midLevelFileName = this.tileArray[x][y].getMidLevel();
        //only proceed if the midLevel String is not blank
        if (midLevelFileName.length() > 0) {
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
                String midLevelName = this.tileArray[xLoop][yLoop].getMidLevel();
                if (midLevelName.length() > 0) {
                    setMidLevelImage(xLoop, yLoop);
                    pane.getChildren().add(midLevelImageViewArray[xLoop][yLoop]);
                }
            }
        }
    }




    //set one top level image
    public void setTopLevelImage(int x, int y) {
        String topLevelFileName = this.tileArray[x][y].getTopLevel();
        //only proceed if the topLevel String is not blank
        if (!topLevelFileName.equals("")) {
            topLevelImageArray[x][y] = new Image(topLevelFileName);
            topLevelImageViewArray[x][y] = new ImageView(topLevelImageArray[x][y]);
            int tileSizeInt = -1;
            if (this.getTileSize().equals("40x40")) {
                tileSizeInt = 40;
            } else if (this.getTileSize().equals("60x60")) {
                tileSizeInt = 60;
            }
            if (! (tileSizeInt == -1)) {
                int newXLayout = x * tileSizeInt;
                int newYLayout = y * tileSizeInt;
                topLevelImageViewArray[x][y].setLayoutX(newXLayout);
                topLevelImageViewArray[x][y].setLayoutY(newYLayout);
            }

        } else {
            System.out.println("Error with setTopLevelImage for " + x + ", " + y);
        }
    }

    //set all top level images
    public void setAllTopLevelImage(Pane pane) {
        for (int xLoop = 0; xLoop < this.getxDimension(); xLoop++) {
            for (int yLoop = 0; yLoop < this.getyDimension(); yLoop++) {
                String topLevelName = this.tileArray[xLoop][yLoop].getTopLevel();
                if (topLevelName.length() > 0) {
                    setTopLevelImage(xLoop, yLoop);
                    pane.getChildren().add(topLevelImageViewArray[xLoop][yLoop]);
                }

            }
        }
    }

    //this is too slow, don't use it
    //put unloadMap_X_Y methods in the MapLoader class instead
    public void destroyWorldMap(Pane mainMenu, Pane worldPane) {
        //destroy tile array
        //used for MapMove -- gotta unload everything on the screen before loading new stuff
        for (int xIter = 0; xIter < xDimension; xIter++) {
            for (int yIter = 0; yIter < yDimension; yIter++) {
                this.tileArray[xIter][yIter].getEvent().destroyEvent();
                this.tileArray[xIter][yIter].destroyTile();
                this.tileArray[xIter][yIter] = null;
                if (topLevelImageViewArray[xIter][yIter] != null) {
                    topLevelImageViewArray[xIter][yIter].setImage(null);
                }
                topLevelImageViewArray[xIter][yIter] = null;
                topLevelImageArray[xIter][yIter] = null;
                if (midLevelImageViewArray[xIter][yIter] != null) {
                    midLevelImageViewArray[xIter][yIter].setImage(null);
                }
                midLevelImageViewArray[xIter][yIter] = null;
                midLevelImageArray[xIter][yIter] = null;
                if (bottomLevelImageViewArray[xIter][yIter] != null) {
                    bottomLevelImageViewArray[xIter][yIter].setImage(null);
                }
                bottomLevelImageViewArray[xIter][yIter] = null;
                bottomLevelImageArray[xIter][yIter] = null;
            }
        }
        mainMenu.getChildren().remove(worldPane);
        setName(null);

    }







}
