package average.ftc;

import javafx.fxml.FXMLLoader;
//import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Line;
//import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.*;

public class Statistics {
    protected Statistics() {
        throw new UnsupportedOperationException();
    }

    public static LinkedList<Integer> enemyDistancesAtDeath = new LinkedList<>();
//    public static int[] confirmedKills; // Of TowerOperators

//    private static Group boxPlot(double width, double min, double max, double median, int... data) { // Never quite got this to work lol
//        Group plot = new Group();
//        Line line = new Line(0d, 100d, width, 100d);
//        line.setStrokeWidth(10);
//        plot.getChildren().add(line);
//
//        Label minimum = new Label(Double.toString(min));
//        minimum.setFont(new Font(75));
//        minimum.setTextFill(Color.LIGHTBLUE);
//        minimum.setTranslateY(110d);
//        plot.getChildren().add(minimum);
//
//        Label maximum = new Label(Double.toString(max));
//        maximum.setFont(new Font(100));
//        maximum.setTextFill(Color.LIGHTBLUE);
//        maximum.setTranslateY(110d + width);
//        plot.getChildren().add(maximum);
//
////        plot.getChildren().add(new Box());
//        return plot;
//    }

    public static void collect(Stage stage) {
        try {
            StackPane root = FXMLLoader.load(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("Statistics.fxml")));


            Label label = (Label) root.getChildrenUnmodifiable().getFirst();

            int[] enemyDistancesAtDeath = Statistics.enemyDistancesAtDeath.stream().mapToInt(Integer::intValue).toArray();

            Arrays.sort(enemyDistancesAtDeath);

            double mean, min, max;
            HashSet<Integer> modes = new HashSet<>();
            HashMap<Integer, Integer> occurrences = new HashMap<>();
            int modeOccurrenceCount;
            if (enemyDistancesAtDeath.length > 0) {
                double sum = min = max = enemyDistancesAtDeath[0];
                modes.add(enemyDistancesAtDeath[0]);
                modeOccurrenceCount = 1;

                for (int i = 1, distance, occurrence; i < enemyDistancesAtDeath.length; i++) {
                    occurrences.put(distance = enemyDistancesAtDeath[i], occurrence = (occurrences.getOrDefault(distance, 0) + 1));
//                    System.out.printf("i: %d, distance: %d, occurrence: %d\n", i, distance, occurrence);
                    if (modes.contains(distance)) {
                        modes.clear();
                        modes.add(distance);
                        modeOccurrenceCount++;
                    } else if (modeOccurrenceCount == occurrence)
                        modes.add(distance);

                    if (distance > max)
                        max = distance;
                    else if (distance < min)
                        min = distance;

                    sum += distance;
                }

                if (modes.size() == enemyDistancesAtDeath.length)
                    modes = new HashSet<>(); // empty--there is no mode

                mean = sum / enemyDistancesAtDeath.length;
            } else {
                mean = 0d;
//                min = 0d;
//                max = 0d;
            }

            int halfLength = enemyDistancesAtDeath.length / 2; // floor div
            double median = enemyDistancesAtDeath.length == 0 ? 0 : (enemyDistancesAtDeath.length % 2 == 0 ? (enemyDistancesAtDeath[halfLength] + enemyDistancesAtDeath[halfLength + 1]) * 0.5 : enemyDistancesAtDeath[halfLength + 1]);
            label.setText("enemyDistancesAtDeath: " + Statistics.enemyDistancesAtDeath.toString().replace('[', '{').replace(']', '}') + "\nProperties of enemyDistancesAtDeath: mean = " + mean + ", median = " + median + ", modes = " + modes.toString().replace('[', '{').replace(']', '}'));

//            AnimationTimer timer = new AnimationTimer() {
//                @Override
//                public void handle(long l) {
//                }
//            };
//            timer.start();

//            root.getChildren().add(boxPlot(5000d, min, max, median, enemyDistancesAtDeath));

            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
