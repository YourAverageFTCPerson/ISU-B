package average.ftc;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public class MapLoader {
    private static final System.Logger LOGGER = System.getLogger(MapLoader.class.getName());

    protected MapLoader() {
        throw new UnsupportedOperationException();
    }

    public static final char WALL = '|', OBSERVATION_POST = '#', GOAL = 'G', START = 'S';

    private static double xScale = Double.NaN;

    private static double yScale = Double.NaN;

    private static int rows, columns;

    public static double getYScale() {
        return yScale;
    }

    static String map;

    public static double getXScale() {
        if (Double.isNaN(xScale)) throw new RuntimeException();
        return xScale;
    }

    public static int getRows() {
        return rows;
    }

    public static int getColumns() {
        return columns;
    }

    private static final class WallHolder {
        private static final Image WALL = new Image(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream("wall.png")), getXScale(), 0d, true, true);

        static {
            System.out.println("WALL loaded: " + getXScale());
            System.out.println("w=" + WALL.getWidth() + ",h=" + WALL.getHeight());
        }
    }

    private static final class ObservationPostHolder {
        private static final Image OBSERVATION_POST = new Image(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream("ObservationPost.png")), getXScale(), 0d, true, true);
    }

    private static final class GoalHolder {
        private static final Image GOAL = new Image(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream("jackpot-logo.png")), getXScale(), 0d, true, true);
    }

    private static final class EmptyListHolder {
        private static final LinkedList<Node> EMPTY_LIST = new LinkedList<>();
    }

//    static MapSolver.Point start, goal;

    private static LinkedList<Node> loadImplementation(String map) {
        LOGGER.log(System.Logger.Level.TRACE, "entering loadImplementation(String) with param: {0}", map);

        if ((MapLoader.map = map).isBlank())
            return EmptyListHolder.EMPTY_LIST;

        LinkedList<Node> children = new LinkedList<>();

        String[] lines = map.lines().toArray(String[]::new);
        String line;
        ImageView view;
        char c;
        rows = lines[0].length();

        xScale = 1217d / rows; // TODO
//        xScale = 100d;
        xScale = ObservationPostHolder.OBSERVATION_POST.getWidth();
        yScale = ObservationPostHolder.OBSERVATION_POST.getHeight();

        System.out.println("xScale: " + xScale);

        columns = lines.length;

        for (int j = 0; j < lines.length; j++) {
            LOGGER.log(System.Logger.Level.DEBUG, "j = {0}", j);
            line = lines[j];
            int lineLength = line.length();
            if (lineLength != rows) {
//                throw new IllegalArgumentException("All lines must be the same length");
                LOGGER.log(System.Logger.Level.ERROR, "ALL LINES MUST BE THE SAME LENGTH");
                System.exit(1);
            }

            for (int i = 0, numberOfGoals = 0, numberOfStarts = 0; i < lineLength; i++) {
                LOGGER.log(System.Logger.Level.DEBUG, "i = {0}", i);
                if (Character.isWhitespace(c = line.charAt(i))) continue;
                switch (c) {
                    case WALL:
                        Rectangle wall = new Rectangle(i * getXScale(), j * getYScale(), getXScale(), getYScale());
                        wall.setFill(Color.NAVY);
                        children.add(wall);
                        break;
                    case OBSERVATION_POST:
                        ActualGame.towerOperators.add(new TierOne(ActualGame.getMap(), i, j));
                        view = new ImageView(ObservationPostHolder.OBSERVATION_POST);
                        view.setX(i * getXScale());
                        view.setY(j * getYScale());
                        children.add(view);
                        break;
                    case GOAL:
                        if (++numberOfGoals == 2)
                            throw new IllegalArgumentException("Only one goal is allowed");
//                        goal = new MapSolver.Point(i, j);
                        view = new ImageView(GoalHolder.GOAL);
                        view.setX(i * getXScale());
                        view.setY(j * getYScale());
                        children.add(view);
                        break;
                    case START:
                        if (++numberOfStarts == 2)
                            throw new IllegalArgumentException("Only one start is allowed");
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid map");
                }
            }
        }

        LOGGER.log(System.Logger.Level.DEBUG, children);

        return children;
    }

    private static final HashMap<String, WeakReference<LinkedList<Node>>> CACHED_MAPS = new HashMap<>();

    public static LinkedList<Node> load(String map) {
        WeakReference<LinkedList<Node>> reference = CACHED_MAPS.get(Objects.requireNonNull(map));
        LinkedList<Node> loaded;
        if (reference == null || (loaded = reference.get()) == null) {
            loaded = loadImplementation(map);
            CACHED_MAPS.put(map, new WeakReference<>(loaded));
        }
        return loaded;
    }
}
