package inputoutput;

import models.Edge;
import models.Node;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by helen on 28/07/2016.
 * @author William Lin
 */
public class OutputWriter {

	//main.Main output writer function
	public OutputWriter(List<Node> scheduleNodes, List<Edge> scheduleEdges, String outputFileName) {
		
		try {
			//Get working directory
			String workingDir = System.getProperty("user.dir");
			String dir = ".";
			if (workingDir.length() > 0 && workingDir.contains("/src")) {
				dir = workingDir.substring(0, workingDir.indexOf("/src"));
			}

			//Instantiate PrintWriter object to create and write to file. Set encoding to UTF-8
			PrintWriter writer = new PrintWriter(dir + "/src/main/resources/" + outputFileName + ".dot", "UTF-8");
			writer.println("digraph \"" + outputFileName + "\" {");
			
			//iterate through list and print
			for(int i = 0; i < scheduleNodes.size(); i++){
				Node node = scheduleNodes.get(i);
				writer.println("\t\t" + node.getName() + "\t\t [Weight=" + node.getWeight() + ", Start=" + node.getStartTime() + ", Processor=" + node.getProcessor() + "];");
			}

			for (Edge edge : scheduleEdges) {
				writer.println("\t\t" + edge.getStartNode().getName() + " -> " + edge.getEndNode().getName() + "\t [Weight=" + edge.getWeight() + "];");
			}
			
			writer.print("}");

			writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
}