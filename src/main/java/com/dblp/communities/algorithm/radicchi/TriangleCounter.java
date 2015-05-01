package com.dblp.communities.algorithm.radicchi;

import java.util.HashMap;
import java.util.LinkedList;

import com.dblp.communities.graphs.Edge;
import com.dblp.communities.graphs.Node;
import com.dblp.communities.graphs.UndirectedGraph;

public class TriangleCounter {
	
	private UndirectedGraph graph;
	private HashMap<Edge,Integer> numTriangles;
	
	public TriangleCounter(UndirectedGraph graph) {
		this.graph = new UndirectedGraph(graph);
		this.numTriangles = countTriangles();
	}
	
	/**
	 * Returns a HashMap mapping edges to the number of triangles
	 * they are part of in the input graph.
	 * 
	 * @return a HashMap mapping edges to the number of triangles
	 * they are part of in the input graph.
	 */
	public HashMap<Edge,Integer> getTriangleStats() {
		return numTriangles;
	}
	
	/**
	 * An implementation of the edge-iterator algorithm that counts the number of
	 * triangles in a graph.
	 * 
	 * @return
	 */
	private HashMap<Edge, Integer> countTriangles() {
		graph.sortAdjacencyList();
		
		HashMap<Edge, Integer> edgesToTriangles = new HashMap<Edge,Integer>();
		
		for (Edge edge : graph.edges()) {
			int numEdgeTriangles = 0;
			int pu = 0;
			int pv = 0;
			Node u = edge.head();
			Node v = edge.tail();
			
			LinkedList<Node> neighborsOfU = (LinkedList<Node>) graph.neighborhood(u);
			LinkedList<Node> neighborsOfV = (LinkedList<Node>) graph.neighborhood(v);
			while (pu < neighborsOfU.size() && pv < neighborsOfV.size()) {
				if (neighborsOfU.get(pu).compareTo(neighborsOfV.get(pv)) == 0) {
					++numEdgeTriangles;
					++pu;
					++pv;
				} else if (neighborsOfU.get(pu).compareTo(neighborsOfV.get(pv)) < 0) {
					++pu;
				} else {
					++pv;
				}
			}
			edgesToTriangles.put(edge,numEdgeTriangles);
		}
		
		return edgesToTriangles;
	}
}