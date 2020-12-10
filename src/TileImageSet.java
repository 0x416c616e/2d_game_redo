import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TileImageSet {
    //this class just has a copy of every tile Image in RAM
    //for faster loading
    //only use a single instace of the class for the whole program

    Image rockImage;
    ImageView rockImageView;
    Image grassImage;
    ImageView grassImageView;
    Image playerImage;
    ImageView playerImageView;

    public TileImageSet() {
        rockImage = new Image("file:assets/tiles/rock.png");
        rockImageView = new ImageView(rockImage);
        grassImage = new Image("file:assets/tiles/grass.png");
        grassImageView = new ImageView(grassImage);
        playerImage = new Image("file:assets/player.png");
        playerImageView = new ImageView(playerImage);
    }


    //getters only, no setters

    public Image getRockImage() {
        return rockImage;
    }

    public ImageView getRockImageView() {
        return rockImageView;
    }

    public Image getGrassImage() {
        return grassImage;
    }

    public ImageView getGrassImageView() {
        return grassImageView;
    }

    public Image getPlayerImage() {
        return playerImage;
    }

    public ImageView getPlayerImageView() {
        return playerImageView;
    }

    public ImageView getImageViewByString(String imageViewName) {
        switch (imageViewName) {
            case "rock.png":
                return this.getRockImageView();
            case "grass.png":
                return this.getGrassImageView();
            default:
                System.out.println("getImageViewByString error");
                break;
        }
        return null;
    }

    public Image getImageByString(String imageName) {
        switch (imageName) {
            case "rock.png":
                return this.getRockImage();
            case "grass.png":
                return this.getGrassImage();
            default:
                System.out.println("getImageViewByString error");
                break;
        }
        return null;
    }
}
