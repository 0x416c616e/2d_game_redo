public class Player {

    //attributes//attributes============================================

    private String name;
    int x;
    int y;
    String position; //facing up, down, left, or right
    String currentMapName;


    //constructors//attributes============================================

    //full constructor
    public Player(String name, int x, int y, String position, String currentMapName) {
        setName(name);
        setX(x);
        setY(y);
        setPosition(position);
    }

    //only use the following two constructors for the "new game" stuff
    public Player(String name){
        setName(name);
        //default starting position is 60, 60 for a new game save
        setX(5);
        setY(5);
        setPosition("down");
        setCurrentMapName("map_0_0");
    }

    public Player() {
        setName("noNameYet");
        setX(5);
        setY(5);
        setPosition("down");
        setCurrentMapName("map_0_0");
    }

    //getters and setters============================================

    public void setName(String name) {
        if (name.length() > 0 && name.length() < 11) {
            this.name = name;
        }
    }

    public String getName(){
        return this.name;
    }


    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        //I guess I'm just arbitrarily setting a map limit of 1000x1000
        if ((x >= 0) && (x <= 1000)) {
            this.x = x;
        }
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        if ((y >= 0) && (y <= 1000)) {
            this.y = y;
        }
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public String getCurrentMapName() {
        return currentMapName;
    }

    public void setCurrentMapName(String currentMapName) {
        this.currentMapName = currentMapName;
    }

    //other methods============================================
}
