package scheduler;

import models.Edge;
import models.Node;
import models.NodeTuple;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Implementation of Master Scheduler Interface. Keeps track of multiple threads when using
 * parallelisation.
 *
 * @author Benjamin Collins, Jacky Mai
 */

public class MasterScheduler implements MasterSchedulerInterface {
    private static MasterSchedulerInterface masterScheduler;
    private List<ParallelSchedulerInterface> schedulerList;
    private List<Node> nodeList;
    private HashMap<String, NodeTuple> optimalSchedule;
    private HashMap<String, NodeTuple> scheduleInfo = new HashMap<String, NodeTuple>();
    private int bestBound = 0;
    private static int traverseThreads;
    private static int numProcessors;

    private Queue<ComparisonTuple> comparisonQueue = new LinkedList<ComparisonTuple>();
    private Queue<SubpathTuple> subpathQueue;

    // Prevents new objects of this class from being instantiated
    private MasterScheduler() {
    }

    public static MasterSchedulerInterface getInstance() {
        if (masterScheduler == null) {
            masterScheduler = new MasterScheduler();
        }

        return masterScheduler;
    }

    public static MasterSchedulerInterface getInstance(int numCores, int numProcessors) {
        MasterScheduler.traverseThreads = numCores - 1;
        MasterScheduler.numProcessors = numProcessors;

        if (masterScheduler == null) {
            masterScheduler = new MasterScheduler();
        }

        return masterScheduler;
    }

    public synchronized void compare(HashMap<String, NodeTuple> schedule, int scheduleBound) {
        this.comparisonQueue.add(new ComparisonTuple(schedule, scheduleBound));
    }

    public void initiateNewSubpathTuple(ParallelSchedulerInterface scheduler) {
        SubpathTuple tuple = getSubpathTuple();
        if (tuple != null) {
            scheduler.initiateNewSubtree(this.nodeList, tuple.processorAllocator, tuple.nodeStack, this.bestBound, tuple.scheduleInfo, tuple.schedule);
        }
    }

    @Override
    public HashMap<String, NodeTuple> getSchedule() {
        return optimalSchedule;
    }

    @Override
    public void createSchedule(List<Node> nodeList, List<Edge> edgeList) {
        this.nodeList = nodeList;
        this.schedulerList = new ArrayList<ParallelSchedulerInterface>(traverseThreads);

        // Initially bestBound is equivalent to serial schedule
        for (Node n : nodeList) {
            this.bestBound += n.getWeight();
            scheduleInfo.put(n.getName(), new NodeTuple());
        }


        // Estimate heuristic costs
        boolean hasDistanceChanged = true;
        while (hasDistanceChanged) {
            hasDistanceChanged = false;
            for (Edge edge : edgeList) {
                int newCritPathLength = edge.getEndNode().getCriticalPathLength() + edge.getStartNode().getWeight();
                if (newCritPathLength > edge.getStartNode().getCriticalPathLength()) {
                    edge.getStartNode().setCriticalPathLength(newCritPathLength);
                    hasDistanceChanged = true;
                }
            }
        }

        // Create schedulers and pass in appropriate partial schedule
        subpathQueue = createSubpathTuples();

        ExecutorService executorService = Executors.newFixedThreadPool(traverseThreads);
        List<Runnable> runnableList = new ArrayList<Runnable>(traverseThreads);

        for (int i = 0; i < traverseThreads; i++) {
            runnableList.add(new Runnable() {
                public void run() {
                    SubpathTuple tuple = getSubpathTuple();
                    if (tuple != null) {
                        ParallelSchedulerInterface scheduler = new Parallel_DFS_BaB_Scheduler(new ValidNodeFinder(), tuple.processorAllocator);
                        schedulerList.add(scheduler);

                        scheduler.initiateNewSubtree(nodeList, tuple.processorAllocator, tuple.nodeStack, bestBound, tuple.scheduleInfo, tuple.schedule);
                    }
                }
            });
        }

        for (Runnable runnable : runnableList) {
            executorService.execute(runnable);
        }

        executorService.shutdown();

        while (executorService.isTerminated() == false || comparisonQueue.isEmpty() == false) {
            checkQueue();
        }
    }

