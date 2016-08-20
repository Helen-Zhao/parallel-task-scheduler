package scheduler;

import models.Node;

import java.util.List;

/**
 * Created by helen on 28/07/2016.
 */

public interface ProcessorAllocatorInterface {
    
	public boolean allocateProcessor(List<Node> schedule, Node node, List<Integer> checkedProcessors);
	
	public int findEarliestStartTime(List<Node> schedule, Node node, int processor);
	
	public int getNumberProcessors();

	public void removeFromProcessor(Node node, int processor);
	
	public void addToProcessor(Node node, int processor);
	
	public int getEarliestProcessorEndTime();

}
