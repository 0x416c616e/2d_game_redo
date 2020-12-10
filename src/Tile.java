import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tile {
    TileEvent tileEvent;
    String npcImageFileName;
    String middleLayerFileName;
    String bottomLayerFileName;
    ImageView bottomLayerImageView;
    Image bottomLayerImage;

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

    public void setBottomLayerImage(Image bottomLayerImage) {
        this.bottomLayerImage = bottomLayerImage;
    }

    public Image getBottomLayerImage() {
        return bottomLayerImage;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public boolean getCollision() {
        return this.collision;
    }

    public void setBottomLayerImageView(ImageView bottomLayerImageView) {
        this.bottomLayerImageView = bottomLayerImageView;
    }

    public ImageView getBottomLayerImageView() {
        return bottomLayerImageView;
    }

    public void setBottomLayerImageViewXY(int x, int y) {
        this.bottomLayerImageView.setLayoutX(x);
        this.bottomLayerImageView.setLayoutY(y);
    }


}
