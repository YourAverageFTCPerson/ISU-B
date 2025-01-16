package average.ftc;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.crypto.SecretKey;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Base64;
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

    public static final class EncryptedHolder {
        private static final SecretKey SECRET_KEY;

        static {
            try {
                SECRET_KEY = AESUtils.getKeyFromPassword(Main.password, "huygyy6*Y*(7yGBbbb");
            } catch (Exception e) {
                LOGGER.log(System.Logger.Level.ERROR, "Failed to accept key.", e);
                System.exit(1);
                throw new AssertionError(e);
            }
        }

        public static final MediaPlayer BGM0;

        static {
            try {
                Path path = Path.of(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("bgm0.txt")).toURI());
                byte[] cipherText = Files.readAllBytes(path);
                File temp = File.createTempFile("tmp", ".wav");
                temp.deleteOnExit();
                Files.write(Path.of(temp.getPath()),
                        AESUtils.decrypt(new AESUtils.Encrypted(Base64.getDecoder().decode(cipherText), Base64.getDecoder().decode(Files.readAllBytes(Path.of(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("bgm0iv.txt")).toURI())))), SECRET_KEY));
                BGM0 = new MediaPlayer(new Media(temp.toURI().toString()));
//                BGM1.play();
//                System.out.println(BGM1.getStatus());
//                BGM1.setOnError(() -> {
//                    System.out.println("Error: " + BGM1.getError().getMessage());
//                });
//                BGM1.stop();
            } catch (NullPointerException e) {
                LOGGER.log(System.Logger.Level.ERROR, "BGM 0 doesn't exist.");
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static final MediaPlayer LOSS0;

        static {
            try {
                Path path = Path.of(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("loss0.txt")).toURI());
                byte[] cipherText = Files.readAllBytes(path);
                File temp = File.createTempFile("tmp", ".wav");
                temp.deleteOnExit();
                Files.write(Path.of(temp.getPath()),
                        AESUtils.decrypt(new AESUtils.Encrypted(Base64.getDecoder().decode(cipherText), Base64.getDecoder().decode(Files.readAllBytes(Path.of(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("loss0iv.txt")).toURI())))), SECRET_KEY));
                LOSS0 = new MediaPlayer(new Media(temp.toURI().toString()));
            } catch (NullPointerException e) {
                LOGGER.log(System.Logger.Level.ERROR, "Loss 0 doesn't exist.");
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        static {
            Arrays.fill(Main.password, '\0');
        }
    }
}
