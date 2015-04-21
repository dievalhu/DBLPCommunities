package com.dblp.communities.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

public class NeighborhoodParser {

	/**
	 * 
	 * Inspiration: http://www.mkyong.com/java/jackson-streaming-api-to-read-and-write-json/
	 * @param content
	 */
	public static ParserResult parseNeighborhood(String content) {
		
		ParserEdge currentEdge = null;
		List<ParserNode> nodes = new ArrayList<ParserNode>();
		List<ParserEdge> edges = new ArrayList<ParserEdge>();
		
		try {
			
			JsonFactory factory = new JsonFactory();
			JsonParser parser = factory.createJsonParser(content);
			
			while (parser.nextToken() != JsonToken.END_OBJECT) {
				String fieldName = parser.getCurrentName();
				if ("coauthors".equals(fieldName)) {
					parser.nextToken(); // move to [
					while (parser.nextToken() != JsonToken.END_ARRAY) {
						nodes.add(new ParserNode(parser.getText()));
					}
				}
				if ("relationships".equals(fieldName)) {
					parser.nextToken(); // move to [
					while (parser.nextToken() != JsonToken.END_ARRAY) {
						String token = parser.getText();
						if ("start".equals(token)) {
							parser.nextToken(); // move to value of start
							currentEdge = new ParserEdge();
							ParserNode start = new ParserNode(parser.getText());
							currentEdge.setStart(start);
						} else if ("end".equals(token)) {
							parser.nextToken(); // move to value of end
							ParserNode end = new ParserNode(parser.getText());
							currentEdge.setEnd(end);
							edges.add(currentEdge);
						}
 					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return new ParserResult(nodes,edges);
	}
	
}
