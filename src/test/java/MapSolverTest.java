import average.ftc.Main;
import average.ftc.MapSolver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapSolverTest {
    @BeforeAll
    public static void initialize() throws ClassNotFoundException {
        Class.forName(Main.class.getName());
    }

    @Test
    public void testGetValidPaths() {
        assertEquals(List.of(new MapSolver.Point("#################".length(), 0), new MapSolver.Point("##################".length(), 0), new MapSolver.Point("##################".length(), 1)), List.of(MapSolver.getFastestPath("""
                ###############  S  ########\s
                ############### # G##########
                ###############    ##########
                """)));
    }
}
