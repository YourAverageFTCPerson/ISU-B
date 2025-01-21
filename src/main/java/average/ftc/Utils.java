package average.ftc;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;

import java.util.List;
import java.util.Objects;

public class Utils {
    public static final Image BULLET_IN_AIR = new Image(Objects.requireNonNull(Thread.currentThread()
                                                            .getContextClassLoader()
                                                            .getResourceAsStream("bullet-in-air.png")),
            MapLoader.getXScale(), MapLoader.getYScale(), true, true);

    private static List<Node> map;

    public static void setMap(List<Node> map) {
        Utils.map = Objects.requireNonNull(map);
    }

    public static void shootAt(double fromX, double fromY, ImageView to) {
        if (true) return;
        Objects.requireNonNull(to, "to");
        ImageView bullet = new ImageView(BULLET_IN_AIR); // Multiple bullets at the same time are possible
        bullet.setTranslateX(fromX);
        bullet.setTranslateY(fromY);
        //noinspection SynchronizeOnNonFinalField
        synchronized (map) {
            if (Platform.isFxApplicationThread())
                map.add(bullet);
            else
                Platform.runLater(() -> map.add(bullet));
        }
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(bullet);

        transition.setToX(to.getX() + to.getTranslateX());
        transition.setToY(to.getY() + to.getTranslateY());
        transition.play();
        transition.setOnFinished(_ -> {
            map.remove(bullet);
            bullet.setVisible(false);
            EnemyController.movements.get(to).stopped().set(true);
            map.remove(to);
            new MediaPlayer(Sounds.DeathHolder.DEATH.getMedia()).play();
        });
    }
}
