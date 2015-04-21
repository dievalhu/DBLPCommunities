package com.dblp.communities.datastructure;

import com.dblp.communities.graphs.Edge;
import com.dblp.communities.graphs.LabeledUndirectedGraph;
import com.dblp.communities.graphs.Node;

public class Community {

	private LabeledUndirectedGraph initialGraph;
	private boolean[] in;
	private int size;
	private boolean sizeComputed;
	private boolean isStrongCommunity;
	private boolean hasComputedStrongProperty;
	private boolean isWeakCommunity;
	private boolean hasComputedWeakProperty;
	private boolean hasComputedWeakWeightedProperty;
	private boolean isWeakWeightedCommunity;
	private boolean hasComputedStrongWeightedProperty;
	private boolean isStrongWeightedCommunity;
	
	public Community(LabeledUndirectedGraph initialGraph, boolean[] in) {
		this.initialGraph = initialGraph;
		this.in = in;
		this.sizeComputed = false;
		this.size = -1;
		this.hasComputedStrongProperty = false;
		this.isStrongCommunity = false;
		this.hasComputedWeakProperty = false;
		this.isWeakCommunity = false;
		this.hasComputedWeakWeightedProperty = false;
		this.hasComputedStrongWeightedProperty = false;
		this.isStrongWeightedCommunity = false;
	}
	
	public boolean isWeakWeightedCommunity() {
		if (hasComputedWeakWeightedProperty)
			return isWeakWeightedCommunity;
		
		int sumInternal = 0;
		int sumExternal = 0;
		
		for (Edge e : initialGraph.edges()) {
			if (in[e.start()] && !in[e.end()]) {
				sumExternal += e.getCount();
			} else if (!in[e.start()] && in[e.end()]) {
				sumExternal += e.getCount();
			} else if (in[e.start()] && in[e.end()]) {
				sumInternal += e.getCount();
				sumInternal += e.getCount();
			} else { 
				// both not in -> do nothing
			}
		}
		
		if (sumInternal > sumExternal) {
			isWeakWeightedCommunity = true;
		} else {
			isWeakWeightedCommunity = false;
		}
		
		hasComputedWeakWeightedProperty = true;
		
		return isWeakWeightedCommunity;
	}
	
	public boolean isWeakCommunity() {
		if (hasComputedWeakProperty)
			return isWeakCommunity;
		
		int sumInternal = 0;
		int sumExternal = 0;
		
		for (int node = 0; node < in.length; ++node) {
			if (in[node]) {
				int internal = 0;
				int external = 0;
				for (Node neighbor : initialGraph.neighborhood(new Node(node))) {
					if (in[neighbor.id()]) {
						++internal;
					} else {
						++external;
					}
				}
				sumInternal += internal;
				sumExternal += external;
			}
		}
		
		if (sumInternal > sumExternal) {
			isWeakCommunity = true;
		} else {
			isWeakCommunity = false;
		}
		
		hasComputedWeakProperty = true;
		
		return isWeakCommunity;
	}
	
	public boolean isStrongWeightedCommunity() {
		if (hasComputedStrongWeightedProperty)
			return isStrongWeightedCommunity;
		
		isStrongWeightedCommunity = true; // true until the opposite is proved
		
		int[] numInternal = new int[initialGraph.numNodes()];
		int[] numExternal = new int[initialGraph.numNodes()];
		
		for (Edge e : initialGraph.edges()) {
			if (in[e.start()] && !in[e.end()]) {
				numExternal[e.start()] += e.getCount();
			} else if (!in[e.start()] && in[e.end()]) {
				numExternal[e.end()] += e.getCount();
			} else if (in[e.start()] && in[e.end()]) {
				numInternal[e.start()] += e.getCount();
				numInternal[e.end()] += e.getCount();
			} else { 
				// if both not inside community -> do nothing
			}
		}
		
		for (int i = 0; i < initialGraph.numNodes(); ++i) {
			if (in[i]) {
				if (numExternal[i] >= numInternal[i]) {
					isStrongWeightedCommunity = false;
					break;
				}
			}
		}
		
		hasComputedStrongWeightedProperty = true;
		
		return true;
	}
	
	public boolean isStrongCommunity() {
		if (hasComputedStrongProperty)
			return isStrongCommunity;
		
		isStrongCommunity = true; // true until the opposite is proved
		
		for (int node = 0; node < in.length; ++node) {
			if (in[node]) {
				if (!hasStrictlyMoreInternalNeighbors(node)) {
					isStrongCommunity = false;
					break;
				}
			}
		}
		
		hasComputedStrongProperty = true;
		
		return isStrongCommunity;
	}
	
	public int size() {
		if (sizeComputed)
			return size;
		
		int n = 0;
		
		for (int i = 0; i < in.length; ++i) {
			if (in[i]) {
				n += 1;
			}
		}
		
		size = n;		
		sizeComputed = true;
		
		return size;
	}
	
	private boolean hasStrictlyMoreInternalNeighbors(int nodeId) {
		
		Node node = new Node(nodeId);
		
		int numInternal = 0;
		int numExternal = 0;
		
		for (Node neighbor : initialGraph.neighborhood(node)) {
			if (in[neighbor.id()]) {
				++numInternal;
			} else {
				++numExternal;
			}
		}
		
		if (numInternal > numExternal)
			return true;
		else
			return false;
	}
}
