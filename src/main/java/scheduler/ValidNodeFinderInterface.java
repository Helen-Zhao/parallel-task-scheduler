package scheduler;

import models.Node;

import java.util.List;

/**
 * @author Helen Zhao
 */

public interface ValidNodeFinderInterface {

	List<Node> findRootNodes(List<Node> nodes);
	
	List<Node> findSatisfiedChildren(Node node);
	
	List<Node> findSatisfiedNodes(List<Node> nodes);
}
