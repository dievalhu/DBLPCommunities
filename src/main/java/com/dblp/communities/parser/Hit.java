package com.dblp.communities.parser;

public class Hit {

	private String id;
	private String author;
	private String url;
	
	public Hit() {
		
	}
	
	/**
	 * EX:   http://dblp.org/pers/d/Dregi:Markus_S=
	 * Returns: d/Dregi:Markus_S=
	 * 
	 * @return
	 */
	public String getUrlPt() {
		return url.substring(21);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
}
