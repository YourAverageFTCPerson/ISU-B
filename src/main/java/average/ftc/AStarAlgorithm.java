package average.ftc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.function.BiFunction;

/**
 * Only used if enemy ISR is allowed to fly over for 20 seconds.
 * <a href="https://en.wikipedia.org/wiki/A*_search_algorithm">A* search algorithm on Wikipedia</a>
 */
public final class AStarAlgorithm {
    private AStarAlgorithm() {
        throw new UnsupportedOperationException();
    }

    private static MapSolver.Point[] reconstructPath(HashMap<MapSolver.Point, MapSolver.Point> cameFrom, MapSolver.Point current) {
        LinkedList<MapSolver.Point> totalPath = new LinkedList<>(List.of(current));
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            totalPath.addFirst(current);
        }
        return totalPath.toArray(new MapSolver.Point[0]);
    }

    private static Iterable<MapSolver.Point> getNeighbours(MapSolver.Point point, HashMap<MapSolver.Point, MapSolver.Direction[]> neighbours) {
        MapSolver.Direction[] directions = neighbours.get(point);
        if (directions == null)
            return () -> new Iterator<>() {
                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public MapSolver.Point next() {
                    return null;
                }
            };
        return () -> new Iterator<>() {
            private int index;

            @Override
            public boolean hasNext() {
                return this.index < directions.length;
            }

            @Override
            public MapSolver.Point next() {
                try {
                    return MapSolver.getImmediate(point, directions[this.index++]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new NoSuchElementException(e);
                }
            }
        };
    }

    private static double weight(MapSolver.Point current, MapSolver.Point neighbour) {
        return 1.0;
    }

    private static double defaultHeuristic(MapSolver.Point goal, MapSolver.Point node) {
        if (goal.equals(node))
            return 0d;
        double x = node.x() - goal.x(), y = node.y() - goal.y();
        return Math.abs(x) + Math.abs(y);
    }

    public static MapSolver.Point[] solve(MapSolver.Point start, MapSolver.Point goal, BiFunction<MapSolver.Point, MapSolver.Point, Double> heuristic, HashMap<MapSolver.Point, MapSolver.Direction[]> neighbours) {
        PriorityQueue<MapSolver.Point> openSet = new PriorityQueue<>(List.of(start));

        HashMap<MapSolver.Point, MapSolver.Point> cameFrom = new HashMap<>();
        HashMap<MapSolver.Point, Double> gScore = new HashMap<>(); // gScore default value is infinity

        gScore.put(start, 0d);

        HashMap<MapSolver.Point, Double> fScore = new HashMap<>(); // default value is infinity

        while (!openSet.isEmpty()) {
            MapSolver.Point current = openSet.stream().reduce((node1, node2) -> (fScore.getOrDefault(node1, Double.POSITIVE_INFINITY) <= fScore.getOrDefault(node2, Double.POSITIVE_INFINITY)) ? node1 : node2).get();
            if (current.equals(goal))
                return reconstructPath(cameFrom, current);
            openSet.remove(current);
            for (MapSolver.Point neighbour : getNeighbours(current, neighbours)) {
                double tentativeGScore = gScore.getOrDefault(current, Double.POSITIVE_INFINITY) + weight(current, neighbour);
                if (tentativeGScore < gScore.getOrDefault(neighbour, Double.POSITIVE_INFINITY)) {
                    cameFrom.put(neighbour, current);
                    gScore.put(neighbour, tentativeGScore);
                    fScore.put(neighbour, tentativeGScore + heuristic.apply(goal, neighbour));
                    if (!openSet.contains(neighbour))
                        openSet.add(neighbour);
                }
            }
        }

//        System.out.println("openSet=" + openSet + ",gScore=" + gScore + ",cameFrom=" + cameFrom + ",fScore=" + fScore);

        throw new IllegalArgumentException();
    }

    public static MapSolver.Point[] solve(MapSolver.Point start, MapSolver.Point goal, HashMap<MapSolver.Point, MapSolver.Direction[]> neighbours) {
        return solve(start, goal, AStarAlgorithm::defaultHeuristic, neighbours);
    }
}