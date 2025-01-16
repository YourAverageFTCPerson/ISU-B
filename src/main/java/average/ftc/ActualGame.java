package average.ftc;

import javafx.animation.ScaleTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.NumberFormat;
import java.util.*;

public class ActualGame {
    private static final System.Logger LOGGER = System.getLogger(ActualGame.class.getName());

    public static final SecureRandom RANDOM;

    static {
        SecureRandom temp;
        try {
            temp = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException _) {
            temp = new SecureRandom();
        }
        RANDOM = temp;
    }

    static StackPane root;

    public static boolean enemyISR = true;

    private static String readFile(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            LOGGER.log(System.Logger.Level.ERROR, "mapLoc is invalid");
            System.exit(1);
            throw new AssertionError();
        }
        String result = new String(inputStream.readAllBytes());
        LOGGER.log(System.Logger.Level.DEBUG, "Exiting readFile(InputStream) with result: {0}", result);
        return result;
    }

    private static void showAlert(Label alert, Runnable onFinished) {
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(alert);
        scale.setFromX(0.8);
        scale.setFromY(0.8);
        scale.setToX(1.5);
        scale.setToY(1.5);
        scale.setDuration(Duration.seconds(1.5));
        scale.setOnFinished(_ -> {
            alert.setVisible(false);
            alert.setScaleX(1d);
            alert.setScaleY(1d);
            if (onFinished != null)
                onFinished.run();
        });
        alert.setVisible(true);
        scale.play();
    }

    private static void showAlert(Label alert) {
        showAlert(alert, null);
    }

    private static final LinkedList<Runnable> ON_ROOT_LOADED = new LinkedList<>();

    static void addOnRootLoaded(Runnable runnable) {
        ON_ROOT_LOADED.add(Objects.requireNonNull(runnable));
    }

    static LinkedList<TowerOperator> towerOperators = new LinkedList<>();

    @SuppressWarnings("FieldMayBeFinal")
    private static double nanoIncrement = 1000000000d;

    private static List<Node> map;

    static List<Node> getMap() {
        return map;
    }

    public static void onAllEnemiesRemoved() {
        Sounds.FriendlyGunHolder.FRIENDLY_GUN.stop();
        shooterThread.interrupt();
    }

    private static Thread shooterThread;

    public static Iterator<ImageView> getEnemies() {
        if (map == null)
            throw new IllegalStateException("Call startOn() first to initialize ActualGame.map");
        //noinspection SynchronizeOnNonFinalField
        synchronized (map) {
            return map.stream().filter(n -> n instanceof ImageView &&
                            EnemyController.getImage().equals(((ImageView) n).getImage())) // This order to allow null
                    .map(n -> (ImageView) n).iterator();
        }
    }

    public static final NumberFormat MONEY_FORMAT = NumberFormat.getCurrencyInstance();

    private static Label money;

    private static final double COST_OF_GROUND_TO_AIR = 1.0E6;

    private static double balance;

    static boolean setBalance(double balance) {
        if (balance < 0)
            return false;
        ActualGame.balance = balance;
        money.setText("Money: " + MONEY_FORMAT.format(balance));
        return true;
    }

    @SuppressWarnings("unused")
    static double getBalance() {
        return balance;
    }

    private static Label insufficientFunds;

    private static void insufficientFunds() {
        try {
            root.getChildren().add(insufficientFunds);
            showAlert(insufficientFunds, () -> root.getChildren().remove(insufficientFunds));
        } catch (IllegalArgumentException _) {
            // duplicate insufficientFunds
        }
    }

    public static void startOn(Stage primaryStage) {
        LOGGER.log(System.Logger.Level.INFO, primaryStage);
        LOGGER.log(System.Logger.Level.DEBUG, "FRIENDLY_GUN duration: " + Sounds.FriendlyGunHolder.FRIENDLY_GUN.getCycleDuration().toSeconds());
        try {
            root = FXMLLoader.load(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("ActualGame.fxml")));
            ConfigParser.Config config = ConfigParser.readConfig();
            Group map = (Group) root.getChildren().getFirst();
            ObservableList<Node> mapUnsynchronized = map.getChildren();
            ActualGame.map = Collections.synchronizedList(map.getChildren());
            mapUnsynchronized.addAll(MapLoader.load(readFile(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(config.getMapLoc())))));

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setMaximized(false);
            primaryStage.setMaximized(true);
            Utils.setMap(ActualGame.map);
            Label warner;
            showAlert(warner = (Label) root.getChildrenUnmodifiable().getLast()); // Always last because it's on top
            money = (Label) root.getChildrenUnmodifiable().get(root.getChildrenUnmodifiable().size() - 2);
            if (!setBalance(config.getMoney())) {
                LOGGER.log(System.Logger.Level.ERROR, "config.json's money property must be a positive real number.");
                System.exit(1);
                throw new AssertionError();
            }
            Button groundToAir = (Button) root.getChildrenUnmodifiable().get(root.getChildrenUnmodifiable().size() - 3);
            insufficientFunds = new Label("Insufficient funds");
            insufficientFunds.setFont(warner.getFont());
            insufficientFunds.setTextFill(warner.getTextFill());
            groundToAir.setOnMouseClicked(_ -> {
                if (!ISRController.instance.isr.isVisible()) return;
                if (setBalance(balance - COST_OF_GROUND_TO_AIR)) {
//                    enemyISR = false;
                    ISRController.instance.isr.setVisible(false);
                    root.getChildren().remove(groundToAir);
                    return;
                }
                insufficientFunds();
            });

            ISRController.ON_ISR_DONE.add(() -> {
                groundToAir.setOnMouseClicked(null);
                groundToAir.setVisible(false);
            });

//            map.minHeight(MapLoader.getXScale() * MapLoader.getRows());
//            map.setLayoutX(0);
//            map.setLayoutY(0);
            mapUnsynchronized.addAll(EnemyController.spawnNormalEnemies(config.getEnemies()));
            ON_ROOT_LOADED.forEach(Runnable::run);
            Sounds.EncryptedHolder.BGM0.play();
            shooterThread = Thread.ofVirtual().unstarted(() -> {
                double previousTime = System.nanoTime() + nanoIncrement;
                Sounds.FriendlyGunHolder.FRIENDLY_GUN.setCycleCount(MediaPlayer.INDEFINITE);
                Sounds.FriendlyGunHolder.FRIENDLY_GUN.play();
                while (!Thread.interrupted()) {
                    double currentTime = System.nanoTime();
                    if (currentTime - previousTime < nanoIncrement) continue;
                    towerOperators.forEach(TowerOperator::update);
                    TowerOperator.alreadyAttacking.clear();
                    previousTime = currentTime;
                }
                // TODO: (wave is over)
            });
            shooterThread.setName("Shooter");
            shooterThread.setUncaughtExceptionHandler((t, e) -> {
                LOGGER.log(System.Logger.Level.ERROR, "Exception on thread '" + t.getName() + '\'', e);
                System.exit(1);
                throw new AssertionError();
            });
            shooterThread.start();
        } catch (Throwable throwable) {
            LOGGER.log(System.Logger.Level.ERROR, "Huh?", throwable);
            System.exit(1);
            throw new AssertionError();
        }
    }
}
