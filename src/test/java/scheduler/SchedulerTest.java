package scheduler;

import inputoutput.InputReader;
import models.Edge;
import models.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Helen Zhao
 */
public class SchedulerTest {
    String dir;

    @Before
    public void setUp() {
        String workingDir = System.getProperty("user.dir");
        dir = ".";
        if (workingDir.length() > 0 && workingDir.contains("src")) {
            dir = workingDir.substring(0, workingDir.indexOf(File.separator + "src"));
        }
    }

    @Test
    public void testNodesSevenOutTree2Proc() {
        int numProcessors = 2;

        List<Node> nodeList;

        File inputFile = new File(dir + "/src/test/resources/dotfiles/input/Nodes_7_OutTree.dot");

        try {
            InputReader inputReader = new InputReader();
            inputReader.readFile(inputFile);
            nodeList = inputReader.getNodeList();
        } catch (IOException io) {
            throw new IllegalArgumentException("Error: invalid input .dot file or location/filepath");
        }

        ValidNodeFinderInterface validNodeFinder = new ValidNodeFinder();
        ProcessorAllocatorInterface processorAllocator = new ProcessorAllocator(numProcessors);
        SchedulerInterface scheduler;

        scheduler = new Mem_DepthFirst_BaB_Scheduler(validNodeFinder, processorAllocator);
        List<Node> optimalSchedule = scheduler.createSchedule(nodeList);

        int latestTime = Integer.MIN_VALUE;
        for (Node node : optimalSchedule) {
//            System.out.println("name=" + node.getName() + " srtTime=" + node.getStartTime() + " processor=" + node.getProcessor() + " endTime=" + (node.getStartTime() + node.getWeight()));
            int currentEndTime = node.getStartTime() + node.getWeight();
            if (currentEndTime > latestTime) {
                latestTime = currentEndTime;
            }
        }

        assertThat(latestTime, equalTo(28));
    }

    @Test
    public void testNodesSevenOutTree4Proc() {
        int numProcessors = 4;

        List<Node> nodeList;

        File inputFile = new File(dir + "/src/test/resources/dotfiles/input/Nodes_7_OutTree.dot");

        try {
            InputReader inputReader = new InputReader();
            inputReader.readFile(inputFile);
            nodeList = inputReader.getNodeList();
        } catch (IOException io) {
            throw new IllegalArgumentException("Error: invalid input .dot file or location/filepath");
        }

        ValidNodeFinderInterface validNodeFinder = new ValidNodeFinder();
        ProcessorAllocatorInterface processorAllocator = new ProcessorAllocator(numProcessors);
        SchedulerInterface scheduler;

        scheduler = new Mem_DepthFirst_BaB_Scheduler(validNodeFinder, processorAllocator);
        List<Node> optimalSchedule = scheduler.createSchedule(nodeList);

        int latestTime = Integer.MIN_VALUE;
        for (Node node : optimalSchedule) {
//            System.out.println("name=" + node.getName() + " srtTime=" + node.getStartTime() + " processor=" + node.getProcessor() + " endTime=" + (node.getStartTime() + node.getWeight()));
            int currentEndTime = node.getStartTime() + node.getWeight();
            if (currentEndTime > latestTime) {
                latestTime = currentEndTime;
            }
        }
        assertThat(latestTime, equalTo(22));
    }

    @Test
    public void testNodesEightOutTree2Proc() {
        int numProcessors = 2;

        List<Node> nodeList;

        File inputFile = new File(dir + "/src/test/resources/dotfiles/input/Nodes_8_Random.dot");

        try {
            InputReader inputReader = new InputReader();
            inputReader.readFile(inputFile);
            nodeList = inputReader.getNodeList();
        } catch (IOException io) {
            throw new IllegalArgumentException("Error: invalid input .dot file or location/filepath");
        }

        ValidNodeFinderInterface validNodeFinder = new ValidNodeFinder();
        ProcessorAllocatorInterface processorAllocator = new ProcessorAllocator(numProcessors);
        SchedulerInterface scheduler;

        scheduler = new Mem_DepthFirst_BaB_Scheduler(validNodeFinder, processorAllocator);
        List<Node> optimalSchedule = scheduler.createSchedule(nodeList);

        int latestTime = Integer.MIN_VALUE;
        for (Node node : optimalSchedule) {
//            System.out.println("name=" + node.getName() + " srtTime=" + node.getStartTime() + " processor=" + node.getProcessor() + " endTime=" + (node.getStartTime() + node.getWeight()));
            int currentEndTime = node.getStartTime() + node.getWeight();
            if (currentEndTime > latestTime) {
                latestTime = currentEndTime;
            }
        }

        assertThat(latestTime, equalTo(581));
    }

