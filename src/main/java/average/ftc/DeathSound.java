package average.ftc;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URISyntaxException;
import java.util.Objects;

public final class DeathSound {
    private static final class DeathSoundHolder {
        private static final MediaPlayer DEATH_SOUND;

        static {
            try {
                DEATH_SOUND = new MediaPlayer(new Media(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("Wilhelm_Scream.wav")).toURI().toString()));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void play() {
        DeathSoundHolder.DEATH_SOUND.play();
    }
}
