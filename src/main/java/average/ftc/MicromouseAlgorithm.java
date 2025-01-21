package average.ftc;

import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Objects;

public class MicromouseAlgorithm {
    public static int columns, rows;

    public static HashMap<MapSolver.Point, MapSolver.Direction[]> whitespaceNodes;

    public static MapSolver.Point start, goal;

    protected MicromouseAlgorithm() {
        throw new UnsupportedOperationException();
    }

    public static class Micromouse {
        public MapSolver.Point position = start;
    }

    public static Micromouse newPathFor(ImageView enemy, MapSolver.Point currentPosition) {
        Objects.requireNonNull(enemy);
        Objects.requireNonNull(currentPosition);
        int[][] maze = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                maze[i][j] = Math.abs(goal.x() - i) + Math.abs(goal.y() - j);
            }
        }
        // TODO
        return null;
    }
}
