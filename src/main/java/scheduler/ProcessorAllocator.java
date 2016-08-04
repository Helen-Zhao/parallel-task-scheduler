package scheduler;

import java.util.List;

import models.Node;

/**
 * Created by helen on 28/07/2016.
 */
public class ProcessorAllocator implements ProcessorAllocatorInterface {

	int numProcessors;
	
	public ProcessorAllocator(int numProcessors) {
		this.numProcessors = numProcessors;
	}
	
	public boolean allocateProcessor(List<Node> schedule, Node node, List<Integer> unavailableProcessors) {
		// TODO Auto-generated method stub
		return false;
	}

	public int findEarliestStartTime(List<Node> schedule, Node node, int processor) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getNumberProcessors() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
