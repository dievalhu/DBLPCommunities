package com.dblp.communities.graphs;

import com.dblp.communities.utilities.MinMax;

public class Edge {
	
	protected Node head;
	protected Node tail;
	protected Integer weight;

	public Edge() {
		this.head = null;
		this.tail = null;
		this.weight = new Integer(0);
	}
	
	public Edge(Node head, Node tail) {
		this.head = head;
		this.tail = tail;
		this.weight = new Integer(1);
	}
	
	/**
	 * Creates an edge.
	 * 
	 * @param head the head node of the edge
	 * @param tail the tail node of the edge
	 * @param weight the weight of the edge
	 */
	public Edge(Node head, Node tail, Integer weight) {
		this.head = head;
		this.tail = tail;
		this.weight = weight;
	}
	
	/**
	 * Getters and setters.
	 */
	
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	
	public Node getHead() {
		return head;
	}

	public void setHead(Node head) {
		this.head = head;
	}

	public Node getTail() {
		return tail;
	}

	public void setTail(Node tail) {
		this.tail = tail;
	}
	
	/**
	 * Other methods.
	 */

	public Edge copy() {
		return new Edge(this.head.copy(), this.tail.copy(), new Integer(this.getWeight()));
	}
	
	public int start() {
		return this.head.id();
	}
	
	public int end() {
		return this.tail.id();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Edge))
			return false;
		
		if (this == obj)
			return true;
		
		Edge other = (Edge) obj;
		
		if (this.head.id() == other.head().id() && this.tail.id() == other.tail().id())
			return true;
		else if (this.head.id() == other.tail().id() && this.tail.id() == other.head().id())
			return true;
		else
			return false;
	}
	
	@Override
	public int hashCode() {
		MinMax<Integer> mm = new MinMax<Integer>(start(), end());
		return mm.min().intValue() * 17 + mm.max().intValue() * 31;
	}

	public Node head() {
		return this.head;
	}

	public Node tail() {
		return this.tail;
	}
}
