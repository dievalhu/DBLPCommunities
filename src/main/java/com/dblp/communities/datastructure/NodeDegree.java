package com.dblp.communities.datastructure;

import com.dblp.communities.graphs.Node;

public class NodeDegree {

	private Node node;
	private int degree;
	
	public NodeDegree(Node node, int degree) {
		this.node = node;
		this.degree = degree;
	}

	public Node getNode() {
		return node;
	}
	
	public int getDegree() {
		return degree;
	}	
}