    private void checkQueue() {
        while (comparisonQueue.isEmpty() == false) {
            ComparisonTuple tuple = comparisonQueue.remove();
            compareBounds(tuple.schedule, tuple.scheduleBound);
        }
    }

    private synchronized void compareBounds(HashMap<String, NodeTuple> schedule, int scheduleBound) {
        if (scheduleBound < this.bestBound) {
            this.bestBound = scheduleBound;

            notifyAllSchedulers(this.bestBound);

            optimalSchedule = schedule;

        } else if (scheduleBound == this.bestBound && optimalSchedule == null) {
            optimalSchedule = schedule;
        }
    }

    private synchronized void notifyAllSchedulers(int bestBound) {
        for (ParallelSchedulerInterface scheduler : schedulerList) {
            scheduler.setBestBound(bestBound);
        }
    }

    private SubpathTuple cloneSubpathTuple(SubpathTuple tuple) {
        List<Queue<Node>> newNodeStack = new ArrayList<Queue<Node>>();
        ProcessorAllocator newProcessorAllocator = new ProcessorAllocator(numProcessors);
        List<Node> newSchedule = new ArrayList<Node>(nodeList.size());

        // Clone the node stack into newNodeStack
        for (Queue<Node> queue : tuple.nodeStack) {
            Queue<Node> newQueue = new LinkedList<Node>();

            if (queue.isEmpty() == false) {
                newQueue.add(queue.peek());
                newNodeStack.add(newQueue);
            }
        }

        HashMap<String, NodeTuple> scheduleInfo = tuple.scheduleInfo;
        HashMap<String, NodeTuple> newScheduleInfo = new HashMap<String, NodeTuple>();
        newProcessorAllocator.addNodeInfo(newScheduleInfo);

        // Cloning ProcessorAllocator and ScheduleInfo from SubpathTuple
        for (Node node : this.nodeList) {
            NodeTuple nodeTuple = scheduleInfo.get(node.getName());
            NodeTuple newNodeTuple = nodeTuple.clone();
            newScheduleInfo.put(node.getName(), newNodeTuple);

            if (newNodeTuple.getHasRun() == true) {
                newProcessorAllocator.addToProcessor(node, newNodeTuple.getProcessor()); // Add scheduled node to processor object
                newSchedule.add(node); //Add to ScheduledNodes
            }

        }

        SubpathTuple clonedTuple = new SubpathTuple(newNodeStack, newProcessorAllocator, newScheduleInfo, newSchedule);

        return clonedTuple;
    }

