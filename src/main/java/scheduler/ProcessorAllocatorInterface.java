package scheduler;

import java.util.List;

import models.Node;

/**
 * Created by helen on 28/07/2016.
 */
interface ProcessorAllocatorInterface {
    
	public boolean allocateProcessor(List<Node> schedule, Node node, List<Integer> checkedProcessors);
	
	public int findEarliestStartTime(List<Node> schedule, Node node, int processor);
	
	public int getNumberProcessors();
	
}
