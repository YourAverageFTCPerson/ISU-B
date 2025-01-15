package average.ftc; // Just my GitHub username

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {
    private static final System.Logger LOGGER = System.getLogger(Main.class.getName());

    static {
        Logger root = Logger.getLogger("");
        root.setLevel(Level.ALL);
        try {
            List<String> loggerInclusionList = Files.readAllLines(Path.of(Objects.requireNonNull(
                    Thread.currentThread().getContextClassLoader().getResource(
                            "logger-inclusion-list.txt")).toURI()));
            Filter filter = lr -> lr.getLevel().intValue() >= root.getLevel().intValue() && loggerInclusionList.contains(lr.getLoggerName());
            for (Handler handler : root.getHandlers()) {
                handler.setLevel(Level.ALL);
                handler.setFilter(filter);
            }
            System.out.println("len: " + root.getHandlers().length);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        LOGGER.log(System.Logger.Level.INFO, "Hello World!");
    }

    static byte[] password;

    public static void main(String[] args) throws Exception {
        try {
            Class.forName(ActualGame.class.getName());
        } catch (ClassNotFoundException e) {
            throw new AssertionError(e);
        }
        Console console = System.console();
        char[] temp;
        if (console != null)
            temp = console.readPassword("password: ");
        else {
            System.out.print("password: ");
            System.out.flush();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                temp = reader.readLine().toCharArray();
            }
        }

        password = new byte[temp.length];

        for (int i = 0; i < temp.length; i++) {
            password[i] = (byte) temp[i];
            temp[i] = '\0';
        }

        launch(args);
    }

    public static void dotDotDot(Label label) throws InterruptedException {
        Platform.runLater(() -> label.setText("")); // Fix bug where the text blows up very big for a sliver of time
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
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane root = FXMLLoader.load(Objects.requireNonNull(
                Thread.currentThread().getContextClassLoader().getResource("SplashScreen.fxml")));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        LOGGER.log(System.Logger.Level.DEBUG, root.getChildrenUnmodifiable());
        Label label = (Label) root.getChildrenUnmodifiable().getFirst();
        Font originalFont = label.getFont();
        primaryStage.show();

        for (Node node : root.getChildrenUnmodifiable())
            if (node instanceof Button button)
                ButtonEffects.applyAnimationsTo(button);

        primaryStage.setTitle("Amazing Logo");

        primaryStage.getIcons().add(new Image(Objects.requireNonNull(
                Thread.currentThread().getContextClassLoader().getResource("icon.png")).toExternalForm()));

//        Enemy enemy = new Enemy((ImageView) root.getChildrenUnmodifiable().get(1));

        Thread funny = Thread.ofVirtual().start(() -> {
            try {
                Thread.sleep(1000L);
                Platform.runLater(() -> label.setText(label.getText() + " This is JavaFX."));
                Thread.sleep(1000L);
                Platform.runLater(
                        () -> label.setText(label.getText() + " The objective is to kill enemies. Like this one."));
                Thread.sleep(1000L);

                Sounds.FriendlyGunHolder.FRIENDLY_GUN.play();
//                Sounds.FriendlyGunHolder.FRIENDLY_GUN.setOnEndOfMedia(() -> System.out.println("WHY"));

                Platform.runLater(() -> root.getChildrenUnmodifiable().get(1).setVisible(false)); // Hide now dead enemy
                Sounds.DeathHolder.DEATH.play();

                Thread.sleep(500L);
                dotDotDot(label);
                Platform.runLater(() -> {
                    if (label.getText().equals("Press it again.")) return;
                    label.setPrefWidth(500d);
                    label.setText("PLEASE STAND BY");
                });
            } catch (InterruptedException _) {
            }
        });

        Button start = (Button) root.getChildrenUnmodifiable().get(2);
        start.setOnMouseClicked(e -> {
            LOGGER.log(System.Logger.Level.DEBUG, e);
            funny.interrupt();
            Platform.runLater(() -> {
                label.setFont(originalFont);
                label.setText("Press it again.");
            });
            start.setOnMouseClicked(_ -> {
                ActualGame.startOn(primaryStage);
                primaryStage.getIcons().add(new Image(Objects.requireNonNull(
                        Thread.currentThread().getContextClassLoader().getResource("icon.png")).toExternalForm()));
            });
        });
    }
}