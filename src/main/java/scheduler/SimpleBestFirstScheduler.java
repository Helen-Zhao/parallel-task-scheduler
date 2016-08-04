package scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import models.Node;

/**
 * Created by helen on 28/07/2016.
 */
public class SimpleBestFirstScheduler implements SchedulerInterface{

	private int upperBound = 0;
	ArrayList<Node> optimalSchedule;
	
	ValidNodeFinderInterface nodeFinder;
	ProcessorAllocatorInterface processorAllocator;
	
	public SimpleBestFirstScheduler(ValidNodeFinderInterface nodeFinder, ProcessorAllocatorInterface processorAllocator) {
		this.nodeFinder = nodeFinder;
		this.processorAllocator = processorAllocator;
	}
	
	public List<Node> createSchedule(List<Node> nodeList) {
		
		for(Node node : nodeList) {
			upperBound += node.getWeight();
		}
		
		List<Node> availableNodes = nodeFinder.findRootNodes(nodeList);
		Collections.sort(availableNodes);
		
		buildSchedule(availableNodes, new ArrayList<Node>(), upperBound);
		
		return optimalSchedule;
	}

	private void buildSchedule(List<Node> availableNodes, ArrayList<Node> currentSchedule,
			int currentWeight) {
		
		if(availableNodes.size() == 0 && currentWeight <= upperBound) {
			upperBound = currentWeight;
			optimalSchedule = currentSchedule;
			return;
		}
		
		List<Integer> checkedProcessors = new ArrayList<Integer>();
		
		for (Node node : availableNodes) {
			while (processorAllocator.allocateProcessor(currentSchedule, node, checkedProcessors)) {
			
				int newWeight = node.getStartTime()+node.getWeight();
				checkedProcessors.add(node.getProcessor());
				
				if(newWeight > currentWeight) {
					currentWeight = newWeight;
					if(currentWeight < upperBound) {
						currentSchedule.add(node);
			
						List<Node> newAvailableNodes = new ArrayList<Node>(availableNodes);
						newAvailableNodes.addAll(nodeFinder.findSatisfiedChildren(node));
						newAvailableNodes.remove(node);
						Collections.sort(newAvailableNodes);
			
						buildSchedule(newAvailableNodes, currentSchedule, currentWeight);
					}
				}
				
			}
			
			currentSchedule.remove(node);
			checkedProcessors.clear();
			node.setStartTime(-1);
			node.setProcessor(-1);
			node.setHasRun(false);
		}
		
	}
		
}
