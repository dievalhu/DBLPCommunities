package com.dblp.communities.datastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * A community as it is to be displayed on the web page.
 * 
 * @author erlend321
 *
 */
public class WebCommunity implements Iterable<PersonInCommunity> {

	private int id; // shown on the web page
	private int color; // from DFS
	private int metagraphParentId;
	private List<PersonInCommunity> elements;
	private HashMap<Integer,Integer> numEdgesTo;
	
	public WebCommunity() {
		this.elements = new ArrayList<PersonInCommunity>();
		this.numEdgesTo = new HashMap<Integer,Integer>();
	}
	
	public String toString() {
		String result = "Component: Visible ID = " + id + ", DFS-color = " + color + ", metagraph-ID = " + metagraphParentId
				+ ", elements = {";
		for (int i = 0; i < elements.size(); ++i) {
			PersonInCommunity e = elements.get(i); 
			result += "(" + e.nodeId + "," + e.name + "," + e.color + "), ";
		}
		result += "}";
		
		return result;
	}
	
	/**
	 * Returns the parent id of the community, that is,
	 * the id of the lowest numbered vertex in the community,
	 * as found by the DivisiveMetagraph.
	 * 
	 * @return the parent id of the community, that is,
	 * the id of the lowest numbered vertex in the community,
	 * as found by the DivisiveMetagraph.
	 */
	public int getMetagraphParentId() {
		return metagraphParentId;
	}

	public void setMetagraphParentId(int metagraphParentId) {
		this.metagraphParentId = metagraphParentId;
	}

	/**
	 * The coloring obtained from DFS.
	 * 
	 * @return
	 */
	public int getColor() {
		return color;
	}

	/**
	 * Sets the color obtained from DFS.
	 * 
	 * @param color
	 */
	public void setColor(int color) {
		this.color = color;
	}
	
	/**
	 * Sets the number of edges from this community to the community
	 * with community id 'communityId' to numEdges edges.
	 * 
	 * @param communityId the visible id of a community
	 * @param numEdges the number of edges from this community to the
	 * community with id communityId.
	 */
	public void setNumEdgesToCommunity(int communityId, int numEdges) {
		this.numEdgesTo.put(communityId, numEdges);
	}
	
	public HashMap<Integer, Integer> getNumEdgesTo() {
		return numEdgesTo;
	}

	public void setNumEdgesTo(HashMap<Integer, Integer> numEdgesTo) {
		this.numEdgesTo = numEdgesTo;
	}

	/**
	 * Get the id shown on the web page.
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set the id to be shown on the web page.
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	public void add(PersonInCommunity elm) {
		this.elements.add(elm);
	}

	public List<PersonInCommunity> getElements() {
		return elements;
	}

	public void setElements(List<PersonInCommunity> elements) {
		this.elements = elements;
	}

	public Iterator<PersonInCommunity> iterator() {
		return elements.iterator();
	}
}
