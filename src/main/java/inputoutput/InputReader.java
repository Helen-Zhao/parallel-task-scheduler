package inputoutput;

import models.Edge;
import models.Node;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author William Lin
 *         <p>
 *         InputReader parses a .dot file into a list of nodes and a list of edges
 */

public class InputReader {

    // List of nodes and edges compiled from input .dot file. Can be accessible from other classes and methods.
    private List<Node> nodeList = new ArrayList<>();
    private List<Edge> edgeList = new ArrayList<>();

    // main.Main input reader function
    // main.Main input reader function
    public void readFile(File file) throws IOException {

        BufferedReader br = null;

        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(file));
            // skips the first line as it has text we don't want to extract
            br.readLine();

            String patternString = "\\[Weight=\\d+\\];";
            Pattern pattern = Pattern.compile(patternString);

            while ((sCurrentLine = br.readLine()) != null) {
                if (!(sCurrentLine.contains("}"))) {

                    //If the line contains "[Weight=x];"
                    if (pattern.matcher(sCurrentLine).find()) {

                        // If the current line does not contain an arrow then it is a node, else it is an edge.
                        if (sCurrentLine.indexOf("->") == -1) {
                            int weight = weightCreator(sCurrentLine);
                            String[] nodeNameParts = sCurrentLine.trim().split("\\s+");
                            String nodeName = nodeNameParts[0];

                            nodeCreator(nodeName, weight);
                        } else {
                            String[] parts = sCurrentLine.trim().split("->");
                            String firstNodeName = parts[0].trim();

                            String[] parts2 = parts[1].trim().split("\\s+");
                            String secondNodeName = parts2[0].trim();

                            int weight = weightCreator(sCurrentLine);

                            edgeCreator(firstNodeName, secondNodeName, weight);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e2) {
                e2.printStackTrace();
                throw e2;

            }
        }
    }

    //Getter for list of edges
    public List<Edge> getEdgeList() {
        return this.edgeList;
    }

    public List<Node> getNodeList() {
        return this.nodeList;
    }

    // Private method used to parse obtained "weight" property into an Integer Type from a String
    private static int weightCreator(String s) {
        String[] weightSplit = s.split("=");
        String weight = weightSplit[1];

        Pattern p = Pattern.compile("-?\\d+");
        Matcher m = p.matcher(weight);
        while (m.find()) {
            return (Integer.parseInt(m.group()));
        }
        // If weight is of invalid type or invalid input, return -1 to denote it as invalid.
        return -1;
    }

    // Checks if a node already has that name in the current nodeList
    // Create a new node if the name is available
    private void nodeCreator(String nodeName, int weight) {
        if (!containsNode(nodeName)) {
            Node node = new Node(nodeName, weight);
            nodeList.add(node);
        } else {
            throw new IllegalArgumentException("Node name already exists");
        }
    }

    private void edgeCreator(String firstNodeName, String secondNodeName, int weight) {
        // Index of first and second node for verification. Also acts as a boolean.
        int firstNodeIndex = -1;
        int secondNodeIndex = -1;

        // If weight is valid, then find the index of both nodes in the list.
        if (weight != -1) {
            for (int i = 0; i < nodeList.size(); i++) {
                if (firstNodeName.equals(nodeList.get(i).getName())) {
                    firstNodeIndex = i;
                } else if (secondNodeName.equals(nodeList.get(i).getName())) {
                    secondNodeIndex = i;
                }
            }

            // If both indexes found, then create an edge object using the name of the nodes and it's weight.
            if ((firstNodeIndex != -1) && (secondNodeIndex != -1)) {
                Node firstNode = nodeList.get(firstNodeIndex);
                Node secondNode = nodeList.get(secondNodeIndex);
                Edge edge = new Edge(firstNode, secondNode, weight);

                edgeList.add(edge);
                firstNode.addOutgoingEdge(edge);
                secondNode.addIncomingEdge(edge);
            } else {
                // Indexes not found. Node has not been created yet. File contains an error in the order of node creation/listing
                throw new IllegalArgumentException("Invalid nodes. Nodes not initialised yet");
            }
        } else {
            // Invalid weight property.
            throw new IllegalArgumentException("Invalid weight");
        }
    }

    private boolean containsNode(String nodeName) {
        for (Node node : nodeList) {
            if (node.getName().equals(nodeName)) {
                return true;
            }
        }

        return false;
    }
}
