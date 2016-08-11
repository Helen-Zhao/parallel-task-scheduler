package scheduler;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import models.Edge;
import models.Node;

public class DepthFirst_BaB_SchedulerTest {
	
	@Test
	public void simpleLitmusTest() {
		SchedulerInterface sched = new Mem_DepthFirst_BaB_Scheduler(new ValidNodeFinderCustomMock(), new ProcessorAllocatorCustomMock());
		
		List<Node> nodes = new ArrayList<Node>();
		Node a = new Node("a", 2);
		Node b = new Node("b", 2);
		Node c = new Node("c", 3);
		Node d = new Node("d", 2);
		Edge ab = new Edge(a, b, 2);
		Edge ac = new Edge(a, c, 1);
		Edge bd = new Edge(b, d, 2);
		Edge cd = new Edge(c, d, 1);
		
		a.addOutgoingEdge(ab);
		a.addOutgoingEdge(ac);
		b.addIncomingEdge(ab);
		b.addOutgoingEdge(bd);
		c.addIncomingEdge(ac);
		c.addOutgoingEdge(cd);
		d.addIncomingEdge(bd);
		d.addIncomingEdge(cd);
		
		nodes.add(a);
		nodes.add(b);
		nodes.add(c);
		nodes.add(d);
		
		List<Node> schedNodes = sched.createSchedule(nodes);
		
		for(Node node : schedNodes) {
			if (node.getName() == "a") {
				if (node.getStartTime() != 0 || node.getProcessor() != 1 || node.getHasRun() != true) {
					System.out.println("a failed");	
					System.out.println("a startTime:" + a.getStartTime() + " a processor:" + a.getProcessor() + " a hasRun:" + a.getHasRun());
					fail();
				}
			}
			if (node.getName() == "b") {
				if (node.getStartTime() != 4 || node.getProcessor() != 2 || node.getHasRun() != true) {
					System.out.println("b failed");
					System.out.println("b startTime:" + b.getStartTime() + " b processor:" + b.getProcessor() + " b hasRun:" + b.getHasRun());
					fail();
				}
			}
			if (node.getName() == "c") {
				if (node.getStartTime() != 2 || node.getProcessor() != 1 || node.getHasRun() != true) {
					System.out.println("c failed");
					System.out.println("c startTime:" + c.getStartTime() + " c processor:" + c.getProcessor() + " c hasRun:" + c.getHasRun());
					fail();
				}
			}
			if (node.getName() == "d") {
				if (node.getStartTime() != 6 || node.getProcessor() != 2 || node.getHasRun() != true) {
					System.out.println("d failed");
					System.out.println("d startTime:" + d.getStartTime() + " d processor:" + d.getProcessor() + " d hasRun:" + d.getHasRun());
					fail();
				}
			}
		}
		
	}
}
