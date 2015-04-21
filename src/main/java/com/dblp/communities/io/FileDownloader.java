package com.dblp.communities.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.SAXException;

import com.dblp.communities.exception.TooManyRequestsException;
import com.dblp.communities.parser.Coauthor;
import com.dblp.communities.parser.CoauthorSearchParser;
import com.dblp.communities.parser.Hit;
import com.dblp.communities.parser.PersonSearchParser;

public class FileDownloader {

	private FileDownloader() {
		
	}
	
	public static String readUrl(String urlString) throws Exception {
		BufferedReader reader = null;
	    try {
	        URL url = new URL(urlString);
	        reader = new BufferedReader(new InputStreamReader(url.openStream()));
	        StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[1024];
	        while ((read = reader.read(chars)) != -1)
	            buffer.append(chars, 0, read); 

	        return buffer.toString();
	    } finally {
	        if (reader != null)
	            reader.close();
	    }
	}
	
	public static ArrayList<Coauthor> searchForCoauthorsOf(String personURLPt) throws TooManyRequestsException {
		String url = "http://dblp.uni-trier.de/pers/xc/" + personURLPt + ".xml";
		ArrayList<Coauthor> authors = new ArrayList<Coauthor>();
		
		try {
			BufferedInputStream bis = new BufferedInputStream(new URL(url).openStream());
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			StreamSource source = new StreamSource(bis);
			StreamResult result = new StreamResult(baos);
			transformer.transform(source, result);
			baos.close();

			SAXParserFactory parserFactory = SAXParserFactory.newInstance();
			SAXParser parser = parserFactory.newSAXParser();
			CoauthorSearchParser handler = new CoauthorSearchParser(authors);
			parser.getXMLReader().setFeature("http://xml.org/sax/features/validation", true);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			parser.parse(bais, handler);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			if (e.getMessage().contains("429")) {
				throw new TooManyRequestsException("429");
			}
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		return authors;
	}

	/**
	 * Searches for a person in the DBLP database using the 
	 * keywords stored in 'keywords'.
	 * 
	 * @param keywords a List of String keywords to be used in a person
	 *        search in the DBLP database.
	 * @return a List of Hits in the DBLP database.
	 */
	public static List<Hit> searchForPerson(List<String> keywords) {
		
		// Example URL: 
		// http://dblp.uni-trier.de/search/author/api?q=Fedor+Fomin&h=1000&c=0&rd=1a&format=xml
		
		ArrayList<Hit> hits = new ArrayList<Hit>();
		
		String url = buildPersonSearchURL(keywords);
		
		try {

			BufferedInputStream bis = new BufferedInputStream(new URL(url).openStream());
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			StreamSource source = new StreamSource(bis);
			StreamResult result = new StreamResult(baos);
			transformer.transform(source, result);
			baos.close();

			SAXParserFactory parserFactory = SAXParserFactory.newInstance();
			SAXParser parser = parserFactory.newSAXParser();
			PersonSearchParser handler = new PersonSearchParser(hits);
			parser.getXMLReader().setFeature("http://xml.org/sax/features/validation", true);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			parser.parse(bais, handler);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		return hits;
	}
	
	private static String buildPersonSearchURL(List<String> keywords) {
		String url = "http://dblp.uni-trier.de/search/author/api?q=";
		
		for (int i = 0; i < keywords.size(); ++i) {
			if (i < keywords.size()-1) {
				url += keywords.get(i) + "+";
			} else {
				url += keywords.get(i);
			}
		}
		
		url += "&h=1000&c=0&rd=1a&format=xml";
		
		return url;
	}
}
