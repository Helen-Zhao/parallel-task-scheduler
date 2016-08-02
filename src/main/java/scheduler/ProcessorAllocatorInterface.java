package scheduler;

import java.util.ArrayList;
import java.util.List;

import models.Node;

/**
 * Created by helen on 28/07/2016.
 */
interface ProcessorAllocatorInterface {
    
	public boolean allocateProcessor(ArrayList<Node> schedule, Node node, List<Integer> checkedProcessors);
	
}
