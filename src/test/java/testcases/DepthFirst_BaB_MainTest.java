package testcases;

import main.Main;
import models.Edge;
import models.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

public class DepthFirst_BaB_MainTest {

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
    public void oneProcSimpleTest() {

        int expectedEndTime = 10;

        String[] args = {dir +
                "/src/test/resources/dotfiles/input/1_processor_simple.dot",
                "4", "-o", "1_processor_simple-output"};

        Main.main(args);
        List<Node> optimalSchedule = Main.getOptimalSchedule();
        int latestEndTime = endToEndCheck(optimalSchedule);
        // Compare optimal time with calculated time
        assertEquals(expectedEndTime, latestEndTime);
    }

    @Test
    public void twoProcSimpleTest() {

        int expectedEndTime = 6;

        String[] args = {dir +
                "/src/test/resources/dotfiles/input/2_processor_simple.dot",
                "4", "-o", "2_processor_simple-output"};

        Main.main(args);
        List<Node> optimalSchedule = Main.getOptimalSchedule();
        int latestEndTime = endToEndCheck(optimalSchedule);
        // Compare optimal time with calculated time
        assertEquals(expectedEndTime, latestEndTime);
    }

    @Test
    public void fourProcOneSrcOneDestTest() {

        int expectedEndTime = 10;

        String[] args = {dir +
                "/src/test/resources/dotfiles/input/4_processor_1_src_1_dest.dot",
                "4", "-o", "4_processor_1_src_1_dest-output"};

        Main.main(args);
        List<Node> optimalSchedule = Main.getOptimalSchedule();
        int latestEndTime = endToEndCheck(optimalSchedule);
        // Compare optimal time with calculated time
        assertEquals(expectedEndTime, latestEndTime);
    }

    @Test
    public void fourProcOneSrcThreeDestTest() {

        int expectedEndTime = 14;

        String[] args = {dir +
                "/src/test/resources/dotfiles/input/4_processor_1_src_3_dest.dot",
                "4", "-o", "4_processor_1_src_3_dest-output"};


        Main.main(args);
        List<Node> optimalSchedule = Main.getOptimalSchedule();
        int latestEndTime = endToEndCheck(optimalSchedule);
        // Compare optimal time with calculated time
        assertEquals(expectedEndTime, latestEndTime);

    }

    @Test
    public void fourProcThreeSrcOneDestTest() {

        int expectedEndTime = 9;

        String[] args = {dir + "/src/test/resources/dotfiles/input/4_processor_3_src_1_dest.dot", "4", "-o",
                "4_processor_3_src_1_dest-output"};

        Main.main(args);
        List<Node> optimalSchedule = Main.getOptimalSchedule();
        int latestEndTime = endToEndCheck(optimalSchedule);
        // Compare optimal time with calculated time
        assertEquals(expectedEndTime, latestEndTime);
    }

    @Test
    public void fourProcThreeSrcTwoDestTest() {

        int expectedEndTime = 10;

        String[] args = {dir + "/src/test/resources/dotfiles/input/4_processor_3_src_2_dest.dot", "4", "-o",
                "4_processor_3_src_2_dest-output"};

        Main.main(args);
        List<Node> optimalSchedule = Main.getOptimalSchedule();
        int latestEndTime = endToEndCheck(optimalSchedule);
        // Compare optimal time with calculated time
        assertEquals(expectedEndTime, latestEndTime);
    }

    @Test
    public void Nodes_7_OutTree_TwoProc_Test() {

        int expectedEndTime = 28;

        String[] args = {dir + "/src/test/resources/dotfiles/input/Nodes_7_OutTree.dot", "2", "-o",
                "Nodes_7_OutTree_TwoProc-output"};

        Main.main(args);
        List<Node> optimalSchedule = Main.getOptimalSchedule();
        int latestEndTime = endToEndCheck(optimalSchedule);
        // Compare optimal time with calculated time
        assertEquals(expectedEndTime, latestEndTime);

    }

    @Test
    public void Nodes_7_OutTree_FourProc_Test() {

        int expectedEndTime = 22;

        String[] args = {dir + "/src/test/resources/dotfiles/input/Nodes_7_OutTree.dot", "4", "-o",
                "Nodes_7_OutTree_FourProc-output"};

        Main.main(args);
        List<Node> optimalSchedule = Main.getOptimalSchedule();
        int latestEndTime = endToEndCheck(optimalSchedule);
        // Compare optimal time with calculated time
        assertEquals(expectedEndTime, latestEndTime);

    }

    @Test
    public void Nodes_8_Random_TwoProc_Test() {

        int expectedEndTime = 581;

        String[] args = {dir + "/src/test/resources/dotfiles/input/Nodes_8_Random.dot", "2", "-o",
                "Nodes_8_Random_TwoProc-output"};

        Main.main(args);
        List<Node> optimalSchedule = Main.getOptimalSchedule();
        int latestEndTime = endToEndCheck(optimalSchedule);
        // Compare optimal time with calculated time
        assertEquals(expectedEndTime, latestEndTime);

    }

    @Test
    public void Nodes_8_Random_FourProc_Test() {

        int expectedEndTime = 581;

        String[] args = {dir + "/src/test/resources/dotfiles/input/Nodes_8_Random.dot", "4", "-o",
                "Nodes_8_Random_FourProc-output"};

        Main.main(args);
        List<Node> optimalSchedule = Main.getOptimalSchedule();
        int latestEndTime = endToEndCheck(optimalSchedule);
        // Compare optimal time with calculated time
        assertEquals(expectedEndTime, latestEndTime);

    }

