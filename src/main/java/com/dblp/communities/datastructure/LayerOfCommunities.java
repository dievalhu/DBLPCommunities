package com.dblp.communities.datastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import com.dblp.communities.graphs.DivisiveMetagraph;
import com.dblp.communities.graphs.Edge;
import com.dblp.communities.graphs.LabeledUndirectedGraph;
import com.dblp.communities.graphs.Node;

public class LayerOfCommunities {

	/**
	 * The untouched and untouchable input graph.
	 */
	private LabeledUndirectedGraph initialGraph;
	/**
	 * An integer array of length equal to the number of vertices
	 * in the input graph "initialGraph".
	 */
	private int[] connectedComponents;
	/**
	 * The communities that are to be displayed on the web page.
	 */
	private List<WebCommunity> visualCommunities;	
	/**
	 * A metagraph for this partition into communities.
	 */
	private DivisiveMetagraph metagraph;
	/**
	 * True if and only if the communities have been prepared to be 
	 * displayed on the web page.
	 */
	private boolean communitiesArePrepared;
	/**
	 * A unique integer id for this layer of communities.
	 */
	private int layerId;
	/**
	 * The total number of edges in the given snapshot of the 
	 * workbench graph.
	 */
	private int totalNumEdges;
	/**
	 * The number of intra-community edges in the given
	 * snapshot of the workbench graph.
	 */
	private int numIntraCommunityEdges;
	/**
	 * The number of inter-community edges in the given
	 * snapshot of the workbench graph.
	 */
	private int numInterCommunityEdges;
	/**
	 * The sum of the weights on the intra-community edges.
	 */
	private int totalWeightOnIntraCommunityEdges;
	/**
	 * The sum of the weights on the inter-community edges.
	 */
	private int totalWeightOnInterCommunityEdges;
	
	private Double modularity;
	
	private boolean modularityComputed;
	
	private Double weightedModularity;
	
	private boolean weightedModularityComputed;
	
	private Integer totalEdgeWeight;
	
	private boolean totalEdgeWeightComputed;
	
	private HashMap<Integer,Integer> metaIdToVisualId = new HashMap<Integer,Integer>();
	
	private int[] communityWeights;
	

	/**
	 * Makes a layer of communities, that is, a partition into a certain number of communities.
	 * Different layers have different numbers of communities.
	 * 
	 * @param connectedComponents an integer array giving integer ids to every vertex of the 
	 * graph specifying the connected components that they belong to. The lowest id of a 
	 * connected component is 1.
	 * @param initialGraph the untouched input graph to the algorithm
	 */
	public LayerOfCommunities(int id, int[] connectedComponents, LabeledUndirectedGraph initialGraph) {
		this.layerId = id;
		this.initialGraph = initialGraph;
		this.connectedComponents = connectedComponents;
		this.visualCommunities = new ArrayList<WebCommunity>();
		this.communitiesArePrepared = false;
		this.modularityComputed = false;
		this.weightedModularityComputed = false;
		this.totalEdgeWeightComputed = false;
		this.communityWeights = new int[connectedComponents.length];
		countEdgeTypes();
	}
	
	////////////////////
	// PUBLIC METHODS //
	////////////////////
	
	public Double getWeightedModularity() {
		if (!communitiesArePrepared()) {
			prepareCommunities();	
		}
		
		if (weightedModularityComputed) {
			return weightedModularity;
		}
		
		Double sum = 0.0;
		Integer totalWeight = totalEdgeWeight();
		
		for (int i = 0; i < initialGraph.numNodes(); ++i) {
			for (int j = i + 1; j < initialGraph.numNodes(); ++j) {
				if (metagraph.areInSameCommunity(i, j)) {
					Node iNode = new Node(i);
					Node jNode = new Node(j);
					if (initialGraph.areNeighbors(iNode, jNode)) {
						int si = initialGraph.strength(iNode);
						int sj = initialGraph.strength(jNode);
						Integer ijWeight = initialGraph.weight(iNode, jNode);
						sum = sum + (ijWeight - ((si * sj) / (2 * totalWeight)));
					}
				}
			}
		}
		
		weightedModularity = sum/(2.0 * totalWeight);
		
		weightedModularityComputed = true;
		
		return weightedModularity;
	}
	
