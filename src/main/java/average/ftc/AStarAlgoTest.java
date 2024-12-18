package average.ftc;

import jdk.jshell.tool.JavaShellToolBuilder;

import java.io.File;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;

public class AStarAlgoTest {
    public static void main(String[] args) throws Exception {
//        File temp = new File("temp.jsh");
//        temp.deleteOnExit();
//        temp.createNewFile();
//        try (PrintStream s = new PrintStream(temp)) {
//            s.println("import average.ftc.*");
//        }
//        System.exit(JavaShellToolBuilder.builder().start("temp.jsh", "--execution", "local"));
        System.out.println(Arrays.toString(AStarAlgorithm.solve(new MapSolver.Point(1, 0), new MapSolver.Point(2, 0), new HashMap<>() {{
            put(new MapSolver.Point(1, 0), new MapSolver.Direction[]{MapSolver.Direction.RIGHT});
            put(new MapSolver.Point(2, 0), new MapSolver.Direction[]{MapSolver.Direction.LEFT});
        }})));
    }
}
