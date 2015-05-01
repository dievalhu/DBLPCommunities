package com.dblp.communities.graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import com.dblp.communities.utilities.MinMax;

public class UndirectedGraph implements GraphI {

	protected ArrayList<LinkedList<Node>> graph;
	protected ArrayList<LinkedList<Edge>> neighborEdgeList;
	protected ArrayList<Edge> edgeList;
	protected LinkedList<Node> nodeList;
	protected int numNodes;
	protected int numEdges;
	
	/**
	 * A copy constructor.
	 * 
	 * @param toBeCopied
	 */
	public UndirectedGraph(UndirectedGraph toBeCopied) {
		this.graph = new ArrayList<LinkedList<Node>>(toBeCopied.graph.size());
		this.neighborEdgeList = new ArrayList<LinkedList<Edge>>(toBeCopied.graph.size());
		for (int i = 0; i < toBeCopied.graph.size(); ++i) {
			graph.add(i, new LinkedList<Node>());
			neighborEdgeList.add(i, new LinkedList<Edge>());
		}
		for (int i = 0; i < toBeCopied.graph.size(); ++i) {
			for (Node n : toBeCopied.graph.get(i)) {
				graph.get(i).add(n.copy());
			}
		}
		for (int i = 0; i < toBeCopied.neighborEdgeList.size(); ++i) {
			for (Edge e : toBeCopied.neighborEdgeList.get(i)) {
				neighborEdgeList.get(i).add(e.copy());
			}
		}
		
		this.edgeList = new ArrayList<Edge>(toBeCopied.edgeList.size());
		for (Edge e : toBeCopied.edgeList) {
			this.edgeList.add(e.copy());
		}		
		
		this.nodeList = new LinkedList<Node>();
		for (Node n : toBeCopied.nodeList) {
			this.nodeList.add(n.copy());
		}
		
		this.numNodes = toBeCopied.numNodes;
		this.numEdges = toBeCopied.numEdges;	
		System.out.println("End of UndirectedGraph constructur");
	}
	
	public UndirectedGraph(int numNodes) {
		this.graph = new ArrayList<LinkedList<Node>>(numNodes);
		this.neighborEdgeList = new ArrayList<LinkedList<Edge>>();
		this.nodeList = new LinkedList<Node>();
		for (int i = 0; i < numNodes; ++i) {
			graph.add(i, new LinkedList<Node>());
			neighborEdgeList.add(i, new LinkedList<Edge>());
			nodeList.add(new Node(i));
		}
		
		this.edgeList = new ArrayList<Edge>(numNodes*numNodes);
		
		this.numNodes = numNodes;
		this.numEdges = 0;
	}
	
	public UndirectedGraph() {
		this.graph = new ArrayList<LinkedList<Node>>();
		this.neighborEdgeList = new ArrayList<LinkedList<Edge>>();
		this.nodeList = new LinkedList<Node>();
		this.edgeList = new ArrayList<Edge>();
		this.numNodes = 0;
		this.numEdges = 0;
	}
	
	public LinkedList<Edge> edgesAdjacentTo(Node u) {
		if (neighborEdgeList == null)
			return new LinkedList<Edge>();
		if (u.id() >= neighborEdgeList.size())
			return new LinkedList<Edge>();
		
		return neighborEdgeList.get(u.id());
	}
	
	public final ArrayList<LinkedList<Node>> adjacencyList() {
		return graph;
	}
	
	public void sortAdjacencyList() {
		System.out.println("UndirectedGraph: sortAdjacencyList(): size of adjacency list = " + graph.size());
		for (int i = 0; i < graph.size(); ++i) {
			Collections.sort(graph.get(i));
		}
	}
	
	public int numNodes() {
		return this.numNodes;
	}

	public int numEdges() {
		return this.numEdges;
	}
	
	/**
	 * Get the weight of the edge between the two given nodes. 0
	 * is returned if the edge does not exist.
	 * 
	 * @param u one node
	 * @param v another node
	 * @return the weight of the edge between nodes u and v but 0
	 * if the edge does not exist.
	 */
	public Integer weight(Node u, Node v) {
		Integer weight = new Integer(0);
		if (u.equals(v)) {
			return weight;
		}
		for (Edge e : neighborEdgeList.get(u.id())) {
			if (e.getHead().equals(v)) {
				weight = new Integer(e.getWeight());
				break;
			}
		}
		return weight;
	}
	
	public Integer strength(Node u) {
		Integer sum = new Integer(0);
		for (Edge e : neighborEdgeList.get(u.id())) {
			sum = sum + e.getWeight();
		}
		return sum;
	}

	public void addEdge(Edge e) {
		
		Integer weight = e.getWeight();
		Node uNode = e.head();
		Node vNode = e.tail();
		int uId = uNode.id();
		int vId = vNode.id();
		
		if (uId < 0 || uId >= numNodes)
			throw new IndexOutOfBoundsException();
		if (vId < 0 || vId >= numNodes)
			throw new IndexOutOfBoundsException();
		
		if (areNeighbors(uNode, vNode))
			return;
		
		++this.numEdges;
		
		graph.get(uId).add(vNode);
		graph.get(vId).add(uNode);
		
		Edge uv = new Edge(vNode, uNode, weight);
		Edge vu = new Edge(uNode, vNode, weight);
		
		neighborEdgeList.get(uId).add(uv);
		neighborEdgeList.get(vId).add(vu);
		
		MinMax<Integer> minMax = new MinMax<Integer>(uId,vId);
		
		// make the tail lowest numbered
		if (minMax.min() == uId) {
			edgeList.add(uv);
		} else {
			edgeList.add(vu);
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