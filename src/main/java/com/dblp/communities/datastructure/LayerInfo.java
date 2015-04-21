package com.dblp.communities.datastructure;

/**
 * Holds information about a layer of communities, that is,
 * of a partition into communities.
 */
public class LayerInfo {

	private int layerId;
	private int numCommunities;
	
	public LayerInfo() {
		
	}
	
	public LayerInfo(int id, int numCommunities) {
		this.layerId = id;
		this.numCommunities = numCommunities;
	}
	
	public int getLayerId() {
		return layerId;
	}
	public void setLayerId(int layerId) {
		this.layerId = layerId;
	}
	public int getNumCommunities() {
		return numCommunities;
	}
	public void setNumCommunities(int numCommunities) {
		this.numCommunities = numCommunities;
	}
	
}
