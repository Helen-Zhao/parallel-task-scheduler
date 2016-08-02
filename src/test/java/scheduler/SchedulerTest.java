package scheduler;

import models.Node;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by helen on 28/07/2016.
 */
public class SchedulerTest {

    @Test
    public void testScheduler() {
        SchedulerInterface scheduler = new Scheduler();

        List<Node> nodeList = new ArrayList<Node>();
        nodeList.add(new Node("a", 1));
        nodeList.add(new Node("b", 2));
        nodeList.add(new Node("c", 3));
        nodeList.add(new Node("d", 4));
        List<Node> schedule = scheduler.createSchedule(nodeList);


    }
}
