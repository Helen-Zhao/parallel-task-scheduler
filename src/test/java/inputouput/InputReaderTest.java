package inputouput;

import inputoutput.InputReader;
import models.Edge;
import models.Node;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author William Lin
 */
public class InputReaderTest {
    public List<Node> correctNodeList = new ArrayList<Node>();
    public List<Edge> correctEdgeList = new ArrayList<Edge>();

    @Test
    public void FileInputTest() {
        //Selecting input file
        File input = new File("./src/test/resources/inputReader.dot");

        //Instantiation of correct nodes
        Node firstNode = new Node("a", 2);
        Node secondNode = new Node("b", 3);
        Node thirdNode = new Node("c", 3);
        Node fourthNode = new Node("d", 2);

        //Instantiation of correct edges
        Edge firstEdge = new Edge(firstNode, secondNode, 1);
        Edge secondEdge = new Edge(firstNode, thirdNode, 2);
        Edge thirdEdge = new Edge(secondNode, fourthNode, 2);
        Edge fourthEdge = new Edge(thirdNode, fourthNode, 1);

        //Adding correct node dependencies
        firstNode.addOutgoingEdge(firstEdge);
        firstNode.addOutgoingEdge(secondEdge);
        secondNode.addIncomingEdge(firstEdge);
        secondNode.addOutgoingEdge(thirdEdge);
        thirdNode.addIncomingEdge(secondEdge);
        thirdNode.addOutgoingEdge(fourthEdge);
        fourthNode.addIncomingEdge(thirdEdge);
        fourthNode.addIncomingEdge(fourthEdge);

        correctNodeList.add(firstNode);
        correctNodeList.add(secondNode);
        correctNodeList.add(thirdNode);
        correctNodeList.add(fourthNode);

        correctEdgeList.add(firstEdge);
        correctEdgeList.add(secondEdge);
        correctEdgeList.add(thirdEdge);
        correctEdgeList.add(fourthEdge);

        InputReader reader;
        try {
            reader = new InputReader();
            reader.readFile(input);

            //checks each node's properties to it's correct counterpart
            for (int i = 0; i < correctNodeList.size(); i++) {
                assertEquals(correctNodeList.get(i).getName(), reader.getNodeList().get(i).getName());
                assertEquals(correctNodeList.get(i).getWeight(), reader.getNodeList().get(i).getWeight());
                assertEquals(correctNodeList.get(i).getNumberIncomingEdges(), reader.getNodeList().get(i).getNumberIncomingEdges());
                assertEquals(correctNodeList.get(i).getNumberOutgoingEdges(), reader.getNodeList().get(i).getNumberOutgoingEdges());

                List<Edge> readerNodeListIncEdge = reader.getNodeList().get(i).getIncomingEdges();
                List<Edge> correctNodeListIncEdge = correctNodeList.get(i).getIncomingEdges();
                //checking each node's incoming edges list to it's correct counterpart
                for (int j = 0; j < correctNodeListIncEdge.size(); j++) {
                    assertEquals(correctNodeListIncEdge.get(j).getStartNode().getName(), readerNodeListIncEdge.get(j).getStartNode().getName());
                    assertEquals(correctNodeListIncEdge.get(j).getEndNode().getName(), readerNodeListIncEdge.get(j).getEndNode().getName());
                    assertEquals(correctNodeListIncEdge.get(j).getWeight(), readerNodeListIncEdge.get(j).getWeight());
                }

                List<Edge> readerNodeListOutEdge = reader.getNodeList().get(i).getOutgoingEdges();
                List<Edge> correctNodeListOutEdge = correctNodeList.get(i).getOutgoingEdges();
                //checking each node's outgoing edges list to it's correct counterpart
                for (int k = 0; k < correctNodeListOutEdge.size(); k++) {
                    assertEquals(correctNodeListOutEdge.get(k).getStartNode().getName(), readerNodeListOutEdge.get(k).getStartNode().getName());
                    assertEquals(correctNodeListOutEdge.get(k).getEndNode().getName(), readerNodeListOutEdge.get(k).getEndNode().getName());
                    assertEquals(correctNodeListOutEdge.get(k).getWeight(), readerNodeListOutEdge.get(k).getWeight());
                }
            }

            //checks each edge's properties to it's correct counterpart
            for (int i = 0; i < correctEdgeList.size(); i++) {
                assertEquals(correctEdgeList.get(i).getStartNode().getName(), reader.getEdgeList().get(i).getStartNode().getName());
                assertEquals(correctEdgeList.get(i).getEndNode().getName(), reader.getEdgeList().get(i).getEndNode().getName());
                assertEquals(correctEdgeList.get(i).getWeight(), reader.getEdgeList().get(i).getWeight());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
