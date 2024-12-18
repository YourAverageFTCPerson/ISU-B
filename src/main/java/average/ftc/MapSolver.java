package average.ftc;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

public class MapSolver {
    private static final boolean DEBUG = true;

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
            return (int) Math.signum(this.x + this.y - point.x - point.y);
        }
    }

    public record Wanderer(Point current, Direction previous, HashMap<Point, Direction[]> whitespaceNodes) {
        private static final SecureRandom RANDOM;

        static {
            SecureRandom random;
            try {
                random = SecureRandom.getInstanceStrong();
            } catch (NoSuchAlgorithmException e) {
                random = new SecureRandom();
            }
            RANDOM = random;
        }

        private Direction pickRandomWithoutPrevious() {
            Direction[] available = Arrays.stream(this.whitespaceNodes.get(this.current))
                                                  .filter(t -> t != this.previous)
                                                  .toArray(Direction[]::new);

            return available[RANDOM.nextInt(0, available.length)];
        }

        public Wanderer next() {
            Direction pick = this.pickRandomWithoutPrevious();
            Point next = getImmediate(this.current, pick);
            return new Wanderer(next, pick, this.whitespaceNodes);
        }
    }

//    public static class ProbabilisticRoute implements Cloneable {
//        private static final SecureRandom RANDOM;
//
//        static {
//            SecureRandom random;
//            try {
//                random = SecureRandom.getInstanceStrong();
//            } catch (NoSuchAlgorithmException e) {
//                random = new SecureRandom();
//            }
//            RANDOM = random;
//        }
//
//        private Point currentPoint;
//
//        private Direction[] currentDirections;
//
//        private Direction currentlyChosen;
//
//        private HashMap<Point, Direction[]> whitespaceNodes;
//
//        private ProbabilisticRoute(HashMap<Point, Direction[]> whitespaceNodes, Point[] first) {
//            this.whitespaceNodes = Objects.requireNonNull(whitespaceNodes);
//            this.currentPoint = Objects.requireNonNull(first);
//            this.currentDirections = whitespaceNodes.get(first);
//            this.currentlyChosen = this.currentDirections[RANDOM.nextInt(0, this.currentDirections.length)];
//        }
//
//        private Direction pickRandomWithoutPrevious() {
//            Direction[] currentDirections = Arrays.stream(this.currentDirections).filter(t -> t != this.currentlyChosen).toArray(Direction[]::new);;
//
//            return this.currentlyChosen = currentDirections[RANDOM.nextInt(0, currentDirections.length)];
//        }
//
//        public boolean hasNext(int exitX) {
//            return this.currentPoint.x() < exitX;
//        }
//
//        public Point next() {
//            this.currentPoint = getImmediate(this.currentPoint, this.pickRandomWithoutPrevious());
//            this.currentDirections = this.whitespaceNodes.get(this.currentPoint);
//            return this.currentPoint;
//        }
//
//        @Override
//        public String toString() {
//            return this.getClass().getName() + "[currentPoint=" + this.currentPoint + ",currentDirections=" +
//                    Arrays.toString(this.currentDirections) + ",whitespaceNodes=" + this.whitespaceNodes +
//                    ",currentlyChosen=" + this.currentlyChosen;
//        }
//
//        @Override
//        public ProbabilisticRoute clone() {
//            try {
//                ProbabilisticRoute clone = (ProbabilisticRoute) super.clone();
//                clone.currentPoint = this.currentPoint;
//                clone.currentDirections = this.currentDirections;
//                clone.whitespaceNodes = this.whitespaceNodes;
//                clone.currentlyChosen = this.currentlyChosen;
//                if (DEBUG)
//                    System.getLogger(ProbabilisticRoute.class.getName()).log(
//                            System.Logger.Level.DEBUG, "this: {0}, clone: {1}", this, clone);
//                return clone;
//            } catch (CloneNotSupportedException e) {
//                throw new AssertionError();
//            }
//        }
//    }

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
            case UP -> new Point(Objects.requireNonNull(current).x(), current.y() + 1);
            case DOWN -> new Point(current.x(), current.y() - 1);
            case LEFT -> new Point(current.x() - 1, current.y());
            case RIGHT -> new Point(current.x() + 1, current.y());
        };
    }

    public static Point[] getFastestPath(String map, Point start, Point goal) {
        if (map.isEmpty())
            throw new IllegalArgumentException("The map is empty. That's not allowed. I can't show anything that way.");

        String[] lines = map.lines().toArray(String[]::new);
        int numberOfLines = lines.length;

//        // I don't care about optimization.
//        // Rows first.
//        char[][] matrix = Arrays.stream(lines).map(String::toCharArray).toArray(char[][]::new);
        int firstLineLength = lines[0].length();
        char[][] matrix = new char[numberOfLines][firstLineLength];
        for (int i = 0; i < numberOfLines; i++) { // Regular for loop to save time instead of looping like 5 times with streams.
            String line = lines[i];
            if (line.length() != firstLineLength)
                throw new IllegalArgumentException("All rows of the map must have the same length.");
            matrix[i] = line.toCharArray();
        }

        HashSet<Point> whitespaces = new HashSet<>(Math.round(firstLineLength * numberOfLines * 0.75f));
//        LinkedList<Integer> entrances = new LinkedList<>(), exits = new LinkedList<>();

//        for (int i = 0; i < numberOfLines - 1; i++) {
//            if (!Character.isWhitespace(matrix[i][0])) continue;
//            entrances.add(i);
//            whitespaces.add(new Point(i, 0));
//        }
//
//        int firstLineLengthMinusOne = firstLineLength - 1;
//
//        for (int j = 1; j < firstLineLengthMinusOne; j++)
//            for (int i = 0; i < numberOfLines; i++) {
//                if (!Character.isWhitespace(matrix[i][j])) continue;
//                whitespaces.add(new Point(i, j));
//            }
//
//        for (int i = 0; i < numberOfLines; i++) {
//            if (!Character.isWhitespace(matrix[i][firstLineLengthMinusOne])) continue;
//            exits.add(i);
//            whitespaces.add(new Point(i, firstLineLengthMinusOne));
//        }

        for (int i = 0; i < numberOfLines; i++)
            for (int j = 0; j < firstLineLength; j++)
                if (Character.isWhitespace(matrix[i][j]))
                    whitespaces.add(new Point(i, j));

        HashMap<Point, Direction[]> whitespaceNodes = new HashMap<>();

        whitespaces.forEach(e -> {
            int x = e.x(), y = e.y();
            ArrayList<Direction> directions = new ArrayList<>(4);
            if (x > 0 && whitespaces.contains(new Point(x - 1, y)))
                directions.add(Direction.UP);
            if (x < numberOfLines - 1 && whitespaces.contains(new Point(x + 1, y)))
                directions.add(Direction.DOWN);
            if (y > 0 && whitespaces.contains(new Point(x, y - 1)))
                directions.add(Direction.LEFT);
            if (y < firstLineLength - 1 && whitespaces.contains(new Point(x, y + 1)))
                directions.add(Direction.RIGHT);
            whitespaceNodes.put(e, directions.toArray(new Direction[0]));
        });

        if (DEBUG) // Save time if release mode
            System.getLogger(MapSolver.class.getName()).log(
                    System.Logger.Level.DEBUG, "whitespaceNodes: {0}", deepToString(whitespaceNodes));

//        List<List<Point>> validPaths = new ArrayList<>(numberOfLines);
//
//        entrances.forEach(i -> {
//            Point entrance = new Point(i, 0);
//            Direction[] node = whitespaceNodes.get(entrance);
//            if (node.length == 0) return;
//            if (node.length == 1) {
//                getImmediate(entrance, node[0]);
//            }
//        });

        return AStarAlgorithm.solve(start, goal, whitespaceNodes);
    }
}
