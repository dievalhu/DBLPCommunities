package com.dblp.communities.graphs;

public class MultigraphEdge extends Edge {

	protected Integer id;
	
	public MultigraphEdge() {
		super(null,null);
		this.id = null;
	}
	
	public MultigraphEdge(Integer id, Node head, Node tail) {
		super(head,tail);
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MultigraphEdge))
			return false;
		
		if (this == obj)
			return true;
		
		MultigraphEdge other = (MultigraphEdge) obj;
		
		if (this.id.equals(other.id))
			return true;
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}

	public Edge copy() {
		return new MultigraphEdge(this.id, this.head.copy(), this.tail.copy());
	}
	
}
