package com.dblp.communities.domain;

import java.io.Serializable;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.dblp.communities.validator.Definition;

public class Input implements Serializable {

	private static final long serialVersionUID = -9212908399732734496L;

	@Pattern(regexp = "urlpt=[a-zA-ZàèìòùÀÈÌÒÙáéíóúýÁÉÍÓÚÝâêîôûÂÊÎÔÛãñõÃÑÕäëïöüÿÄËÏÖÜŸçÇßØøÅåÆæœ0-9:\\-_/=.]*&name=[a-zA-ZàèìòùÀÈÌÒÙáéíóúýÁÉÍÓÚÝâêîôûÂÊÎÔÛãñõÃÑÕäëïöüÿÄËÏÖÜŸçÇßØøÅåÆæœ0-9:\\-_/=. ']*", message = "{Input.person.pattern}")
	private transient String person;
	
	@Definition
	private transient String definition;
	
	@DecimalMin(value = "0.0", message = "{Input.lower.decimalmin}")
	@DecimalMax(value = "1.0", message = "{Input.upper.decimalmax}")
	@NotNull(message = "{Input.lower.notnull}")
	private transient Double lower = new Double(0);
	
	@DecimalMin(value = "0.0", message = "{Input.lower.decimalmin}")
	@DecimalMax(value = "1.0", message = "{Input.upper.decimalmax}")
	@NotNull(message = "{Input.lower.notnull}")
	private transient Double upper = new Double(1);
	
	@NotNull
	private transient boolean showNumEdges = false;
	
	@NotNull
	private transient boolean includeMainAuthor = false;

	public Input() {
		super();
	}
	
	public Double getUpper() {
		return upper;
	}

	public void setUpper(Double upper) {
		this.upper = upper;
	}

	public boolean isIncludeMainAuthor() {
		return includeMainAuthor;
	}

	public void setIncludeMainAuthor(boolean includeMainAuthor) {
		this.includeMainAuthor = includeMainAuthor;
	}

	public boolean isShowNumEdges() {
		return showNumEdges;
	}


	public void setShowNumEdges(boolean showNumEdges) {
		this.showNumEdges = showNumEdges;
	}


	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getName() {
		if (person != null && person.length() > 0) {
			int s = person.indexOf('&');
			return person.substring(s+6);
		} else {
			return "";
		}
	}
	
	public String getUrlpt() {
		if (person != null && person.length() > 0) {
			int s = person.indexOf('&');
			return person.substring(6,s);
		} else {
			return "";
		}
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public Double getLower() {
		return lower;
	}

	public void setLower(Double lower) {
		this.lower = lower;
	}
}
