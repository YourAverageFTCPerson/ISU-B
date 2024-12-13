package average.ftc;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javax.sound.sampled.Control;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public class MapLoader {
    private static final char WALL = '|', ENEMY = '#';

    private final String map;

    private MapLoader(String map) {
        this.map = map;
    }

    public AnchorPane load() {
        AnchorPane result = new AnchorPane();
        ObservableList<Node> children = result.getChildren();

        String[] lines = map.lines().toArray(String[]::new);
        String line;
        for (int i = 0; i < lines.length; i++) {
            line = lines[i];
            for (int j = 0; j < line.length(); i++) {
                children.add(new ImageView());
            }
        }

        return result;
    }

    private static final HashMap<String, WeakReference<MapLoader>> CACHED_MAPS = new HashMap<>();

    public static MapLoader of(String map) {
        WeakReference<MapLoader> reference = CACHED_MAPS.get(Objects.requireNonNull(map));
        MapLoader loader;
        if (reference == null || (loader = reference.get()) == null) {
            loader = new MapLoader(map);
            CACHED_MAPS.put(map, new WeakReference<>(loader));
        }
        return loader;
    }
}
