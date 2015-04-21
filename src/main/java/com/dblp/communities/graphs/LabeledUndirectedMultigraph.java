package com.dblp.communities.graphs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import com.dblp.communities.parser.Coauthor;
import com.dblp.communities.utilities.MinMax;

public class LabeledUndirectedMultigraph extends LabeledUndirectedGraph implements Serializable {

	private static final long serialVersionUID = 5175408527254262141L;
	
	public LabeledUndirectedMultigraph(LabeledUndirectedMultigraph toBeCopied) {
		super(toBeCopied);
	}
	
	public LabeledUndirectedMultigraph(String mainPersonName, String mainPersonUrlPt, boolean includeMainAuthor,
			ArrayList<Coauthor> coauthors, ArrayList<ArrayList<Coauthor>> listOfListOfCoauthors) {
		
		super();
		
		this.nameToId = new HashMap<String,Integer>();
		this.idToName = new HashMap<Integer,String>();
		this.mainPersonName = mainPersonName;
		
		if (includeMainAuthor) {
			this.numNodes = 1 + coauthors.size();	
			graph.add(0, new LinkedList<Node>());
			idToName.put(0, mainPersonName);
			nameToId.put(mainPersonName, 0);
			nodeList.add(new LabeledMultigraphNode(0, mainPersonName));
			for (int i = 1; i < numNodes; ++i) {
				Coauthor coauthor = coauthors.get(i-1);
				String coauthorName = coauthor.getName();
				nodeList.add(new LabeledMultigraphNode(i, coauthorName));
				nameToId.put(coauthorName, i);
				idToName.put(i, coauthorName);
				graph.add(i, new LinkedList<Node>());
				for (int j = 0; j < coauthor.getCount(); ++j) {
					addEdge(new MultigraphEdge(numEdges, new LabeledMultigraphNode(0,mainPersonName), new LabeledMultigraphNode(i, coauthorName)));
				}
			}
		} else {
			this.numNodes = coauthors.size();
			for (int i = 0; i < numNodes; ++i) {
				String coauthorName = coauthors.get(i).getName();
				nodeList.add(new LabeledMultigraphNode(i, coauthorName));
				nameToId.put(coauthorName, i);
				idToName.put(i, coauthorName);
				graph.add(i, new LinkedList<Node>());	
			}
		}
		
		for (int i = 0; i < listOfListOfCoauthors.size(); ++i) {
			Coauthor x = coauthors.get(i);
			Integer idOfX = nameToId.get(x.getName());
			String nameOfX = x.getName();
			ArrayList<Coauthor> coauthorsOfX = listOfListOfCoauthors.get(i);
			for (Coauthor y : coauthorsOfX) {
				Integer idOfY = nameToId.get(y.getName());
				String nameOfY = y.getName();
				if (idOfY != null && !y.getName().equalsIgnoreCase(mainPersonName)) {
					for (int j = 0; j < y.getCount(); ++j) {
						addEdge(new MultigraphEdge(numEdges, new LabeledMultigraphNode(idOfX, nameOfX), new LabeledMultigraphNode(idOfY, nameOfY)));
					}
				}
			}
		}
	}
	
	public int numNeighbors(LabeledMultigraphNode node) {
		return graph.get(node.id()).size();
	}
	
	public boolean removeEdge(MultigraphEdge edge) {
		
		edgeList.remove(edge);
		graph.get(edge.getHead().id()).remove(edge.getTail());
		graph.get(edge.getTail().id()).remove(edge.getHead());
		
		--numEdges;
		
		return true;
	}
	
	public void addEdge(Edge e) {
		
		Node head = e.getHead();
		Node tail = e.getTail();
		
		int headId = head.id();
		int tailId = tail.id();
		
		if (headId < 0 || headId >= numNodes)
			throw new IndexOutOfBoundsException();
		if (tailId < 0 || tailId >= numNodes)
			throw new IndexOutOfBoundsException();
		
		graph.get(headId).add(tail);
		graph.get(tailId).add(head);
		
		MinMax<Integer> minMax = new MinMax<Integer>(headId,tailId);
		
		if (minMax.min().intValue() == headId) {
			edgeList.add(new MultigraphEdge(numEdges,head,tail));
		} else {
			edgeList.add(new MultigraphEdge(numEdges,tail,head));
		}
		
		++numEdges;
	}

	/**
	 * Removes the node 'node' from the graph.
	 * 
	 * @param node a node
	 * @return true iff node was removed from the graph
	 */
	public boolean removeNode(Node node) {
		/**
		 * http://stackoverflow.com/questions/223918/iterating-through-a-list-avoiding-concurrentmodificationexception-when-removing
		 */
		
		graph.get(node.id()).clear();
		
		for (int i = 0; i < graph.size(); ++i)
			graph.get(i).remove(node);
		
		for (Iterator<Edge> iterator = edgeList.iterator(); iterator.hasNext();) {
			Edge edge = iterator.next();
			if (edge.getHead().equals(node) || edge.getTail().equals(node)) {
				iterator.remove();
			}
		}
		
		return true;
	}
}
