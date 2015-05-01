package com.dblp.communities.graphs;

import java.util.ArrayList;
import java.util.List;

import com.dblp.communities.datastructure.PersonInCommunity;
import com.dblp.communities.datastructure.WebCommunity;

public class JSONGraph {

	private List<JSONEdge> edges;
	private List<JSONNode> nodes;
	
	public JSONGraph(LabeledUndirectedGraph graph, List<WebCommunity> components) {
		this.nodes = new ArrayList<JSONNode>(graph.numNodes());
		this.edges = new ArrayList<JSONEdge>(graph.numEdges());
		
		int numEdges = 1;
		for (Edge edge : graph.edges()) {
			String headId = new String(edge.head().id() + "");
			String tailId = new String(edge.tail().id() + "");
			String edgeId = new String(numEdges + "");
			String label = new String(edge.getWeight() + "");
			++numEdges;
			JSONEdge jsonEdge = new JSONEdge(edgeId, headId, tailId, label);
			this.edges.add(jsonEdge);
		}
		
		int x = 0;
		int y = 0;
		
		for (WebCommunity component : components) {
			int curr = 0;
			x = 0;
			for (PersonInCommunity elm : component) {
				String id = new String(elm.getNodeId() + "");
				JSONNode jsonNode = new JSONNode(id,graph.idToName.get(Integer.parseInt(id)));
				jsonNode.setGroup(component.getId());
				jsonNode.setX(x);
				jsonNode.setY(y);
				++curr;
				x = updateX(x);
				y = updateY(y, curr);
				
				this.nodes.add(jsonNode);
			}
			y = y + 40;
		}
	}
	
	private int updateX(int currentX) {
		int newX = (currentX + 20) % (200);
		return newX;
	}
	
	private int updateY(int currentY, int currentNodeNumber) {
		int newY = currentY;
		if (currentNodeNumber % 10 == 0) {
			newY = currentY + 20;
		} else {
			newY = currentY;
		}
		if (currentNodeNumber == 0)
			newY = currentY;
		return newY;
	}

	public List<JSONNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<JSONNode> nodes) {
		this.nodes = nodes;
	}

	public List<JSONEdge> getEdges() {
		return edges;
	}

	public void setEdges(List<JSONEdge> edges) {
		this.edges = edges;
	}
}
