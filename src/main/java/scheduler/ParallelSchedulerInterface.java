package scheduler;

import java.util.List;
import java.util.Queue;

import models.Node;

public interface ParallelSchedulerInterface extends SchedulerInterface {
	
	public void initiateNewSubtree(List<Node> nodes, List<Queue<Node>> initialNodeStack, int initialBestBound);
	
}
