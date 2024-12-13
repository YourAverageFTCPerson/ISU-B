package average.ftc; // Just my GitHub username

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.*;

public class Main extends Application {
    static {
        for (Handler handler : Logger.getLogger("").getHandlers()) {
            handler.setLevel(Level.FINEST);
            handler.setFilter(record -> record.getSourceClassName().startsWith("average.ftc"));
        }
    }

    private static final System.Logger LOGGER = System.getLogger(Main.class.getName());

    public static void main(String[] args) {
        launch(args);
    }

    public static Runnable loadingAnimation(Label label) {
        return () -> {
            try {
                label.setFont(Font.font(50));
                for (int i = 0; i < 3; i++) {
                    Platform.runLater(() -> label.setText(""));
                    Thread.sleep(500L);
                    Platform.runLater(() -> label.setText("."));
                    Thread.sleep(500L);
                    Platform.runLater(() -> label.setText(".."));
                    Thread.sleep(500L);
                    Platform.runLater(() -> label.setText("..."));
                    Thread.sleep(500L);
                }
            } catch (InterruptedException _) {
            }
        };
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane root = FXMLLoader.load(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("SplashScreen.fxml")));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("application.css")).toExternalForm());
        primaryStage.setScene(scene);
        LOGGER.log(System.Logger.Level.DEBUG, root.getChildrenUnmodifiable());
        Label label = (Label) root.getChildrenUnmodifiable().getFirst();
        primaryStage.show();

        primaryStage.setTitle("Amazing Logo");

        primaryStage.getIcons().add(new Image(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("icon.png")).toExternalForm()));

//        Enemy enemy = new Enemy((ImageView) root.getChildrenUnmodifiable().get(1));

        Thread funny = Thread.ofVirtual().start(() -> {
            try {
                Thread.sleep(1000L);
                Platform.runLater(() -> label.setText(label.getText() + " This is JavaFX."));
                Thread.sleep(1000L);
                Platform.runLater(() -> label.setText(label.getText() + " The objective is to kill enemies. Like this one."));
                Thread.sleep(1000L);
                DeathSound.play();
                Platform.runLater(() -> root.getChildrenUnmodifiable().get(1).setVisible(false));
                Thread.sleep(500L);
            } catch (InterruptedException _) {
                Platform.runLater(() -> label.setText("Press it again."));
                return;
            }
            loadingAnimation(label).run();
            Platform.runLater(() -> {
                label.setPrefWidth(500);
                label.setText("PLEASE STAND BY");
            });
        });

        Button start = (Button) root.getChildrenUnmodifiable().get(2);
        start.setOnMouseClicked(e -> {
            LOGGER.log(System.Logger.Level.DEBUG, e);
            funny.interrupt();
            start.setOnMouseClicked(_ -> ActualGame.startOn(primaryStage));
        });
    }
}