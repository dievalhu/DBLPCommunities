package com.dblp.communities.graphs;

public class JSONNode {

	private String id;
	private String label;
	private Integer x;
	private Integer y;	
	private Double size;
	private Integer group;
	
	public JSONNode(String id, String label, Integer x, Integer y, Double size) {
		this.label = label;
		this.id = id;
		this.size = size;
		this.x = x;
		this.y = y;
	}
	
	public JSONNode(String id, String label) {
		this.label = label;
		this.id = id;
		this.x = 0;
		this.y = 0;
		this.size = 1.0;
	}

	public Integer getGroup() {
		return group;
	}

	public void setGroup(Integer group) {
		this.group = group;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public Double getSize() {
		return size;
	}

	public void setSize(Double size) {
		this.size = size;
	}
}
