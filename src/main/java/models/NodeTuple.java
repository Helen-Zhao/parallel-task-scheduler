package models;

import java.util.ArrayList;
import java.util.List;

public class NodeTuple {
	
	private int startTime = -1;
	private int processor = -1;
	private List<Integer> checkedProcessors = new ArrayList<Integer>(4);
	private boolean hasRun = false;
	
	public NodeTuple(int startTime, int processor, List<Integer> checkedProcessors, boolean hasRun) {
		this.startTime = startTime;
		this.processor = processor;
		this.checkedProcessors = checkedProcessors;
		this.hasRun = hasRun;
	}	 

    public NodeTuple() {}

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
    
    public NodeTuple clone() {
    	NodeTuple tuple = new NodeTuple(this.startTime, this.processor, new ArrayList<Integer>(), this.hasRun);
    	for (Integer i : checkedProcessors) {
    		tuple.addCheckedProcessor(i);
    	}
    	return tuple;
    }
}
