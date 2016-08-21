package scheduler;

import models.Edge;
import models.Node;
import models.Processor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Henry Wu
 *
 * ProcessorAllocator is an implementation of the ProcessorAllocatorInterface.
 * ProcessorAllocator takes a node, the current schedule, a list of checked processors and the number
 * of processors to use and returns the processor with the earliest start time for that node
 */
public class ProcessorAllocator implements ProcessorAllocatorInterface {

    int numProcessors;
    List<Processor> processors;

    public ProcessorAllocator(int numProcessors) {
        this.numProcessors = numProcessors;
        this.processors = new ArrayList<Processor>(numProcessors);
        for (int i = 0; i < numProcessors; i++) {
        	processors.add(new Processor());
        }
    }

    public boolean allocateProcessor(List<Node> schedule, Node node, List<Integer> checkedProcessors) {
        // There are no available processors for which the node can be assigned to.
        if (checkedProcessors.size() >= numProcessors) {
            return false;
        }

        int earliestStartTime = Integer.MAX_VALUE;
        int bestProcessor = -1;
        int tempEarliestStartTime;

        // Go through every processor and find the best time available for each one
        for (int i = 1; i <= numProcessors; i++) {
            if (!checkedProcessors.contains(i)) {
                // If the specified processor is available, then find the best time for the specified processor
                tempEarliestStartTime = findEarliestStartTime(schedule, node, i);

                // Determining and updating the earliest possible start time and respective processor
                if (tempEarliestStartTime < earliestStartTime) {
                    earliestStartTime = tempEarliestStartTime;
                    bestProcessor = i;
                } else if (tempEarliestStartTime == earliestStartTime && processors.get(bestProcessor - 1).isEmpty() && processors.get(i - 1).isEmpty()) {
					// If equal each placement mirrors the other, pre-emptively regard as checked
					node.addCheckedProcessor(i);
				}
            }
        }

        // Allocates the start time, which processor to use, and sets it to have run as it will be placed into the Scheduler.
        node.addCheckedProcessor(bestProcessor);
        node.setStartTime(earliestStartTime);
        node.setProcessor(bestProcessor);
        node.setHasRun(true);
        processors.get(bestProcessor - 1).addNode(node);

        // Node has been assigned a Processor and startTime
        return true;
    }

    public int findEarliestStartTime(List<Node> schedule, Node node, int i) {
        List<Edge> dependencies = node.getIncomingEdges();
        int earliestValidStart = 0;
        int earliestValidEnd = node.getWeight();

        // Assumed all dependencies are satisfied
        // Find earliest start based on dependencies
        for (Edge edge : dependencies) {
            if (edge.getStartNode().getProcessor() == i) {
                // Start new node after dependency
                int earliestDependencyStart = edge.getStartNode().getStartTime() + edge.getStartNode().getWeight();
                if (earliestDependencyStart > earliestValidStart) {
                    earliestValidStart = earliestDependencyStart;
                    earliestValidEnd = earliestValidStart + node.getWeight();
                }
            } else {
                // Start new node after dependency and comms time
                int earliestDependencyStart = edge.getStartNode().getStartTime() + edge.getStartNode().getWeight() + edge.getWeight();
                if (earliestDependencyStart > earliestValidStart) {
                    earliestValidStart = earliestDependencyStart;
                    earliestValidEnd = earliestValidStart + node.getWeight();
                }
            }
        }

        earliestValidStart = processors.get(i - 1).findEarliestStartTime(node, earliestValidStart, earliestValidEnd);

        return earliestValidStart;
    }
    
    public void addToProcessor(Node node, int p) {
    	
    	if (p > -1) {
    		processors.get(p - 1).addNode(node);
    	}
    	
    }
    
    public void removeFromProcessor(Node node, int p) {
    	
    	if (p > -1) {
    		processors.get(p - 1).removeNode(node);
    	}
    	
    }
    
    public int getEarliestProcessorEndTime() {
    	int endTime = Integer.MAX_VALUE;
    	for (int i = 0; i < processors.size(); i++) {
    		int pEndTime = processors.get(i).getEndTime();
    		if (pEndTime < endTime) {
    			endTime = pEndTime;
    		}
    	}
    	return endTime;
    }

    public int getNumberProcessors() {
        return numProcessors;
    }
}