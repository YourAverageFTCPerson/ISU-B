package average.ftc;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ActualGame {
    private static final System.Logger LOGGER = System.getLogger(ActualGame.class.getName());

    private static boolean enemyISR = true;

    private static String readFile(InputStream inputStream) throws IOException {
        String result = new String(inputStream.readAllBytes());
        LOGGER.log(System.Logger.Level.DEBUG, "Exiting readFile(InputStream) with result: {0}", result);
        return result;
    }

    public static void startOn(Stage primaryStage) {
        LOGGER.log(System.Logger.Level.INFO, primaryStage);
        try {
            AnchorPane root = FXMLLoader.load(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("ActualGame.fxml")));
            root.getChildren().addAll(MapLoader.load(readFile(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream("level0.my_td")))));
            Scene scene = new Scene(root);
//            scene.getStylesheets().add(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("application.css")).toExternalForm());
            primaryStage.setScene(scene);
        } catch (Throwable throwable) {
            LOGGER.log(System.Logger.Level.ERROR, "Huh?", throwable);
            Platform.exit();
        }
    }
}
