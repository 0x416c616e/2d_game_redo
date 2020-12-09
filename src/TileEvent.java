public class TileEvent {
    //not yet implemented
    //it will be used by the Tile class
    //and the WorldMap class uses many Tile objects
    String name;

    public TileEvent() {
        setName("blank_event");
    }

    public TileEvent(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
