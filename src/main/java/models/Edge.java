package models;

/**
 * @author Helen Zhao
 *         <p>
 *         Edge models an edge in a DAG with a start node, end node and weight
 */
public class Edge {
    private Node startNode;
    private Node endNode;
    private int weight;

    public Edge(Node startNode, Node endNode, int weight) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.weight = weight;
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public int getWeight() {
        return weight;
    }

}
