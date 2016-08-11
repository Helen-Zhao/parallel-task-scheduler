package scheduler;

import models.Node;

import java.util.List;

/**
 * Created by helen on 28/07/2016.
 */

public interface ProcessorAllocatorInterface {
    
	public boolean allocateProcessor(List<Node> schedule, Node node, int processorIndex);
	
	public int findEarliestStartTime(List<Node> schedule, Node node, int processor);
	
	public int getNumberProcessors();

}
