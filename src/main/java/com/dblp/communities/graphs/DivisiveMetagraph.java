package com.dblp.communities.graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.dblp.communities.utilities.MinMax;

/**
 * A metagraph that can be used in a divisive algorithm.
 */
public class DivisiveMetagraph {

	private int numCommunities;
	private ArrayList<HashMap<Integer, Integer>> neighbors; // (neighborId,numEdgesToNeighbor)
	private int[] parent;
	private int[] totalDegree;
	private int[] internalDegree;
	private int[] externalDegree;
	
	private int[] dfsColor;
	
	private HashMap<Integer, Integer> communityIdToVisibleId;
	private HashMap<Integer, Integer> visibleIdToCommunityId;

	public DivisiveMetagraph(LabeledUndirectedGraph graph) {
		this.numCommunities = graph.numNodes();
		this.parent = new int[numCommunities];
		this.totalDegree = new int[numCommunities];
		this.internalDegree = new int[numCommunities];
		this.externalDegree = new int[numCommunities];
		this.dfsColor = new int[numCommunities];
		this.communityIdToVisibleId = new HashMap<Integer,Integer>();
		this.visibleIdToCommunityId = new HashMap<Integer,Integer>();
		
		this.makeInitialNeighborhoodStructure(graph);
	}
	
	public ArrayList<HashMap<Integer, Integer>> getNeighborhoodStructure() {
		return neighbors;
	}
	
	/**
	 * Returns the visible id that corresponds to the
	 * metagraph id. Returns -1 if there
	 * exists no metagraph id equal to visibleId.
	 * 
	 * @param metagraphId the id of a parent
	 * @return the visible id that corresponds to the
	 * metagraph id.
	 */
	public Integer metagraphIdToVisibleId(int communityId) {
		Integer returnValue = communityIdToVisibleId.get(communityId);
		if (returnValue != null)
			return returnValue.intValue();
		else
			return -1;
	}
	
	/**
	 * Returns the metagraph ID that corresponds to the
	 * visible ID 'visibleID'. Returns -1 if there
	 * exists no visble id equal to visibleId.
	 * 
	 * @param visibleId the visible ID of a community
	 * @return the metagraph ID that correspond to the
	 * visible ID 'visibleID'.
	 */
	public int visibleIdToMetagraphId(int visibleId) {	
		Integer returnValue = visibleIdToCommunityId.get(visibleId);
		if (returnValue != null)
			return returnValue.intValue();
		else
			return -1;
	}
	
	public void setDFSColoring(int[] dfsColoring) {
		for (int i = 0; i < dfsColor.length; ++i) {
			dfsColor[i] = dfsColoring[i];
		}
	}

	/**
	 * Creates the initial neighborhood structure for the metagraph.
	 * 
	 * @param graph
	 */
	private void makeInitialNeighborhoodStructure(LabeledUndirectedGraph graph) {
		this.neighbors = new ArrayList<HashMap<Integer, Integer>>();
		// # communities = # vertices
		for (int nodeId = 0; nodeId < numCommunities; ++nodeId) {
			this.neighbors.add(nodeId, new HashMap<Integer, Integer>());
			Node node = new Node(nodeId);
			this.makeItsOwnCommunity(nodeId);
			this.totalDegree[nodeId] = graph.numNeighbors(node);
			this.internalDegree[nodeId] = 0;
			this.externalDegree[nodeId] = this.totalDegree[nodeId];
			for (Node neighbor : graph.neighborhood(node)) {
				this.neighbors.get(nodeId).put(neighbor.id(), 1);
			}
		}
	}
	
	/**
	 * Returns the number of internal edges in the community 
	 * of vertex v.
	 * 
	 * @param v a vertex
	 * @return the number of internal edges in the community 
	 * of vertex v.
	 */
	public int numInternalEdges(int v) {
		int communityOfV = find(v);
		return internalDegree[communityOfV];
	}
	
	/**
	 * Returns the number of external edges in the community
	 * of vertex v.
	 * 
	 * @param v a vertex
	 * @return the number of external edges in the community
	 * of vertex v.
	 */
	public int numExternalEdges(int v) {
		int communityOfV = find(v);
		return externalDegree[communityOfV];
	}
	
	/**
	 * Returns the number of edges between communities of
	 * vertices i and j. If i and j are in the same community,
	 * then 0 is returned.
	 * 
	 * @param i one vertex
	 * @param j another vertex
	 * @return the number of edges between communities with ids i and j.
	 */
	public int numEdgesBetweenCommunities(int i, int j) {
		
		if (i == j)
			return 0;
		
		int communityOfI = find(i);
		int communityOfJ = find(j);
		
		if (communityOfI == communityOfJ)
			return 0;
		
		MinMax<Integer> minMax = new MinMax<Integer>(communityOfI, communityOfJ);
		Integer numEdges = this.neighbors.get(minMax.min()).get(minMax.max());
		
		// i and j are not neighbors
		if (numEdges == null)
			return 0;
		
		return numEdges.intValue();
	}

	public boolean areNeighbors(int i, int j) {
		if (neighbors.get(i).containsKey(j))
			return true;
		else
			return false;
	}

	/**
	 * Are vertices i and j in the same community?
	 * 
	 * @param i one vertex
	 * @param j another vertex
	 * @return true if and only if vertices i and j are in the same community.
	 */
	public boolean areInSameCommunity(int i, int j) {

		int iRoot = find(i);
		int jRoot = find(j);

		if (iRoot == jRoot)
			return true;
		else
			return false;
	}

