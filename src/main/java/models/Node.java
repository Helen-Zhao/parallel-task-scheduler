package models;

import java.util.List;

/**
 * Created by helen on 28/07/2016.
 */
public class Node {

    private String name;
    private int weight;
    private List<Edge> incomingEdges;
    private int startTime;
    private boolean hasRun;
    private int processor;

    public Node(String name, int weight, List<Edge> incomingEdges) {

        this.name = name;
        this.weight = weight;
        this.incomingEdges = incomingEdges;
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

    public List<Edge> getIncomingEdges() {
        return incomingEdges;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public boolean isHasRun() {
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

}
