package average.ftc;

import java.util.*;

public class MapSolver {
    private static final boolean DEBUG = false;

    /**
     * Allow subclasses but not instances.
     */
    protected MapSolver() {
        throw new UnsupportedOperationException();
    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public record Point(int x, int y) implements Comparable<Point> {
        @Override
        public int compareTo(Point point) {
            return Integer.compare(this.x + this.y, point.x + point.y);
        }
    }

    private static String deepToString(HashMap<Point, Direction[]> map) {
        Iterator<Map.Entry<Point, Direction[]>> i = map.entrySet().iterator();
        if (!i.hasNext())
            return "{}";

        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (; ; ) {
            Map.Entry<Point, Direction[]> e = i.next();
            Point key = e.getKey();
            Direction[] value = e.getValue();
            sb.append(key);
            sb.append('=');
            sb.append(Arrays.toString(value));
            if (!i.hasNext())
                return sb.append('}').toString();
            sb.append(',').append(' ');
        }
    }

    public static Point getImmediate(Point current, Direction direction) {
        return switch (Objects.requireNonNull(direction)) {
            case UP -> new Point(Objects.requireNonNull(current).x(), current.y() - 1);
            case DOWN -> new Point(current.x(), current.y() + 1);
            case LEFT -> new Point(current.x() - 1, current.y());
            case RIGHT -> new Point(current.x() + 1, current.y());
        };
    }

    public static Point[] getFastestPath(String map) {
        if (map.isEmpty())
            throw new IllegalArgumentException("The map is empty. That's not allowed. I can't show anything that way.");

        String[] lines = map.lines().toArray(String[]::new);
        int numberOfLines = lines.length;

        int firstLineLength = lines[0].length();
        char[][] matrix = new char[numberOfLines][firstLineLength];
        for (int j = 0; j < numberOfLines; j++) { // Regular for loop to save time instead of looping like 5 times with streams.
            String line = lines[j];
            if (line.length() != firstLineLength)
                throw new IllegalArgumentException("All rows of the map must have the same length.");
            matrix[j] = line.toCharArray();
        }

        HashSet<Point> whitespaces = new HashSet<>(Math.round(firstLineLength * numberOfLines * 0.75f));

        char c;
        Point start = null, goal = null;
        for (int i = 0; i < firstLineLength; i++)
            for (int j = 0; j < numberOfLines; j++) {
                if (Character.isWhitespace(c = matrix[j][i]))
                    whitespaces.add(new Point(i, j));
                switch (c) {
                    case MapLoader.START:
                        whitespaces.add(start = new Point(i, j));
                    case MapLoader.GOAL:
                        whitespaces.add(goal = new Point(i, j));
                }
            }
        if (start == null)
            throw new IllegalArgumentException("No start to maze (should be represented by '" + MapLoader.START + "')");
        else if (goal == null)
            throw new IllegalArgumentException("Maze has no goal (should be represented by '" + MapLoader.GOAL + "')");

        HashMap<Point, Direction[]> whitespaceNodes = new HashMap<>();

        whitespaces.forEach(e -> {
            int x = e.x(), y = e.y();
            ArrayList<Direction> directions = new ArrayList<>(4);
            if (x > 0 && whitespaces.contains(new Point(x - 1, y)))
                directions.add(Direction.LEFT);
            if (x < firstLineLength && whitespaces.contains(new Point(x + 1, y)))
                directions.add(Direction.RIGHT);
            if (y > 0 && whitespaces.contains(new Point(x, y - 1)))
                directions.add(Direction.UP);
            if (y < numberOfLines && whitespaces.contains(new Point(x, y + 1)))
                directions.add(Direction.DOWN);
            whitespaceNodes.put(e, directions.toArray(new Direction[0]));
        });

        if (DEBUG) // Save time if release mode
            System.getLogger(MapSolver.class.getName()).log(
                    System.Logger.Level.DEBUG, "whitespaceNodes: {0}", deepToString(whitespaceNodes));

        return AStarAlgorithm.solve(start, goal, whitespaceNodes);
    }
}
