package scheduler;
import java.util.List;

import models.Node;
/**
 * Created by helen on 28/07/2016.
 */
public interface SchedulerInterface {
	
    public List<Node> createSchedule(List<Node> nodeList);
    
}
