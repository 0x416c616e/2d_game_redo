public class WorldMap {

    String filename = "";
    public WorldMap(String filename) {
        setFilename(filename);
    }

    public WorldMap() {
        setFilename("maps/map1.map");
    }

    public void setFilename(String filaname) {
        if (filename.length() > 0 && filename.length() < 30) {
            this.filename = filename;
        }
    }

    public String getFilename() {
        return this.filename;
    }


}
