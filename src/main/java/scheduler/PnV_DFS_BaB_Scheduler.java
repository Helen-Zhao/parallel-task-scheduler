package scheduler;

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
 * @author Jacky and Ben - Modified version of the original scheduler produced by Jay
 *
 */
public class PnV_DFS_BaB_Scheduler implements ParallelSchedulerInterface {

	int currentBound = 0;
	int bestBound = 0;
	int heuristicBound = 0;
	int heuristicValue = 0;
	int criticalPathLength = 0;
	
	private PriorityQueue<Node> criticalQueue = new PriorityQueue<Node>(new CriticalNodeComparator());
	
	private List<Node> nodeList;
	private List<Node> scheduledNodes;
	private HashMap<String, NodeTuple> scheduleInfo;
	private HashMap<String, NodeTuple> optimalSchedule;
	private List<Queue<Node>> nodeStack;
	private MasterSchedulerInterface masterScheduler;
	int level = 0;
	int initialLevel = 0;
	
	
	Node node;
	ValidNodeFinderInterface nodeFinder;
	ProcessorAllocatorInterface processorAllocator;
	
	public PnV_DFS_BaB_Scheduler(ValidNodeFinderInterface nodeFinder, ProcessorAllocatorInterface processAllocator) {
		this.nodeFinder = nodeFinder;
		this.processorAllocator = processAllocator;
		masterScheduler = MasterScheduler.getInstance();
	}
	
	@Override
	public void initiateNewSubtree(List<Node> nodes, ProcessorAllocatorInterface processorAllocator, List<Queue<Node>> initialNodeStack, 
			int initialBestBound, HashMap<String, NodeTuple> scheduleInfo, List<Node> scheduledNodes) {
		this.processorAllocator = processorAllocator;
		this.scheduleInfo = scheduleInfo;
		this.scheduledNodes = scheduledNodes;
		processorAllocator.addNodeInfo(scheduleInfo);
		nodeFinder.addNodeInfo(scheduleInfo);
		bestBound = initialBestBound;
		
		heuristicValue = bestBound / processorAllocator.getNumberProcessors();
		heuristicBound = heuristicValue;
		
		nodeStack = initialNodeStack;
				
		createSchedule(nodes, null);
				
		// Called when other work is complete
		notifyMaster();
	}
	
	@Override
	public void createSchedule(List<Node> nodes, List<Edge> edgeList) {
		// Initialise availability
		this.nodeList = nodes;
		
		for (int i = 0; i < nodeList.size(); i++) {
			criticalQueue.add(nodeList.get(i));
		}
		
		// Initialise level to be last initialised level in nodeStack
		initialLevel = nodeStack.size();
		this.level = initialLevel;
		
		// Initialise nodeStack
		for (int i = initialLevel; i < nodeList.size()+1; i++) {
			nodeStack.add(null);
		}
		
		nodeStack.set(initialLevel, new LinkedList<Node>(nodeFinder.findSatisfiedNodes(nodeList)));
		
		// While not all paths have been searched (not all paths from level 0 have been searched)
		while (level >= initialLevel) {			
			// While a complete path has not been found (not all nodes allocated)
			while (scheduledNodes.size() < nodeList.size()) {
				// If a node is available at this index, get it for allocation
				if (nodeStack.get(level).size() > 0) {
					node = nodeStack.get(level).peek();
				// If a node is not available, all paths from the last scheduled node have been searched
				} else {
					// Return to previous level
					returnToPreviousLevel();
					
					if (level < initialLevel) {
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
				updateHeuristic(node, true);
				
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
			
			if (scheduledNodes.size() == nodeList.size() && setBestBound(currentBound) && level >= initialLevel) {	
				optimalSchedule = scheduleInfo;
				scheduleInfo = new HashMap<String, NodeTuple>();
				processorAllocator.addNodeInfo(scheduleInfo);
				nodeFinder.addNodeInfo(scheduleInfo);
				for (Node n : nodeList) {
					scheduleInfo.put(n.getName(), optimalSchedule.get(n.getName()).clone());
				}
				masterScheduler.compare(optimalSchedule, bestBound);
			} else if (currentBound == bestBound && optimalSchedule == null && level >= initialLevel) {
				optimalSchedule = scheduleInfo;
				scheduleInfo = new HashMap<String, NodeTuple>();
				processorAllocator.addNodeInfo(scheduleInfo);
				nodeFinder.addNodeInfo(scheduleInfo);
				for (Node n : nodeList) {
					scheduleInfo.put(n.getName(), optimalSchedule.get(n.getName()).clone());
				}
				masterScheduler.compare(optimalSchedule, bestBound);
			}
			
			returnToPreviousLevel();
		}
		
	}
	
	public synchronized boolean setBestBound(int newBestBound) {
		if(newBestBound < bestBound) {
			bestBound = newBestBound;
			return true;
		} 
		return false;
	}
	
	@Override
	public HashMap<String, NodeTuple> getSchedule() {
		return optimalSchedule;
	}
	
	private void updateHeuristic(Node node, boolean isAllocated) {
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
		
			updateHeuristic(lastNode, false);
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
	
	private void notifyMaster(){
		MasterSchedulerInterface masterScheduler = MasterScheduler.getInstance();
		masterScheduler.initiateNewSubpathTuple(this);
	}
	

	private class CriticalNodeComparator implements Comparator<Node> {
		@Override
		public int compare(Node n1, Node n2) {
			return n2.getCriticalPathLength() - n1.getCriticalPathLength();
		}
		
	}
}

