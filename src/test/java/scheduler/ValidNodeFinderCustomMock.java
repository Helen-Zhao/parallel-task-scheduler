package scheduler;
import java.util.ArrayList;
import java.util.List;

import models.Node;
import scheduler.ValidNodeFinderInterface;

	public class ValidNodeFinderCustomMock implements ValidNodeFinderInterface {
		
		@Override
		public List<Node> findRootNodes(List<Node> nodes) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<Node> findSatisfiedChildren(Node node) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<Node> findSatisfiedNodes(List<Node> nodes) {
			List<Node> snodes = new ArrayList<Node>();
			for(Node node : nodes) {
				if (node.getName() == "a" && !(node.getHasRun())) {
					snodes.add(node);
					return snodes;
				}
			}
			for(Node node : nodes) {
				if (node.getName() == "b" && !(node.getHasRun())) {
					snodes.add(node);
				}
				if (node.getName() == "c" && !(node.getHasRun())) {
					snodes.add(node);
				}
			}
			if (snodes.size() == 0) {
				for(Node node : nodes) {
					if (node.getName() == "d" && !(node.getHasRun())) {
						snodes.add(node);
						return snodes;
					}
				}
			}
			return snodes;
		}
	}