package scheduler;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.swing.JFrame;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;

import main.Main;
import models.Edge;
import models.Node;
import models.NodeTuple;

/**
 * Implementation of depth first branch and bound scheduler using while loops
 * 
 * @author Jay
 *
 */
public class DepthFirst_BaB_Scheduler_Visualisation implements SchedulerInterface {

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
	
	public DepthFirst_BaB_Scheduler_Visualisation(ValidNodeFinderInterface nodeFinder, ProcessorAllocatorInterface processAllocator) {
		  this.nodeFinder = nodeFinder;
		  this.processorAllocator = processAllocator;
		  
		  processorAllocator.addNodeInfo(scheduleInfo);
		  nodeFinder.addNodeInfo(scheduleInfo);
	}
	
	@Override
	public void createSchedule(List<Node> nodes, List<Edge> edgeList) {

		Graph graph = new MultiGraph("Schedule 1");
		
		// Initialize availability
		nodeList = nodes;
		
		// Create Graph of current available nodes
		for (Node n : nodeList) {
			org.graphstream.graph.Node graphNode = graph.addNode("foo" + n.getName() + "foo");
			graphNode.addAttribute("ui.label", n.getName());
		}
		
		// Creating edges for graph
		for (Node n : nodeList) {
			List<Edge> edgeList2 = n.getOutgoingEdges();
			for (Edge e : edgeList2) {
				String edgeName = "Edge" + e.getStartNode().getName() + "Edge" + e.getEndNode().getName();
				graph.addEdge(edgeName, "foo" + e.getStartNode().getName() + "foo", "foo" + e.getEndNode().getName() + "foo", true);
			}
		}
		
		// Generates viewable graph to display
		Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		viewer.addDefaultView(false);   
		viewer.enableAutoLayout();
		View defaultView = viewer.getDefaultView();
		defaultView.setSize(400, 400);
		JFrame myJFrame = new JFrame();
		myJFrame.setBounds(600, 150, 800, 800);
		myJFrame.setVisible(true);
		myJFrame.add(defaultView);
		myJFrame.validate();

		setAttributeMethod(graph);
		
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
				sleepFunction(20);
				// Shows current status of program in chart
				Main.gui.printOptimalLabel(2);
				
				// If a node is available at this index, get it for allocation
				if (nodeStack.get(level).size() > 0) {
					node = nodeStack.get(level).peek();
					
					// Changes node to red
					graph.addAttribute("ui.stylesheet", "node#" + "foo" + node.getName()  + "foo" + "{fill-color: #c0392b;}");
					
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
					
					sleepFunction(1);
					
					// Changes node back to black
					graph.addAttribute("ui.stylesheet", "node#" + "foo" + node.getName()  + "foo" + " { fill-color: #2c3e50; }");
					continue;
				}

				scheduledNodes.add(node);
				updateHeurisitic(node, true);
				
				sleepFunction(1);
				
				// Changes node to red
				graph.addAttribute("ui.stylesheet", "node#" + "foo" + node.getName()  + "foo" + " { fill-color: #c0392b; }");
				List<Edge> nodeEdges = node.getIncomingEdges();
				for (Edge e : nodeEdges) {
					Node startNode = e.getStartNode();
					
					sleepFunction(1);
					
					// Changes edge to seen (orange)
					graph.addAttribute("ui.stylesheet", "edge#Edge" + startNode.getName() + "Edge" + node.getName() + " { fill-color: #d35400; }");
				}
				
				// Check end time of new node against current bound
				int nBound = scheduleInfo.get(node.getName()).getStartTime() + node.getWeight();
				// If end time of new node is greater than current bound, it is the new bound
				if (nBound > currentBound) {
					// Check new bound does not exceed best bound; if it does, it will never be better than best
					if (nBound > bestBound) {
						removeLastNodeFromSchedule();
						
						sleepFunction(1);
						
						// Changes node back to black
						graph.addAttribute("ui.stylesheet", "node#" + "foo" + node.getName()  + "foo" + " { fill-color: #2c3e50; }");
						
						List<Edge> nodeEdges2 = node.getIncomingEdges();
						for (Edge e : nodeEdges2) {
							Node startNode = e.getStartNode();
							
							sleepFunction(1);
							
							// Changes edge back to unseen (black)
							graph.addAttribute("ui.stylesheet", "edge#Edge" + startNode.getName() + "Edge" + node.getName() + " { fill-color: #7f8c8d; }");
						}
						
						continue;
					} else {
						currentBound = nBound;
						sleepFunction(15);
						Main.gui.calculateHeuristic(currentBound, bestBound, heuristicBound);
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
				sleepFunction(15);
				Main.gui.calculateHeuristic(currentBound, bestBound, heuristicBound);
				optimalSchedule = scheduleInfo;
				scheduleInfo = new HashMap<String, NodeTuple>();
				processorAllocator.addNodeInfo(scheduleInfo);
				nodeFinder.addNodeInfo(scheduleInfo);
				for (Node n : nodeList) {
					scheduleInfo.put(n.getName(), optimalSchedule.get(n.getName()).clone());
				}
				
				// Shows current status of program in chart
				Main.gui.printOptimalLabel(1);
				
				// Updates chart with new optimal schedule
				Main.gui.setOptimalSchedule(scheduledNodes, optimalSchedule);
				
				// Draws optimal path on graph
				optimalSchedulePath(graph);
				
			} else if (optimalSchedule == null && level > -1) {
				optimalSchedule = scheduleInfo;
				scheduleInfo = new HashMap<String, NodeTuple>();
				processorAllocator.addNodeInfo(scheduleInfo);
				nodeFinder.addNodeInfo(scheduleInfo);
				for (Node n : nodeList) {
					scheduleInfo.put(n.getName(), optimalSchedule.get(n.getName()).clone());
				}
				
				// Shows current status of program in chart
				Main.gui.printOptimalLabel(1);
				
				// Updates chart with new optimal schedule
				Main.gui.setOptimalSchedule(scheduledNodes, optimalSchedule);

				// Draws optimal path on graph
				optimalSchedulePath(graph);
				
			}
			returnToPreviousLevel();
		}
		// Prints final optimal schedule onto chart and graph
		Main.gui.printOptimalLabel(0);
		sleepFunction(15);
		Main.gui.calculateHeuristic(currentBound, bestBound, heuristicBound);
		finalOptimalSchedulePath(graph);
		
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
	
	// Prints the optimal path found, while also displaying dependencies being satisfied
	private void optimalSchedulePath(Graph graph){
		graph.removeAttribute("ui.stylesheet");
		setAttributeMethod(graph);

		HashMap<String, Node> matchedNodes = new HashMap<String, Node>();
		sleepFunction(250);
		
		for (int i = 0; i < optimalSchedule.size(); i++) {
			if (i == 0) {
				Node n = scheduledNodes.get(i);
				sleepFunction(250);
				graph.addAttribute("ui.stylesheet", "node#" + "foo" + n.getName()  + "foo" + " { fill-color: #27ae60; }");
				matchedNodes.put(n.getName(), n);
			}
			else {
				Node n = scheduledNodes.get(i);
				List<Edge> optimalEdgeList = new ArrayList<Edge>();
				for (Node nl : nodeList) {
					if (n.getName().equals(nl.getName())) {
						optimalEdgeList = nl.getIncomingEdges();
						break;
					}
				}
				for (Edge e : optimalEdgeList) {
					Node incomingNode = e.getStartNode();
					if (matchedNodes.containsKey(incomingNode.getName())) {
						sleepFunction(250);
						graph.addAttribute("ui.stylesheet", "edge#Edge" + incomingNode.getName() + "Edge" + n.getName() + " { fill-color: #16a085; }");
					}
				}
				graph.addAttribute("ui.stylesheet", "node#" + "foo" + n.getName()  + "foo" + " { fill-color: #27ae60; }");
				matchedNodes.put(n.getName(), n);
			}
		}
		sleepFunction(250);
		
	}
	
	// Prints the final optimal path, while also displaying dependencies being satisfied
	private void finalOptimalSchedulePath(Graph graph){
		graph.removeAttribute("ui.stylesheet");
		setAttributeMethod(graph);

		HashMap<String, Node> matchedNodes = new HashMap<String, Node>();
		ArrayList<Node> orderArray = new ArrayList<Node>();
		sleepFunction(250);
		Node earliestNode = nodeList.get(0);
		
		// Generates ordered schedule according to node start times, to be used to draw graph
		while (true) {
			if (orderArray.size() == nodeList.size()) {
				break;
			}
			int earliestStartTime = Integer.MAX_VALUE;
			for (int i = 0; i < nodeList.size(); i++) {
				
				String nodeTupleKey = nodeList.get(i).getName();
				NodeTuple tuple = optimalSchedule.get(nodeTupleKey);
				int tupleStartTime = tuple.getStartTime();
				if (tupleStartTime <= earliestStartTime) {
					if (!matchedNodes.containsKey(nodeList.get(i).getName())) {
						earliestStartTime = tupleStartTime;
						earliestNode = nodeList.get(i);
					}
				}	
			}
			orderArray.add(earliestNode);
			matchedNodes.put(earliestNode.getName(), earliestNode);
			
		}
		
		// Updates graph with final optimal schedule found
		for (int i = 0; i < orderArray.size(); i++) {
			
			if (i == 0) {
				Node n = orderArray.get(i);
				sleepFunction(250);
				graph.addAttribute("ui.stylesheet", "node#" + "foo" + n.getName()  + "foo" + " { fill-color: #27ae60; }");
				matchedNodes.put(n.getName(), n);
			}
			else {
				Node n = orderArray.get(i);
				List<Edge> optimalEdgeList = new ArrayList<Edge>();
				for (Node nl : nodeList) {
					if (n.getName().equals(nl.getName())) {
						optimalEdgeList = nl.getIncomingEdges();
						break;
					}
				}
				for (Edge e : optimalEdgeList) {
					Node incomingNode = e.getStartNode();
					if (matchedNodes.containsKey(incomingNode.getName())) {
						sleepFunction(250);
						graph.addAttribute("ui.stylesheet", "edge#Edge" + incomingNode.getName() + "Edge" + n.getName() + " { fill-color: #16a085; }");
						
					}
				}
				graph.addAttribute("ui.stylesheet", "node#" + "foo" + n.getName()  + "foo" + " { fill-color: #27ae60; }");
				matchedNodes.put(n.getName(), n);
			}
		}
		sleepFunction(250);
	}
	
	// Sets default styling for graph
	private void setAttributeMethod(Graph graph) {
		graph.addAttribute("ui.stylesheet", "node{text-color: blue; text-size: 20px; size: 20px; fill-color: #2c3e50;}");
		graph.addAttribute("ui.stylesheet", "edge{size: 2.5px; fill-color: #7f8c8d;}");
	}
	
	private void sleepFunction(int sleepTime) {
		try {
		    Thread.sleep(sleepTime);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
	}
	
}

