package average.ftc;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URISyntaxException;
import java.util.Objects;

public class Sounds {
    private static final System.Logger LOGGER = System.getLogger(Sounds.class.getName());

    protected Sounds() {
        throw new UnsupportedOperationException();
    }

    public static final class DeathHolder {
        public static final MediaPlayer DEATH;

        static {
            try {
                DEATH = new MediaPlayer(new Media(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("Wilhelm_Scream.wav")).toURI().toString()));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void playSound(MediaPlayer player) {
        player.play();
        try {
            while (!player.getCycleDuration().subtract(player.getCurrentTime()).equals(Duration.ZERO))
                //noinspection BusyWait
                Thread.sleep(10L);
        } catch (InterruptedException e) {
            LOGGER.log(System.Logger.Level.ERROR, (String) null, e);
        }
        player.stop();
    }

    public static final class FriendlyGunHolder {
        public static final MediaPlayer FRIENDLY_GUN;

        static {
            try {
                FRIENDLY_GUN = new MediaPlayer(new Media(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("friendly.wav")).toURI().toString()));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
