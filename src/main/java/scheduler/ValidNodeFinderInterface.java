package scheduler;

import java.util.List;
import models.Node;

public interface ValidNodeFinderInterface {

	List<Node> findRootNodes(List<Node> nodes);
	List<Node> checkDependentNodes(Node n);
	
}
