package models;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * @author Helen Zhao
 */
public class EdgeTest {

    @Test
    public void testCreateEdge() {
        Node startNode = mock(Node.class);
        Node endNode = mock(Node.class);
        int weight = 4;

        Edge edge = new Edge(startNode, endNode, weight);

        assertEquals(startNode, edge.getStartNode());
        assertEquals(endNode, edge.getEndNode());
        assertEquals(weight, edge.getWeight());
    }

}
