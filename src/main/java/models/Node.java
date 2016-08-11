package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by helen on 28/07/2016.
 */
public class Node implements Comparable<Node>, Cloneable {

    private String name;
    private int weight;
    private List<Edge> incomingEdges;
    private List<Edge> outgoingEdges;
    private int startTime;
    private boolean hasRun;
    private int processor;
    private List<Integer> checkedProcessors = new ArrayList<Integer>(4);

    public Node(String name, int weight) {

        this.name = name;
        this.weight = weight;
        this.incomingEdges = new ArrayList<Edge>();
        this.outgoingEdges = new ArrayList<Edge>();
        this.startTime = -1;
        this.hasRun = false;
        this.processor = -1;

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

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public boolean getHasRun() {
        return hasRun;
    }

    public void setHasRun(boolean hasRun) {
        this.hasRun = hasRun;
    }

    public int getProcessor() {
        return processor;
    }

    public void setProcessor(int processor) {
        this.processor = processor;
    }
    
    public List<Integer> getCheckedProcessors() {
    	return checkedProcessors;
    }
    
    public void addCheckedProcessor(int num) {
    	checkedProcessors.add(num);
    }
    
    public void resetCheckedProcessors() {
    	checkedProcessors.clear();
    }

	public int compareTo(Node node) {
		return node.getWeight() - this.weight;
	}
	
	public Node clone() {
		Node clone = new Node(this.name, this.weight);
		clone.setStartTime(this.startTime);
		clone.setProcessor(this.processor);
		clone.setHasRun(this.hasRun);
		return clone;
	}
}
