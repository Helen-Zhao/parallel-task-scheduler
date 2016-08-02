package scheduler;

import models.Edge;
import models.Node;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by helen on 28/07/2016.
 */
public class SchedulerTest {

    @Test
    public void testScheduler() {
        ValidNodeFinderInterface nodeFinderMock = mock(ValidNodeFinderInterface.class);
        ProcessorAllocatorInterface processorAllocatorMock = mock(ProcessorAllocator.class);
        SchedulerInterface scheduler = new Scheduler(nodeFinderMock, processorAllocatorMock);


        List<Node> nodeList = new ArrayList<Node>();
        Node a = new Node("a", 1);
        Node b = new Node("b", 2);
        b.addIncomingEdge(new Edge(a, b, 1));

        nodeList.add(a);
        nodeList.add(b);

        List<Node> expected = new ArrayList<Node>();
        expected.add(a);
        when(nodeFinderMock.findRootNodes(nodeList)).thenReturn(expected);

        List<Node> expected2 = new ArrayList<Node>();
        expected.add(b);
        when(nodeFinderMock.checkDependentNodes(a)).thenReturn(expected2);




        when(processorAllocatorMock.allocateProcessor(new ArrayList<Node>(), a, new ArrayList<Integer>())).thenReturn(true);
        when(processorAllocatorMock.allocateProcessor(new ArrayList(expected), b, new ArrayList<Integer>())).thenReturn(true);
        when(processorAllocatorMock.allocateProcessor(new ArrayList(expected), b, new ArrayList<Integer>())).thenReturn(true);
        when(processorAllocatorMock.allocateProcessor(new ArrayList<Node>(), a, new ArrayList<Integer>())).thenReturn(false);


        List<Node> schedule = scheduler.createSchedule(nodeList);


    }
}
