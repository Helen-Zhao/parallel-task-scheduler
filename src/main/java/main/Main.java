package main;

import inputoutput.InputReader;
import inputoutput.OutputWriter;
import models.Edge;
import models.Node;
import scheduler.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author helen zhao
 *         Created 08/08/16
 */

public class Main {

    public static void main(String[] args) throws IllegalArgumentException {
        /*
            Input params take the form of: input file; number of processors p;
			where input.dot is the input file and p is the number of processors
		 */

        File inputFile = new File(args[0]);

        int numProcessors;
        try {
            numProcessors = Integer.parseInt(args[1]);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("Error: Argument 2 (Number of Processors) " + args[1] + " is not a number.");
        }

        List<Node> nodeList;
        List<Edge> edgeList;

        try {
            InputReader inputReader = new InputReader(inputFile);
            nodeList = inputReader.nodeList;
            edgeList = inputReader.edgeList;
        } catch (IOException io) {
            throw new IllegalArgumentException("Error: invalid input .dot file or location/filepath");
        }

        ValidNodeFinderInterface validNodeFinder = new ValidNodeFinder();
        ProcessorAllocatorInterface processorAllocator = new ProcessorAllocator(numProcessors);
        SchedulerInterface scheduler;

        scheduler = new Mem_DepthFirst_BaB_Scheduler(validNodeFinder, processorAllocator);
        List<Node> optimalSchedule = scheduler.createSchedule(nodeList);

        OutputWriter outputWriter = new OutputWriter(optimalSchedule, edgeList, "INPUT-output");

    }

}
