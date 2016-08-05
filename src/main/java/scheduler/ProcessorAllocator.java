package scheduler;

import java.util.ArrayList;

import java.util.List;

import models.Node;
import models.Edge;

/**
 * Created by helen on 28/07/2016.
 */
public class ProcessorAllocator implements ProcessorAllocatorInterface {

	int numProcessors;
	
	public ProcessorAllocator(int numProcessors) {
		this.numProcessors = numProcessors;
	}
	
	public boolean allocateProcessor(List<Node> schedule, Node node, List<Integer> unavailableProcessors) {
		// There are no available processors for which the node can be assigned to.
		if (unavailableProcessors.size() == numProcessors) {
			return false;
		}
		
		int earliestStartTime = -1, bestProcessor = -1;
		int tempEarliestStartTime;
		
		// Go through every processor and find the best time available for each one
		for (int i=1; i<=numProcessors; i++) {
			// If the specified processor is available, then find the best time for the specified processor
			if (!unavailableProcessors.contains(i)) {
				// The last/maximum time will be the latest 
				tempEarliestStartTime = findEarliestStartTime(schedule, node, i);
				// Initialises the first earliestStartTime as the best with its respective processor,
				// Which is then used to compare with the other earliestStartTime's in the other processors
				if (earliestStartTime == -1) {
					earliestStartTime = tempEarliestStartTime;
					bestProcessor = i;
					// Determining and updating the earliest possible start time and respective processor
				} else if (tempEarliestStartTime < earliestStartTime) {
					earliestStartTime = tempEarliestStartTime;
					bestProcessor = i;
				}
			}
		}
		
		// Allocates the start time, which processor to use, and sets it to have run as it will be placed into the Scheduler.
		node.setStartTime(earliestStartTime);
		node.setProcessor(bestProcessor);
		node.setHasRun(true);
		
		// Makes the processor which we just assigned into the scheduler unavailable (Do I do want to do this here?)
		//unavailableProcessors.add(bestProcessor);
		
		// Determines if there are still available processors which can be used
		if (unavailableProcessors.size() == numProcessors) {
			return false;
		} 
		else {
			return true;
		}
	}

	public int findEarliestStartTime(List<Node> schedule, Node node, int processor) {
		List<Edge> edges = node.getIncomingEdges();
		int longestEndTime = 0;
		int endTime, dependentNodeProc;
		
		// Comparison with already scheduled items for finding minimum starting time
		for (Node scheduledNode : schedule) {
			
			// Loops through all the dependency nodes
			for (Edge edge : edges) {
				Node dependentNode = edge.getStartNode();
				dependentNodeProc = dependentNode.getProcessor();
				
				// If  the dependent node occurs in the specified processor
				if((dependentNode.equals(scheduledNode)) && (dependentNodeProc == processor)) {
					// No communication time required
					endTime = dependentNode.getStartTime() + dependentNode.getWeight();
					if (endTime > longestEndTime) {
						longestEndTime = endTime;
					}
				} 
				// If the dependent node does not occur in the specified processor
				else if ((dependentNode.equals(scheduledNode)) && (dependentNodeProc != processor)) {
					// Communication time is required
					endTime = dependentNode.getStartTime() + dependentNode.getWeight() + edge.getWeight();
					if (endTime > longestEndTime) {
						longestEndTime = endTime;
					}
				}
				// If something is already scheduled in the specified processor, then minimum starting time can occur immediately after, as there is no dependency
				else if (scheduledNode.getProcessor() == processor) {
					endTime = scheduledNode.getStartTime() + scheduledNode.getWeight();
					if (endTime > longestEndTime) {
						longestEndTime = endTime;
					}
				}
			}
		}
		return longestEndTime;
	}

	public int getNumberProcessors() {
		return numProcessors;
	}
	
}
