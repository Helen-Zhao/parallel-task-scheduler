package scheduler;

import java.util.ArrayList;
import java.util.List;

import models.Node;

/**
 * Created by helen on 28/07/2016.
 */
public class Scheduler implements SchedulerInterface{

	private int upperBound = 0;
	
	ArrayList<Node> currentSchedule;
	ArrayList<Node> optimalSchedule; 
	ArrayList<Node> unallocatedNodes;
	ArrayList<Node> availableNodes;
	
	NodeFinderInterface nodeFinder = new NodeFinder();
	ProcessorAllocatorInterface processorAllocator = new ProcessorAllocator();
	
	public List<Node> createSchedule(List<Node> nodeList) {
		
		for(Node node : nodeList) {
			upperBound += node.getWeight();
		}
		
		unallocatedNodes = new ArrayList<Node>(nodeList);
		
		// To be implemented
		
		availableNodes = nodeFinder.findRootNodes(unallocatedNodes);
		
		int sumWeight = 0;
		retrievePossibleSchedules(availableNodes, currentSchedule, sumWeight);
		
		return null;
	}

	private void retrievePossibleSchedules(ArrayList<Node> availableNodes2, ArrayList<Node> currentSchedule2,
			int currentWeight) {
		// TODO Auto-generated method stub
		
		if(availableNodes2.size() == 0 && currentWeight < upperBound) {
			upperBound = currentWeight;
			optimalSchedule = currentSchedule2;
			return;
		}
		
		List<Integer> blacklist = new ArrayList<Integer>();
		
		for (Node node : availableNodes2) {
			while (processorAllocator.selectProcessor(currentSchedule2, node, blacklist)) {
			
				int newWeight = node.getStartTime()+node.getWeight();
				blacklist.add(node.getProcessor());
				
				if(newWeight > currentWeight) {
					currentWeight = newWeight;
					if(currentWeight < upperBound) {
						currentSchedule2.add(node);
			
						ArrayList<Node> availableNodes3 = new ArrayList(availableNodes2);
						availableNodes3.add(nodeFinder.findNodes(node));
						availableNodes3.remove(node);
			
						retrievePossibleSchedules(availableNodes3, currentSchedule2, currentWeight);
					}
				}
				
			}
			
			currentSchedule2.remove(node);
		}
		
	}
		
}
