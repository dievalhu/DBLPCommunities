package com.dblp.communities.graphs;

public class Node implements Comparable<Node> {

	protected int id;
	
	public Node(int id) {
		this.id = id;
	}
	
	public Node copy() {
		return new Node(this.id);
	}
	
	public int id() {
		return this.id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Node))
			return false;
		
		if (obj == this)
			return true;
		
		Node other = (Node) obj;
		
		if (this.id == other.id)
			return true;
		else
			return false;
	}
	
	@Override
	public int hashCode() {
		return id;
	}

	public int compareTo(Node o) {
		if (this.id() < o.id())
			return -1;
		else if (this.id() > o.id())
			return 1;
		else
			return 0;
	}
}
