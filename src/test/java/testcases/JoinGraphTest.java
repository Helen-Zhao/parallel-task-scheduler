package testcases;

import main.Main;
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
}
