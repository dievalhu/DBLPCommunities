package com.dblp.communities.algorithm.radicchi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.dblp.communities.datastructure.Community;
import com.dblp.communities.datastructure.IndexMinPQ;
import com.dblp.communities.datastructure.LayerInfo;
import com.dblp.communities.datastructure.LayerOfCommunities;
import com.dblp.communities.datastructure.Pair;
import com.dblp.communities.datastructure.WebCommunity;
import com.dblp.communities.graphs.Edge;
import com.dblp.communities.graphs.LabeledUndirectedGraph;
import com.dblp.communities.graphs.Node;
import com.dblp.communities.utilities.MinMax;


public class LabeledRadicchi {

	private LabeledUndirectedGraph workbenchGraph;
	private LabeledUndirectedGraph initialGraph;
	private HashMap<Edge,Integer> numTriangles;
	private TriangleCounter triangleCounter;
	private double lowerBound;
	private String definition;
	private IndexMinPQ<Triple> queue;
	private HashMap<Edge,Integer> edgeToId;
	private List<LayerOfCommunities> communityPartitions;
	private boolean weighted = false;
	/**
	 * The id (index) of a layer of communities, that is, of a
	 * partition into communities.
	 */
	private int layerId;
	/**
	 * Information about every layer of communities, that is,
	 * of every partition into communities found during the
	 * course of the algorithm.
	 */
	private List<LayerInfo> layerInfos;
	
	/**
	 * 
	 * @param graph a labeled undirected graph
	 * @param l the lower bound on the community sizes in the range [0, 1].
	 * @param u the upper bound on the community sizes in the range [0, 1].
	 * @param strong true if the strong community definition is to be used and
	 *        false if the weak community definition is to be used
	 * @param offset 0 if the vertices should be numbered from 0 in the output and 1
	 *        if the vertices should be numbered from 1 in the output
	 */
	public LabeledRadicchi(LabeledUndirectedGraph graph, double lowerBound, String definition, String graphType) {
		this.weighted = (graphType.equalsIgnoreCase("weighted")) ? true : false;
		this.triangleCounter = new TriangleCounter(graph);
		this.numTriangles = triangleCounter.getTriangleStats();
		this.workbenchGraph = new LabeledUndirectedGraph(graph);
		this.initialGraph = new LabeledUndirectedGraph(graph);
		this.lowerBound = lowerBound;
		this.definition = definition;
		this.communityPartitions = new ArrayList<LayerOfCommunities>();
		this.layerId = 0;
		this.layerInfos = new ArrayList<LayerInfo>();
		
		System.out.println("LabeledRadicchi: constructor: weighted = " + weighted);
	}
	
	class Triple implements Comparable<Triple> {
		public int id;
		public Edge edge;
		public Double coefficient;
		
		public Triple(int id, Edge edge, Double coeff) {
			this.id = id;
			this.edge = edge;
			this.coefficient = coeff;
		}

		public int compareTo(Triple o) {
			if (this.coefficient.compareTo(o.coefficient) < 0) {
				return 1;
			} else if (this.coefficient.compareTo(o.coefficient) > 0) {
				return -1;
			} else {
				return 0;
			}
		}
	}
	
