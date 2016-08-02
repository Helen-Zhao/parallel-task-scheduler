package inputoutput;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.Edge;
import models.Node;

/**
 * Created by helen on 28/07/2016.
 */
public class InputReader {
	
	//List of nodes and edges compiled from input .dot file. Can be accessible from other classes and methods.
	public static List<Node> nodeList = new ArrayList<Node>();
	public static List<Edge> edgeList = new ArrayList<Edge>();
	
	//Main input reader function
	public InputReader(File f, int processors) {
		BufferedReader br = null;
		
		try {
			
			String sCurrentLine;
			//instantiation of buffered reader on .dot file
			br = new BufferedReader(new FileReader(f));
			//skips the first line as it has text we don't want to extract
			br.readLine();
			
			//If current line read is not end of file or the last line of file (again has no value to us), then do the following
			while((sCurrentLine = br.readLine()) != null || (sCurrentLine = br.readLine()) != "}") {
				//if current line does not contain an arrow then it is a node, else it is an edge.
				if(sCurrentLine.indexOf("->") == -1) {
					//parse weight and get node name
					int weight = weightCreator(sCurrentLine);
					String nodeName = sCurrentLine.substring(0, 1);
					//create the new node object
					Node node = new Node(nodeName, weight);
					//add node object to list
					nodeList.add(node);
				}
				else {
					//Instantiate string array to store the multiple split strings
					String[] parts = sCurrentLine.split("->");
					//name of first node
					String firstNodeName = parts[0];
					//name of second node
					String secondNodeName = parts[1].substring(0, 1);
					//index of first and second node for verification. Also acts as a boolean.
					int firstNodeIndex = -1;
					int secondNodeIndex = -1;
					
					//input weight property of edge
					int weight = weightCreator(sCurrentLine);
					//if weight is valid, then find the index of both nodes in the list.
					if (weight != -1){
						for (int i = 0; i < nodeList.size(); i++){
							if (firstNodeName.equals(nodeList.get(i).getName())){
								firstNodeIndex = i;
							}
							else if (secondNodeName.equals(nodeList.get(i).getName())){
								secondNodeIndex = i;
							}
						}
						//if both indexes found, then create an edge object using the name of the nodes and it's weight.
						if ((firstNodeIndex != -1) && (secondNodeIndex != -1)){
							Edge edge = new Edge(nodeList.get(firstNodeIndex), nodeList.get(secondNodeIndex), weight);
							//add newly created edge object to list
							edgeList.add(edge);
							//add the new edge as an incoming/outgoing edge to its specific nodes.
							nodeList.get(firstNodeIndex).addOutgoingEdge(edge);
							nodeList.get(secondNodeIndex).addIncomingEdge(edge);
						}
						//indexes not found. Node has not been created yet. File contains an error in the order of node creation/listing
						else {
							throw new IllegalArgumentException("Invalid nodes. Nodes not initialised yet");
						}
					}
					//Invalid weight property.
					else {
						throw new IllegalArgumentException("Invalid weight");
					}
				}
			}
		} catch (IOException e){
			e.printStackTrace();
		} finally {
			try {
				if (br != null){
					br.close();
				}
			}catch(IOException e2) {
				e2.printStackTrace();
			}
		}
	}
	
	//private method used to parse obtained "weight" property into an Integer Type from a String
	private static int weightCreator(String s) {
		Pattern p = Pattern.compile("-?\\d+");
		Matcher m = p.matcher(s);
		while(m.find()){
			return (Integer.parseInt(m.group()));
		}
		//if weight is of invalid type or invalid input, return -1 to denote it as invalid.
		return -1;
	}
}
