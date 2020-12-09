import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TileData {
    //I'm not implementing events just yet
    //just wanna try the tile data

    //TILE GRAPHICS ARE HANDLED SEPARATELY
    TileEvent tileEvent;
    String npcImageFileName;
    String middleLayerFileName;
    String bottomLayerFileName;
    boolean collision;
    int x;
    int y;
    public TileData() {
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



    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public boolean getCollision() {
        return this.collision;
    }
}
