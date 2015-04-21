package com.dblp.communities.datastructure;

public class PersonInCommunity implements Comparable<PersonInCommunity> {
	public int nodeId;
	public int color;
	public String name;
	
	public PersonInCommunity(int n, int c, String na) {
		nodeId = n;
		color = c;
		name = na;
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
