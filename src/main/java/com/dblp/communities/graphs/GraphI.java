package com.dblp.communities.graphs;

public interface GraphI {
	int numNodes();
	int numEdges();
	void addEdge(Edge e);
	boolean areNeighbors(Node i, Node j);
	Iterable<? extends Node> neighborhood(Node i);
	int numNeighbors(Node i);
	Iterable<? extends Node> nodes();
	Iterable<? extends Edge> edges();
}
