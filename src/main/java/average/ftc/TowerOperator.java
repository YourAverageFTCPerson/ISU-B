package average.ftc;

import javafx.scene.image.ImageView;

import java.util.Iterator;
import java.util.Vector;

public interface TowerOperator {
    Vector<ImageView> alreadyAttacking = new Vector<>(); // Vectors are synchronized as opposed to ArrayLists

    int x();

    int y();

    default void update() {
        System.out.println("update()");
        synchronized (ActualGame.getMap()) {
            Iterator<ImageView> enemies = ActualGame.getEnemies();
            System.out.println("update(): enemies: " + enemies);
            if (!enemies.hasNext()) {
                ActualGame.onAllEnemiesRemoved();
                return;
            }
            ImageView closest = null;
            double closestDistance = Double.POSITIVE_INFINITY;
            double fromX = this.x() * MapLoader.getXScale();
            double fromY = this.y() * MapLoader.getYScale();
            do {
                ImageView enemy = enemies.next();
//                System.out.println(enemy);
                double x = fromX - enemy.getX(), y = fromY - enemy.getY(), distance = Math.sqrt(x * x + y * y);
                double probabilityOfAcquiringCloserTarget = probabilityOfAcquiringCloserTarget();

                if (Double.isNaN(probabilityOfAcquiringCloserTarget) || probabilityOfAcquiringCloserTarget > 1d
                        || probabilityOfAcquiringCloserTarget < 0d)
                    throw new IllegalArgumentException("the probability: "
                            + probabilityOfAcquiringCloserTarget
                            + " is not valid. (not real between 0 and 1)");

                if (distance > this.range()) {
                    System.out.println("Condition 1: distance > this.range()");
                }
                if (distance >= closestDistance) {
                    System.out.println("Condition 2: distance >= closestDistance");
                }
                if (ActualGame.RANDOM.nextDouble() > probabilityOfAcquiringCloserTarget) {
                    System.out.println("Condition 3: ActualGame.RANDOM.nextDouble() > probabilityOfAcquiringCloserTarget");
                }
                if (alreadyAttacking.contains(enemy)) {
                    System.out.println("Condition 4: alreadyAttacking.contains(enemy)");
                }

                if (distance > this.range()
                        || distance >= closestDistance
                        || ActualGame.RANDOM.nextDouble() > probabilityOfAcquiringCloserTarget
                        || alreadyAttacking.contains(enemy)) {
                    System.out.println("continuing");
                    continue;
                }
                closestDistance = distance;
                closest = enemy;
            } while (enemies.hasNext());
            if (Double.isInfinite(closestDistance)) return;
            alreadyAttacking.add(closest);
            Utils.shootAt(fromX, fromY, closest);
        }
    }

    double probabilityOfAcquiringCloserTarget();

    double range();
}
