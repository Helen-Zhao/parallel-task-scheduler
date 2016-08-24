package inputoutput;

import models.Edge;
import models.Node;
import models.NodeTuple;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

/**
 * @author William Lin
 *         <p>
 *         OutputWriter takes a file name/path to print to, a list of nodes and a list of edges in the optimal schedule
 *         and creates a .dot file with the specified name
 */
public class OutputWriter {

    public void writeFile(List<Node> nodes, HashMap<String, NodeTuple> nodeInfo, List<Edge> scheduleEdges, String outputFileName) {
        try {
            //Get working directory
            String dir = System.getProperty("user.dir");

            //Instantiate PrintWriter object to create and write to file. Set encoding to UTF-8
            PrintWriter writer = new PrintWriter(dir + "/" + outputFileName + ".dot", "UTF-8");
            writer.println("digraph \"" + outputFileName + "\" {");

            //iterate through list and print
            for (int i = 0; i < nodes.size(); i++) {
            	
                Node node = nodes.get(i);
                
                if (nodeInfo.get(node.getName()) == null) {
                	System.out.println("nodeInfo.get(node.getName()) is null dude and node is: " + node.getName());
                }
                writer.println("\t\t" + node.getName() + "\t\t [Weight=" + node.getWeight() + ", Start=" + nodeInfo.get(node.getName()).getStartTime() + ", Processor=" + nodeInfo.get(node.getName()).getProcessor() + "];");
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