package com.dblp.communities.graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import com.dblp.communities.utilities.MinMax;

public class UndirectedGraph implements GraphI {

	protected ArrayList<LinkedList<Node>> graph;
	protected ArrayList<Edge> edgeList;
	protected LinkedList<Node> nodeList;
	protected int numNodes;
	protected int numEdges;
	
	/**
	 * A copy constructor.
	 * 
	 * @param g
	 */
	public UndirectedGraph(UndirectedGraph g) {
		this.graph = new ArrayList<LinkedList<Node>>(g.graph.size());
		for (int i = 0; i < g.graph.size(); ++i) {
			graph.add(i, new LinkedList<Node>());
		}
		for (int i = 0; i < graph.size(); ++i) {
			for (Node n : g.graph.get(i)) {
				graph.get(i).add(n.copy());
			}
		}
		
		this.edgeList = new ArrayList<Edge>(g.edgeList.size());
		for (Edge e : g.edgeList) {
			this.edgeList.add(e.copy());
		}		
		
		this.nodeList = new LinkedList<Node>();
		for (Node n : g.nodeList) {
			this.nodeList.add(n.copy());
		}
		
		this.numNodes = g.numNodes;
		this.numEdges = g.numEdges;		
	}
	
	public UndirectedGraph(int numNodes) {
		this.graph = new ArrayList<LinkedList<Node>>(numNodes);
		this.nodeList = new LinkedList<Node>();
		for (int i = 0; i < numNodes; ++i) {
			graph.add(i, new LinkedList<Node>());
			nodeList.add(new Node(i));
		}
		
		this.edgeList = new ArrayList<Edge>(numNodes*numNodes);
		
		this.numNodes = numNodes;
		this.numEdges = 0;
	}
	
	public UndirectedGraph() {
		this.graph = new ArrayList<LinkedList<Node>>();
		this.nodeList = new LinkedList<Node>();
		this.edgeList = new ArrayList<Edge>();
		this.numNodes = 0;
		this.numEdges = 0;
	}
	
	public final ArrayList<LinkedList<Node>> adjacencyList() {
		return graph;
	}
	
	public void sortAdjacencyList() {
		for (int i = 0; i < numNodes; ++i) {
			Collections.sort(graph.get(i));
		}
	}
	
	public int numNodes() {
		return this.numNodes;
	}

	public int numEdges() {
		return this.numEdges;
	}

	public void addEdge(Edge e) {
		
		int u = e.head().id();
		int v = e.tail().id();
		
		if (u < 0 || u >= numNodes)
			throw new IndexOutOfBoundsException();
		if (v < 0 || v >= numNodes())
			throw new IndexOutOfBoundsException();
		
		if (areNeighbors(e.head(), e.tail()))
			return;
		
		++this.numEdges;
		
		graph.get(u).add(e.tail());
		graph.get(v).add(e.head());
		
		MinMax<Integer> minMax = new MinMax<Integer>(u,v);
		
		if (minMax.min() == u) {
			edgeList.add(new Edge(e.head(),e.tail()));
		} else {
			edgeList.add(new Edge(e.tail(),e.head()));
		}
	}

	public boolean areNeighbors(Node i, Node j) {
		for (Node n : neighborhood(i))
			if (n.equals(j))
				return true;
		
		return false;
	}

	public Iterable<Node> neighborhood(Node node) {
		if (node == null) {
			System.out.println("UndirectedGraph: neighborhood: node == null");
			return null;
		}
		if (node.id() < 0 || node.id() >= numNodes())
			throw new IndexOutOfBoundsException();
		
		return graph.get(node.id());
	}

	public int numNeighbors(Node i) {
		return graph.get(i.id()).size();
	}

	public Iterable<Node> nodes() {		
		return nodeList;
	}

	public Iterable<Edge> edges() {
		return edgeList;
	}
	
	public boolean removeEdge(Edge edge) {
		
		edgeList.remove(edge);
		graph.get(edge.head().id()).remove(edge.tail());
		graph.get(edge.tail().id()).remove(edge.head());
		
		--numEdges;
		
		return true;
	}
	
	/**
	 * Removes the node 'node' from the graph.
	 * 
	 * @param node a node
	 * @return true iff node was removed from the graph
	 */
	public boolean removeNode(Node node) {
		/**
		 * http://stackoverflow.com/questions/223918/iterating-through-a-list-avoiding-concurrentmodificationexception-when-removing
		 */
		
		graph.get(node.id()).clear();
		
		for (int i = 0; i < graph.size(); ++i)
			graph.get(i).remove(node);
		
		for (Iterator<Edge> iterator = edgeList.iterator(); iterator.hasNext();) {
			Edge edge = iterator.next();
			if (edge.head().equals(node) || edge.tail().equals(node)) {
				iterator.remove();
			}
		}
		
		return true;
	}
	
	/**
	 * 
	 * 
	 * @param nodeList
	 * @param in
	 * @return
	 */
	public UndirectedGraph inducedSubgraph(boolean[] in) {
		UndirectedGraph copy = new UndirectedGraph(this);
		for (Node node : copy.nodes()) {
			if (!in[node.id()]) {
				copy.removeNode(node);
			}
		}
		
		return copy;
	}
}