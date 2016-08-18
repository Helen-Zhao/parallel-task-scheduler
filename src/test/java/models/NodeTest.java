package models;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Helen Zhao
 */
public class NodeTest {

    @Test
    public void testCreateNode() {
        String name = "test";
        int weight = 3;

        Node node = new Node(name, weight);

        assertEquals(name, node.getName());
        assertEquals(weight, node.getWeight());
        assertEquals(0, node.getNumberIncomingEdges());
        assertEquals(0, node.getNumberOutgoingEdges());
        assertEquals(-1, node.getStartTime());
        assertEquals(-1, node.getProcessor());
        assertEquals(false, node.getHasRun());
    }


}
