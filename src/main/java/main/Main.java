package main;

import graphgui.GraphGUI;
import inputoutput.InputReader;
import inputoutput.OutputWriter;
import models.Edge;
import models.Node;
import models.NodeTuple;
import scheduler.*;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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
    public static GraphGUI gui;
	
    private static List<Node> nodeList;
    private static HashMap<String, NodeTuple> optimalInfo;

    public static void main(String[] args) throws IllegalArgumentException {
    	
        if (args.length < 2) {
            throw new IllegalArgumentException("Error: Not enough parameters. Please use the following argument format: <input-file-path> <number of processors>");
        }

        String inputName = args[0];

        //Flags for optional params
        boolean hasOutputName = false;
        boolean visualisation = false;
        boolean parallelisation = false;

        String outputFile = "";
        int numProcessors;
        int numCores = 1;


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
                            numCores = Integer.parseInt(args[i + 1]);
                            break;
                    }
                }

            }
        }

        System.out.println("Processing...");

        File inputFile = new File(inputName);

        try {
            numProcessors = Integer.parseInt(args[1]);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("Error: Argument 2 (Number of Processors) " + args[1] + " is not a number.");
        }

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
        
        // Checks what mode to run program in, depending on input arguments
        if(parallelisation) {
        	if(visualisation) {
        		EventQueue.invokeLater(new Runnable() {
            		public void run() {
            			try {
                    		gui = new GraphGUI(numProcessors);
                    		gui.setVisible(true);
                    	} catch (Exception e) {
                    		e.printStackTrace();
                    	}
            		}
            	});
        		MasterSchedulerInterface scheduler = V_MasterScheduler.getInstance(numCores, numProcessors);
            	scheduler.createSchedule(nodeList, edgeList);
            	optimalInfo = scheduler.getSchedule();
        	}
        	else {
        		MasterSchedulerInterface scheduler = MasterScheduler.getInstance(numCores, numProcessors);
            	scheduler.createSchedule(nodeList, edgeList);
            	optimalInfo = scheduler.getSchedule();
        	}
        } else {
        	if (visualisation) {
            	EventQueue.invokeLater(new Runnable() {
            		public void run() {
            			try {
                    		gui = new GraphGUI(numProcessors);
                    		gui.setVisible(true);
                    	} catch (Exception e) {
                    		e.printStackTrace();
                    	}
            		}
            	});
            	
            	SchedulerInterface scheduler = new Visual_DFS_BaB_Scheduler(validNodeFinder, processorAllocator);
            	scheduler.createSchedule(nodeList, edgeList);
                optimalInfo = scheduler.getSchedule();
                
            }
            else {
            	SchedulerInterface scheduler = new DepthFirst_BaB_Scheduler(validNodeFinder, processorAllocator);
            	scheduler.createSchedule(nodeList, edgeList);
                optimalInfo = scheduler.getSchedule();
            }  
        }

        // Generates output file
        String outputFileName = hasOutputName ? outputFile : format(inputName) + "-output";
        OutputWriter outputWriter = new OutputWriter();
        outputWriter.writeFile(nodeList, optimalInfo, edgeList, outputFileName);
        System.out.println("Completed.");

    }

    public static HashMap<String, NodeTuple> getOptimalSchedule() {
        return optimalInfo;
    }
    
    public static List<Node> getNodeList() {
    	return nodeList;
    }

    // Formats input name
    private static String format(String rawInputName) {
        if (rawInputName.contains(File.separator)){
            return rawInputName.substring(rawInputName.lastIndexOf(File.separator), rawInputName.indexOf(".dot"));
        } else {
            return rawInputName.substring(0, rawInputName.indexOf(".dot"));
        }
    }
}
