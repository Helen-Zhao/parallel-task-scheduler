package testcases;

import main.Main;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Created by helen on 20/08/16.
 */
public class JoinGraphTest {
    String dir;

    @Before
    public void setUp() {
        boolean testingExecutionTime = false;
        Assume.assumeTrue(testingExecutionTime);
        String workingDir = System.getProperty("user.dir");
        dir = ".";

        if (workingDir.length() > 0 && workingDir.contains("src")) {
            dir = workingDir.substring(0, workingDir.indexOf(File.separator + "src"));
        }
    }


    @Test
    public void testJoinGraph5() {

        String[] args = {dir +
                "/src/test/resources/dotfiles/input/join-graph-5.dot",
                "4"};

        Main.main(args);

    }

    @Test
    public void testJoinGraph6() {

        String[] args = {dir +
                "/src/test/resources/dotfiles/input/join-graph-6.dot",
                "4"};

        Main.main(args);

    }

    @Test
    public void testJoinGraph7() {

        String[] args = {dir +
                "/src/test/resources/dotfiles/input/join-graph-7.dot",
                "4"};

        Main.main(args);

    }

    @Test
    public void testJoinGraph8() {

        String[] args = {dir +
                "/src/test/resources/dotfiles/input/join-graph-8.dot",
                "4"};

        Main.main(args);

    }
}
