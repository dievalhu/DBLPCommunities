package com.dblp.communities.graphs;

import com.dblp.communities.utilities.MinMax;

public class Edge {
	
	protected Node head;
	protected Node tail;
	protected Integer count;

	public Edge() {
		this.head = null;
		this.tail = null;
		this.count = new Integer(0);
	}
	
	public Edge(Node head, Node tail) {
		this.head = head;
		this.tail = tail;
		this.count = new Integer(1);
	}
	
	public Edge(Node head, Node tail, Integer count) {
		this.head = head;
		this.tail = tail;
		this.count = count;
	}
	
	/**
	 * Getters and setters.
	 */
	
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
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
		return new Edge(this.head.copy(), this.tail.copy());
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
