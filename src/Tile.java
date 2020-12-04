import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tile {
    //I'm not implementing events just yet
    //just wanna try the tile images and collision detection
    TileEvent tileEvent;
    String npcImageFileName;
    String middleLayerFileName;
    String bottomLayerFileName;
    Image npcImage;
    ImageView npcImageView;
    Image middleLayerImage;
    ImageView middleLayerImageView;
    Image bottomLayerImage;
    ImageView bottomLayerImageView;

    boolean collision;
    int x;
    int y;
    public Tile() {
        //not implemented yet
    }

    public void update() {
        //how a tile will be updated once the player moves on the map
    }
}
