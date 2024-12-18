package average.ftc;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class HoverButtons {
    protected HoverButtons () {
        throw new UnsupportedOperationException();
    }

    public static void applyAnimationTo(Button button) {
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(button);
        transition.setDuration(Duration.millis(1000d));

        ScaleTransition scale = new ScaleTransition(Duration.millis(500d), button);
        scale.setInterpolator(Interpolator.EASE_BOTH);

        button.hoverProperty().addListener((_, _, current) -> {
            scale.stop();
            if (current) {
                scale.setToX(1.5);
                scale.setToY(1.5);
            } else {
                scale.setToX(1.0);
                scale.setToY(1.0);
            }
            scale.play();
        });
        transition.setCycleCount(TranslateTransition.INDEFINITE);
        transition.setInterpolator(Interpolator.EASE_BOTH);
        transition.setByY(20);
        transition.setAutoReverse(true);
        transition.play();
    }
}
