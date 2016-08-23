package scheduler;

import models.Node;
import models.NodeTuple;

import java.util.HashMap;
import java.util.List;

/**
 * Created by helen on 28/07/2016.
 */

public interface ProcessorAllocatorInterface {
    
	public void addNodeInfo(HashMap<String, NodeTuple> nodeInfo);
	
	public boolean allocateProcessor(List<Node> schedule, Node node);
	
	public int findEarliestStartTime(List<Node> schedule, Node node, int processor);
	
	public int getNumberProcessors();

	public void removeFromProcessor(Node node, int processor);
	
	public void addToProcessor(Node node, int processor);
	
	public int getEarliestProcessorEndTime();

}
