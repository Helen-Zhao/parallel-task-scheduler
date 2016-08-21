package scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import models.Edge;
import models.Node;

/**
 * Implementation of depth first branch and bound scheduler using while loops
 * Compares with a master scheduler's optimal schedule to provide parallel capacity
 * 
 * @author Jay
 *
 */
public class PnV_DepthFirst_BaB_Scheduler implements ParallelSchedulerInterface {

	int currentBound = 0;
	int bestBound = 0;
	int heuristicValue = 0;
	int heuristicBound = 0;
	
	List<Node> nodeList;
	List<Node> schedule = new ArrayList<Node>();
	List<Node> optimalSchedule;
	
	int level = 0;
	int initialLevel;
	List<Queue<Node>> nodeStack;
	
	Node node;
	ValidNodeFinderInterface nodeFinder;
	ProcessorAllocatorInterface processorAllocator;

//	MasterSchedulerInterface masterScheduler;
	
	public PnV_DepthFirst_BaB_Scheduler(ValidNodeFinderInterface nodeFinder, ProcessorAllocatorInterface processAllocator) {
		  this.nodeFinder = nodeFinder;
		  this.processorAllocator = processAllocator;
//		  masterScheduler = MasterScheduler.getInstance();
	}
	
	@Override
	public void initiateNewSubtree(List<Node> nodes, List<Edge> edges, List<Queue<Node>> initialNodeStack, int initialBestBound) {
		
		bestBound = initialBestBound;
		
		heuristicValue = bestBound / processorAllocator.getNumberProcessors();
		heuristicBound = heuristicValue;
		
		nodeStack = initialNodeStack;
		
		createSchedule(nodes, edges);
		
//		notifyMasterDone();
	}
	
	@Override
	public List<Node> createSchedule(List<Node> nodes, List<Edge> edges) {
		
		if (nodes.size() == 0) return new ArrayList<Node>();
		
		// Initialize availability
		nodeList = nodes;

		// Initialize level to be last initialized level in nodeStack
		initialLevel = nodeStack.size() - 1;
		level = initialLevel;
		
		// While not all paths have been searched (not all paths from initialLevel have been searched)
		while (level >= initialLevel) {
			
			findNewPath();
			
			if (currentBound < bestBound && level > -1) {
				if (setBestBound(currentBound)) {
					optimalSchedule = new ArrayList<Node>();
					for (int i = 0; i < schedule.size(); i++) {
						optimalSchedule.add(schedule.get(i).clone());
					}
//					masterScheduler.compare(optimalSchedule, bestBound);
				}
			} else if (optimalSchedule.size() == 0 && currentBound == bestBound && level > -1) {
				optimalSchedule = new ArrayList<Node>();
				for (int i = 0; i < schedule.size(); i++) {
					optimalSchedule.add(schedule.get(i).clone());
				}
//				masterScheduler.compare(optimalSchedule, bestBound);
			}
			returnToPreviousLevel();
		}
		
		return optimalSchedule;
	}

	public synchronized boolean setBestBound(int newBestBound) {
		if (newBestBound < bestBound) {
			bestBound = newBestBound;
			return true;
		}
		return false;
	}
	
	private void findNewPath() {
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
			
			processorAllocator.removeFromProcessor(node, node.getProcessor());
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
			
			schedule.add(node);
			updateHeurisitic(node, true);
			
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
			
			if (heuristicBound > bestBound) {
				removeLastNodeFromSchedule();
				continue;
			}
			
			level++;
			nodeStack.set(level, new LinkedList<Node>(nodeFinder.findSatisfiedNodes(nodeList)));
		}
	}
	
	private void updateHeurisitic(Node node, boolean isAllocated) {
		if (isAllocated) {
			heuristicValue -= node.getWeight() / processorAllocator.getNumberProcessors();
		} else {
			heuristicValue += node.getWeight() / processorAllocator.getNumberProcessors();
		}
		heuristicBound = heuristicValue + processorAllocator.getEarliestProcessorEndTime();
	}
	
	private void removeLastNodeFromSchedule() {
		if (schedule.size() > 0) {
			// Get the last scheduled node (node allocated on current level)
			Node lastNode = schedule.get(schedule.size() - 1);
			// Node has no longer been allocated
			lastNode.setHasRun(false);
			// Remove the node from the schedule
			schedule.remove(schedule.size() - 1);
			
			updateHeurisitic(lastNode, false);
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