	public void detectCommunities() {		
		try {		
			if (weighted) {
				initializePriorityQueueWeightedCase();
			} else {
				initializePriorityQueue();
			}					
			saveLayerOfCommunities();
			
			while (!queue.isEmpty()) {
				Pair<Triple> minimum = queue.deleteMin();
				Edge edgeToBeRemoved = (minimum != null) ? minimum.key().edge : null;
				if (minimum != null && edgeToBeRemoved != null) {
					Node nodeI = edgeToBeRemoved.head();
					Node nodeJ = edgeToBeRemoved.tail();
					workbenchGraph.removeEdge(edgeToBeRemoved);
					boolean edgeDeleted = true;
					
					if (areInDifferentCommunities(nodeI, nodeJ)) {
						if (mustReplaceEdge(communityOf(nodeI), communityOf(nodeJ))) {
							workbenchGraph.addEdge(edgeToBeRemoved);
							edgeDeleted = false;
						}
						
						if (edgeDeleted) {
							saveLayerOfCommunities();
						}
					}
					
					if (edgeDeleted) {
						updateAllDataStructures(edgeToBeRemoved);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Initializes the indexed minimum priority queue for the weighted case.
	 */
	private void initializePriorityQueueWeightedCase() {
		queue = new IndexMinPQ<Triple>(workbenchGraph.numEdges());
		edgeToId = new HashMap<Edge,Integer>(workbenchGraph.numEdges());
		int id = 1;
		for (Edge edge : workbenchGraph.edges()) {
			double wecc = WECC(edge);
			Triple triple = new Triple(id, edge, wecc);
			queue.insert(id, triple);
			edgeToId.put(edge, id);
			++id;
		}
	}
	
	/**
	 * Initializes the indexed minimum priority queue for the unweighted case.
	 */
	private void initializePriorityQueue() {
		queue = new IndexMinPQ<Triple>(workbenchGraph.numEdges());
		edgeToId = new HashMap<Edge,Integer>(workbenchGraph.numEdges());
		// Fill priority queue with elements
		int id = 1;
		for (Edge edge : workbenchGraph.edges()) {
			Triple triple = new Triple(id, edge, ECC(edge));
			queue.insert(id, triple);
			edgeToId.put(edge, id);
			++id;
		}
	}
	
	/**
	 * Returns true if and only if edge must be put back into the
	 * workbench graph.
	 * 
	 * @param communityOfI one community
	 * @param communityOfJ another community
	 * @param edge an edge
	 * @return true if and only if edge must be put back into the
	 * workbench graph.
	 */
	public boolean mustReplaceEdge(Community communityOfI, Community communityOfJ) {
		boolean mustReplaceEdge = false;
		
		if ("strong".equalsIgnoreCase(definition)) { // using the strong community definition
			if (!(communityOfI.isStrongCommunity() && communityOfJ.isStrongCommunity())) {
				mustReplaceEdge = true;
			}
		} else if ("weak".equalsIgnoreCase(definition)) { // using the weak community definition
			if (!(communityOfI.isWeakCommunity() && communityOfJ.isWeakCommunity())) {
				mustReplaceEdge = true;
			}
		} else if ("bounded".equalsIgnoreCase(definition)) {
			if (!(communityOfI.isBoundedCommunity() && communityOfJ.isBoundedCommunity())) {
				mustReplaceEdge = true;
			}
		} else if ("strongweighted".equalsIgnoreCase(definition)) {
			if (!(communityOfI.isStrongWeightedCommunity() && communityOfJ.isStrongWeightedCommunity())) {
				mustReplaceEdge = true;
			}
		} else {
			if (!(communityOfI.isWeakWeightedCommunity() && communityOfJ.isWeakWeightedCommunity())) {
				mustReplaceEdge = true;
			}
		}
		
		return mustReplaceEdge;
	}
	
	/**
	 * Get a List of LayerInfo's. Each LayerInfo contains 
	 * information about each layer of communities, that is,
	 * of each partition into communities.
	 * 
	 * @return a List of LayerInfo's.
	 */
	public List<LayerInfo> getLayerInfos() {
		return this.layerInfos;
	}
	
	/**
	 * Get a list of community partitions.
	 * 
	 * @return a list of community partitions, that is, of LayerOfCommunities.
	 */
	public List<LayerOfCommunities> getCommunityPartitions() {
		return this.communityPartitions;
	}
	
	/**
	 * Get the community partition with index "index". If there
	 * are k partitions, then the partitions are numbered
	 * 0, 1, 2, ..., k-1.
	 * 
	 * @param index
	 * @return the community partition with index "index".
	 */
	public LayerOfCommunities getPartition(int index) {
		return communityPartitions.get(index);
	}
	
	/**
	 * Get the number of community partitions found by the algorithm.
	 * 
	 * @return the number of community partitions found by the algorithm.
	 */
	public int getNumPartitions() {
		return communityPartitions.size();
	}
	
	/**
	 * Saves the current community partition.
	 */
	private void saveLayerOfCommunities() {
		
		int[] connectedComponents = getConnectedComponents();
		LayerOfCommunities layerOfCommunities = new LayerOfCommunities(layerId, connectedComponents, initialGraph);
		communityPartitions.add(layerId, layerOfCommunities);
		LayerInfo layerInfo = new LayerInfo(layerId, numCommunities(connectedComponents));
		layerInfos.add(layerId, layerInfo);
		++layerId;
		
	}
	
	/**
	 * Count the number of communities (connected components) in
	 * the given community partition array.
	 * 
	 * @param connectedComponents the connected components of the graph as found by DFS
	 * at a certain point in the course of the algorithm. The length of the array is 
	 * equal to the number of vertices in the graph and each vertex is assigned an 
	 * integer the specifies the connected component that the vertex belongs to. The
	 * numbering begings at 1.
	 * @return the number of communities (connected components) in
	 * the given community partition array.
	 */
	private int numCommunities(int[] connectedComponents) {
		int currentHighestId = 1;
		for (int i = 0; i < connectedComponents.length; ++i) {
			if (connectedComponents[i] > currentHighestId) {
				currentHighestId = connectedComponents[i];
			}
		}
		return currentHighestId;
	}
	
	/**
	 * Compute the edge clustering coefficient of the
	 * given edge.
	 * 
	 * @param edge an edge
	 * @return the edge clustering coefficient of the
	 * given edge.
	 */
	private double ECC(Edge edge) {
		MinMax<Integer> minMax = new MinMax<Integer>(
				workbenchGraph.numNeighbors(edge.head())-1, 
				workbenchGraph.numNeighbors(edge.tail())-1);
		
		double numerator = numTriangles.get(edge) + 1.0;
		double denominator = minMax.min();
		double coefficient = Double.MAX_VALUE;
		
		if (denominator > 0.0)
			coefficient = numerator/denominator;
		
		return coefficient;
	}
	
	/**
	 * Computes the weighted edge clustering coefficient (WECC)
	 * of the given edge.
	 * 
	 * @param edge an edge
	 * @return the weighted edge clustering coefficient (WECC)
	 * of the given edge.
	 */
	private double WECC(Edge edge) {
		MinMax<Integer> minMax = new MinMax<Integer>(
				workbenchGraph.numNeighbors(edge.head())-1, 
				workbenchGraph.numNeighbors(edge.tail())-1);
		
		double numerator = numTriangles.get(edge) * edge.getWeight() + 1.0;
		double denominator = minMax.min();
		double coefficient = Double.MAX_VALUE;
		
		if (denominator > 0.0)
			coefficient = numerator/denominator;
		
		return coefficient;
	}
	
	/**
	 * Given an edge e = {u,v}, for every common neighbor w of u and v,
	 * uvw form a triangle and edges uw and vw must have their edge
	 * clustering coefficients on the minimum priority queue updated.
	 * 
	 * @param removedEdge
	 */
	private void updateAllDataStructures(Edge removedEdge) {
		
		Comparator<Node> nodeComparator = new Comparator<Node>() {
			@Override
			public int compare(Node a, Node b) {
				if (a.id() < b.id()) {
					return -1;
				} else if (a.id() > b.id()) {
					return 1;
				} else {
					return 0;
				}
			}
		};
		
		numTriangles.put(removedEdge, 0);
		
		int pu = 0;
		int pv = 0;
		Node u = removedEdge.head();
		Node v = removedEdge.tail();
		LinkedList<Node> neighborsOfU = (LinkedList<Node>) workbenchGraph.neighborhood(u);
		LinkedList<Node> neighborsOfV = (LinkedList<Node>) workbenchGraph.neighborhood(v);
		Collections.sort(neighborsOfU, nodeComparator);
		Collections.sort(neighborsOfV, nodeComparator);
		while (pu < neighborsOfU.size() && pv < neighborsOfV.size()) {
			if (neighborsOfU.get(pu).compareTo(neighborsOfV.get(pv)) == 0) {
				Node w = neighborsOfU.get(pu);
				updateEdgeInTriangle(new Edge(u,w));
				updateEdgeInTriangle(new Edge(v,w));
				++pu;
				++pv;
			} else if (neighborsOfU.get(pu).compareTo(neighborsOfV.get(pv)) < 0) {
				++pu;
			} else {
				++pv;
			}
		}
	}
	
	private void updateEdgeInTriangle(Edge e) {
		Integer numTrianglesOfEdge = numTriangles.get(e);
		numTriangles.put(e, numTrianglesOfEdge.intValue()-1);
		
		double coefficient = ECC(e);
		int edgeId = edgeToId.get(e).intValue();
		
		queue.changeKey(edgeId, new Triple(edgeId,e,coefficient));
	}
	
	private int[] getConnectedComponents() {
		int[] connectedComponents = dfs();		
		return connectedComponents;
	}
	
	/**
	 * Return the final community partition.
	 */
	public List<WebCommunity> getFinalCommunityPartition() {
		if (communityPartitions.size() > 0)
			return communityPartitions.get(communityPartitions.size()-1).getCommunities();
		else
			return new ArrayList<WebCommunity>();
	}
	
	/**
	 * Perform a DFS in the workbench graph and return
	 * a coloring of the graph specifying the various
	 * connected components. The first connected component
	 * is given the number 1.
	 * 
	 * @return an int array specifying the various connected
	 *         components of the workbench graph.
	 */
	private int[] dfs() {
		
		boolean[] visited = new boolean[workbenchGraph.numNodes()];
		int[] color = new int[workbenchGraph.numNodes()];
		int c = 1;
		
		for (Node node : workbenchGraph.nodes()) {
			if (!visited[node.id()]) {
				explore(node,visited,color,c);
				c = c + 1;
			}
		}
		
		return color;
	}
	
	private void explore(Node node, boolean[] visited, int[] color, int currentColor) {
		visited[node.id()] = true;
		color[node.id()] = currentColor;
		
		for (Node neighbor : workbenchGraph.neighborhood(node)) {
			if (!visited[neighbor.id()]) {
				explore(neighbor,visited,color,currentColor);
			}
		}
	}
	
	private Community communityOf(Node node) {		
		boolean[] visited = new boolean[workbenchGraph.numNodes()];
		
		explore(node, visited);
		
		return new Community(initialGraph, visited, lowerBound);
	}
	
	private boolean areInDifferentCommunities(Node i, Node j) {
		boolean[] visited = new boolean[workbenchGraph.numNodes()];
		explore(i,visited);
		if (!visited[j.id()])
			return true;
		else
			return false;
	}
	
	/**
	 * Performs a DFS starting at the node 'node'.
	 * 
	 * @param node the node where the DFS starts
	 * @param visited a boolean array holding one entry per node in the graph.
	 *        After the search, entry of every node in the connected component 
	 *        of 'node' will be true and the rest of the entries will be false.
	 */
	private void explore(Node node, boolean[] visited) {
		visited[node.id()] = true;
		for (Node neighbor : workbenchGraph.neighborhood(node))
			if (!visited[neighbor.id()])
				explore(neighbor,visited);
	}
}
