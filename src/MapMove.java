public class MapMove extends Event {

    //attributes============================================

    String mapToLoad; //which map to go to, corresponds to methods in MapLoader


    //new x,y for where the player will be put on the new map they're being moved to
    int newXPositionForPlayer;
    int newYPositionForPlayer;


    //constructors============================================

    public MapMove() {
        super();
        //System.out.println("made a new MapMove event");
        setMapToLoad("not_loaded");

    }

    public MapMove(String trigger, String mapToLoad, int newX, int newY) {
        super(trigger, "MapMove");
        setMapToLoad(mapToLoad);
        setNewXPositionForPlayer(newX);
        setNewYPositionForPlayer(newY);
    }

    //getters and setters============================================

    public void setMapToLoad(String mapToLoad) {
        this.mapToLoad = mapToLoad;
    }

    @Override
    public String getMapToLoad() {
        return mapToLoad;
    }

    @Override
    public void setNewXPositionForPlayer(int newXPositionForPlayer) {
        this.newXPositionForPlayer = newXPositionForPlayer;
    }

    @Override
    public int getNewXPositionForPlayer() {
        return newXPositionForPlayer;
    }

    @Override
    public void setNewYPositionForPlayer(int newYPositionForPlayer) {
        this.newYPositionForPlayer = newYPositionForPlayer;
    }

    @Override
    public int getNewYPositionForPlayer() {
        return newYPositionForPlayer;
    }

    @Override
    public void setNewXY(int x, int y) {
        setNewXPositionForPlayer(x);
        setNewYPositionForPlayer(y);
    }

    //other methods============================================


    @Override
    public void destroyEvent() {
        //System.out.println("MapMove Event destroyed");
        setMapToLoad(null);
        setEventType(null);
        setTrigger(null);
        //System.gc();
    }
}
