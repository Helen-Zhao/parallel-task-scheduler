package scheduler;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import main.Main;
import models.Node;
import models.Edge;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

/**
 * Implementation of depth first branch and bound scheduler using while loops
 * Optimized to reduce memory use through minimal persistent memory objects
 * 
 * @author Jay
 *
 */
public class DepthFirst_BaB_Scheduler_Visualisation implements SchedulerInterface {

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
	JFrame graphFrame;
	
	public DepthFirst_BaB_Scheduler_Visualisation(ValidNodeFinderInterface nodeFinder, ProcessorAllocatorInterface processAllocator) {
		  this.nodeFinder = nodeFinder;
		  this.processorAllocator = processAllocator;
	}
	
	@Override
	public List<Node> createSchedule(List<Node> nodes) {
		
		Graph graph = new MultiGraph("Schedule 1");
		
		if (nodes.size() == 0) return new ArrayList<Node>();
		
		// Initialize availability
		nodeList = nodes;
		
		// Create Graph of current available nodes
		for (Node n : nodeList) {
			graph.addNode(n.getName());
		}
		
		// Creating edges for graph
		for (Node n : nodeList) {
			List<Edge> edgeList = n.getOutgoingEdges();
			for (Edge e : edgeList) {
				String edgeName = "Edge" + e.getStartNode().getName() + "Edge" + e.getEndNode().getName();
				graph.addEdge(edgeName, e.getStartNode().getName(), e.getEndNode().getName(), true);
			}
		}
		
		graph.display();

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
					try {
					    Thread.sleep(1);                 //1000 milliseconds is one second.
					} catch(InterruptedException ex) {
					    Thread.currentThread().interrupt();
					}

					graph.addAttribute("ui.stylesheet", "node#" + node.getName() + " { fill-color: red; }");

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
					
					try {
					    Thread.sleep(1);                 //1000 milliseconds is one second.
					} catch(InterruptedException ex) {
					    Thread.currentThread().interrupt();
					}
					
					graph.addAttribute("ui.stylesheet", "node#" + node.getName() + " { fill-color: black; }");
					continue;
				}
				
				// Add newly allocated processor to list of processors already attempted
				node.addCheckedProcessor(node.getProcessor());
				
				schedule.add(node);
				
				try {
				    Thread.sleep(1);                 //1000 milliseconds is one second.
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
				
				graph.addAttribute("ui.stylesheet", "node#" + node.getName() + " { fill-color: red; }");
				List<Edge> nodeEdges = node.getIncomingEdges();
				for (Edge e : nodeEdges) {
					Node startNode = e.getStartNode();
					
					try {
					    Thread.sleep(1);                 //1000 milliseconds is one second.
					} catch(InterruptedException ex) {
					    Thread.currentThread().interrupt();
					}
					
					graph.addAttribute("ui.stylesheet", "edge#Edge" + startNode.getName() + "Edge" + node.getName() + " { fill-color: red; }");
				}

				
				// Check end time of new node against current bound
				int nBound = node.getStartTime() + node.getWeight();
				// If end time of new node is greater than current bound, it is the new bound
				if (nBound > currentBound) {
					// Check new bound does not exceed best bound; if it does, it will never be better than best
					if (nBound > bestBound) {
						removeLastNodeFromSchedule();
						
						try {
						    Thread.sleep(1);                 //1000 milliseconds is one second.
						} catch(InterruptedException ex) {
						    Thread.currentThread().interrupt();
						}
						
						graph.addAttribute("ui.stylesheet", "node#" + node.getName() + " { fill-color: black; }");
						
						List<Edge> nodeEdges2 = node.getIncomingEdges();
						for (Edge e : nodeEdges2) {
							Node startNode = e.getStartNode();
							
							try {
							    Thread.sleep(1);                 //1000 milliseconds is one second.
							} catch(InterruptedException ex) {
							    Thread.currentThread().interrupt();
							}
							
							graph.addAttribute("ui.stylesheet", "edge#Edge" + startNode.getName() + "Edge" + node.getName() + " { fill-color: black; }");
						}						
						
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
				//graph.removeAttribute("ui.stylesheet");
				for (int i = 0; i < schedule.size(); i++) {
					optimalSchedule.add(schedule.get(i).clone());
				}
							
				
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("New Optimal Schedule Found");
				Main.gui.setOptimalSchedule(optimalSchedule);
				
				graph.removeAttribute("ui.stylesheet");
				HashMap<String, Node> matchedNodes = new HashMap<String, Node>();
				for (int i = 0; i < optimalSchedule.size(); i++) {
					
					if (i == 0) {
						Node n = optimalSchedule.get(i);
						try {
							Thread.sleep(500);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						graph.addAttribute("ui.stylesheet", "node#" + n.getName() + " { fill-color: red; }");
						matchedNodes.put(n.getName(), n);
					}
					else {
						Node n = optimalSchedule.get(i);
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
								//HEAT MAP ME
								try {
									Thread.sleep(500);
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								graph.addAttribute("ui.stylesheet", "edge#Edge" + incomingNode.getName() + "Edge" + n.getName() + " { fill-color: red; }");
								graph.addAttribute("ui.stylesheet", "node#" + n.getName() + " { fill-color: red; }");
							}
						}
						matchedNodes.put(n.getName(), n);
					}
				}
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} else if (currentBound == bestBound && optimalSchedule.size() == 0 && level > -1) {
				for (int i = 0; i < schedule.size(); i++) {
					optimalSchedule.add(schedule.get(i).clone());

				}
				System.out.println("New Optimal Schedule Found");
				Main.gui.setOptimalSchedule(optimalSchedule);
				
				graph.removeAttribute("ui.stylesheet");
				HashMap<String, Node> matchedNodes = new HashMap<String, Node>();
				for (int i = 0; i < optimalSchedule.size(); i++) {
					
					if (i == 0) {
						Node n = optimalSchedule.get(i);
						try {
							Thread.sleep(500);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						graph.addAttribute("ui.stylesheet", "node#" + n.getName() + " { fill-color: red; }");
						matchedNodes.put(n.getName(), n);
					}
					else {
						Node n = optimalSchedule.get(i);
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
								//HEAT MAP ME
								try {
									Thread.sleep(500);
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								graph.addAttribute("ui.stylesheet", "edge#Edge" + incomingNode.getName() + "Edge" + n.getName() + " { fill-color: red; }");
								graph.addAttribute("ui.stylesheet", "node#" + n.getName() + " { fill-color: red; }");
							}
						}
						matchedNodes.put(n.getName(), n);
					}
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			returnToPreviousLevel();
		}
		
		System.out.println("Final Optimal Schedule");
		graph.removeAttribute("ui.stylesheet");
		HashMap<String, Node> matchedNodes = new HashMap<String, Node>();
		for (int i = 0; i < optimalSchedule.size(); i++) {
			
			if (i == 0) {
				Node n = optimalSchedule.get(i);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				graph.addAttribute("ui.stylesheet", "node#" + n.getName() + " { fill-color: red; }");
				matchedNodes.put(n.getName(), n);
			}
			else {
				Node n = optimalSchedule.get(i);
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
						//HEAT MAP ME
						try {
							Thread.sleep(500);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						graph.addAttribute("ui.stylesheet", "edge#Edge" + incomingNode.getName() + "Edge" + n.getName() + " { fill-color: red; }");
						graph.addAttribute("ui.stylesheet", "node#" + n.getName() + " { fill-color: red; }");
					}
				}
				matchedNodes.put(n.getName(), n);
			}
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

