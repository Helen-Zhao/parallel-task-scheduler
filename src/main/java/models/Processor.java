package models;

import java.util.ArrayList;
import java.util.List;

public class Processor {

	int endTime = 0;
	List<Node> allocatedNodes = new ArrayList<Node>();
	
	public int getEndTime() {
		return endTime;
	}
	
	public int findEarliestStartTime(Node node, int earliestValidStart, int earliestValidEnd) {
//        int foundStartTime = 0;
//        int foundEndTime = 0;

        if (earliestValidStart > endTime) { 
        	return earliestValidStart; 
        } else { 
        	return endTime; 
        }
        
//        boolean startChanged = true;
//        while (startChanged) {
//            startChanged = false;
//            // Find earliest start based on allocated nodes
//            for (Node snode : allocatedNodes) {
//                foundStartTime = snode.getStartTime();
//                foundEndTime = foundStartTime + snode.getWeight();
//                // Current earliest valid start time overlaps another task
//                if (earliestValidStart >= foundStartTime && earliestValidStart < foundEndTime) {
//                    // Start new node after found node
//                    earliestValidStart = foundEndTime;
//                    earliestValidEnd = earliestValidStart + node.getWeight();
//                    startChanged = true;
//                    // Current earliest valid end time overlaps another task
//                } else if (earliestValidEnd > foundStartTime && earliestValidEnd <= foundEndTime) {
//                    earliestValidStart = foundEndTime;
//                    earliestValidEnd = earliestValidStart + node.getWeight();
//                    startChanged = true;
//                    // Current allocated times are enveloping another task
//                } else if (foundStartTime >= earliestValidStart && foundStartTime < earliestValidEnd) {
//                    earliestValidStart = foundEndTime;
//                    earliestValidEnd = earliestValidStart + node.getWeight();
//                    startChanged = true;
//                }
//            }
//        }
//        
//        return earliestValidStart;
	}
	
	public void addNode(Node node) {
		allocatedNodes.add(node);
		int nodeEndTime = node.getStartTime() + node.getWeight();
		if (nodeEndTime > endTime) {
			endTime = nodeEndTime;
		}
	}
	
	public void removeNode(Node node) {
		allocatedNodes.remove(node);
		int nodeEndTime = node.getStartTime() + node.getWeight();
		if (nodeEndTime == endTime) {
			endTime = 0;
			for (int i = allocatedNodes.size() - 1; i > -1; i--) {
				nodeEndTime = allocatedNodes.get(i).getStartTime() + allocatedNodes.get(i).getWeight();
				if (nodeEndTime > endTime) {
					endTime = nodeEndTime;
				}
			}
		}		
	}
	
	public boolean isEmpty() {
		return allocatedNodes.isEmpty();
	}
	
}