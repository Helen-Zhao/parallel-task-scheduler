package scheduler;

import models.Edge;
import models.Node;
import models.NodeTuple;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Henry Wu
 */
public class ProcessorAllocatorTest {
    Node a, b, c, d;
    List<Node> schedule;
    ProcessorAllocator processorAllocator;
    List<Integer> unavailableProcessors;
    HashMap<String, NodeTuple> nodeInfo = new HashMap<String, NodeTuple>();

    @Before
    // A -> B; A -> C; B,C -> D
    public void init() {
        a = new Node("a", 1);
        b = new Node("b", 2);
        c = new Node("c", 3);
        d = new Node("d", 1);

        a.addOutgoingEdge(new Edge(a, b, 3));
        a.addOutgoingEdge(new Edge(a, c, 1));
        b.addOutgoingEdge(new Edge(b, d, 1));
        c.addOutgoingEdge(new Edge(c, d, 1));
        b.addIncomingEdge(new Edge(a, b, 3));
        c.addIncomingEdge(new Edge(a, c, 1));
        d.addIncomingEdge(new Edge(b, d, 1));
        d.addIncomingEdge(new Edge(c, d, 1));

        nodeInfo.put(a.getName(), new NodeTuple(0, 1, new ArrayList<Integer>(), true));

        nodeInfo.put(b.getName(), new NodeTuple(1, 1, new ArrayList<Integer>(), true));
        
        nodeInfo.put(c.getName(), new NodeTuple());
        nodeInfo.put(d.getName(), new NodeTuple());

        // Current schedule so far only has node 'a' and 'b' processed and assigned
        schedule = new ArrayList<Node>();
        schedule.add(a);
        schedule.add(b);

        processorAllocator = new ProcessorAllocator(2);
        processorAllocator.addNodeInfo(nodeInfo);
        
        processorAllocator.addToProcessor(a, 1);
        processorAllocator.addToProcessor(b, 1);
    }

    @Test
    // Tests when processor 2 is unavailable, leaving only 1 processor available
    // Tests that the correct Processor is generated
    public void allocateProcessorTest1() {
    	nodeInfo.get(c.getName()).addCheckedProcessor(2);

        int expectedProcessor = 1;

        processorAllocator.allocateProcessor(schedule, c);
        int actualProcessor = nodeInfo.get(c.getName()).getProcessor();

        assertEquals(expectedProcessor, actualProcessor);
        
        nodeInfo.get(c.getName()).resetCheckedProcessors();
    }

    // Tests when processor 2 is unavailable, leaving only 1 processor available
    // Tests that the correct Start Time is generated
    @Test
    public void allocateProcessorTest2() {
    	nodeInfo.get(c.getName()).addCheckedProcessor(2);

        int expectedStartTime = 3;

        processorAllocator.allocateProcessor(schedule, c);
        int actualStartTime = nodeInfo.get(c.getName()).getStartTime();

        assertEquals(expectedStartTime, actualStartTime);
        
        nodeInfo.get(c.getName()).resetCheckedProcessors();
    }

    // Tests when it has the option to use either processor 1 or 2, and chooses the best one
    // Tests that the correct Processor is generated
    @Test
    public void allocateProcessorTest3() {
        nodeInfo.put(c.getName(), new NodeTuple(2, 2, new ArrayList<Integer>(), true));
        schedule.add(c);
        int expectedProcessor = 2;

        processorAllocator.allocateProcessor(schedule, d);
        int actualProcessor = nodeInfo.get(d.getName()).getProcessor();

        assertEquals(expectedProcessor, actualProcessor);
        
        nodeInfo.get(d.getName()).resetCheckedProcessors();
    }

    // Tests when it has the option to use either processor 1 or 2, and chooses the best one
    // Tests that the correct Start Time is generated
    @Test
    public void allocateProcessorTest4() {
        // Assigns 'c' into the schedule
        nodeInfo.put(c.getName(), new NodeTuple(2, 2, new ArrayList<Integer>(), true));
        schedule.add(c);
        int expectedStartTime = 5;

        processorAllocator.allocateProcessor(schedule, d);
        int actualStartTime = nodeInfo.get(d.getName()).getStartTime();

        assertEquals(expectedStartTime, actualStartTime);
    }

    // Tests that the earliest Start Time is generated with an empty processor available
    @Test
    public void findEarliestStartTimeTest1() {
        int expected = 2;

        int actual = processorAllocator.findEarliestStartTime(schedule, c, 2);

        assertEquals(expected, actual);
    }

    // Tests that the earliest Start Time is generated with a schedule which already is utilising the specified processor
    @Test
    public void findEarliestStartTimeTest2() {
        int expected = 3;

        int actual = processorAllocator.findEarliestStartTime(schedule, c, 1);

        assertEquals(expected, actual);
    }

    // Tests that the earliest Start Time is generated with a node which has 2/multiple dependencies in the specified processor
    @Test
    public void findEarliestStartTimeTest3() {
        // Assigns 'c' into the schedule
        nodeInfo.put(c.getName(), new NodeTuple(2, 2, new ArrayList<Integer>(), true));
        schedule.add(c);
        int expected = 6;

        int actual = processorAllocator.findEarliestStartTime(schedule, d, 1);

        assertEquals(expected, actual);
    }
}
