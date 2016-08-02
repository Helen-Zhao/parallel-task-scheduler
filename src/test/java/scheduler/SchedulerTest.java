package scheduler;

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
        ValidNodeFinderInterface nodeFinderMock = mock(ValidNodeFinder.class);
        ProcessorAllocatorInterface processorAllocatorMock = mock(ProcessorAllocator.class);
        SchedulerInterface scheduler = new Scheduler(nodeFinderMock, processorAllocatorMock);


        List<Node> nodeList = new ArrayList<Node>();
        nodeList.add(new Node("a", 1));
        nodeList.add(new Node("b", 2));
        nodeList.add(new Node("c", 3));
        nodeList.add(new Node("d", 4));

        when(nodeFinderMock.checkDependentNodes(nodeList.get(0))).thenReturn();

        List<Node> schedule = scheduler.createSchedule(nodeList);


    }
}
