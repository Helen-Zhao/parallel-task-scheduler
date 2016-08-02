package scheduler;

import java.util.ArrayList;
import java.util.List;

import models.Node;

/**
 * Created by helen on 28/07/2016.
 */
public class Scheduler implements SchedulerInterface{

	private int upperBound = 0;
	ArrayList<Node> optimalSchedule;
	
	ValidNodeFinderInterface nodeFinder;
	ProcessorAllocatorInterface processorAllocator;
	
	public Scheduler(ValidNodeFinderInterface nodeFinder, ProcessorAllocatorInterface processorAllocator) {
		this.nodeFinder = nodeFinder;
		this.processorAllocator = processorAllocator;
	}
	
	public List<Node> createSchedule(List<Node> nodeList) {
		
		for(Node node : nodeList) {
			upperBound += node.getWeight();
		}
		
		retrievePossibleSchedules(nodeFinder.findRootNodes(nodeList), new ArrayList<Node>(), upperBound);
		
		return optimalSchedule;
	}

	private void retrievePossibleSchedules(ArrayList<Node> availableNodes, ArrayList<Node> currentSchedule,
			int currentWeight) {
		// TODO Auto-generated method stub
		
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
			
						ArrayList<Node> newAvailableNodes = new ArrayList(availableNodes);
						newAvailableNodes.add(nodeFinder.checkDependentNodes(node));
						newAvailableNodes.remove(node);
			
						retrievePossibleSchedules(newAvailableNodes, currentSchedule, currentWeight);
					}
				}
				
			}
			
			currentSchedule.remove(node);
			node.setStartTime(-1);
			node.setProcessor(-1);
		}
		
	}
		
}
