public class WorldMapData {

    //how big the map is i.e. 200x200 tiles
    int xDimension;
    int yDimension;
    public TileData tiles[][]; //[x][y]

    String filename = "";

    private void populateBlankTiles(int xMax, int yMax) {
        for (int x = 0; x < xMax; x++) {
            for (int y = 0; y < yMax; y++) {
                tiles[x][y] = new TileData();
            }
        }
    }

    //no name provided assumes the default map of map1, the starting one
    public WorldMapData(int xDimension, int yDimension) {
        setxDimension(xDimension);
        setyDimension(yDimension);
        tiles = new TileData[xDimension][yDimension];
        setFilename("maps/map1.map");
        populateBlankTiles(xDimension, yDimension);
    }

    public WorldMapData(int xDimension, int yDimension, String filename) {
        setxDimension(xDimension);
        setyDimension(yDimension);
        tiles = new TileData[xDimension][yDimension];
        setFilename(filename);
        populateBlankTiles(xDimension, yDimension);
    }



    //getters and setters

    public void setFilename(String filaname) {
        if (filename.length() > 0 && filename.length() < 30) {
            this.filename = filename;
        }
    }

    public String getFilename() {
        return this.filename;
    }

    public int getxDimension() {
        return xDimension;
    }

    public void setxDimension(int yDimension) {
        this.yDimension = yDimension;
    }

    public int getyDimension() {
        return yDimension;
    }

    public void setyDimension(int yDimension) {
        this.yDimension = yDimension;
    }
}
