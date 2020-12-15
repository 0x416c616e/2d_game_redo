public class Tile {

    //attributes============================================

    Event event;
    String topLevel; //decoration
    String midLevel;
    String bottomLevel;
    boolean collision; //true means you can't pass through it
    int xPosition;
    int yPosition;

    //constructors============================================

    public Tile() {
        setEvent(new Event());
        setTopLevel("");
        setMidLevel("");
        setBottomLevel("file:assets/tiles/grass_40x40.png");
        collision = false;
        int xPosition = -1; //needs to be initialized
        int yPosition = -1; //needs to be initialized
    }

    public Tile(String bottomLevel, int xPosition, int yPosition) {
        setEvent(new Event());
        setTopLevel("");
        setMidLevel("");
        setBottomLevel(bottomLevel);
        setCollision(false);
        setxPosition(xPosition);
        setyPosition(yPosition);
    }

    public Tile(String bottomLevel, int xPosition, int yPosition, boolean collision) {
        setEvent(new Event());
        setTopLevel("");
        setMidLevel("");
        setBottomLevel(bottomLevel);
        setCollision(collision);
        setxPosition(xPosition);
        setyPosition(yPosition);
    }

    public Tile(String bottomLevel, String midLevel, int xPosition, int yPosition, boolean collision) {
        setEvent(new Event());
        setTopLevel("");
        setMidLevel(midLevel);
        setBottomLevel(bottomLevel);
        setCollision(collision);
        setxPosition(xPosition);
        setyPosition(yPosition);
    }

    public Tile(String bottomLevel, String midLevel, String topLevel, int xPosition, int yPosition, boolean collision) {
        setEvent(new Event());
        setTopLevel(topLevel);
        setMidLevel(midLevel);
        setBottomLevel(bottomLevel);
        setCollision(collision);
        setxPosition(xPosition);
        setyPosition(yPosition);
    }

    public Tile(String bottomLevel, String midLevel, String topLevel, Event event, int xPosition, int yPosition, boolean collision) {
        setEvent(event);
        setTopLevel(topLevel);
        setMidLevel(midLevel);
        setBottomLevel(bottomLevel);
        setCollision(collision);
        setxPosition(xPosition);
        setyPosition(yPosition);
    }

    //getters and setters============================================

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public void setBottomLevel(String bottomLevel) {
        this.bottomLevel = bottomLevel;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setMidLevel(String midLevel) {
        this.midLevel = midLevel;
    }

    public void setTopLevel(String topLevel) {
        this.topLevel = topLevel;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public Event getEvent() {
        return event;
    }

    public String getBottomLevel() {
        return bottomLevel;
    }

    public String getMidLevel() {
        return midLevel;
    }

    public String getTopLevel() {
        return topLevel;
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    //multiple setters

    public void setBottomLevelWithCollision(String bottomLevel, boolean collision) {
        this.setBottomLevel(bottomLevel);
        this.setCollision(collision);
    }

    public void setMidLevelWithCollision(String midLevel, boolean collision) {
        this.setMidLevel(midLevel);
        this.setCollision(collision);
    }

    public void setTopLevelWithCollision(String topLevel, boolean collision) {
        this.setTopLevel(topLevel);
        this.setCollision(collision);
    }

    //other methods============================================

    //use this in MapLoader unloadMap methods
    public void destroyTile() {
        //System.out.println("Destroying Tile");
        event.destroyEvent();
        setEvent(null);
        setTopLevel(null);
        setMidLevel(null);
        setBottomLevel(null);
        //System.gc();
    }
}
