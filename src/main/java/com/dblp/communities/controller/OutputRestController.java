package com.dblp.communities.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dblp.communities.datastructure.WebCommunity;
import com.dblp.communities.exception.NotFoundException;
import com.dblp.communities.graphs.JSONGraph;
import com.dblp.communities.graphs.LabeledUndirectedGraph;
import com.dblp.communities.graphs.LabeledUndirectedMultigraph;
import com.dblp.communities.exception.Error;

@RestController
public class OutputRestController {

	@RequestMapping(value = "/json/{name:.+}", produces = "application/json")
	public JSONGraph getGraphAsJSON(Model model, @PathVariable("name") String name,
			HttpServletRequest request) throws NotFoundException {
		
		if (request.getSession().getAttribute("graph") == null) {
			throw new NotFoundException();
		}	
		
		if (request.getSession().getAttribute("name") == null) {
			throw new NotFoundException();
		}
		
		String mainPersonName = (String) request.getSession().getAttribute("name");
		
		if (!mainPersonName.equalsIgnoreCase(name)) {
			throw new NotFoundException();
		}
		
		LabeledUndirectedGraph graph = new LabeledUndirectedGraph((LabeledUndirectedGraph) request.getSession().getAttribute("graph"));
		List<WebCommunity> communities = (List<WebCommunity>) request.getSession().getAttribute("communities");			
		
		return new JSONGraph(graph, communities);
	}
	
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Error personNotFound(NotFoundException e) {
		return new Error(404, "Person not found. The REST API only works after first searching "
				+ "for the given person.");
	}
}
