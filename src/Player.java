public class Player {
    private String name;
    int x;
    int y;

    public Player(String name){
        setName(name);
    }

    public void setName(String name) {
        if (name.length() > 0 && name.length() < 11) {
            this.name = name;
        }
    }

    public String getName(){
        return this.name;
    }
}