    @Test
    public void testNodesEightOutTree4Proc() {
        int numProcessors = 4;

        List<Node> nodeList;

        File inputFile = new File(dir + "/src/test/resources/dotfiles/input/Nodes_8_Random.dot");

        try {
            InputReader inputReader = new InputReader();
            inputReader.readFile(inputFile);
            nodeList = inputReader.getNodeList();
        } catch (IOException io) {
            throw new IllegalArgumentException("Error: invalid input .dot file or location/filepath");
        }

        ValidNodeFinderInterface validNodeFinder = new ValidNodeFinder();
        ProcessorAllocatorInterface processorAllocator = new ProcessorAllocator(numProcessors);
        SchedulerInterface scheduler;

        scheduler = new Mem_DepthFirst_BaB_Scheduler(validNodeFinder, processorAllocator);
        List<Node> optimalSchedule = scheduler.createSchedule(nodeList);

        int latestTime = Integer.MIN_VALUE;
        for (Node node : optimalSchedule) {
//            System.out.println("name=" + node.getName() + " srtTime=" + node.getStartTime() + " processor=" + node.getProcessor() + " endTime=" + (node.getStartTime() + node.getWeight()));
            int currentEndTime = node.getStartTime() + node.getWeight();
            if (currentEndTime > latestTime) {
                latestTime = currentEndTime;
            }
        }

        assertThat(latestTime, equalTo(581));
    }

    @Test
    public void testNodesNineSeriesParallel2Proc() {
        int numProcessors = 2;

        List<Node> nodeList;

        File inputFile = new File(dir + "/src/test/resources/dotfiles/input/Nodes_9_SeriesParallel.dot");

        try {
            InputReader inputReader = new InputReader();
            inputReader.readFile(inputFile);
            nodeList = inputReader.getNodeList();
        } catch (IOException io) {
            throw new IllegalArgumentException("Error: invalid input .dot file or location/filepath");
        }

        ValidNodeFinderInterface validNodeFinder = new ValidNodeFinder();
        ProcessorAllocatorInterface processorAllocator = new ProcessorAllocator(numProcessors);
        SchedulerInterface scheduler;

        scheduler = new Mem_DepthFirst_BaB_Scheduler(validNodeFinder, processorAllocator);
        List<Node> optimalSchedule = scheduler.createSchedule(nodeList);

        int latestTime = Integer.MIN_VALUE;
        for (Node node : optimalSchedule) {
//            System.out.println("name=" + node.getName() + " srtTime=" + node.getStartTime() + " processor=" + node.getProcessor() + " endTime=" + (node.getStartTime() + node.getWeight()));
            int currentEndTime = node.getStartTime() + node.getWeight();
            if (currentEndTime > latestTime) {
                latestTime = currentEndTime;
            }
        }

        assertThat(latestTime, equalTo(55));
    }

    @Test
    public void testNodesNineSeriesParallel4Proc() {
        int numProcessors = 4;

        List<Node> nodeList;

        File inputFile = new File(dir + "/src/test/resources/dotfiles/input/Nodes_9_SeriesParallel.dot");

        try {
            InputReader inputReader = new InputReader();
            inputReader.readFile(inputFile);
            nodeList = inputReader.getNodeList();
        } catch (IOException io) {
            throw new IllegalArgumentException("Error: invalid input .dot file or location/filepath");
        }

        ValidNodeFinderInterface validNodeFinder = new ValidNodeFinder();
        ProcessorAllocatorInterface processorAllocator = new ProcessorAllocator(numProcessors);
        SchedulerInterface scheduler;

        scheduler = new Mem_DepthFirst_BaB_Scheduler(validNodeFinder, processorAllocator);
        List<Node> optimalSchedule = scheduler.createSchedule(nodeList);

        int latestTime = Integer.MIN_VALUE;
        for (Node node : optimalSchedule) {
//            System.out.println("name=" + node.getName() + " srtTime=" + node.getStartTime() + " processor=" + node.getProcessor() + " endTime=" + (node.getStartTime() + node.getWeight()));
            int currentEndTime = node.getStartTime() + node.getWeight();
            if (currentEndTime > latestTime) {
                latestTime = currentEndTime;
            }
        }
        assertThat(latestTime, equalTo(55));
    }

    @Test
    public void testNodesTenSeriesParallel2Proc() {
        int numProcessors = 2;

        List<Node> nodeList;

        File inputFile = new File(dir + "/src/test/resources/dotfiles/input/Nodes_10_Random.dot");

        try {
            InputReader inputReader = new InputReader();
            inputReader.readFile(inputFile);

            nodeList = inputReader.getNodeList();
        } catch (IOException io) {
            throw new IllegalArgumentException("Error: invalid input .dot file or location/filepath");
        }

        ValidNodeFinderInterface validNodeFinder = new ValidNodeFinder();
        ProcessorAllocatorInterface processorAllocator = new ProcessorAllocator(numProcessors);
        SchedulerInterface scheduler;

        scheduler = new Mem_DepthFirst_BaB_Scheduler(validNodeFinder, processorAllocator);
        List<Node> optimalSchedule = scheduler.createSchedule(nodeList);

        int latestTime = Integer.MIN_VALUE;
        for (Node node : optimalSchedule) {
//            System.out.println("name=" + node.getName() + " srtTime=" + node.getStartTime() + " processor=" + node.getProcessor() + " endTime=" + (node.getStartTime() + node.getWeight()));
            int currentEndTime = node.getStartTime() + node.getWeight();
            if (currentEndTime > latestTime) {
                latestTime = currentEndTime;
            }
        }

        assertThat(latestTime, equalTo(50));
    }

