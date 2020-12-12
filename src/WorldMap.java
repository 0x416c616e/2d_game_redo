public class WorldMap {

    //attributes============================================

    int xDimension;
    int yDimension;
    Tile tileArray[][];
    String name;
    String tileSize;

    //constructors============================================

    public WorldMap(int xDimension, int yDimension, String tileSize) {
        setyDimension(yDimension);
        setxDimension(xDimension);
        this.tileArray = new Tile[xDimension][yDimension];
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

    //other methods============================================

    public void loadMap() {
        System.out.println("not implemented yet");
    }

    public void unloadMap() {
        System.out.println("not implemented yet");
    }
}
