package com.dblp.communities.parser;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ParserNode implements Serializable {

	private static final long serialVersionUID = -166374483974370194L;
	private String name;
	
	public ParserNode() {
		this.name = null;
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof ParserNode))
			return false;
		if (this == obj)
			return true;
		
		ParserNode other = (ParserNode) obj;
		return new EqualsBuilder().append(name, other.getName()).isEquals();
	}
	
	public int hashCode() {
		return new HashCodeBuilder(17,31).append(name).toHashCode();
	}
	
	public ParserNode(String n) {
		this.name = n;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
