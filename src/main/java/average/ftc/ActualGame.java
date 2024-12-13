package average.ftc;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ActualGame {
    private static final System.Logger LOGGER = System.getLogger(ActualGame.class.getName());

    public static void startOn(Stage primaryStage) {
        try {
            AnchorPane root = FXMLLoader.load(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("ActualGame.fxml")));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("application.css")).toExternalForm());
            primaryStage.setScene(scene);
        } catch (Throwable throwable) {
            LOGGER.log(System.Logger.Level.ERROR, "Huh?", throwable);
            Platform.exit();
        }
    }
}
