package scheduler;
import java.util.HashMap;
import java.util.List;

import models.Edge;
import models.Node;
import models.NodeTuple;
/**
 * Created by helen on 28/07/2016.
 */
public interface SchedulerInterface {
	
    public void createSchedule(List<Node> nodeList, List<Edge> edgeList);
    
    public HashMap<String, NodeTuple> getSchedule();
    
}
