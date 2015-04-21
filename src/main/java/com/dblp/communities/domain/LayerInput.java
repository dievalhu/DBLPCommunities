package com.dblp.communities.domain;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class LayerInput implements Serializable {

	private static final long serialVersionUID = -5215383002419516306L;
	
	@NotNull
	private transient int layerId = 0;
	private transient String person;
	
	public int getLayerId() {
		return layerId;
	}
	public void setLayerId(int layerId) {
		this.layerId = layerId;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	
}
