package scheduler;

import models.Edge;
import models.Node;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

/**
 * Created by helen on 6/08/16.
 */
public class DepthFirst_BaB_SchedulerTest {

    @Test
    public void testBaB_Scheduler() {

        ValidNodeFinderInterface nodeFinderMock = mock(ValidNodeFinder.class);
        ProcessorAllocatorInterface processorAllocatorMock = mock(ProcessorAllocator.class);

        SchedulerInterface scheduler = new DepthFirst_BaB_Scheduler(nodeFinderMock, processorAllocatorMock);

        Node a = new Node("a", 1);
        Node b = new Node("b", 1);
        a.addOutgoingEdge(new Edge(a, b, 1));
        b.addIncomingEdge(new Edge(a, b, 1));

        List<Node> initialNodeList = new ArrayList<>();
        initialNodeList.add(a);
        initialNodeList.add(b);

        List<Node> justA = new ArrayList<>();
        justA.add(a);

        List<Node> justB = new ArrayList<>();
        justB.add(b);

        List<Node> processedNodeList = new ArrayList<>();
        Node allocatedA = new Node("a", 1);
        allocatedA.setProcessor(1);
        allocatedA.setStartTime(0);
        allocatedA.setHasRun(true);
        processedNodeList.add(allocatedA);
        processedNodeList.add(b);

        List<Node> justAllocatedA = new ArrayList<>();
        justAllocatedA.add(allocatedA);

        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                List<Node> nodes = (List<Node>) args[0];
                if (!nodes.get(0).getHasRun() && !nodes.get(1).getHasRun()) {
                    List<Node> toReturn = new ArrayList<Node>();
                    toReturn.add(new Node("a", 1));
                    return toReturn;
                } else if (nodes.get(0).getHasRun() && !nodes.get(1).getHasRun()) {
                    List<Node> toReturn = new ArrayList<Node>();
                    toReturn.add(new Node("b", 1));
                    return toReturn;
                } else {
                    return new ArrayList<Node>();
                }
            }
        }).when(nodeFinderMock).findSatisfiedNodes(anyList());

        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();

                if (((Node) args[1]).getName().equals("a")) {
                    ((Node) args[1]).setHasRun(true);
                    ((Node) args[1]).setStartTime(0);
                    ((Node) args[1]).setProcessor(1);
                    return true;
                } else {
                    ((Node) args[1]).setHasRun(true);
                    ((Node) args[1]).setStartTime(1);
                    ((Node) args[1]).setProcessor(1);
                    return false;
                }
            }
        }).when(processorAllocatorMock).allocateProcessor(anyList(), anyObject(), eq(null));


        List<Node> expectedOptimalSchedule = new ArrayList<>();
        Node allocatedB = new Node("b", 1);
        allocatedB.setHasRun(true);
        allocatedB.setProcessor(1);
        allocatedB.setStartTime(1);
        expectedOptimalSchedule.add(allocatedA);
        expectedOptimalSchedule.add(allocatedB);

        List<Node> actualOptimalSchedule = scheduler.createSchedule(initialNodeList);

        assertEquals(expectedOptimalSchedule, actualOptimalSchedule);

    }
}
