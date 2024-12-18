import average.ftc.Main;
import average.ftc.MapSolver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MapSolverTest {
    @BeforeAll
    public static void initialize() {
        Main.doNothing();
    }

    @Test
    public void testGetValidPaths() {
        MapSolver.getFastestPath("""
                ################## #########\s
                ################## ##########
                #############################
              \s""");
    }
}
