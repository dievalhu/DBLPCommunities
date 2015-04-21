package com.dblp.communities.algorithm.radicchi;

import java.util.ArrayList;
import java.util.LinkedList;

import com.dblp.communities.datastructure.NodeDegree;
import com.dblp.communities.graphs.GraphI;
import com.dblp.communities.graphs.Node;

public class SmallestLastOrdering {

	private GraphI graph;
	private ArrayList<LinkedList<Node>> degreeStructure;
	private int[] degrees;
	private int smallestDegree;
	private int maxDegree;
	private ArrayList<NodeDegree> smallestLastOrdering;
	private boolean[] exists;
	private boolean orderingComputed;
	
	public SmallestLastOrdering(GraphI graph) {
		this.graph = graph;
		this.exists = new boolean[graph.numNodes()];
		for (int i = 0; i < graph.numNodes(); ++i)
			this.exists[i] = true;
		this.degrees = new int[graph.numNodes()];
		this.smallestDegree = Integer.MAX_VALUE;
		this.maxDegree = graph.numNodes()-1;
		createInitialDegreeStructure();
		this.smallestLastOrdering = new ArrayList<NodeDegree>();
		this.orderingComputed = false;
	}
	
	private void createInitialDegreeStructure() {
		degreeStructure = new ArrayList<LinkedList<Node>>();
		// one LinkedList for every possible degree
		for (int i = 0; i <= maxDegree; ++i)
			degreeStructure.add(i, new LinkedList<Node>());
		
		for (Node node : graph.nodes()) {
			int degree = graph.numNeighbors(node); // O(deg(node)) = O(|V|)
			degrees[node.id()] = degree;
			if (degree < smallestDegree)
				smallestDegree = degree;
			degreeStructure.get(degree).add(node);
		}
	}
	
	private NodeDegree extractSmallestDegreeNode() {
		if (degreeStructure.get(smallestDegree).isEmpty())
			return null;
		
		Node smallestDegreeNode = degreeStructure.get(smallestDegree).removeFirst();
		NodeDegree answer = new NodeDegree(smallestDegreeNode, smallestDegree);
		
		for (Node neighbor : graph.neighborhood(smallestDegreeNode)) {
			if (exists[neighbor.id()]) {
				int degree = degrees[neighbor.id()];
				degreeStructure.get(degree).remove(neighbor);
				if (degree > 1) {
					degreeStructure.get(degree-1).add(neighbor);
					degrees[neighbor.id()]--;
				} else {
					exists[neighbor.id()] = false;
					degrees[neighbor.id()] = 0;
				}
			}
		}
		
		// Find the new smallest degree
		if (degreeStructure.get(smallestDegree).isEmpty()) {
			for (int i = smallestDegree+1; i <= maxDegree; ++i) {
				if (!degreeStructure.get(i).isEmpty()) {
					smallestDegree = i;
					break;
				}
			}
		}
		
		return answer;
	}
	
	public ArrayList<NodeDegree> getSmallestLastOrdering() {
		if (!orderingComputed)
			computeSmallestLastOrdering();
		
		return smallestLastOrdering;
	}

	private void computeSmallestLastOrdering() {
		
		NodeDegree smallestDegreeNode = null;
		
		while ((smallestDegreeNode = extractSmallestDegreeNode()) != null) {
			smallestLastOrdering.add(smallestDegreeNode);
		}
		
		orderingComputed = true;
	}
}
