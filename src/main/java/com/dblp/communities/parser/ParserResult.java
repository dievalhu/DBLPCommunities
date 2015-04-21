package com.dblp.communities.parser;

import java.io.Serializable;
import java.util.List;

public class ParserResult implements Serializable {

	private static final long serialVersionUID = -4973652249539574358L;
	private List<ParserNode> coauthors;
	private List<ParserEdge> relationships;
	
	public ParserResult() {
		this.coauthors = null;
		this.relationships = null;
	}
	
	public ParserResult(List<ParserNode> nodes, List<ParserEdge> edges) {
		this.coauthors = nodes;
		this.relationships = edges;
	}

	public List<ParserNode> getCoauthors() {
		return coauthors;
	}

	public void setCoauthors(List<ParserNode> coauthors) {
		this.coauthors = coauthors;
	}

	public List<ParserEdge> getRelationships() {
		return relationships;
	}

	public void setRelationships(List<ParserEdge> relationships) {
		this.relationships = relationships;
	}
	
}
