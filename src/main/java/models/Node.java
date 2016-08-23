package models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Helen Zhao
 *         <p>
 *         Node models a node in a DAG.
 */
public class Node implements Comparable<Node>, Cloneable {

    private String name;
    private int weight;
    private List<Edge> incomingEdges;
    private List<Edge> outgoingEdges;
    private int criticalPathLength = 0;

    public Node(String name, int weight) {

        this.name = name;
        this.weight = weight;
        this.incomingEdges = new ArrayList<Edge>();
        this.outgoingEdges = new ArrayList<Edge>();

    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public Edge getIncomingEdge(int i) {
        return incomingEdges.get(i);
    }

    public List<Edge> getIncomingEdges() {
        return incomingEdges;
    }

    public void addIncomingEdge(Edge edge) {
        incomingEdges.add(edge);
    }

    public int getNumberIncomingEdges() {
        return incomingEdges.size();
    }

    public Edge getOutgoingEdge(int i) {
        return outgoingEdges.get(i);
    }

    public List<Edge> getOutgoingEdges() {
        return outgoingEdges;
    }

    public void addOutgoingEdge(Edge edge) {
        outgoingEdges.add(edge);
    }

    public int getNumberOutgoingEdges() {
        return outgoingEdges.size();
    }
    
    public void setCriticalPathLength(int i) {
    	this.criticalPathLength = i;
	}

	public int getCriticalPathLength() {
		return criticalPathLength;
	}
   

    public int compareTo(Node node) {
        return node.getWeight() - this.weight;
    }

//    public Node clone() {
//        Node clone = new Node(this.name, this.weight);
//        clone.setStartTime(this.startTime);
//        clone.setProcessor(this.processor);
//        clone.setHasRun(this.hasRun);
//        return clone;
//    }
}
