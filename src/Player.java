public class Player {
    private String name;
    int x;
    int y;

    public Player(String name, int x, int y) {
        setName(name);
        setX(x);
        setY(y);
    }

    public Player(String name){
        setName(name);
        //default starting position is 60, 60 for a new game save
        int x = 60;
        int y = 60;
    }

    public Player() {
        setName("noNameYet");
        int x = 60;
        int y = 60;
    }

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
}
