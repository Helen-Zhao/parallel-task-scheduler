package scheduler;

import java.util.ArrayList;
import java.util.List;

import models.Edge;
import models.Node;

public class ValidNodeFinder implements ValidNodeFinderInterface{
	
	// Find nodes with no dependencies (root) by searching for nodes with no incoming edges
	public void findRootNodes(List<Node> nodes) {
		List<Node> rootNodes = new ArrayList<Node>();
		
		for(Node n : nodes){
			if(n.getIncomingEdges() == null || n.getIncomingEdges().size() == 0){
				rootNodes.add(n);
			}
		}
	}
	
	// Check whether children or dependent nodes for particular node is satisfied
	// If yes, add it to a list of satisfied nodes and return it
	public List<Node> checkDependentNodes(Node n){
		List<Node> satisfiedNodes = new ArrayList<Node>();
		List<Edge> edges = n.getOutgoingEdges();
		
		for(Edge e: edges){
			Node endNode = e.getEndNode();
			if(isAvailable(endNode)){
				satisfiedNodes.add(endNode);
			}
		}
		
		return satisfiedNodes;
	}
	
	// Check incoming edges for particular node and determine whether it is available or not 
	public boolean isAvailable(Node n){
		List<Edge> edges = n.getIncomingEdges();
		
		for(Edge e: edges){
			Node startNode = e.getStartNode();
			if(startNode.getHasRun() == false){
				return false;
			}
		}
		
		return true;
	}
	
		
	
	
	

}
