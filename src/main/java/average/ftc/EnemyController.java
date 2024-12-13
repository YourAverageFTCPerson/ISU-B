package average.ftc;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class EnemyController implements Initializable {
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
        transition.play();
    }
}
