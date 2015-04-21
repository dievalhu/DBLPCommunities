package com.dblp.communities.graphs;

public class LabeledMultigraphNode extends Node {

	protected String name;
	
	public LabeledMultigraphNode() {
		super(0);
		this.name = null;
	}
	
	public LabeledMultigraphNode(int id, String name) {
		super(id);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof LabeledMultigraphNode))
			return false;
		
		if (this == obj)
			return true;
		
		LabeledMultigraphNode other = (LabeledMultigraphNode) obj;
		
		if (this.id == other.id)
			return true;
		
		return false;
	}
	
	public int hashCode() {
		return new Integer(this.id).hashCode();
	}

	public LabeledMultigraphNode copy() {
		return new LabeledMultigraphNode(id,name);
	}
	
}
