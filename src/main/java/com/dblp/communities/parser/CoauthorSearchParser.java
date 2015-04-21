package com.dblp.communities.parser;

import java.util.List;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CoauthorSearchParser extends DefaultHandler {

	private Stack<String> elementStack = new Stack<String>();
	private Stack<Coauthor> objectStack = new Stack<Coauthor>();
	private List<Coauthor> authors;
	
	/**
	 * Instantiate a SAX Handler that handles coauthor search
	 * XML files.
	 * 
	 * @param hits The List in which the hits will be stored.
	 */
	public CoauthorSearchParser(List<Coauthor> authors) {
		this.authors = authors;
	}
	
	public List<Coauthor> getAuthors() {
		return authors;
	}
	
	public void startElement(String uri, String localName, 
			String qName, Attributes attributes) throws SAXException {
		
		elementStack.push(qName);
		
		if ("author".equals(qName)) {
			Coauthor author = new Coauthor();
			if (attributes.getLength() > 0) {
				if (attributes.getValue("urlpt") != null) {
					author.setUrlpt(attributes.getValue("urlpt"));
				}
				if (attributes.getValue("count") != null) {
					try {
						author.setCount(Integer.parseInt(attributes.getValue("count")));
					} catch (Exception e) {  }
				}
			}
			objectStack.add(author);
		}
		
	}
	
	public void endElement(String uri, String localName,
	        String qName) throws SAXException {
		
		elementStack.pop();
		
		if ("author".equals(qName)) {
			Coauthor author = objectStack.pop();
			authors.add(author);
		}
		
	}
	
	public void characters(char ch[], int start, int length)
	        throws SAXException {
		
		String value = new String(ch,start,length).trim();
		
		if (value.length() == 0)
			return;
		
		if ("author".equals(currentElement())) {
			Coauthor author = objectStack.peek();
			author.setName(value);
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
