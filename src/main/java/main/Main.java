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
 * @author Helen Zhao
 *         <p>
 *         Main is the entry point for the program and takes the arguments: <input-file> <num processors> <optional flags>
 *         in that order. Flags that can be used:
 *         -o OUTPUT_NAME	manually specify output file name
 *         -v	enable visualisation
 *         -p	enable parallelisation
 */

public class Main {
    private static List<Node> optimalSchedule;

    public static void main(String[] args) throws IllegalArgumentException {

        if (args.length < 2) {
            throw new IllegalArgumentException("Error: Not enough parameters. Please use the following argument format: <input-file-path> <number of processors>");
        }

        //Flags for optional params
        boolean hasOutputName = false;
        boolean visualisation = false;
        boolean parallelisation = false;

        String outputFile = "";
        int numThreads;


        //If there are extra parameters specified
        if (args.length > 2) {
            for (int i = 2; i < args.length; i++) {
                //If it is a flag param, identify what it is
                if (args[i].substring(0, 1).equals("-")) {
                    switch (args[i]) {
                        case "-o":
                            hasOutputName = true;
                            outputFile = args[i + 1];
                            break;
                        case "-v":
                            visualisation = true;
                            break;
                        case "-p":
                            parallelisation = true;
                            numThreads = Integer.parseInt(args[i + 1]);
                            break;
                    }
                }

            }
        }

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
            InputReader inputReader = new InputReader();
            inputReader.readFile(inputFile);
            nodeList = inputReader.getNodeList();
            edgeList = inputReader.getEdgeList();
        } catch (IOException io) {
            throw new IllegalArgumentException("Error: invalid input .dot file or location/filepath");
        }

        ValidNodeFinderInterface validNodeFinder = new ValidNodeFinder();
        ProcessorAllocatorInterface processorAllocator = new ProcessorAllocator(numProcessors);
        SchedulerInterface scheduler;

        scheduler = new Mem_DepthFirst_BaB_Scheduler(validNodeFinder, processorAllocator);
        optimalSchedule = scheduler.createSchedule(nodeList);

        String outputFileName = hasOutputName ? outputFile : format(args[0]) + "-output";
        OutputWriter outputWriter = new OutputWriter();
        outputWriter.writeFile(optimalSchedule, edgeList, outputFileName);

    }

    public static List<Node> getOptimalSchedule() {
        return optimalSchedule;
    }

    private static String format(String rawInputName) {
        return rawInputName.substring(rawInputName.lastIndexOf(File.separator), rawInputName.indexOf(".dot"));
    }

}
