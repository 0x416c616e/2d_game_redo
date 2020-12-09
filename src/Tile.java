import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tile {
    //I'm not implementing events just yet
    //just wanna try the tile images and collision detection
    TileEvent tileEvent;
    String npcImageFileName;
    String middleLayerFileName;
    String bottomLayerFileName;
    ImageView npcImageView;
    ImageView middleLayerImageView;
    ImageView bottomLayerImageView;

    boolean collision;
    int x;
    int y;
    public Tile() {
        //not finished yet
    }

    public TileEvent getTileEvent() {
        return tileEvent;
    }

    public void setTileEvent(TileEvent tileEvent) {
        this.tileEvent = tileEvent;
    }

    public String getNpcImageFileName() {
        return npcImageFileName;
    }

    public void setNpcImageFileName(String npcImageFileName) {
        this.npcImageFileName = npcImageFileName;
    }

    public void setMiddleLayerFileName(String middleLayerFileName) {
        this.middleLayerFileName = middleLayerFileName;
    }

    public String getMiddleLayerFileName() {
        return middleLayerFileName;
    }

    public String getBottomLayerFileName() {
        return bottomLayerFileName;
    }

    public void setBottomLayerFileName(String bottomLayerFileName) {
        this.bottomLayerFileName = bottomLayerFileName;
    }

    public ImageView getNpcImageView() {
        return npcImageView;
    }

    public void setNpcImageView(ImageView npcImageView) {
        this.npcImageView = npcImageView;
    }

    public ImageView getMiddleLayerImageView() {
        return middleLayerImageView;
    }

    public void setMiddleLayerImageView(ImageView middleLayerImageView) {
        this.middleLayerImageView = middleLayerImageView;
    }

    public ImageView getBottomLayerImageView() {
        return bottomLayerImageView;
    }

    public void setBottomLayerImageView(ImageView bottomLayerImageView) {
        this.bottomLayerImageView = bottomLayerImageView;
    }
}
