public class WorldMap {

    //how big the map is i.e. 200x200 tiles
    int xDimension;
    int yDimension;
    Tile tiles[][];

    String filename = "";
    public WorldMap(String filename) {
        setFilename(filename);
    }


    //no name provided assumes the default map of map1, the starting one
    public WorldMap(int xDimension, int yDimension) {
        setxDimension(xDimension);
        setyDimension(yDimension);
        setFilename("maps/map1.map");
    }

    public WorldMap(int xDimension, int yDimension, String filename) {
        setxDimension(xDimension);
        setyDimension(yDimension);
        setFilename(filename);
    }

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
