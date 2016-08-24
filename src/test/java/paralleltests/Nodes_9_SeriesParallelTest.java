package paralleltests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import main.Main;
import models.Edge;
import models.Node;
import models.NodeTuple;

public class Nodes_9_SeriesParallelTest {
	
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
    public void Nodes_9_SeriesParallel_TwoProc_Test() {

        int expectedEndTime = 55;

        String[] args = {dir + "/src/test/resources/dotfiles/input/Nodes_9_SeriesParallel.dot", "2", "-p", "4", "-o",
                "Nodes_9_SeriesParallel_TwoProc-output"};

        Main.main(args);
        HashMap<String, NodeTuple> optimalSchedule = Main.getOptimalSchedule();
        List<Node> nodeList = Main.getNodeList();
        int latestEndTime = endToEndCheck(nodeList, optimalSchedule);
        // Compare optimal time with calculated time
        assertEquals(expectedEndTime, latestEndTime);

    }

	
	public int endToEndCheck(List<Node> nodeList, HashMap<String, NodeTuple> optimalInfo) {
		int latestEndTime = 0;

		for (Node node : nodeList) {

			// Determine end time(duration) of schedule
			int currentEndTime = optimalInfo.get(node.getName()).getStartTime() + node.getWeight();
			if (currentEndTime > latestEndTime) {
				latestEndTime = currentEndTime;
			}

			List<Edge> incomingEdges = node.getIncomingEdges();
			for (Edge edge : incomingEdges) {
				Node startNode = edge.getStartNode();
				int startNodeProcessor = optimalInfo.get(startNode.getName()).getProcessor();
				int currentNodeProcessor = optimalInfo.get(node.getName()).getProcessor();
				int dependencySatisfiedTime;

				// Determine time dependency is completed
				// If node in same processor ignore communication weight
				if (startNodeProcessor == currentNodeProcessor) {
					dependencySatisfiedTime = optimalInfo.get(startNode.getName()).getStartTime()
							+ startNode.getWeight();
				} else {
					dependencySatisfiedTime = optimalInfo.get(startNode.getName()).getStartTime()
							+ startNode.getWeight() + edge.getWeight();
				}

				// If task starts before dependencies are completed, fail
				if (optimalInfo.get(node.getName()).getStartTime() < dependencySatisfiedTime) {
					fail();
				}

			}

			int startTime = optimalInfo.get(node.getName()).getStartTime();
			int endTime = startTime + node.getWeight();

			// Checks for overlaps in tasks
			// Compare with every other node whether they start during the
			// processing of this node, on same processor
			for (Node node2 : nodeList) {
				// Ignore comparison with self(same node)
				if (node2 != node) {
					if (optimalInfo.get(node.getName()).getProcessor() == optimalInfo.get(node2.getName())
							.getProcessor()) {
						int node2StartTime = optimalInfo.get(node2.getName()).getStartTime();
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
