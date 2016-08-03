package inputoutput;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;

import models.Edge;
import models.Node;

/**
 * Created by helen on 28/07/2016.
 */
public class OutputWriter {
	
	private static HashSet<Node> printedNodes = new HashSet<Node>();
	
	//Main output writer function
	public OutputWriter(List<Node> scheduleNodes, List<Edge> scheduleEdges) {
		
		try {
			//Instantiate PrintWriter object to create and write to file. Set encoding to UTF-8
			PrintWriter writer = new PrintWriter("./src/main/resources/output.dot", "UTF-8");
			writer.println("digraph \"output\" {");
			
			int j = 0;
			//iterate through list and print
			for(int i = 0; i < scheduleNodes.size(); i++){
				Node node = scheduleNodes.get(i);
				Edge edge = scheduleEdges.get(j);
				//if start and end nodes of an edge is printed, then print the edge
				if ((printedNodes.contains(edge.getStartNode()))&&(printedNodes.contains(edge.getEndNode()))) {
					writer.println("\t\t" + edge.getStartNode().getName() + " -> " + edge.getEndNode().getName() + "\t [Weight=" + edge.getWeight() + "];");
					j++;
					//iterate through current node again as it is not printed
					i--;
				}
				else {
				writer.println("\t\t" + node.getName() + "\t\t [Weight=" + node.getWeight() + ", Start=" + node.getStartTime() + ", Processor=" + node.getProcessor() + "];");
				printedNodes.add(node);
				}
				//if i has iterated through the entire list, then print remaining edges
				if(i == scheduleNodes.size()-1){
					for(int k = j; k < scheduleEdges.size(); k++){
						edge = scheduleEdges.get(k);
						writer.println("\t\t" + edge.getStartNode().getName() + " -> " + edge.getEndNode().getName() + "\t [Weight=" + edge.getWeight() + "];");
					}
				}
			}
			
			writer.print("}");
			
			writer.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
