package scheduler;

import java.util.ArrayList;
import java.util.List;

import models.Edge;
import models.Node;

public class ValidNodeFinder {
	
	// Find nodes with no dependencies
	public void findRootNodes(List<Node> nodes) {
		List<Node> rootNodes = new ArrayList<Node>();
		for(Node n : nodes){
			if(n.getIncomingEdges() == null || n.getIncomingEdges().size() == 0){
				rootNodes.add(n);
			}
		}
	}
	
	// Check if dependent nodes now have dependencies satisfied
	public List<Node> checkDependentNodes(Node n){
		List<Node> satisfiedNodes = new ArrayList<Node>();
		List<Edge> edges = n.getOutgoingEdges();
		for(Edge e: edges){
			Node endNode = e.getEndNode();
			if(checkDependencies(endNode)){
				satisfiedNodes.add(endNode);
			}
		}
		
		return satisfiedNodes;
	}
	
	// Check Incoming Edges for particular node
	public boolean checkDependencies(Node n){
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
