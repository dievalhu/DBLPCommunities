package com.dblp.communities.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Person implements Serializable {

	private static final long serialVersionUID = -191467172380784780L;
	
	private String personName;
	
	public Person() {
		super();
	}
	
	public Person(String name) {
		super();
		this.personName = name;
	}
	
	@Override
	public String toString() {		
		return "Person with name the " + personName;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.personName == null) ? 0 : this.personName.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (this.getClass() != obj.getClass())
			return false;
		
		Person other = (Person) obj;
		
		if (this.personName == null && other.personName != null)
			return false;
		
		if (this.personName != null && other.personName == null)
			return false;
		
		if (this.personName.equals(other.personName))
			return true;
		
		return false;
	}
	
	public List<String> getNameParts() {
		StringTokenizer tokenizer = new StringTokenizer(personName);
		
		List<String> parts = new ArrayList<String>();
		while (tokenizer.hasMoreTokens()) {
			parts.add(tokenizer.nextToken());
		}
		
		return parts;
	}

	public final String getPersonName() {
		return personName;
	}

	public final void setPersonName(String name) {
		this.personName = name;
	}
}
