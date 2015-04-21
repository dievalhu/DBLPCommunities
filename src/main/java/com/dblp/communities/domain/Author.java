package com.dblp.communities.domain;
import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class Author {

	@GraphId
	private Long id;
	
	private Integer externalId;

	@Indexed(unique = true)
	private String name;
	
	@RelatedTo(type = "IS_COAUTHOR_OF", direction = Direction.BOTH)
	private Set<Author> coauthors;

	public Author() {
		this.coauthors = new HashSet<Author>();
	}
	
	public Author(String name) {
		this.name = name;
		this.externalId = null;
		this.coauthors = new HashSet<Author>();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (this.getClass() != obj.getClass())
			return false;
		
		Author other = (Author) obj;
		
		if (this.name != null && other.name != null && this.name.equals(other.name)) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.name == null ? 0 : this.name.hashCode();
	}
	
	public void addCoauthor(Author coauthor) {
		if (coauthors == null)
			System.out.println("addCoauthor(): coauthors == null");
		if (coauthor == null)
			System.out.println("addCoauthor(): coauthor to be added == null");
		coauthors.add(coauthor);
	}
	
	public Set<Author> getCoauthors() {
		return coauthors;
	}

	public void setCoauthors(Set<Author> coauthors) {
		this.coauthors = coauthors;
	}
	
	public Integer getExternalId() {
		return externalId;
	}

	public void setExternalId(Integer externalId) {
		this.externalId = externalId;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}