	private void makeItsOwnCommunity(int vertex) {
		makeSet(vertex);
	}

	private void makeSet(int vertex) {
		parent[vertex] = vertex;
	}
	
	public int parent(int v) {
		return find(v);
	}

	private int find(int v) {
		if (parent[v] == v)
			return v;
		else
			return find(parent[v]);
	}

	/**
	 * Merge the communities of vertices u and v.
	 * 
	 * @param u one vertex
	 * @param v another vertex
	 */
	private void union(int u, int v) {

		int uRoot = find(u);
		int vRoot = find(v);

		if (uRoot == vRoot)
			return;

		if (uRoot < vRoot) {
			parent[vRoot] = uRoot;
			parent[uRoot] = uRoot;
		} else {
			parent[uRoot] = vRoot;
			parent[vRoot] = vRoot;
		}
	}

	public int numCommunities() {
		return this.numCommunities;
	}

	/**
	 * Retrieves the community id of vertex v.
	 * 
	 * @param v a vertex
	 * @return the community id of vertex v.
	 */
	public int communityOf(int v) {
		int root = find(v);
		return root;
	}

	public int totalDegree(int v) {
		return totalDegree[v];
	}

	/**
	 * Retrieve the neighborhood of the community 
	 * of vertex v.
	 * 
	 * @param v a vertex
	 * @return the neighborhood of the community of vertex v.
	 */
	public Set<Integer> neighborhood(int v) {
		int communityOfV = find(v);
		return neighbors.get(communityOfV).keySet();
	}
	
	/**
	 * Returns the number of neighboring communities 
	 * of the community of vertex v.
	 * 
	 * @param v a vertex
	 * @return the number of neighboring communities 
	 * of the community of vertex v.
	 */
	public int numNeighbors(int v) {
		int communityOfV = find(v);
		return neighbors.get(communityOfV).size();
	}

	/**
	 * Merges the communities of vertices i and j.
	 * 
	 * @param i one vertex
	 * @param j another vertex
	 */
	public void merge(int i, int j) {
		
		if (i == j)
			return;
		
		int communityOfI = find(i);
		int communityOfJ = find(j);
		
		if (communityOfI == communityOfJ)
			return;
		
		union(i, j);		

		totalDegree[communityOfI] = totalDegree[communityOfI] + totalDegree[communityOfJ];
		totalDegree[communityOfJ] = 0;

		updateNeighborhoodStructure(communityOfI,communityOfJ);
		
		Integer numEdgesBetweenIAndJ = neighbors.get(communityOfI).get(communityOfJ);
		if (numEdgesBetweenIAndJ == null)
			numEdgesBetweenIAndJ = 0;
		internalDegree[communityOfI] = internalDegree[communityOfI] + internalDegree[communityOfJ] + 2 * numEdgesBetweenIAndJ;
		internalDegree[communityOfJ] = 0;		
		externalDegree[communityOfI] = externalDegree[communityOfI] + externalDegree[communityOfJ] - 2 * numEdgesBetweenIAndJ;
		externalDegree[communityOfJ] = 0;
		
		numCommunities = numCommunities - 1;
		
		updateVisibleIds();
	}
	
	private void updateVisibleIds() {
		int visibleId = 1;
		communityIdToVisibleId.clear();
		visibleIdToCommunityId.clear();
		
		for (int metaID = 0; metaID < neighbors.size(); ++metaID) {
			if (neighbors.get(metaID).size() > 0) {
				communityIdToVisibleId.put(metaID, visibleId);
				visibleIdToCommunityId.put(visibleId, metaID);
				++visibleId;
			}
		}
	}

	/**
	 * Update the neighborhood structure when merging communities
	 * with ids i and j.
	 * 
	 * @param i the metagraph id of one community
	 * @param j the metagraph id of another community
	 */
	private void updateNeighborhoodStructure(int i, int j) {
		// Transfer neighbors of j to i
		Set<Integer> neighborsofJ = neighbors.get(j).keySet();
		for (Integer k : neighborsofJ) {
			Integer numEdgesBetweenIAndK = neighbors.get(i).get(k);
			Integer numEdgesBetweenJAndK = neighbors.get(j).get(k);
			if (numEdgesBetweenIAndK != null) { // commen neighbor of i and j
				neighbors.get(i).put(k,	numEdgesBetweenIAndK + numEdgesBetweenJAndK);
				neighbors.get(k).put(i, numEdgesBetweenIAndK + numEdgesBetweenJAndK);
			} else { // neighbor only of j
				neighbors.get(i).put(k, numEdgesBetweenJAndK);
				neighbors.get(k).put(i, numEdgesBetweenJAndK);
			}
			neighbors.get(k).remove(j);
		}

		neighbors.get(i).remove(i);
		neighbors.get(i).remove(j);
		neighbors.get(j).clear();
	}
	
	public void printNeighborhoods() {
		
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < neighbors.size(); ++i) {
			builder.append(i + ": ");
			HashMap<Integer, Integer> neighborsOfI = neighbors.get(i);
			for (Entry<Integer,Integer> entry : neighborsOfI.entrySet()) {
				builder.append("(" + entry.getKey() + "," + entry.getValue() + ")");
			}
			builder.append(" TOTAL = " + neighbors.get(i).size() + "\n");
		}
		
		System.out.println(builder);
		
	}
}
