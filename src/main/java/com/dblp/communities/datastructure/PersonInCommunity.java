package com.dblp.communities.datastructure;

public class PersonInCommunity implements Comparable<PersonInCommunity> {
	private int nodeId;
	private int color;
	private String name;
	private int numPublicationsWithMainAuthor;
	
	/**
	 * Creates an object representing a person in a community.
	 * 
	 * @param nodeId the node id of the node representing the given person
	 * @param communityId the community id of this person, as given by a DFS in the graph
	 * @param name the name of this person
	 */
	public PersonInCommunity(int nodeId, int communityId, String name, int numPublicationsWithMainAuthor) {
		this.nodeId = nodeId;
		this.color = communityId;
		this.name = name;
		this.numPublicationsWithMainAuthor = numPublicationsWithMainAuthor;
	}

	public int compareTo(PersonInCommunity other) {
		if (this.color < other.color) {
			return -1;
		} else if (this.color > other.color) {
			return 1;
		} else if (this.nodeId < other.nodeId) {
			return -1;
		} else if (this.nodeId > other.nodeId) {
			return 1;
		} else {
			return 0;
		}
	}
	
	public int getNumPublicationsWithMainAuthor() {
		return numPublicationsWithMainAuthor;
	}

	public void setNumPublicationsWithMainAuthor(int numPublicationsWithMainAuthor) {
		this.numPublicationsWithMainAuthor = numPublicationsWithMainAuthor;
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
