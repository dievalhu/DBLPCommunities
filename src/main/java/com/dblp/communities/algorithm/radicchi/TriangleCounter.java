package com.dblp.communities.algorithm.radicchi;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import com.dblp.communities.graphs.Edge;
import com.dblp.communities.graphs.Node;
import com.dblp.communities.graphs.UndirectedGraph;

public class TriangleCounter {
	
	private UndirectedGraph graph;
	private HashMap<Edge,Integer> numTriangles;
	private int offset;
	
	public TriangleCounter(UndirectedGraph graph, int offset) {
		this.graph = graph;
		this.numTriangles = countTriangles();
		this.offset = offset;
	}
	
	public void printTriangleStats() {		
		for (Entry<Edge,Integer> entry : numTriangles.entrySet()) {
			System.out.printf("(%d,%d): %d\n", entry.getKey().head().id()+offset, 
					entry.getKey().tail().id()+offset, entry.getValue());
		}
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
			
			if (edge == null) {
				System.out.println("TriangleCounter: countTriangles(): edge == null");
			}
			if (edge.head() == null) {
				System.out.println("TriangleCounter: countTriangles(): edge.head == null");
			}
			if (edge.tail() == null) {
				System.out.println("TriangleCounter: countTriangles(): edge.tail == null");
			}
			
			LinkedList<Node> neighborsOfU = (LinkedList<Node>) graph.neighborhood(edge.head());
			LinkedList<Node> neighborsOfV = (LinkedList<Node>) graph.neighborhood(edge.tail());
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