	public Double getModularity() {
		if (!communitiesArePrepared()) {
			prepareCommunities();	
		}
		
		if (modularityComputed) {
			return modularity;
		}
		
		Double sum = 0.0;
		int m = initialGraph.numEdges();
		
		for (int i = 0; i < initialGraph.numNodes(); ++i) {
			for (int j = i + 1; j < initialGraph.numNodes(); ++j) {
				if (metagraph.areInSameCommunity(i, j)) {
					if (initialGraph.areNeighbors(new Node(i), new Node(j))) {
						int degI = initialGraph.numNeighbors(new Node(i));
						int degJ = initialGraph.numNeighbors(new Node(j));
						sum = sum + (1 - ((degI * degJ) / (2 * m)));
					}
				}
			}
		}
		
		modularity = sum/(2.0 * m);
		
		modularityComputed = true;
		
		return modularity;
	}
	
	public int getTotalWeightOnIntraCommunityEdges() {
		return totalWeightOnIntraCommunityEdges;
	}
	
	public int getTotalWeightOnInterCommunityEdges() {
		return totalWeightOnInterCommunityEdges;
	}
	
	/**
	 * Get the number of inter-community edges in the
	 * given snapshot of the workbench graph.
	 * 
	 * @return the number of inter-community edges in the
	 * given snapshot of the workbench graph.
	 */
	public int getNumInterCommunityEdges() {
		return this.numInterCommunityEdges;
	}

	/**
	 * Get the number of intra-community edges in the
	 * given snapshot of the workbench graph.
	 * 
	 * @return the number of intra-community edges in the
	 * given snapshot of the workbench graph.
	 */
	public int getNumIntraCommunityEdges() {
		return this.numIntraCommunityEdges;
	}
	
	/**
	 * Get the total number of edges in the given
	 * snapshot of the workbench graph.
	 * 
	 * @return the total number of edges in the given
	 * snapshot of the workbench graph.
	 */
	public int getTotalNumEdges() {
		return this.totalNumEdges;
	}
	
	/**
	 * Get the id of this layer of communities.
	 * 
	 * @return the id of this layer of communities.
	 */
	public int getId() {
		return this.layerId;
	}
	
	public int numCommunities() {
		if (!communitiesArePrepared()) {
			prepareCommunities();	
		}
		
		return this.visualCommunities.size();
	}
	
	public List<WebCommunity> getCommunities() {
		if (!communitiesArePrepared()) {
			prepareCommunities();	
		}
		
		return this.visualCommunities;
	}
	
	/////////////////////
	// PRIVATE METHODS //
	/////////////////////
	
	/**
	 * Get the total edge weight of the graph.
	 * 
	 * @return
	 */
	private Integer totalEdgeWeight() {
		if (totalEdgeWeightComputed) {
			return totalEdgeWeight;
		}
		
		totalEdgeWeight = new Integer(0);
		
		for (Edge e : initialGraph.edges()) {
			totalEdgeWeight = totalEdgeWeight + e.getWeight();
		}
		
		totalEdgeWeightComputed = true;
		
		return totalEdgeWeight;
	}
	
	/**
	 * Retrieves the total number of edges in the original
	 * graph, and counts the number of intra and inter 
	 * community edges in the same graph. 
	 */
	private void countEdgeTypes() {
		totalNumEdges = initialGraph.numEdges();
		
		numIntraCommunityEdges = 0;
		numInterCommunityEdges = 0;
		totalWeightOnIntraCommunityEdges = 0;
		totalWeightOnInterCommunityEdges = 0;
		for (Edge edge : initialGraph.edges()) {
			if (connectedComponents[edge.start()] == connectedComponents[edge.end()]) {
				++numIntraCommunityEdges;
				totalWeightOnIntraCommunityEdges += edge.getWeight();
				int communityId = connectedComponents[edge.start()];
				communityWeights[communityId] += edge.getWeight();
			} else {
				++numInterCommunityEdges;
				totalWeightOnInterCommunityEdges += edge.getWeight();
			}
		}
	}
	
