package scheduler;

import models.Edge;
import models.Node;
import models.NodeTuple;
import models.Processor;

import java.util.ArrayList;
import java.util.HashMap;
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
    
    HashMap<String, NodeTuple> nodeInfo;

    public ProcessorAllocator(int numProcessors) {
        this.numProcessors = numProcessors;
        this.processors = new ArrayList<Processor>(numProcessors);
        for (int i = 0; i < numProcessors; i++) {
        	processors.add(new Processor());
        }
    }
    
	public void addNodeInfo(HashMap<String, NodeTuple> nodeInfo) {
		this.nodeInfo = nodeInfo;
	}

    public boolean allocateProcessor(List<Node> schedule, Node node) {
        // There are no available processors for which the node can be assigned to.
        if (nodeInfo.get(node.getName()).getCheckedProcessors().size() >= numProcessors) {
            return false;
        }

        int earliestStartTime = Integer.MAX_VALUE;
        int bestProcessor = -1;
        int tempEarliestStartTime;

        // Go through every processor and find the best time available for each one
        for (int i = 1; i <= numProcessors; i++) {
            if (!nodeInfo.get(node.getName()).getCheckedProcessors().contains(i)) {
                // If the specified processor is available, then find the best time for the specified processor
                tempEarliestStartTime = findEarliestStartTime(schedule, node, i);

                // Determining and updating the earliest possible start time and respective processor
                if (tempEarliestStartTime < earliestStartTime) {
                    earliestStartTime = tempEarliestStartTime;
                    bestProcessor = i;
                } else if (tempEarliestStartTime == earliestStartTime && processors.get(bestProcessor - 1).isEmpty() && processors.get(i - 1).isEmpty()) {
					// If equal each placement mirrors the other, pre-emptively regard as checked
                	nodeInfo.get(node.getName()).addCheckedProcessor(i);

				}
            }
        }

        // Allocates the start time, which processor to use, and sets it to have run as it will be placed into the Scheduler.
        nodeInfo.get(node.getName()).addCheckedProcessor(bestProcessor);
        nodeInfo.get(node.getName()).setStartTime(earliestStartTime);
        nodeInfo.get(node.getName()).setProcessor(bestProcessor);
        nodeInfo.get(node.getName()).setHasRun(true);
        processors.get(bestProcessor-1).addNode(node, nodeInfo.get(node.getName()));

        // Node has been assigned a Processor and startTime
        return true;
    }

    public int findEarliestStartTime(List<Node> schedule, Node node, int processor) {
        List<Edge> dependencies = node.getIncomingEdges();
        int earliestValidStart = 0;
        int earliestValidEnd = node.getWeight();

        // Assumed all dependencies are satisfied
        // Find earliest start based on dependencies
        for (Edge edge : dependencies) {
            if (nodeInfo.get(edge.getStartNode().getName()).getProcessor() == processor) {
                // Start new node after dependency
                int earliestDependencyStart = nodeInfo.get(edge.getStartNode().getName()).getStartTime() + edge.getStartNode().getWeight();
                if (earliestDependencyStart > earliestValidStart) {
                    earliestValidStart = earliestDependencyStart;
                    earliestValidEnd = earliestValidStart + node.getWeight();
                }
            } else {
                // Start new node after dependency and comms time
                int earliestDependencyStart = nodeInfo.get(edge.getStartNode().getName()).getStartTime() + edge.getStartNode().getWeight() + edge.getWeight();
                if (earliestDependencyStart > earliestValidStart) {
                    earliestValidStart = earliestDependencyStart;
                    earliestValidEnd = earliestValidStart + node.getWeight();
                }
            }
        }

        earliestValidStart = processors.get(processor - 1).findEarliestStartTime(node, earliestValidStart, earliestValidEnd);

        return earliestValidStart;
    }
    
    public void addToProcessor(Node node, int p) {
    	// Processors shouldn't equal 0 or below if assigned
    	if (p > 0) {
    		processors.get(p - 1).addNode(node, nodeInfo.get(node.getName()));
    	}
    }
    
    public void removeFromProcessor(Node node, int p) {
    	// Processors shouldn't equal 0 or below if assigned
    	if (p > 0) {
    		processors.get(p - 1).removeNode(node, nodeInfo.get(node.getName()));
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