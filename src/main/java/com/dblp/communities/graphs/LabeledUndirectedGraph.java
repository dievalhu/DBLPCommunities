package com.dblp.communities.graphs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.dblp.communities.parser.Coauthor;
import com.dblp.communities.parser.ParserEdge;
import com.dblp.communities.parser.ParserResult;

public class LabeledUndirectedGraph extends UndirectedGraph implements Serializable {

	private static final long serialVersionUID = -3213642919458975262L;
	protected HashMap<String,Integer> nameToId;
	protected HashMap<Integer,String> idToName;
	protected String mainPersonName;
	protected HashMap<Edge,Integer> edgeToCount;
	protected HashMap<String,Integer> numPublicationsWithMainAuthor;
	
	public void giveNodeName(int id, String name) {
		nameToId.put(name, id);
		idToName.put(id, name);
	}
	
	public LabeledUndirectedGraph() {
		super();
	}
	
	public LabeledUndirectedGraph(int numNodes) {
		super(numNodes);
		this.nameToId = new HashMap<String,Integer>();
		this.idToName = new HashMap<Integer,String>();
		this.edgeToCount = new HashMap<Edge,Integer>();
		this.numPublicationsWithMainAuthor = new HashMap<String,Integer>();
	}
	
	public LabeledUndirectedGraph(LabeledUndirectedGraph graph) {
		super(graph);
		this.nameToId = new HashMap<String,Integer>(graph.nameToId);
		this.idToName = new HashMap<Integer,String>(graph.idToName);
		this.mainPersonName = graph.mainPersonName;
		this.edgeToCount = new HashMap<Edge,Integer>();
		this.numPublicationsWithMainAuthor = new HashMap<String,Integer>(graph.numPublicationsWithMainAuthor);
	}
	
	// TODO: Update this method to include WEIGHTS on edges
	public LabeledUndirectedGraph(String mainPersonName, String mainPersonUrlPt, boolean includeMainAuthor, ParserResult result) {
		this.nameToId = new HashMap<String,Integer>();
		this.idToName = new HashMap<Integer,String>();
		this.edgeToCount = new HashMap<Edge,Integer>();
		this.mainPersonName = mainPersonName;
		
		this.graph = new ArrayList<LinkedList<Node>>(numNodes);
		this.nodeList = new LinkedList<Node>();
		this.edgeList = new ArrayList<Edge>();
		this.numEdges = 0;
		
		if (includeMainAuthor) {
			this.numNodes = result.getCoauthors().size();
			graph.add(0, new LinkedList<Node>());
			idToName.put(0, mainPersonName);
			nameToId.put(mainPersonName, 0);		
			nodeList.add(new Node(0));
			for (int i = 1; i < numNodes; ++i) {
				nodeList.add(new Node(i));
				nameToId.put(result.getCoauthors().get(i-1).getName(), i);
				idToName.put(i, result.getCoauthors().get(i-1).getName());
				graph.add(i, new LinkedList<Node>());
				Edge edge = new Edge(new Node(0), new Node(i));
				addEdge(edge);
			}
		} else {
			this.numNodes = result.getCoauthors().size() - 1;
			for (int i = 0; i < numNodes; ++i) {
				nodeList.add(new Node(i));
				nameToId.put(result.getCoauthors().get(i).getName(), i);
				idToName.put(i, result.getCoauthors().get(i).getName());
				graph.add(i, new LinkedList<Node>());	
			}
		}
		
		for (ParserEdge e : result.getRelationships()) {
			String startName = e.getStart().getName();
			String endName = e.getEnd().getName();
			Integer startId = nameToId.get(startName);
			Integer endId = nameToId.get(endName);
			addEdge(new Edge(new Node(startId), new Node(endId)));
		}
	}
	
	public LabeledUndirectedGraph(String mainPersonName, String mainPersonUrlPt, boolean includeMainAuthor,
			ArrayList<Coauthor> coauthors, ArrayList<ArrayList<Coauthor>> listOfListOfCoauthors) {
		
		this.nameToId = new HashMap<String,Integer>();
		this.idToName = new HashMap<Integer,String>();
		this.edgeToCount = new HashMap<Edge,Integer>();
		this.numPublicationsWithMainAuthor = new HashMap<String,Integer>();
		this.mainPersonName = mainPersonName;
		
		this.graph = new ArrayList<LinkedList<Node>>(numNodes);
		this.nodeList = new LinkedList<Node>();
		this.edgeList = new ArrayList<Edge>();
		this.numEdges = 0;
		
		this.neighborEdgeList = new ArrayList<LinkedList<Edge>>();
		
		if (includeMainAuthor) {
			this.numNodes = 1 + coauthors.size();	
			graph.add(0, new LinkedList<Node>());
			neighborEdgeList.add(0, new LinkedList<Edge>());
			idToName.put(0, mainPersonName);
			nameToId.put(mainPersonName, 0);
			nodeList.add(new Node(0));
			for (int i = 1; i < numNodes; ++i) {
				nodeList.add(new Node(i));
				Coauthor coauthor = coauthors.get(i-1);
				String coauthorName = coauthor.getName();
				Integer count = coauthor.getCount();
				nameToId.put(coauthorName, i);
				idToName.put(i, coauthorName);
				numPublicationsWithMainAuthor.put(coauthorName, coauthor.getCount());
				graph.add(i, new LinkedList<Node>());
				neighborEdgeList.add(i, new LinkedList<Edge>());
				Edge edge = new Edge(new Node(0), new Node(i), count);
				edgeToCount.put(edge, count);
				addEdge(edge);
			}
		} else {
			this.numNodes = coauthors.size();
			for (int i = 0; i < numNodes; ++i) {
				nodeList.add(new Node(i));
				Coauthor coauthor = coauthors.get(i);
				String coauthorName = coauthor.getName();
				nameToId.put(coauthorName, i);
				idToName.put(i, coauthorName);
				numPublicationsWithMainAuthor.put(coauthorName, coauthor.getCount());
				graph.add(i, new LinkedList<Node>());
				neighborEdgeList.add(i, new LinkedList<Edge>());
			}
		}
		
		for (int i = 0; i < listOfListOfCoauthors.size(); ++i) {
			Coauthor x = coauthors.get(i);
			Integer idOfX = nameToId.get(x.getName());
			ArrayList<Coauthor> coauthorsOfX = listOfListOfCoauthors.get(i);
			for (Coauthor y : coauthorsOfX) {
				Integer idOfY = nameToId.get(y.getName());
				Integer numPublicationsTogether = y.getCount();
				if (idOfY != null && !y.getName().equalsIgnoreCase(mainPersonName)) {
					Edge edge = new Edge(new Node(idOfX), new Node(idOfY), numPublicationsTogether);
					edgeToCount.put(edge, numPublicationsTogether);
					addEdge(edge);
				}
			}
		}
	}
	
	public int numPublicationsWithMainAuthor(String coauthorName) {
		Integer answer = numPublicationsWithMainAuthor.get(coauthorName);
		if (answer != null) {
			return answer.intValue();
		} else {
			return 0;
		}
	}
	
	public Integer edgeToCount(Edge e) {
		return edgeToCount.get(e);
	}
	
	public String nameOfNodeWithId(int id) {
		return idToName.get(id);
	}
}