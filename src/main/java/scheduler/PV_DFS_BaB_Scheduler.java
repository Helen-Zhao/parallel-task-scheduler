package scheduler;

import main.Main;
import models.Edge;
import models.Node;
import models.NodeTuple;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;

import javax.swing.*;
import java.util.*;

/**
 * Implementation of depth first branch and bound scheduler using while loops with additions
 * for using both parallelisation and visualisation simultaneously.
 *
 * @author Jacky, Ben, William, Henry - Modified version of the original scheduler produced by Jay
 */
public class PV_DFS_BaB_Scheduler implements ParallelSchedulerInterface {

    int currentBound = 0;
    int bestBound = 0;
    int heuristicBound = 0;
    int heuristicValue = 0;
    int criticalPathLength = 0;
    int graphId;
    JFrame myJFrame;

    private PriorityQueue<Node> criticalQueue = new PriorityQueue<Node>(new CriticalNodeComparator());

    private List<Node> nodeList;
    private List<Node> scheduledNodes;
    private HashMap<String, NodeTuple> scheduleInfo;
    private HashMap<String, NodeTuple> optimalSchedule;
    private List<Queue<Node>> nodeStack;
    private MasterSchedulerInterface masterScheduler;
    int level = 0;
    int initialLevel = 0;

    Graph graph;


    Node node;
    ValidNodeFinderInterface nodeFinder;
    ProcessorAllocatorInterface processorAllocator;

    public PV_DFS_BaB_Scheduler(ValidNodeFinderInterface nodeFinder, ProcessorAllocatorInterface processAllocator, int graphId) {
        this.nodeFinder = nodeFinder;
        this.processorAllocator = processAllocator;
        this.graphId = graphId;
        masterScheduler = V_MasterScheduler.getInstance();
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

        graph = new MultiGraph("Schedule 1");

        // Initialise availability
        this.nodeList = nodes;

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

        // Determines where graph is displayed on screen when using multiple threads
        int x = 0;
        int y = 0;
        switch (graphId) {
            case 0:
                break;
            case 1:
                y = 400;
                break;
            case 2:
                x = 400;
                break;
            case 3:
                x = 400;
                y = 400;
                break;
            case 4:
                x = 800;
                break;
            case 5:
                x = 800;
                y = 400;
                break;
            case 6:
                x = 1200;
                break;
        }

        //Viewer for graph being instantiated and displayed
        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.addDefaultView(false);
        viewer.enableAutoLayout();
        View defaultView = viewer.getDefaultView();
        defaultView.setSize(350, 350);
        myJFrame = new JFrame();
        myJFrame.setBounds(x, y, 400, 400);
        myJFrame.setVisible(true);
        myJFrame.add(defaultView);
        myJFrame.validate();

        setAttributeMethod(graph);

        for (int i = 0; i < nodeList.size(); i++) {
            criticalQueue.add(nodeList.get(i));
        }


        // Initialize level to be last initialized level in nodeStack
        initialLevel = nodeStack.size();
        this.level = initialLevel;

        // Initialize nodeStack
        for (int i = initialLevel; i < nodeList.size() + 1; i++) {
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

                    graph.addAttribute("ui.stylesheet", "node#" + "foo" + node.getName() + "foo" + "{fill-color: #c0392b;}");

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

                    sleepFunction(1);

                    // Changes node back to black
                    graph.addAttribute("ui.stylesheet", "node#" + "foo" + node.getName() + "foo" + " { fill-color: #2c3e50; }");
                    continue;
                }

                scheduledNodes.add(node);
                updateHeuristic(node, true);

                sleepFunction(1);

                // Changes node to red
                graph.addAttribute("ui.stylesheet", "node#" + "foo" + node.getName() + "foo" + " { fill-color: #c0392b; }");
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
                        graph.addAttribute("ui.stylesheet", "node#" + "foo" + node.getName() + "foo" + " { fill-color: #2c3e50; }");

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
                        sleepFunction(40);
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

            if (scheduledNodes.size() == nodeList.size() && setBestBound(currentBound) && level >= initialLevel) {
                optimalSchedule = scheduleInfo;

                sleepFunction(1);
                Main.gui.calculateHeuristic(currentBound, bestBound, heuristicBound);

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
        sleepFunction(15);
        Main.gui.calculateHeuristic(currentBound, bestBound, heuristicBound);
    }

    public synchronized boolean setBestBound(int newBestBound) {
        if (newBestBound < bestBound) {
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

    private void notifyMaster() {
        MasterSchedulerInterface masterScheduler = V_MasterScheduler.getInstance();
        // Closes graph
        myJFrame.dispose();
        masterScheduler.initiateNewSubpathTuple(this);
    }


    private class CriticalNodeComparator implements Comparator<Node> {

        @Override
        public int compare(Node n1, Node n2) {
            return n2.getCriticalPathLength() - n1.getCriticalPathLength();
        }

    }

    // Prints the optimal path, while displaying the dependencies being satisfied
    private void optimalSchedulePath(Graph graph) {
        graph.removeAttribute("ui.stylesheet");
        setAttributeMethod(graph);

        HashMap<String, Node> matchedNodes = new HashMap<String, Node>();
        sleepFunction(250);
        for (int i = 0; i < optimalSchedule.size(); i++) {

            if (i == 0) {
                Node n = scheduledNodes.get(i);
                sleepFunction(250);
                graph.addAttribute("ui.stylesheet", "node#" + "foo" + n.getName() + "foo" + " { fill-color: #27ae60; }");
                matchedNodes.put(n.getName(), n);
            } else {
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
                graph.addAttribute("ui.stylesheet", "node#" + "foo" + n.getName() + "foo" + " { fill-color: #27ae60; }");
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
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}

