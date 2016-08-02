package scheduler;

import java.util.List;
import models.Node;

public interface ValidNodeFinderInterface {

	public void findRootNodes(List<Node> nodes);
	
	public List<Node> findSatisfiedNodes(Node n);
	
}
