package scheduler;

import models.Node;

import java.util.List;

/**
 * @author Helen Zhao
 */
public interface SchedulerInterface {

    List<Node> createSchedule(List<Node> nodeList);

}
