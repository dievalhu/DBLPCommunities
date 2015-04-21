package com.dblp.communities.parser;

import java.util.List;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PersonSearchParser extends DefaultHandler {
	
	private Stack<String> elementStack = new Stack<String>();
	private Stack<Hit> objectStack = new Stack<Hit>();
	private List<Hit> hits;
	
	/**
	 * Instantiate a SAX Handler that handles person search
	 * XML files.
	 * 
	 * @param hits The List in which the hits will be stored.
	 */
	public PersonSearchParser(List<Hit> hits) {
		this.hits = hits;
	}
	
	public List<Hit> getHits() {
		return hits;
	}
	
	public void startElement(String uri, String localName, 
			String qName, Attributes attributes) throws SAXException {
		
		this.elementStack.push(qName);
		
		if ("hit".equals(qName)) {
			Hit hit = new Hit();
			if (attributes.getLength() > 0) {
				if (attributes.getValue("id") != null) {
					hit.setId(attributes.getValue("id"));
				}
			}
			objectStack.push(hit);
		}
		
	}
	
	public void endElement(String uri, String localName,
	        String qName) throws SAXException {
		
		this.elementStack.pop();
		
		if ("hit".equals(qName)) {
			Hit hit = this.objectStack.pop();
			hits.add(hit);
		} else if ("result".equals(qName)) {
			
		}
		
	}
	
	public void characters(char ch[], int start, int length)
	        throws SAXException {
		
		String value = new String(ch,start,length).trim();
		
		if (value.length() == 0)
			return;
		
		if ("author".equals(currentElement())) {
			Hit hit = objectStack.peek();
			hit.setAuthor(value);
		} else if ("url".equals(currentElement())) {
			if ("info".equals(parentElement())) {
				Hit hit = objectStack.peek();
				hit.setUrl(value);
			}
		}
	}
	
	/**
	 * Returns the parent element of the current element if there
	 * are at least two elements on the stack, and null otherwise. 
	 * 
	 * @return the parent element of the current element if there
	 * are at least two elements on the stack, and null otherwise.
	 */
	private String parentElement() {
		if (elementStack.size() > 1) {			
			return elementStack.get(elementStack.size()-2);
		} else {
			return null;
		}
	}
	
	/**
	 * Returns the top element of the element stack, that is,
	 * the element currently considered by this SAX handler.
	 * 
	 * @return the top element of the element stack, that is,
	 * the element currently considered by this SAX handler.
	 */
	private String currentElement() {
		if (elementStack.size() > 0)
			return elementStack.peek();
		else
			return null;
	}
}