    @Test
    public void testNodesTenSeriesParallel4Proc() {
        int numProcessors = 4;

        List<Node> nodeList;

        File inputFile = new File(dir + "/src/test/resources/dotfiles/input/Nodes_10_Random.dot");

        try {
            InputReader inputReader = new InputReader();
            inputReader.readFile(inputFile);
            nodeList = inputReader.getNodeList();
        } catch (IOException io) {
            throw new IllegalArgumentException("Error: invalid input .dot file or location/filepath");
        }

        ValidNodeFinderInterface validNodeFinder = new ValidNodeFinder();
        ProcessorAllocatorInterface processorAllocator = new ProcessorAllocator(numProcessors);
        SchedulerInterface scheduler;

        scheduler = new Mem_DepthFirst_BaB_Scheduler(validNodeFinder, processorAllocator);
        List<Node> optimalSchedule = scheduler.createSchedule(nodeList);

        int latestTime = Integer.MIN_VALUE;
        for (Node node : optimalSchedule) {
//            System.out.println("name=" + node.getName() + " srtTime=" + node.getStartTime() + " processor=" + node.getProcessor() + " endTime=" + (node.getStartTime() + node.getWeight()));
            int currentEndTime = node.getStartTime() + node.getWeight();
            if (currentEndTime > latestTime) {
                latestTime = currentEndTime;
            }
        }

        assertThat(latestTime, equalTo(50));
    }

    @Test
    public void testNodesElevenSeriesParallel2Proc() {
        int numProcessors = 2;

        List<Node> nodeList;

        File inputFile = new File(dir + "/src/test/resources/dotfiles/input/Nodes_11_OutTree.dot");

        try {
            InputReader inputReader = new InputReader();
            inputReader.readFile(inputFile);
            nodeList = inputReader.getNodeList();
        } catch (IOException io) {
            throw new IllegalArgumentException("Error: invalid input .dot file or location/filepath");
        }

        ValidNodeFinderInterface validNodeFinder = new ValidNodeFinder();
        ProcessorAllocatorInterface processorAllocator = new ProcessorAllocator(numProcessors);
        SchedulerInterface scheduler;

        scheduler = new Mem_DepthFirst_BaB_Scheduler(validNodeFinder, processorAllocator);
        List<Node> optimalSchedule = scheduler.createSchedule(nodeList);

        int latestTime = Integer.MIN_VALUE;
        for (Node node : optimalSchedule) {
//            System.out.println("name=" + node.getName() + " srtTime=" + node.getStartTime() + " processor=" + node.getProcessor() + " endTime=" + (node.getStartTime() + node.getWeight()));
            int currentEndTime = node.getStartTime() + node.getWeight();
            if (currentEndTime > latestTime) {
                latestTime = currentEndTime;
            }
        }

        assertThat(latestTime, equalTo(350));
    }

    @Test
    public void testNodesElevenSeriesParallel4Proc() {
        int numProcessors = 4;

        List<Node> nodeList;
        List<Edge> edgeList;

        File inputFile = new File(dir + "/src/test/resources/dotfiles/input/Nodes_11_OutTree.dot");

        try {
            InputReader inputReader = new InputReader();
            inputReader.readFile(inputFile);
            nodeList = inputReader.getNodeList();
        } catch (IOException io) {
            throw new IllegalArgumentException("Error: invalid input .dot file or location/filepath");
        }

        ValidNodeFinderInterface validNodeFinder = new ValidNodeFinder();
        ProcessorAllocatorInterface processorAllocator = new ProcessorAllocator(numProcessors);
        SchedulerInterface scheduler;

        scheduler = new Mem_DepthFirst_BaB_Scheduler(validNodeFinder, processorAllocator);
        List<Node> optimalSchedule = scheduler.createSchedule(nodeList);

        int latestTime = Integer.MIN_VALUE;
        for (Node node : optimalSchedule) {
//            System.out.println("name=" + node.getName() + " srtTime=" + node.getStartTime() + " processor=" + node.getProcessor() + " endTime=" + (node.getStartTime() + node.getWeight()));
            int currentEndTime = node.getStartTime() + node.getWeight();
            if (currentEndTime > latestTime) {
                latestTime = currentEndTime;
            }
        }

        assertThat(latestTime, equalTo(227));
    }

}
