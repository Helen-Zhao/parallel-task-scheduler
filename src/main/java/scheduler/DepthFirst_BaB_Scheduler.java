package scheduler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import models.Edge;
import models.Node;
import models.NodeTuple;

/**
 * Implementation of depth first branch and bound scheduler using while loops
 * 
 * @author Jay
 *
 */
public class DepthFirst_BaB_Scheduler implements SchedulerInterface {

	int currentBound = 0;
	int bestBound = 0;
	
	int heuristicValue = 0;
	int criticalPathLength = 0;
	PriorityQueue<Node> criticalQueue = new PriorityQueue<Node>(new CriticalNodeComparator());
	int heuristicBound = 0;
	
	List<Node> nodeList;
	List<Node> scheduledNodes = new ArrayList<Node>();
	HashMap<String, NodeTuple> scheduleInfo = new HashMap<String, NodeTuple>();
	HashMap<String, NodeTuple> optimalSchedule;
	
	int level = 0;
	List<Queue<Node>> nodeStack;
	
	Node node;
	ValidNodeFinderInterface nodeFinder;
	ProcessorAllocatorInterface processorAllocator;
	
	public DepthFirst_BaB_Scheduler(ValidNodeFinderInterface nodeFinder, ProcessorAllocatorInterface processAllocator) {
		  this.nodeFinder = nodeFinder;
		  this.processorAllocator = processAllocator;
		  
		  processorAllocator.addNodeInfo(scheduleInfo);
		  nodeFinder.addNodeInfo(scheduleInfo);
	}
	
	@Override
	public void createSchedule(List<Node> nodes, List<Edge> edgeList) {

		
		// Initialize availability
		nodeList = nodes;
		
		for (Node n : nodeList) {
			bestBound += n.getWeight();
			scheduleInfo.put(n.getName(), new NodeTuple());
		}
		heuristicValue = bestBound / processorAllocator.getNumberProcessors();
		heuristicBound = heuristicValue;
		
		// Estimate heuristic costs
		boolean hasDistanceChanged = true;
		while (hasDistanceChanged) {
			hasDistanceChanged = false;
			for (Edge edge : edgeList) {
				int newCritPathLength = edge.getEndNode().getCriticalPathLength() + edge.getStartNode().getWeight();
				if (newCritPathLength > edge.getStartNode().getCriticalPathLength()) {
					edge.getStartNode().setCriticalPathLength(newCritPathLength);
					hasDistanceChanged = true;
				}
			}
		}
		
		for (int i = 0; i < nodeList.size(); i++) {
			criticalQueue.add(nodeList.get(i));
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
			while (scheduledNodes.size() < nodeList.size()) {
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
				
				processorAllocator.removeFromProcessor(node, scheduleInfo.get(node.getName()).getProcessor());
				// Try to allocate a processor to the node
				// If returns false, no processors available to allocate
				if (!processorAllocator.allocateProcessor(nodeList, node)) {
					// Reset checked processors for this node
					scheduleInfo.get(node.getName()).resetCheckedProcessors();
					// Increment index to next node (all paths from this node have been searched)
					nodeStack.get(level).remove();
					// This node was not valid, find next node on this level
					continue;
				}

				scheduledNodes.add(node);
				updateHeurisitic(node, true);
				
				// Check end time of new node against current bound
				int nBound = scheduleInfo.get(node.getName()).getStartTime() + node.getWeight();
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
			
			if (currentBound < bestBound && level > -1) {
				bestBound = currentBound;
				optimalSchedule = scheduleInfo;
				scheduleInfo = new HashMap<String, NodeTuple>();
				processorAllocator.addNodeInfo(scheduleInfo);
				nodeFinder.addNodeInfo(scheduleInfo);
				for (Node n : nodeList) {
					scheduleInfo.put(n.getName(), optimalSchedule.get(n.getName()).clone());
				}
			} else if (optimalSchedule == null && level > -1) {
				optimalSchedule = scheduleInfo;
				scheduleInfo = new HashMap<String, NodeTuple>();
				processorAllocator.addNodeInfo(scheduleInfo);
				nodeFinder.addNodeInfo(scheduleInfo);
				for (Node n : nodeList) {
					scheduleInfo.put(n.getName(), optimalSchedule.get(n.getName()).clone());
				}
			}
			returnToPreviousLevel();
		}
		
		return;
	}
	
	@Override
	public HashMap<String, NodeTuple> getSchedule() {
		return optimalSchedule;
	}
	
	private void updateHeurisitic(Node node, boolean isAllocated) {
		if (isAllocated) {
			heuristicValue -= node.getWeight() / processorAllocator.getNumberProcessors();
			while (criticalQueue.size() > 0 && scheduleInfo.get(criticalQueue.peek().getName()).getHasRun()) {
				criticalQueue.remove();
			}
			if (criticalQueue.size() > 0) {
				criticalPathLength = criticalQueue.peek().getCriticalPathLength();
			} else {
				criticalPathLength = 0;
			}			
		} else {
			heuristicValue += node.getWeight() / processorAllocator.getNumberProcessors();
			criticalQueue.add(node);
			int newCriticalPathLength = criticalQueue.peek().getCriticalPathLength();
			if (newCriticalPathLength > criticalPathLength) {
				criticalPathLength = newCriticalPathLength;
			}
		}
		
		heuristicBound = Math.max(
				heuristicValue + processorAllocator.getEarliestProcessorEndTime(),
				criticalPathLength + processorAllocator.getEarliestProcessorEndTime()
				);
	}
	
	private void removeLastNodeFromSchedule() {
		if (scheduledNodes.size() > 0) {
			// Remove the last scheduled node (node allocated on current level)
			Node lastNode = scheduledNodes.remove(scheduledNodes.size() - 1);
			// Node has no longer been allocated
			scheduleInfo.get(lastNode.getName()).setHasRun(false);
		
			updateHeurisitic(lastNode, false);
		}
	}
	
	/*
	 * Calculates the appropriate max runtime for the schedule
	 */
	private void updateCurrentBound() {
		// Reset the current bound
		currentBound = 0;
		for (int i = scheduledNodes.size() - 1; i > -1; i--) {
			int nBound = scheduleInfo.get(scheduledNodes.get(i).getName()).getStartTime() + scheduledNodes.get(i).getWeight();
			if (nBound > currentBound) {
				currentBound = nBound;
			}
		}
	}
	
	/*
	 * Decrements the level and performs necessary functions for when returning to a previous level,
	 * including resetting node and updating the current bound
	 */
	private void returnToPreviousLevel() {
	
		removeLastNodeFromSchedule();
		
		updateCurrentBound();
		
		// Reduce level
		level--;
	}

	private class CriticalNodeComparator implements Comparator<Node> {

		@Override
		public int compare(Node n1, Node n2) {
			return n2.getCriticalPathLength() - n1.getCriticalPathLength();
		}
		
	}
	
	
}

