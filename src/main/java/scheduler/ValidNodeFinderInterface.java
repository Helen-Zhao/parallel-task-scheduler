package scheduler;

import models.Node;
import models.NodeTuple;

import java.util.HashMap;
import java.util.List;

/**
 * @author Helen Zhao
 */

public interface ValidNodeFinderInterface {

	public void addNodeInfo(HashMap<String, NodeTuple> nodeInfo);
	
	public List<Node> findRootNodes(List<Node> nodes);
	
	public List<Node> findSatisfiedChildren(Node node);
	
	public List<Node> findSatisfiedNodes(List<Node> nodes);
}
