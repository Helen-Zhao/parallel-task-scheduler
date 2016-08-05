package scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import models.Node;

/**
 * Implementation of depth first branch and bound scheduler using while loops
 * 
 * TODO : Consider branches where one node is assigned a different processor
 * @author Jay
 *
 */
public class DepthFirst_BaB_Scheduler implements SchedulerInterface {

	int bestBound = 0;
	List<Node> optimalSchedule;
	
	List<Node> schedule = new ArrayList<Node>();
	List<Integer> indexStack = new ArrayList<Integer>();
	
	ValidNodeFinderInterface nodeFinder;
	ProcessorAllocatorInterface processorAllocator;
	
	public List<Node> createSchedule(List<Node> nodeList) {
		
		// Bound initialized 
		for (Node node : nodeList) {
			bestBound += node.getWeight();
		}
		
		// Find initial set of available nodes
		List<Node> availableNodes = nodeFinder.findSatisfiedNodes(nodeList);
		
		// Current max weight of the schedule
		int currentBound = 0;
		// Level of the state tree
		int level = 0;
		
		Node node;
		
		// While all paths from level 0 have not been explored, continue finding paths
		while (level != -1) {
			
			// While there are nodes still to be allocated, continue to allocate
			// When there are no more available nodes, end of a path has been reached
			while(availableNodes.size() != 0) {
				// Get the index of the next node in the current level to explore from the available nodes
				int nextNodeIndex;
				try {
					nextNodeIndex = indexStack.get(level);
				} catch(ArrayIndexOutOfBoundsException e)  { // this level has not yet been reached
					// next node in new level is the first available node
					nextNodeIndex = 0;
					// add index of new level to list of indices
					indexStack.add(level, 0);
				}
				
				// Get the next available node
				try {
					node = availableNodes.get(nextNodeIndex);
				} catch (ArrayIndexOutOfBoundsException e) { // No new nodes available on this level
					// Reset index of level for new path
					indexStack.set(level, 0);
					// Return to previous level
					level--;
					// Remove node from the latest level, has no longer been run
					node = schedule.get(schedule.size()-1);
					node.setHasRun(false);
					schedule.remove(schedule.size()-1);
					// Reset list of available nodes to state of previous level
					// TODO Find more efficient way of resetting available nodes -- add previous node back into available (not at/after nextNodeIndex), remove children of previous node
					availableNodes = nodeFinder.findSatisfiedNodes(nodeList);
					continue;
				}
				
				// Update next node index in list of indices
				indexStack.set(level, nextNodeIndex++);
				
				// Allocate node to a processor
				// TODO consider branches with a node being assigned different processors
				processorAllocator.allocateProcessor(schedule, node, null);
				
				// Find when the newly allocated node finishes
				int nodeEndTime = node.getStartTime()+node.getWeight();
				
				// If end time of node is not greater than bound, there is another node in that finishes after the current node
				// Only update bound if the end time of node is later than the end time of all other nodes
				if (nodeEndTime > currentBound) {
					currentBound = nodeEndTime;
					
					// Check if current bound is worse than best known bound
					// If worse, abandon path
					if (currentBound > bestBound) {
						// Return to previous level
						level--;
						// Remove node from the latest level, has no longer been run
						node = schedule.get(schedule.size()-1);
						node.setHasRun(false);
						schedule.remove(schedule.size()-1);
						// Reset list of available nodes to state of previous level
						// TODO Find more efficient way of resetting available nodes -- add previous node back into available (not at/after nextNodeIndex), remove children of previous node
						availableNodes = nodeFinder.findSatisfiedNodes(nodeList);			
						continue;
					}
				}
				
				// Current path may be better than best known path
				// Add newly allocated node to current schedule
				schedule.add(node);
				
				// Update available nodes to new level
				// TODO Finds satisfied to guarantee list is same as original when reset, better to use findSatisfiedChildren if can reset to exact same list
				availableNodes = nodeFinder.findSatisfiedNodes(nodeList);
				
				// Increment to next level
				level++;
			}
			
			// End of a path is reached
			// If bound of new path is better than best known bound, update best bound and store new optimal schedule
			if (currentBound <= bestBound) { // TODO alter check to MasterSchedule for parallelization
				bestBound = currentBound;
				// TODO Is there a more memory/speed efficient way of keeping track of optimal schedule?  Map?
				// Clone current schedule -- clone to prevent alteratio
				optimalSchedule = schedule.stream().map(n -> n.clone()).collect(Collectors.toCollection(ArrayList::new));
			}
			
			// Return to previous level and find more paths
			level--;
			// Remove node from the latest level, has no longer been run
			node = schedule.get(schedule.size()-1);
			node.setHasRun(false);
			schedule.remove(schedule.size()-1);
			// Reset list of available nodes to state of previous level
			// TODO Find more efficient way of resetting available nodes -- add previous node back into available (not at/after nextNodeIndex), remove children of previous node
			availableNodes = nodeFinder.findSatisfiedNodes(nodeList);	
		}

		return optimalSchedule;
	}

	
	
}
