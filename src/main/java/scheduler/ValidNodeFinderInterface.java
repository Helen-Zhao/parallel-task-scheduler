package scheduler;

import java.util.List;
import models.Node;

public interface ValidNodeFinderInterface {

	public List<Node> findRootNodes(List<Node> nodes);
	
	public List<Node> findSatisfiedChildren(Node node);
	
	public List<Node> findSatisfiedNodes(List<Node> nodes);
}

