package scheduler;

import models.Node;

import java.util.List;

/**
 * @author Helen Zhao
 */

public interface ProcessorAllocatorInterface {

    boolean allocateProcessor(List<Node> schedule, Node node, List<Integer> checkedProcessors);

    int findEarliestStartTime(List<Node> schedule, Node node, int processor);

    int getNumberProcessors();

}
