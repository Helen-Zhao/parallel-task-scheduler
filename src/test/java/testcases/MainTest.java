package testcases;

import main.Main;
import models.Edge;
import models.Node;

import org.junit.Before;
import org.junit.Test;

import junitx.framework.FileAssert;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;


public class MainTest {

    String dir;

    @Before
    public void setUp() {
        String workingDir = System.getProperty("user.dir");
        dir = ".";
        if (workingDir.length() > 0 && workingDir.contains("/src")) {
            dir = workingDir.substring(0, workingDir.indexOf("/src"));
        }
    }

    @Test
    public void oneProcSimpleTest() {

        // Expected output file
        File expected = new File(dir + "/src/main/resources/dotfiles/outputfiles/1_processor_simple-output.dot");

        // TODO
        /* Varying methods for obtaining the final output file which is to be compared with the expected output file. (Our implementation)
		  
		File input = new main.Main.readInput(dir + "/src/main/resources/dot_Files/Input_Files/1_processor_simple.dot");
		
		File output = main.Main.writeOutput();
		*/

        // FileAssert.assertEquals(expected, output);
    }

    @Test
    public void twoProcSimpleTest() {

        // Expected output file
        File expected = new File(dir + "/src/main/resources/dotfiles/outputfiles/2_processor_simple-output.dot");

        // TODO
		/* Varying methods for obtaining the final output file which is to be compared with the expected output file. (Our implementation)
		  
		File input = new main.Main.readInput(dir + "/src/main/resources/dot_Files/Input_Files/1_processor_simple.dot");
		
		File output = main.Main.writeOutput();
		*/

        // FileAssert.assertEquals(expected, output);
    }

    @Test
    public void fourProcOneSrcOneDestTest() {

        // Expected output file
        File expected = new File(dir + "/src/main/resources/dotfiles/outputfiles/4_processor_1_src_1_dest-output.dot");

        // TODO
		/* Varying methods for obtaining the final output file which is to be compared with the expected output file. (Our implementation)
		  
		File input = new main.Main.readInput(dir + "/src/main/resources/dot_Files/Input_Files/1_processor_simple.dot");
		
		File output = main.Main.writeOutput();
		*/

        // FileAssert.assertEquals(expected, output);
    }

    @Test
    public void fourProcOneSrcThreeDestTest() {

        // Expected output file
        // File expected = new File(dir + "/src/main/resources/dotfiles/outputfiles/4_processor_1_src_3_dest-output.dot");
    	int expectedEndTime = 16;
    	
        // String[] args = {dir + "/src/main/resources/dotfiles/inputfiles/4_processor_1_src_3_dest.dot", "4", "-o", "4_processor_1_src_3_dest-output"};
    	String[] args = {dir + "/src/main/resources/dotfiles/inputfiles/Nodes_7_OutTree.dot", "4", "-o", "Nodes_7_OutTree-output"};	
    	
    	Main.main(args);
        List<Node> optimalSchedule = Main.getOptimalSchedule();
        int latestEndTime = 0;
        
        for(Node node : optimalSchedule){
        	//Determine end time(duration) of schedule
        	int currentEndTime = node.getStartTime() + node.getWeight();
        	if(currentEndTime > latestEndTime) {
        		latestEndTime = currentEndTime;
        	}
        	
        	List<Edge> incomingEdges = node.getIncomingEdges();
        	for(Edge edge : incomingEdges) {
        		Node startNode = edge.getStartNode();
        		int startNodeProcessor = startNode.getProcessor();
        		int currentNodeProcessor = node.getProcessor();
        		int dependencySatisfiedTime;
        		
        		//Determine time dependency is completed
        		//If node in same processor ignore communication weight
        		if(startNodeProcessor == currentNodeProcessor){
        			dependencySatisfiedTime = startNode.getStartTime() + startNode.getWeight(); 
        		} else {
        			dependencySatisfiedTime = startNode.getStartTime() + startNode.getWeight() + edge.getWeight();
        		}
        		
        		//If task starts before dependencies are completed, fail
        		if(node.getStartTime() < dependencySatisfiedTime){
        			fail();
        		} 
        		
        	}
        	
        	int startTime = node.getStartTime();
        	int endTime = startTime + node.getWeight();
        	
        	//Checks for overlaps in tasks
        	//Compare with every other node whether they start during the processing of this node, on same processor
        	for(Node node2 : optimalSchedule) {
        		//Ignore comparison with self(same node)
        		if(node2 != node){
        			if(node2.getProcessor() == node.getProcessor()) {
        				int node2StartTime = node2.getStartTime();
        				if ((node2StartTime > startTime) && (node2StartTime < endTime)){
        					fail();
        				}
        			}
        		}
        	}
        }
        //Compare optimal time with calculated time
        assertEquals(expectedEndTime,latestEndTime);
        
        
        
    }

    @Test
    public void fourProcThreeSrcOneDestTest() {

        // Expected output file
        File expected = new File(dir + "/src/main/resources/dotfiles/outputfiles/4_processor_3_src_1_dest-output.dot");
        String[] args = {dir + "/src/main/resources/dotfiles/inputfiles/4_processor_3_src_1_dest.dot", "4", "-o", "4_processor_3_src_1_dest-output"};
        //Main.main(args);

        File output = new File(dir + "/src/main/resources/4_processor_3_src_1_dest-output.dot");
        //FileAssert.assertEquals(expected, output);
    }

    @Test
    public void fourProcThreeSrcTwoDestTest() {

        // Expected output file

        File expected = new File(dir + "/src/main/resources/dotfiles/outputfiles/4_processor_3_src_2_dest-output.dot");

        // TODO
		/* Varying methods for obtaining the final output file which is to be compared with the expected output file. (Our implementation)
		  
		File input = new main.Main.readInput(dir + "/src/main/resources/dot_Files/Input_Files/1_processor_simple.dot");
		
		File output = main.Main.writeOutput();
		*/

        // FileAssert.assertEquals(expected, output);
    }
}
