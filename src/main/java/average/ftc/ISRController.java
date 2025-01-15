package average.ftc;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class ISRController implements Initializable {
    private static final System.Logger LOGGER = System.getLogger(ISRController.class.getName());
    private static final long ISR_DATA_CAPTURE_TIME = 2L * 1000L;

    static ISRController instance;

    @FXML
    ImageView isr;

    static final LinkedList<Runnable> ON_ISR_DONE = new LinkedList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(this.isr);
        translate.setAutoReverse(true);
        translate.setCycleCount(2); // auto reverse
        translate.setDuration(Duration.seconds(2d));
        translate.setInterpolator(Interpolator.EASE_BOTH);

        ActualGame.addOnRootLoaded(() -> {
            double x = ActualGame.root.getWidth() / 2d;
            LOGGER.log(System.Logger.Level.DEBUG, "x = {0}", x);
            translate.setFromX(x);
            translate.setByX(-x);
            Thread.ofVirtual().start(() -> {
                try {
                    Thread.sleep(1000L);
                    Platform.runLater(() -> isr.setVisible(true));
                    translate.play();
                    Thread.sleep(ISR_DATA_CAPTURE_TIME);
                    isr.setVisible(false);
                    ON_ISR_DONE.forEach(Runnable::run);
                    ActualGame.enemyISR = true;
                } catch (InterruptedException e) {
                    LOGGER.log(System.Logger.Level.ERROR, "Unexpected error. Terminating...", e);
                    System.exit(1);
                    throw new AssertionError();
                }
            }).setUncaughtExceptionHandler((t, e) -> {
                LOGGER.log(System.Logger.Level.ERROR, "Exception on thread '" + t.getName() + '\'', e);
                System.exit(1);
                throw new AssertionError();
            });
        });
        instance = this;
    }
}