    private Queue<SubpathTuple> createSubpathTuples() {
        // Find root nodes to work from
        ValidNodeFinder nodeFinder = new ValidNodeFinder();
        List<Node> rootNodes = nodeFinder.findRootNodes(this.nodeList);

        // Store all root nodes into a queue
        Queue<Node> rootNodeQueue = new LinkedList<Node>();
        rootNodeQueue.addAll(rootNodes);

        // Queue of subpath tuples used for ParallelScheduler
        Queue<SubpathTuple> subpathQueue = new LinkedList<SubpathTuple>();

        // Create empty node stack for SubpathTuple
        List<Queue<Node>> nodeStack = new ArrayList<Queue<Node>>();
        nodeStack.add(0, rootNodeQueue);    // Add root nodes onto the stack

        // Create tuple to store in queue
        ProcessorAllocatorInterface processorAllocatorInitial = new ProcessorAllocator(numProcessors);
        processorAllocatorInitial.addNodeInfo(this.scheduleInfo);
        SubpathTuple subpathTuple = new SubpathTuple(nodeStack, processorAllocatorInitial, this.scheduleInfo, new ArrayList<Node>());
        subpathQueue.add(subpathTuple);

        // Initially subpaths are equal to root nodes, only one processor is allocated, others will just be mirrors
        int numSubpath = rootNodeQueue.size();
        int nextNumSubpath = 0;
        int heuristic = (int) Math.min(Math.ceil(traverseThreads * 1.5), nodeList.size() / 2);
        if (heuristic < traverseThreads){
            heuristic = traverseThreads;
        }
        int level = 0;

        while (numSubpath < heuristic) {
            SubpathTuple tuple = subpathQueue.peek();
            nodeStack = tuple.nodeStack;
            ProcessorAllocatorInterface processorAllocator = tuple.processorAllocator;

            if (level != nodeStack.size() - 1) {
                level = nodeStack.size() - 1;
                numSubpath = nextNumSubpath;
                nextNumSubpath = 0;
                continue;
            }

            subpathQueue.remove();

            Queue<Node> nodeQueue = nodeStack.get(level);

            // Loop through nodes on this level
            while (nodeQueue.size() > 0) {
                List<Node> schedule = new ArrayList<Node>();
                Node currentNode = nodeQueue.peek();

                int currentProcessor = this.scheduleInfo.get(currentNode.getName()).getProcessor();
                processorAllocator.removeFromProcessor(currentNode, currentProcessor);

                // Loop to new processor on same dependent node
                while (processorAllocator.allocateProcessor(schedule, currentNode)) {
                    SubpathTuple newSubpathTuple = cloneSubpathTuple(tuple);
                    nodeFinder.addNodeInfo(newSubpathTuple.scheduleInfo);

                    List<Queue<Node>> newNodeStack = newSubpathTuple.nodeStack;

                    List<Node> satisfiedNodes = nodeFinder.findSatisfiedNodes(this.nodeList);
                    nextNumSubpath += satisfiedNodes.size() * Math.max(level + 1, numProcessors);

                    Queue<Node> newNodeQueue = new LinkedList<Node>();
                    newNodeQueue.addAll(satisfiedNodes);

                    newNodeStack.add(newNodeQueue);
                    subpathQueue.add(newSubpathTuple);
                }

                nodeQueue.remove();

                this.scheduleInfo.put(currentNode.getName(), new NodeTuple());
            }
        }

        return subpathQueue;
    }

    private synchronized SubpathTuple getSubpathTuple() {
        while (subpathQueue.size() > 0) {
            SubpathTuple subpathTuple = subpathQueue.peek();
            List<Queue<Node>> nodeStack = subpathTuple.nodeStack;
            Queue<Node> queue = nodeStack.get(nodeStack.size() - 1);

            ProcessorAllocatorInterface processorAllocator = subpathTuple.processorAllocator;
            Node node;

            while (queue.size() > 0) {
                node = queue.peek();

                int processor = subpathTuple.scheduleInfo.get(node.getName()).getProcessor();
                if (processor != -1) {
                    processorAllocator.removeFromProcessor(node, processor);
                }

                if (processorAllocator.allocateProcessor(subpathTuple.schedule, node)) {
                    SubpathTuple clone = cloneSubpathTuple(subpathTuple);

                    return clone;
                } else {
                    subpathTuple.scheduleInfo.put(node.getName(), new NodeTuple());
                    queue.remove();
                }
            }

            subpathQueue.remove();
        }

        return null;
    }

    private class ComparisonTuple {
        public HashMap<String, NodeTuple> schedule;
        public int scheduleBound;

        public ComparisonTuple(HashMap<String, NodeTuple> schedule, int scheduleBound) {
            this.schedule = schedule;
            this.scheduleBound = scheduleBound;
        }
    }

    private class SubpathTuple {
        public List<Node> schedule;
        public List<Queue<Node>> nodeStack;
        public ProcessorAllocatorInterface processorAllocator;
        public HashMap<String, NodeTuple> scheduleInfo;

        public SubpathTuple(List<Queue<Node>> nodeStack, ProcessorAllocatorInterface processorAllocator,
                            HashMap<String, NodeTuple> scheduleInfo, List<Node> schedule) {
            this.schedule = schedule;
            this.nodeStack = nodeStack;
            this.processorAllocator = processorAllocator;
            this.scheduleInfo = scheduleInfo;
        }
    }
}