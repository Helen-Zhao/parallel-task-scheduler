package scheduler;

import models.Edge;
import models.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Benjamin Collins, Jacky Mai
 *
 * ValidNodeFinder takes a list of nodes and finds all nodes that have their dependencies
 * fulfilled.
 */

public class ValidNodeFinder implements ValidNodeFinderInterface {

    // Find nodes with no dependencies (root) by searching for nodes with no incoming edges
    public List<Node> findRootNodes(List<Node> nodes) {
        List<Node> rootNodes = new ArrayList<Node>();

        for (Node node : nodes) {
            if (node.getIncomingEdges() == null || node.getIncomingEdges().size() == 0) {
                rootNodes.add(node);
            }
        }

        return rootNodes;
    }

    // Check whether children or dependent nodes for particular node is satisfied
    // If yes, add it to a list of satisfied nodes and return it
    public List<Node> findSatisfiedChildren(Node node) {
        // Children will not be satisfied unless input node itself is satisfied
        if (node.getHasRun() == false) {
            return new ArrayList<Node>();
        }

        List<Node> satisfiedNodes = new ArrayList<Node>();
        List<Edge> edges = node.getOutgoingEdges();

        for (Edge edge : edges) {
            Node endNode = edge.getEndNode();
            if (isAvailable(endNode)) {
                satisfiedNodes.add(endNode);
            }
        }

        return satisfiedNodes;
    }

    // Check incoming edges for particular node and determine whether it is available or not
    public boolean isAvailable(Node node) {
        List<Edge> edges = node.getIncomingEdges();

        for (Edge edge : edges) {
            Node startNode = edge.getStartNode();
            if (startNode.getHasRun() == false) {
                return false;
            }
        }

        return true;
    }

    // Given a list of nodes, find all of which have their dependencies satisfied
    public List<Node> findSatisfiedNodes(List<Node> nodes) {

        List<Node> satisfiedNodes = new ArrayList<Node>();
        for (Node node : nodes) {
            if (isAvailable(node) && !(node.getHasRun())) {
                satisfiedNodes.add(node);
            }
        }

        return satisfiedNodes;
    }
}
