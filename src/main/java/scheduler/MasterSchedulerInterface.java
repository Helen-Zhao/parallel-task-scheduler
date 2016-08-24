package scheduler;

import java.util.HashMap;
import models.NodeTuple;

public interface MasterSchedulerInterface extends SchedulerInterface {
	public void compare(HashMap<String, NodeTuple> schedule, int scheduleBound);
	public void initiateNewSubpathTuple(ParallelSchedulerInterface scheduler);
}