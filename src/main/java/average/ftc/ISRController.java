package average.ftc;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class ISRController implements Initializable {
    @FXML
    private ImageView isr;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing...");
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(this.isr);
        translate.setAutoReverse(true);
        translate.setCycleCount(2);
        translate.setDuration(Duration.seconds(2d));
        translate.setByX(200d);
        Thread.ofVirtual().start(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            translate.play();
        });
    }
}
