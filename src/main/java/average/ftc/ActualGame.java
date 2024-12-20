package average.ftc;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ActualGame {
    private static final System.Logger LOGGER = System.getLogger(ActualGame.class.getName());

    private static boolean enemyISR = true;

    private static String readFile(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            LOGGER.log(System.Logger.Level.ERROR, "mapLoc is invalid");
            Platform.exit();
        }
        @SuppressWarnings("DataFlowIssue") String result = new String(inputStream.readAllBytes());
        LOGGER.log(System.Logger.Level.DEBUG, "Exiting readFile(InputStream) with result: {0}", result);
        return result;
    }

    private static void showAlert(Label alert) {
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(alert);
        scale.setToX(1.5);
        scale.setToY(1.5);
        scale.setDuration(Duration.seconds(1.5));
        alert.setVisible(true);
        scale.play();
        scale.setOnFinished(_ -> alert.setVisible(false));
    }

    public static void startOn(Stage primaryStage) {
        LOGGER.log(System.Logger.Level.INFO, primaryStage);
        try {
            StackPane root = FXMLLoader.load(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("ActualGame.fxml")));
            ConfigParser.Config config = ConfigParser.readConfig();
            Group map = (Group) root.getChildren().get(0);
            map.getChildren().addAll(MapLoader.load(readFile(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(config.getMapLoc())))));
            root.getChildren().addAll(map);
//            AnchorPane.setLeftAnchor(warner, 0.0);
//            AnchorPane.setRightAnchor(warner, 0.0);
//            AnchorPane.setBottomAnchor(warner, 0.0);
//            AnchorPane.setTopAnchor(warner, 0.0);
//            warner.setAlignment(Pos.CENTER);
//            root.getChildren().add(warner);

//            for (Node node : root.getChildrenUnmodifiable()) {
//                StackPane.setAlignment(node, Pos.CENTER);
//            }
            Scene scene = new Scene(root);
//            scene.getStylesheets().add(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("application.css")).toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setMaximized(false);
            showAlert(warner);
//            System.out.println("before: " + root.getChildren().size());
            map.getChildren().addAll(EnemyController.spawnNormalEnemies(config.getEnemies()));
//            System.out.println("after: " + root.getChildren().size());
        } catch (Throwable throwable) {
            LOGGER.log(System.Logger.Level.ERROR, "Huh?", throwable);
            System.exit(1);
            throw new AssertionError();
        }
    }
}
