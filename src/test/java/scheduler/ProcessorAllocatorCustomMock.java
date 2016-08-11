package scheduler;
import java.util.List;

import models.Node;
import scheduler.ProcessorAllocatorInterface;

public class ProcessorAllocatorCustomMock implements ProcessorAllocatorInterface {
		public boolean allocateProcessor(List<Node> schedule, Node node, List<Integer> checkedProcessors) {
			
			if(node.getName() == "a") {
				node.setStartTime(0);
				node.setProcessor(1);
				node.setHasRun(true);
			}
			if(node.getName() == "b") {
				if(node.getStartTime() == -1) {
					node.setStartTime(2);
					node.setProcessor(1);
					node.setHasRun(true);
				} else {
					node.setStartTime(4);
					node.setProcessor(2);
					node.setHasRun(true);
				}
			}
			if(node.getName() == "c") {
				if(node.getStartTime() == -1) {
					node.setStartTime(3);
					node.setProcessor(2);
					node.setHasRun(true);
				} else {
					node.setStartTime(2);
					node.setProcessor(1);
					node.setHasRun(true);
				}
			}
			if(node.getName() == "d") {
				node.setStartTime(6);
				node.setProcessor(2);
				node.setHasRun(true);
			}
			return false;
		}

		@Override
		public int findEarliestStartTime(List<Node> schedule, Node node, int processor) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getNumberProcessors() {
			return 2;
		}
		
}
