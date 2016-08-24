package scheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Queue;

import models.Node;
import models.NodeTuple;

public interface ParallelSchedulerInterface extends SchedulerInterface {
	
	public void initiateNewSubtree(List<Node> nodes, ProcessorAllocatorInterface processorAllocator, List<Queue<Node>> initialNodeStack, int initialBestBound, HashMap<String, NodeTuple> scheduleInfo, List<Node> scheduledNodes);
	
	public boolean setBestBound(int newBestBound);
}
