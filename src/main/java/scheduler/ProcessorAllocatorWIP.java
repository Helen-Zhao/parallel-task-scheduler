package scheduler;

import java.util.List;

import models.Node;
import models.Edge;

/**
 * Created by helen on 28/07/2016.
 */
public class ProcessorAllocatorWIP implements ProcessorAllocatorInterface {

	int numProcessors;
	
	public ProcessorAllocatorWIP(int numProcessors) {
		this.numProcessors = numProcessors;
	}
	
	public boolean allocateProcessor(List<Node> schedule, Node node, List<Integer> unavailableProcessors) {
		// There are no available processors for which the node can be assigned to.
		if (unavailableProcessors.size() == numProcessors) {
			return false;
		}
		
		int earliestStartTime = Integer.MAX_VALUE, bestProcessor = -1;
		int tempEarliestStartTime;
		
		// Go through every processor and find the best time available for each one
		for (int i=1; i<=numProcessors; i++) {
			// If the specified processor is available, then find the best time for the specified processor
			if (!unavailableProcessors.contains(i)) {
				tempEarliestStartTime = findEarliestStartTime(schedule, node, i);

				// Determining and updating the earliest possible start time and respective processor
				if (tempEarliestStartTime < earliestStartTime) {
					earliestStartTime = tempEarliestStartTime;
					bestProcessor = i;
				}
			}
		}
		
		// Allocates the start time, which processor to use, and sets it to have run as it will be placed into the Scheduler.
		node.setStartTime(earliestStartTime);
		node.setProcessor(bestProcessor);
		node.setHasRun(true);
		
		// Node has been assigned a Processor and startTime 
		return true;
	}

	public int findEarliestStartTime(List<Node> schedule, Node node, int processor) {
		List<Edge> dependencies = node.getIncomingEdges();
		int earliestValidStart = 0;
		int earliestValidEnd = node.getWeight();
		
		int foundStartTime = 0;
		int foundEndTime = 0;
		
		// Assumed all dependencies are satisfied
		// Find earliest start based on dependencies
		for (Edge edge : dependencies) {
			if (edge.getStartNode().getProcessor() == processor) {
				// Start new node after dependency
				int earliestDependencyStart = edge.getStartNode().getStartTime() + edge.getStartNode().getWeight();
				int earliestDependencyEnd = earliestDependencyStart + node.getWeight();
				if (earliestDependencyStart > earliestValidStart) {
					earliestValidStart = earliestDependencyStart;
					earliestValidEnd = earliestDependencyEnd;
				}
			} else {
				// Start new node after dependency and comms time
				int earliestDependencyStart = edge.getStartNode().getStartTime() + edge.getStartNode().getWeight() + edge.getWeight();
				int earliestDependencyEnd = earliestValidStart + node.getWeight();
				if (earliestDependencyStart > earliestValidStart) {
					earliestValidStart = earliestDependencyStart;
					earliestValidEnd = earliestDependencyEnd;
				}
			}
		}
		
		boolean startChanged = true;
		while (startChanged) {
			startChanged = false;
			// Find earliest start based on allocated nodes
			for (Node snode : schedule) {
				// If scheduled node is in another processor we don't care when it starts
				if (snode.getProcessor() == processor) {
					foundStartTime = snode.getStartTime();
					foundEndTime = foundStartTime + snode.getWeight();
					// Current earliest valid start time overlaps another task
					if (earliestValidStart >= foundStartTime && earliestValidStart < foundEndTime) {
						// Start new node after found node
						earliestValidStart = foundEndTime;
						earliestValidEnd = earliestValidStart + node.getWeight();
						startChanged = true;
					// Current earliest valid end time overlaps another task
					} else if (earliestValidEnd > foundStartTime && earliestValidEnd <= foundEndTime) {
						earliestValidStart = foundEndTime;
						earliestValidEnd = earliestValidStart + node.getWeight();
						startChanged = true;
					// Current allocated times are enveloping another task
					} else if (foundStartTime >= earliestValidStart && foundStartTime < earliestValidEnd) {
						earliestValidStart = foundEndTime;
						earliestValidEnd = earliestValidStart + node.getWeight();
						startChanged = true;
					}
				}
			}
		}
		
		return earliestValidStart;
	}

	public int getNumberProcessors() {
		return numProcessors;
	}

	@Override
	public boolean allocateProcessor(List<Node> schedule, Node node, int processorIndex) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
