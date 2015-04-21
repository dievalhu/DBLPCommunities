package com.dblp.communities.io;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.dblp.communities.graphs.Edge;
import com.dblp.communities.graphs.LabeledUndirectedGraph;
import com.dblp.communities.graphs.Node;

public class Exporter {

	private Exporter() {
		
	}
	
	public static String exportAsJson2(LabeledUndirectedGraph graph) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("{ \"edges\": [");		
		int edgeId = 1;
		for (Edge e : graph.edges()) {
			builder.append("{\"source\":\"" + e.start() + "\",");
			builder.append("\"target\":\"" + e.end() + "\",");
			builder.append("\"id\":\"" + edgeId + "\",");
			builder.append("\"attributes\":{\"Weight\":\"1.0\"},\"color\":\"rgb(100,149,237)\",");
			builder.append("\"size\":1.0}");
			if (edgeId < graph.numEdges())
				builder.append(",");
			++edgeId;
		}
		builder.append("], \"nodes\": [");
		int nodeId = 1;
		for (Node n : graph.nodes()) {
			builder.append("{\"label\":\"" + n.id() + "\",");
			builder.append("\"id\":\"" + n.id() + "\",\"attributes\":{},");
			builder.append("\"color\":\"rgb(100,149,237)\",");
			builder.append("\"size\":24}");
			if (nodeId < graph.numNodes())
				builder.append(",");
			++nodeId;
		}
		builder.append("] }");
		
		return builder.toString();
	}
	
	public static void exportAsJson(LabeledUndirectedGraph graph) {
		Writer writer = null;
		
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream("coauthorGraph.json"), "utf-8"));
			
			writer.write("{ \"edges\": [");
			int edgeId = 1;
			for (Edge e : graph.edges()) {
				writer.write("{\"source\":\"" + e.start() + "\",");
				writer.write("\"target\":\"" + e.end() + "\",");
				writer.write("\"id\":\"" + edgeId + "\",");
				writer.write("\"attributes\":{\"Weight\":\"1.0\"},\"color\":\"rgb(100,149,237)\",");
				writer.write("\"size\":1.0}");
				if (edgeId < graph.numEdges())
					writer.write(",");
				++edgeId;
			}
			writer.write("], \"nodes\": [");
			int nodeId = 1;
			for (Node n : graph.nodes()) {
				writer.write("{\"label\":\"" + n.id() + "\",");
				writer.write("\"id\":\"" + n.id() + "\",\"attributes\":{},");
				writer.write("\"color\":\"rgb(100,149,237)\",");
				writer.write("\"size\":24}");
				if (nodeId < graph.numNodes())
					writer.write(",");
				++nodeId;
			}
			writer.write("] }");
			
		} catch (IOException e) {
			
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
				
			}
		}
	}
}
