package average.ftc;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class ButtonEffects {
    protected ButtonEffects() {
        throw new UnsupportedOperationException();
    }

    public static void applyAnimationsTo(Button button) {
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(button);
        transition.setDuration(Duration.millis(1000d));

        ScaleTransition scale = new ScaleTransition(Duration.millis(500d), button);
        scale.setInterpolator(Interpolator.EASE_BOTH);

        button.hoverProperty().addListener((_, previous, current) -> {
            if (previous == current) return;
            scale.stop();
            double amount = current ? 1.5 : 1d;
            scale.setToX(amount);
            scale.setToY(amount);
            scale.play();
        });
        transition.setCycleCount(TranslateTransition.INDEFINITE);
        transition.setInterpolator(Interpolator.EASE_BOTH);
        transition.setByY(20d);
        transition.setAutoReverse(true);
        transition.play();
    }
}
