package scheduler;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import models.Edge;
import models.Node;
import scheduler.ValidNodeFinder;

public class ValidNodeFinderTest {
	Node a, b, c, x;
	List<Node> nodes;
	ValidNodeFinder vnf;
	
	@Before
	// A, X --> B --> C
	public void init() {
		a = new Node("a", 1);
		b = new Node("b", 2);
		c = new Node("c", 3);
		x = new Node("x", 4);
		
		a.addOutgoingEdge(new Edge(a, b, 0));
		b.addOutgoingEdge(new Edge(b, c, 0));
		b.addIncomingEdge(new Edge(a, b, 0));
		b.addIncomingEdge(new Edge(x, b, 0));
		c.addIncomingEdge(new Edge(b, c, 0));
		x.addOutgoingEdge(new Edge(x, b, 0));
		
		nodes = new ArrayList<Node>();
		nodes.add(a);
		nodes.add(b);
		nodes.add(c);
		nodes.add(x);
		
		vnf = new ValidNodeFinder();
	}
	
	@Test
	public void testFindRootNodes() {
		List<Node> expected = new ArrayList<Node>();
		expected.add(a);
		expected.add(x);

		List<Node> actual = vnf.findRootNodes(nodes);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testFindSatisfiedChildren() {
		// Should be empty initially
		List<Node> expected = new ArrayList<Node>();
		List<Node> actual = vnf.findSatisfiedChildren(a);
		assertEquals(expected, actual);
		
		// Should still be empty because node x has not been completed
		a.setHasRun(true);
		actual = vnf.findSatisfiedChildren(a);
		assertEquals(expected, actual);
		
		// Should contain node b as now both node a and x are complete
		x.setHasRun(true);
		expected.add(b);
		actual = vnf.findSatisfiedChildren(a);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testFindSatisfiedNodes() {
		List<Node> expected = new ArrayList<Node>();
		List<Node> inputList = new ArrayList<Node>();
		inputList.add(a);
		inputList.add(b);
		inputList.add(c);
		inputList.add(x);
		
		a.setHasRun(true);
		x.setHasRun(true);
		
		// b has been satisfied as a and x have run
		expected.add(b);
		
		List<Node> actual = vnf.findSatisfiedNodes(inputList);
		
		assertEquals(expected,actual);
	}
}
