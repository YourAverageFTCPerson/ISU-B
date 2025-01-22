package average.ftc;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.Objects;

public class Statistics {
    public static LinkedList<Integer> enemyDistancesTraveled = new LinkedList<>();
    public static int[] confirmedKills; // Of TowerOperators

    public static void collect(Stage stage) {
        try {
            StackPane root = FXMLLoader.load(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("Statistics.fxml")));

            Canvas canvas = (Canvas) root.getChildrenUnmodifiable().getFirst();

            canvas.widthProperty().bind(root.widthProperty());
            canvas.heightProperty().bind(root.heightProperty());

            GraphicsContext g = canvas.getGraphicsContext2D();

            AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(long l) {
                    g.setFill(Color.WHITE);
                    g.setLineWidth(5);
                    g.setFont(new Font(100d));
                    g.fillText("Hello World!", 100d, 100d);
                    g.fillText("Enemy distances traveled: ");
                }
            };
            timer.start();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setMaximized(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