	private boolean communitiesArePrepared() {
		return this.communitiesArePrepared;
	}
	
	private void prepareCommunities() {
		createCommunitiesInInitialState();
		makeMetagraph();
		injectCommunityIdsIntoComponents();
		makeEdgesBetweenCommunities();
		this.communitiesArePrepared = true;
	}
	
	private void makeEdgesBetweenCommunities() {
		for (WebCommunity c : visualCommunities) {
			if (c.getElements().size() > 0) {
				PersonInCommunity firstPerson = c.getElements().get(0);
				int representativeNodeId = firstPerson.getNodeId();
				Set<Integer> neighborsOfC = metagraph.neighborhood(representativeNodeId);	
				for (Integer neighborMetaID : neighborsOfC) {
					int numEdgesBetweenCommunities = metagraph.numEdgesBetweenCommunities(representativeNodeId, neighborMetaID);
					if (numEdgesBetweenCommunities > 0) {
						c.setNumEdgesToCommunity(metaIdToVisualId.get(neighborMetaID), numEdgesBetweenCommunities);
					}
				}
			}
		}
	}
	
	private void injectCommunityIdsIntoComponents() {		
		for (WebCommunity community : visualCommunities) {
			if (community.getElements().size() > 0) {
				int representative = community.getElements().get(0).getNodeId();
				community.setMetagraphParentId(metagraph.parent(representative));
				metaIdToVisualId.put(community.getMetagraphParentId(), community.getId());
			}
		}
	}
	
	private void makeMetagraph() {
		this.metagraph = new DivisiveMetagraph(initialGraph);
		for (WebCommunity community : visualCommunities) {
			if (community.getElements().size() > 0) {
				PersonInCommunity firstElement = community.getElements().get(0);
				for (PersonInCommunity elm : community) {
					metagraph.merge(firstElement.getNodeId(), elm.getNodeId());
				}
			}
		}
	}

	private void createCommunitiesInInitialState() {
		PriorityQueue<PersonInCommunity> queue = new PriorityQueue<PersonInCommunity>();

		for (int i = 0; i < connectedComponents.length; ++i) {
			String authorName = initialGraph.nameOfNodeWithId(i);
			Integer numPublicationsWithMainAuthor = initialGraph.numPublicationsWithMainAuthor(authorName);
			PersonInCommunity elm = new PersonInCommunity(i, connectedComponents[i], authorName, numPublicationsWithMainAuthor);
			queue.add(elm);
		}

		int currentColor = 1;
		WebCommunity currCommunity = new WebCommunity();
		int visualCommunityId = 1; // the id to be shown on the web page
		PersonInCommunity firstElm = queue.poll();
		currCommunity.add(firstElm);
		currCommunity.setId(visualCommunityId);
		currCommunity.setColor(currentColor);
		currCommunity.setInternalEdgeWeight(communityWeights[firstElm.getColor()]);

		/**
		 * All people with id 1 is retrieved first, then all people with id 2, etc.
		 */
		while (!queue.isEmpty()) {
			PersonInCommunity elm = queue.poll();
			if (elm.getColor() != currentColor) {
				currentColor = elm.getColor();
				visualCommunities.add(currCommunity);				
				++visualCommunityId;
				currCommunity = new WebCommunity();
				currCommunity.setInternalEdgeWeight(communityWeights[elm.getColor()]);
				currCommunity.setId(visualCommunityId);
				currCommunity.setColor(currentColor);
			}
			currCommunity.add(elm);
		}
		
		visualCommunities.add(currCommunity);
	}
}
