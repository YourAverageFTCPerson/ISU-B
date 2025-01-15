package average.ftc;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

public class EnemyController implements Initializable {
    private static final System.Logger LOGGER = System.getLogger(EnemyController.class.getName());

    /**
     * km
     */
    private static final double ONE_STUD = 0.02;

    /**
     * km/h
     */
    private static final double ENEMY_SPEED = 70d;

    private static Image base;

    public static Image getImage() {
        return base;
    }

    @FXML
    private ImageView enemy;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // translate
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(this.enemy);
        transition.setDuration(Duration.seconds(1d));
        transition.setCycleCount(TranslateTransition.INDEFINITE);
        transition.setByX(250);
        transition.setAutoReverse(true);
        base = this.enemy.getImage();
        transition.play();
    }

    private static void playAll(Iterator<TranslateTransition> transitions, SecureRandom random) {
        if (Thread.currentThread().isInterrupted()) return;
        if (!transitions.hasNext()) {
            Sounds.EncryptedHolder.BGM0.stop();
            // TODO
            Sounds.EncryptedHolder.LOSS0.play();
            return;
        }
        TranslateTransition transition = transitions.next();
        while (random.nextDouble() >= 0.5) { // chance
            if (Thread.currentThread().isInterrupted()) return;
        }
//        System.out.println("playing " + transition);
        transition.play();
        transition.setOnFinished(_ -> playAll(transitions, random));
    }

    public record ThreadStuff(Thread thread, SecureRandom random) {
    }

    static HashMap<ImageView, ThreadStuff> movementThreads = new HashMap<>();

    public static ImageView[] spawnNormalEnemies(int amount) {
        if (base == null)
            throw new IllegalStateException("Asset not loaded yet");
        if (amount < 1)
            throw new IllegalArgumentException("illegal amount");
        ImageView[] enemies = new ImageView[amount];
        for (int i = 0; i < amount; i++) {
            enemies[i] = new ImageView(base);
            enemies[i].setFitWidth(MapLoader.getXScale() * 0.75); // TODO remove magic number
            enemies[i].setPreserveRatio(true);
            enemies[i].setTranslateX(-MapLoader.getXScale());
            enemies[i].setTranslateY(i * MapLoader.getYScale());
            System.out.println("ogY:" + enemies[i].getY());
            enemies[i].setVisible(true);
            SecureRandom random;
            try {
                random = SecureRandom.getInstanceStrong();
            } catch (NoSuchAlgorithmException e) {
                LOGGER.log(System.Logger.Level.WARNING, "No strong SecureRandom instances–using 'new SecureRandom()'", e);
                random = new SecureRandom();
            }
            Thread movement = getThread(random, i, enemies);

            movementThreads.put(enemies[i], new ThreadStuff(movement, random));

            movement.start();
        }
        return enemies;
    }

    private static Thread getThread(SecureRandom random, int finalI, ImageView[] enemies) {
        Thread movement = new Thread(() -> {
            try {
                Thread.sleep(finalI * 1000L);
            } catch (InterruptedException e) {
                LOGGER.log(System.Logger.Level.DEBUG, "Thread '{0}' was interrupted.", Thread.currentThread().getName());
                return;
            }
            playAll(createTransitions(enemies[finalI]).iterator(), random);
        });
        movement.setName("EnemyMover-" + finalI);
        return movement;
    }

    static MapSolver.Point[] fastestPath;

    public static HashMap<ImageView, LinkedList<TranslateTransition>> transitions = new HashMap<>();

    /**
     * Creates a path depending on {@link ActualGame#enemyISR}. If true, uses the A* algorithm. If false, uses Micromouse
     * algorithms. TODO support the latter.
     *
     * @param enemy the enemy to move.
     * @return a {@code LinkedList} of transitions to apply in order to follow the path.
     */
    public static LinkedList<TranslateTransition> createTransitions(ImageView enemy) {
        Objects.requireNonNull(enemy);
        if (fastestPath == null) {
            LOGGER.log(System.Logger.Level.DEBUG, "map: {0}", MapLoader.map);
            fastestPath = MapSolver.getFastestPath(MapLoader.map);
        }
        transitions.remove(enemy);
        if (ActualGame.enemyISR) {
            TranslateTransition transition;

            LinkedList<TranslateTransition> path = new LinkedList<>();

            for (MapSolver.Point point : fastestPath) {
                transition = new TranslateTransition();
                transition.setNode(enemy);
                transition.setToX(point.x() * MapLoader.getXScale());
                transition.setToY(MapLoader.getYScale());
                transition.setDuration(Duration.hours(ONE_STUD / ENEMY_SPEED));
                path.add(transition);
            }
            transitions.put(enemy, path);
            return path;
        }
        throw new UnsupportedOperationException("TODO");
    }
}
