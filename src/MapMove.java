public class MapMove extends Event {

    //attributes============================================

    String mapToLoad;

    //constructors============================================

    public MapMove() {
        super();
        //System.out.println("made a new MapMove event");
    }

    public MapMove(String trigger, String mapToLoad) {
        super(trigger, "MapMove");
        setMapToLoad(mapToLoad);
    }

    //getters and setters============================================

    public void setMapToLoad(String mapToLoad) {
        this.mapToLoad = mapToLoad;
    }

    @Override
    public String getMapToLoad() {
        return mapToLoad;
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