    @Test
    public void Nodes_9_SeriesParallel_TwoProc_Test() {

        int expectedEndTime = 55;

        String[] args = {dir + "/src/test/resources/dotfiles/input/Nodes_9_SeriesParallel.dot", "2", "-o",
                "Nodes_9_SeriesParallel_TwoProc-output"};

        Main.main(args);
        List<Node> optimalSchedule = Main.getOptimalSchedule();
        int latestEndTime = endToEndCheck(optimalSchedule);
        // Compare optimal time with calculated time
        assertEquals(expectedEndTime, latestEndTime);

    }

    @Test
    public void Nodes_9_SeriesParallel_FourProc_Test() {

        int expectedEndTime = 55;

        String[] args = {dir + "/src/test/resources/dotfiles/input/Nodes_9_SeriesParallel.dot", "4", "-o",
                "Nodes_9_SeriesParallel_FourProc-output"};

        Main.main(args);
        List<Node> optimalSchedule = Main.getOptimalSchedule();
        int latestEndTime = endToEndCheck(optimalSchedule);
        // Compare optimal time with calculated time
        assertEquals(expectedEndTime, latestEndTime);

    }

    @Test
    public void Nodes_10_Random_TwoProc_Test() {

        int expectedEndTime = 50;

        String[] args = {dir + "/src/test/resources/dotfiles/input/Nodes_10_Random.dot", "2", "-o",
                "Nodes_10_Random_TwoProc-output"};

        Main.main(args);
        List<Node> optimalSchedule = Main.getOptimalSchedule();
        int latestEndTime = endToEndCheck(optimalSchedule);
        // Compare optimal time with calculated time
        assertEquals(expectedEndTime, latestEndTime);

    }

    @Test
    public void Nodes_10_Random_FourProc_Test() {

        int expectedEndTime = 50;

        String[] args = {dir + "/src/test/resources/dotfiles/input/Nodes_10_Random.dot", "4", "-o",
                "Nodes_10_Random_FourProc-output"};

        Main.main(args);
        List<Node> optimalSchedule = Main.getOptimalSchedule();
        int latestEndTime = endToEndCheck(optimalSchedule);
        // Compare optimal time with calculated time
        assertEquals(expectedEndTime, latestEndTime);

    }

    @Test
    public void Nodes_11_OutTree_TwoProc_Test() {

        int expectedEndTime = 350;

        String[] args = {dir + "/src/test/resources/dotfiles/input/Nodes_11_OutTree.dot", "2", "-o",
                "Nodes_11_OutTree_TwoProc-output"};

        Main.main(args);
        List<Node> optimalSchedule = Main.getOptimalSchedule();
        int latestEndTime = endToEndCheck(optimalSchedule);
        // Compare optimal time with calculated time
        assertEquals(expectedEndTime, latestEndTime);

    }

    @Test
    public void Nodes_11_OutTree_FourProc_Test() {

        int expectedEndTime = 227;

        String[] args = {dir + "/src/test/resources/dotfiles/input/Nodes_11_OutTree.dot", "4", "-o",
                "Nodes_11_OutTree_FourProc-output"};

        Main.main(args);
        List<Node> optimalSchedule = Main.getOptimalSchedule();
        int latestEndTime = endToEndCheck(optimalSchedule);
        // Compare optimal time with calculated time
        assertEquals(expectedEndTime, latestEndTime);

    }

    public int endToEndCheck(List<Node> optimalSchedule) {
        int latestEndTime = 0;

        for (Node node : optimalSchedule) {

            // Determine end time(duration) of schedule
            int currentEndTime = node.getStartTime() + node.getWeight();
            if (currentEndTime > latestEndTime) {
                latestEndTime = currentEndTime;
            }

            List<Edge> incomingEdges = node.getIncomingEdges();
            for (Edge edge : incomingEdges) {
                Node startNode = edge.getStartNode();
                int startNodeProcessor = startNode.getProcessor();
                int currentNodeProcessor = node.getProcessor();
                int dependencySatisfiedTime;

                // Determine time dependency is completed
                // If node in same processor ignore communication weight
                if (startNodeProcessor == currentNodeProcessor) {
                    dependencySatisfiedTime = startNode.getStartTime() + startNode.getWeight();
                } else {
                    dependencySatisfiedTime = startNode.getStartTime() + startNode.getWeight() + edge.getWeight();
                }

                // If task starts before dependencies are completed, fail
                if (node.getStartTime() < dependencySatisfiedTime) {
                    fail();
                }

            }

            int startTime = node.getStartTime();
            int endTime = startTime + node.getWeight();

            // Checks for overlaps in tasks
            // Compare with every other node whether they start during the
            // processing of this node, on same processor
            for (Node node2 : optimalSchedule) {
                // Ignore comparison with self(same node)
                if (node2 != node) {
                    if (node2.getProcessor() == node.getProcessor()) {
                        int node2StartTime = node2.getStartTime();
                        if ((node2StartTime > startTime) && (node2StartTime < endTime)) {
                            fail();
                        }
                    }
                }
            }
        }

        return latestEndTime;
    }
}
