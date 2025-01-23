package average.ftc;

import javafx.scene.Node;

import java.util.List;

public record TierOne(List<Node> map, int x, int y) implements TowerOperator {
    @Override
    public double probabilityOfAcquiringCloserTarget() {
        return 0.999;
    }

    @Override
    public double range() {
        return 10000d;
    }
}
