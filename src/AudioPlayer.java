import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

//I THOUGHT I WOULD PLAY SOUND EFFECTS WITH THIS CLASS
//BUT IT INTRODUCES A MEMORY LEAK
//DON'T USE IT UNTIL YOU FIGURE OUT HOW TO FIX IT!!!!

public class AudioPlayer {

    //attributes

    final int NUMBER_OF_SOUND_EFFECTS = 11; //update this when adding more sounds or songs in the assets/music folder
    Media soundEffectsArray[];
    String[] soundEffectsFileNamesArray;
    MediaPlayer player[];

    //constructor
    public AudioPlayer() {
        //loading all sound effects at the beginning to make them faster to play later
        player = new MediaPlayer[NUMBER_OF_SOUND_EFFECTS];
        this.soundEffectsArray = new Media[NUMBER_OF_SOUND_EFFECTS];
        this.soundEffectsFileNamesArray = new String[]{
                "assets/audio/ALERT_Appear.wav",
                "assets/audio/ALERT_Dissappear.wav",
                "assets/audio/ALERT_Error.wav",
                "assets/audio/ETRA.wav",
                "assets/audio/MENU_A_Back.wav",
                "assets/audio/MENU_A_Select.wav",
                "assets/audio/MENU_B_Back.wav",
                "assets/audio/MENU_B_Select.wav",
                "assets/audio/MENU_Pick.wav",
                "assets/audio/MESSAGE_B_Accept.wav",
                "assets/audio/MESSAGE_B_Decline.wav"
        };

        //put all sound effects into Media array so they can be used by playSound(Media m)
        for (int i = 0; i < soundEffectsFileNamesArray.length; i++) {
            soundEffectsArray[i] = new Media("file:///" + System.getProperty("user.dir").replace('\\', '/') + "/" + soundEffectsFileNamesArray[i]);
            player[i] = new MediaPlayer(soundEffectsArray[i]);
            final int iFinal = i;
            player[i].setOnEndOfMedia(() -> {
                //player[iFinal].dispose(); //super important for freeing up RAM used by this memory hog of a class!!!!!
                System.gc();

            });

        }
    }

    //one method for playing based on string (slow), one for playing based on Media (fast)

    /*
    public void playSound(String fileName){
        Media m = new Media("file:///" + System.getProperty("user.dir").replace('\\', '/') + "/" + fileName);
        player = new MediaPlayer(m);
        player.play();
        player.dispose(); //super important for freeing up RAM used by this memory hog of a class!!!!!
        System.gc();
    }*/

    public void playSound(int soundIndex) {



        player[soundIndex].play();
        System.gc();
        player[soundIndex].seek(Duration.ZERO);

        //player = null;



    }

    public Media getSoundMedia(int index) {
        return this.soundEffectsArray[index];
    }


}