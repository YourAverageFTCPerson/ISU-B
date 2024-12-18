package average.ftc;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public class MapLoader {
    private static final System.Logger LOGGER = System.getLogger(MapLoader.class.getName());

    protected MapLoader() {
        throw new UnsupportedOperationException();
    }

    public static final char WALL = '|', OBSERVATION_POST = '#';

    private static final double X_SCALE = 100d;

    private static final class WallHolder {
        private static final Image WALL = new Image(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream("wall.png")), X_SCALE, 0d, true, true);
    }

    private static final class ObservationPostHolder {
        private static final Image OBSERVATION_POST = new Image(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream("ObservationPost.png")), X_SCALE, 0d, true, true);
    }

    private static LinkedList<Node> loadImplementation(String map) {
        LOGGER.log(System.Logger.Level.TRACE, "entering loadImplementation(String) with param: {0}", map);
        LinkedList<Node> children = new LinkedList<>();

        String[] lines = map.lines().toArray(String[]::new);
        String line;
        ImageView view;
        char c;
        final double Y_SCALE = ObservationPostHolder.OBSERVATION_POST.getHeight();
        for (int j = 0; j < lines.length; j++) {
            LOGGER.log(System.Logger.Level.DEBUG, "j = {0}", j);
            line = lines[j];
            for (int i = 0; i < line.length(); i++) {
                LOGGER.log(System.Logger.Level.DEBUG, "i = {0}", i);
                if (Character.isWhitespace(c = line.charAt(i))) continue;
                view = new ImageView(switch (c) {
                    case WALL -> WallHolder.WALL;
                    case OBSERVATION_POST -> ObservationPostHolder.OBSERVATION_POST;
                    default -> throw new IllegalArgumentException("Invalid map");
                });
                LOGGER.log(System.Logger.Level.DEBUG, view);
                view.setX(i * X_SCALE);
                view.setY(j * Y_SCALE);
                children.add(view);
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
