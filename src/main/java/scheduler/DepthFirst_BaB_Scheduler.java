package scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import models.Node;

/**
 * Implementation of depth first branch and bound scheduler using while loops
 * Optimized to reduce memory use through minimal persistent memory objects
 * 
 * @author Jay
 *
 */
public class DepthFirst_BaB_Scheduler implements SchedulerInterface {

	int currentBound = 0;
	int bestBound = 0;
	
	List<Node> nodeList;
	List<Node> schedule = new ArrayList<Node>();
	List<Node> optimalSchedule = new ArrayList<Node>();
	
	int level = 0;
	List<Queue<Node>> nodeStack;
	
	Node node;
	ValidNodeFinderInterface nodeFinder;
	ProcessorAllocatorInterface processorAllocator;
	
	public DepthFirst_BaB_Scheduler(ValidNodeFinderInterface nodeFinder, ProcessorAllocatorInterface processAllocator) {
		  this.nodeFinder = nodeFinder;
		  this.processorAllocator = processAllocator;
	}
	
	@Override
	public List<Node> createSchedule(List<Node> nodes) {
		
		if (nodes.size() == 0) return new ArrayList<Node>();
		
		// Initialize availability
		nodeList = nodes;
		
		for (Node n : nodeList) {
			bestBound += n.getWeight();
		}
		
		nodeStack = new ArrayList<Queue<Node>>(nodeList.size()+1);
		
		// Initialize nodeStack
		for (int i = 0; i < nodeList.size()+1; i++) {
			nodeStack.add(null);
		}
		
		nodeStack.set(0, new LinkedList<Node>(nodeFinder.findRootNodes(nodeList)));
		
		
		
		// While not all paths have been searched (not all paths from level 0 have been searched)
		while (level > -1) {
			// While a complete path has not been found (not all nodes allocated)
			while (schedule.size() < nodeList.size()) {
				// If a node is available at this index, get it for allocation
				if (nodeStack.get(level).size() > 0) {
					node = nodeStack.get(level).peek();
				// If a node is not available, all paths from the last scheduled node have been searched
				} else {
					// Return to previous level
					returnToPreviousLevel();
					
					if (level < 0) {
						// Just finished all paths, break loop
						break;
					}
					// Find next node on previous level
					continue;
				}
				
				// Try to allocate a processor to the node
				// If returns false, no processors available to allocate
				if (!processorAllocator.allocateProcessor(nodeList, node, node.getCheckedProcessors())) {
					// Reset checked processors for this node
					node.resetCheckedProcessors();
					// Increment index to next node (all paths from this node have been searched)
					nodeStack.get(level).remove();
					// This node was not valid, find next node on this level
					continue;
				}
				
				// Add newly allocated processor to list of processors already attempted
				node.addCheckedProcessor(node.getProcessor());
				
				schedule.add(node);
				
				// Check end time of new node against current bound
				int nBound = node.getStartTime() + node.getWeight();
				// If end time of new node is greater than current bound, it is the new bound
				if (nBound > currentBound) {
					// Check new bound does not exceed best bound; if it does, it will never be better than best
					if (nBound > bestBound) {
						removeLastNodeFromSchedule();
						continue;
					} else {
						currentBound = nBound;
					}
				}
				
				level++;
				nodeStack.set(level, new LinkedList<Node>(nodeFinder.findSatisfiedNodes(nodeList)));
			}
			
			if (currentBound < bestBound && level > -1) {
				bestBound = currentBound;
				optimalSchedule.clear();
				for (int i = 0; i < schedule.size(); i++) {
					optimalSchedule.add(schedule.get(i).clone());
				}
			} else if (currentBound == bestBound && optimalSchedule.size() == 0 && level > -1) {
				for (int i = 0; i < schedule.size(); i++) {
					optimalSchedule.add(schedule.get(i).clone());
				}
			}
			returnToPreviousLevel();
		}
		
		return optimalSchedule;
	}
	
	private void removeLastNodeFromSchedule() {
		if (schedule.size() > 0) {
			// Get the last scheduled node (node allocated on current level)
			Node lastNode = schedule.get(schedule.size() - 1);
			// Node has no longer been allocated
			lastNode.setHasRun(false);
			// Remove the node from the schedule
			schedule.remove(schedule.size() - 1);
		}
	}
	
	private void updateCurrentBound() {
		// TODO Potential Optimization: Resetting current bound
		// Reset the current bound
		currentBound = 0;
		for (Node n : schedule) {
			int nBound = n.getStartTime() + n.getWeight();
			if (nBound > currentBound) {
				currentBound = nBound;
			}
		}
	}
	
	private void returnToPreviousLevel() {
	
		removeLastNodeFromSchedule();
		
		updateCurrentBound();
		
		// Reduce level
		level--;
	}
}

