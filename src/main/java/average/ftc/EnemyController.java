package average.ftc;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class EnemyController implements Initializable {
    private static ImageView base;

    @FXML
    private ImageView enemy;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // translate
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(this.enemy);
        transition.setDuration(Duration.millis(1000d));
        transition.setCycleCount(TranslateTransition.INDEFINITE);
        transition.setByX(250);
        transition.setAutoReverse(true);
        base = this.enemy;
        transition.play();
    }

    public static ImageView[] spawnNormalEnemies(int amount) {
        if (base == null)
            throw new IllegalStateException("Asset not loaded yet");
        if (amount < 1)
            throw new IllegalArgumentException("illegal amount");
        Image image = base.getImage();
        ImageView[] enemies = new ImageView[amount];
        for (int i = 0; i < amount; i++) {
            enemies[i] = new ImageView(image);
            enemies[i].setFitWidth(50d); // TODO remove magic number
            enemies[i].setFitHeight(50d);
            enemies[i].setY(i * 50d);
            System.out.println(enemies[i]);
            System.out.println(enemies[i].equals(base));
            enemies[i].setVisible(false);
        }
        System.out.println("enemies.length: " + enemies.length);
        return enemies;
    }
}
