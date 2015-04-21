package com.dblp.communities.parser;

import java.io.Serializable;

public class ParserEdge implements Serializable {

	private static final long serialVersionUID = 7864404639086077394L;
	private ParserNode start;
	private ParserNode end;
	
	public ParserEdge() {
		this.start = null;
		this.end = null;
	}
	
	public ParserEdge(ParserNode s, ParserNode e) {
		this.start = s;
		this.end = e;
	}

	public ParserNode getStart() {
		return start;
	}

	public void setStart(ParserNode start) {
		this.start = start;
	}

	public ParserNode getEnd() {
		return end;
	}

	public void setEnd(ParserNode end) {
		this.end = end;
	}
	
}
