package com.dblp.communities.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestTemplate;

import com.dblp.communities.algorithm.radicchi.LabeledRadicchi;
import com.dblp.communities.datastructure.LayerInfo;
import com.dblp.communities.datastructure.LayerOfCommunities;
import com.dblp.communities.datastructure.WebCommunity;
import com.dblp.communities.domain.Input;
import com.dblp.communities.exception.TooManyRequestsException;
import com.dblp.communities.graphs.LabeledUndirectedGraph;
import com.dblp.communities.graphs.LabeledUndirectedMultigraph;
import com.dblp.communities.io.FileDownloader;
import com.dblp.communities.parser.Coauthor;
import com.dblp.communities.parser.ParserResult;
import com.dblp.communities.service.AlgorithmService;

@Service
public class AlgorithmServiceImpl implements AlgorithmService {
	
	private boolean canUseSettingsFromPreviousSearch(HttpServletRequest request, Input input, boolean newSettings) {		
		String personInPreviousSearch = (String) request.getSession().getAttribute("prevName");
		if ((newSettings || input.getName() != null) && personInPreviousSearch != null) {
			if (input.getName().equalsIgnoreCase(personInPreviousSearch)) {
				if (request.getSession().getAttribute("graphMA") != null && request.getSession().getAttribute("graphNoMA") != null
						&& request.getSession().getAttribute("multigraphMA") != null && request.getSession().getAttribute("multigraphNoMA") != null) {
					return true;
				} else { 
					return false;
				}
			} else {
				return false;
			}
		}
		
		return false;
	}
	
	public void saveInputSettingsInSession(Input input, HttpServletRequest request) {	
		
		request.getSession().setAttribute("prevName", (String) request.getSession().getAttribute("name"));
		
		// Removing old settings
		request.getSession().removeAttribute("definition");
		request.getSession().removeAttribute("name");
		request.getSession().removeAttribute("person");
		request.getSession().removeAttribute("urlpt");
		request.getSession().removeAttribute("lowerValue");
		request.getSession().removeAttribute("showNumEdgesValue");
		request.getSession().removeAttribute("includeMainAuthorValue");
		
		// Saving input settings in session
		request.getSession().setAttribute("definition", input.getDefinition());
		request.getSession().setAttribute("name", input.getName());
		request.getSession().setAttribute("person", input.getPerson());
		request.getSession().setAttribute("urlpt", input.getUrlpt());
		request.getSession().setAttribute("lowerValue", input.getLower());
		request.getSession().setAttribute("showNumEdgesValue", input.isShowNumEdges());
		request.getSession().setAttribute("includeMainAuthorValue", input.isIncludeMainAuthor());
	}
	
	public void saveInputSettingsInModel(Input input, Model model) {
		model.addAttribute("definition", input.getDefinition());
		model.addAttribute("name", input.getName());
		model.addAttribute("person", input.getPerson());
		model.addAttribute("urlpt", input.getUrlpt());
		model.addAttribute("lower", (int) (input.getLower() * 100));
		model.addAttribute("showNumEdges", input.isShowNumEdges());
		model.addAttribute("includeMainAuthor", input.isIncludeMainAuthor());
	}
	
	private boolean mustUse_DBLP_XML_API(ParserResult parserResult) {
		if (parserResult == null || parserResult.getCoauthors() == null || parserResult.getRelationships() == null
				|| parserResult.getCoauthors().size() == 0 || parserResult.getRelationships().size() == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	private void buildNetoworkViaXML_API(HttpServletRequest request, Model model, Input input) {
		ArrayList<Coauthor> coauthors = FileDownloader.searchForCoauthorsOf(input.getUrlpt());				
		ArrayList<ArrayList<Coauthor>> listOfListOfCoauthor = new ArrayList<ArrayList<Coauthor>>();				
		for (Coauthor coauthor : coauthors) {
			ArrayList<Coauthor> cocoauthors = FileDownloader.searchForCoauthorsOf(coauthor.getUrlpt());
			listOfListOfCoauthor.add(cocoauthors);
		}
		
		LabeledUndirectedGraph graphWithMA = new LabeledUndirectedGraph(input.getName(), input.getUrlpt(), true, coauthors, listOfListOfCoauthor);
		LabeledUndirectedGraph graphNoMA = new LabeledUndirectedGraph(input.getName(), input.getUrlpt(), false, coauthors, listOfListOfCoauthor);
		
//		LabeledUndirectedMultigraph multigraphWithMA = new LabeledUndirectedMultigraph(input.getName(), input.getUrlpt(), true, coauthors, listOfListOfCoauthor);
//		LabeledUndirectedMultigraph multigraphNoMA = new LabeledUndirectedMultigraph(input.getName(), input.getUrlpt(), true, coauthors, listOfListOfCoauthor);
		
		request.getSession().removeAttribute("graphMA");
		request.getSession().removeAttribute("graphNoMA");
		request.getSession().removeAttribute("numCoauthors");
//		request.getSession().removeAttribute("multigraphMA");
//		request.getSession().removeAttribute("multigraphNoMA");
		
		request.getSession().setAttribute("graphMA", graphWithMA);
		request.getSession().setAttribute("graphNoMA", graphNoMA);
		request.getSession().setAttribute("numCoauthors", coauthors.size());
//		request.getSession().setAttribute("multigraphMA", multigraphWithMA);
//		request.getSession().setAttribute("multigraphNoMA", multigraphNoMA);
		
		model.addAttribute("numCoauthors", coauthors.size());
	}
	
	private void buildNetworkFromDatabase(HttpServletRequest request, Model model, Input input, ParserResult parserResult) {
		LabeledUndirectedGraph graphWithMA = new LabeledUndirectedGraph(input.getName(), input.getUrlpt(), true, parserResult);
		LabeledUndirectedGraph graphNoMA = new LabeledUndirectedGraph(input.getName(), input.getUrlpt(), false, parserResult);
		
		request.getSession().removeAttribute("graphMA");
		request.getSession().removeAttribute("graphNoMA");
		
		request.getSession().setAttribute("graphMA", graphWithMA);
		request.getSession().setAttribute("graphNoMA", graphNoMA);
		
		if (input.isIncludeMainAuthor()) {
			request.getSession().setAttribute("numCoauthors", parserResult.getCoauthors().size());
			model.addAttribute("numCoauthors", parserResult.getCoauthors().size());
		} else {
			request.getSession().setAttribute("numCoauthors", parserResult.getCoauthors().size()-1);
			model.addAttribute("numCoauthors", parserResult.getCoauthors().size()-1);
		}
	}
	
	public LabeledUndirectedMultigraph buildCollaborationMultiNetwork(BindingResult result, HttpServletRequest request, Model model, Input input,
			boolean newSettings) {
		
		System.out.println("buildCollaborationMultiNetwork: start");
		
		boolean usingPreviouslyBuiltGraph = false;
		LabeledUndirectedMultigraph answerGraph = null;
		
		try {
			
			if (canUseSettingsFromPreviousSearch(request, input, newSettings)) {				
				usingPreviouslyBuiltGraph = true;				
				request.getSession().removeAttribute("graph");
				if (input.isIncludeMainAuthor()) {
					LabeledUndirectedMultigraph graphWithMA = (LabeledUndirectedMultigraph) request.getSession().getAttribute("multigraphMA");
					request.getSession().setAttribute("graph", graphWithMA);
					answerGraph = graphWithMA;
				} else {
					LabeledUndirectedMultigraph graphWithoutMA = (LabeledUndirectedMultigraph) request.getSession().getAttribute("multigraphNoMA");
					request.getSession().setAttribute("graph", graphWithoutMA);
					answerGraph = graphWithoutMA;
				}
			}
			
			if (!usingPreviouslyBuiltGraph) {
				
				System.out.println("buildCollaborationMultiNetwork: NOT using prev built graph");
				
				ParserResult parserResult = null;
				
				if (mustUse_DBLP_XML_API(parserResult)) {
					buildNetoworkViaXML_API(request, model, input);
				} else {
					buildNetworkFromDatabase(request, model, input, parserResult);
				}
				
				if (input.isIncludeMainAuthor()) {
					answerGraph = (LabeledUndirectedMultigraph) request.getSession().getAttribute("multigraphMA");
					request.getSession().setAttribute("graph", answerGraph);
				} else {
					System.out.println("buildCollaborationMultiNetwork: NOT including MA");
					answerGraph = (LabeledUndirectedMultigraph) request.getSession().getAttribute("multigraphNoMA");
					System.out.println("buildCollaborationMultiNetwork: # edges in multigraph = " + answerGraph.numEdges());
					request.getSession().setAttribute("graph", answerGraph);
				}
			}
			
		} catch (Exception e) {
			
		}
		
		return answerGraph;
	}

	public LabeledUndirectedGraph buildCollaborationNetwork(BindingResult result, HttpServletRequest request, Model model, Input input,
			boolean newSettings) {

		boolean usingPreviouslyBuiltGraph = false;
		LabeledUndirectedGraph answerGraph = null;

		try {
			
			if (canUseSettingsFromPreviousSearch(request, input, newSettings)) {				
				usingPreviouslyBuiltGraph = true;				
				request.getSession().removeAttribute("graph");
				if (input.isIncludeMainAuthor()) {
					LabeledUndirectedGraph graphWithMA = (LabeledUndirectedGraph) request.getSession().getAttribute("graphMA");
					request.getSession().setAttribute("graph", graphWithMA);
					answerGraph = graphWithMA;
				} else {
					LabeledUndirectedGraph graphWithoutMA = (LabeledUndirectedGraph) request.getSession().getAttribute("graphNoMA");
					request.getSession().setAttribute("graph", graphWithoutMA);
					answerGraph = graphWithoutMA;
				}
			}

			if (!usingPreviouslyBuiltGraph) {
				
				ParserResult parserResult = null;
				
				if (mustUse_DBLP_XML_API(parserResult)) {
					buildNetoworkViaXML_API(request, model, input);
				} else {
					buildNetworkFromDatabase(request, model, input, parserResult);
				}
				
				if (input.isIncludeMainAuthor()) {
					answerGraph = (LabeledUndirectedGraph) request.getSession().getAttribute("graphMA");
					request.getSession().setAttribute("graph", answerGraph);
				} else {
					answerGraph = (LabeledUndirectedGraph) request.getSession().getAttribute("graphNoMA");
					request.getSession().setAttribute("graph", answerGraph);
				}
			}

		} catch (TooManyRequestsException e) {
			model.addAttribute(
					"tooManyRequests",
					"More requests have been made to the DBLP database than DBLP permits."
							+ " This may happen if you are searching for an author"
							+ " with many coauthors.");
		}
		
		int minNumNodesInCommunity = (int)(input.getLower().doubleValue() * answerGraph.numNodes());
		request.getSession().setAttribute("minNumNodesInCommunity", minNumNodesInCommunity);
		model.addAttribute("minNumNodesInCommunity", minNumNodesInCommunity);

		return answerGraph;
	}

	public void executeRadicchiAlgorithm(LabeledUndirectedGraph graph, HttpServletRequest request, Model model, Input input) {
		LabeledRadicchi radicchi = new LabeledRadicchi(graph, input.getLower(), input.getDefinition(), input.isIncludeMainAuthor());
		radicchi.detectCommunities();
		saveAlgorithmResultsInSession(request, radicchi);
		saveAlgorithmResultsInModel(model, radicchi);
	}
	
	private void saveAlgorithmResultsInModel(Model model, LabeledRadicchi radicchi) {
		int numPartitions = radicchi.getNumPartitions();
		List<LayerInfo> layerInfos = radicchi.getLayerInfos();
		List<LayerOfCommunities> partitions = radicchi.getCommunityPartitions();
		LayerOfCommunities finalLayer = partitions.get(partitions.size()-1);
		List<WebCommunity> communities = radicchi.getFinalCommunityPartition();
		
		model.addAttribute("modularity", finalLayer.getModularity());
		model.addAttribute("communities", communities);
		model.addAttribute("numPartitions", numPartitions);
		model.addAttribute("layerInfos", layerInfos);
		model.addAttribute("totalNumEdges", finalLayer.getTotalNumEdges());
		model.addAttribute("numIntraCommunityEdges", finalLayer.getNumIntraCommunityEdges());
		model.addAttribute("numInterCommunityEdges", finalLayer.getNumInterCommunityEdges());
	}
	
	private void saveAlgorithmResultsInSession(HttpServletRequest request, LabeledRadicchi radicchi) {
		int numPartitions = radicchi.getNumPartitions();
		List<LayerInfo> layerInfos = radicchi.getLayerInfos();
		List<LayerOfCommunities> partitions = radicchi.getCommunityPartitions();
		List<WebCommunity> communities = radicchi.getFinalCommunityPartition();
		LayerOfCommunities finalLayer = partitions.get(partitions.size()-1);
		
		// Remove old results
		request.getSession().removeAttribute("communities");
		request.getSession().removeAttribute("numPartitions");
		request.getSession().removeAttribute("layerInfos");
		request.getSession().removeAttribute("partitions");
		
		// Save new results
		request.getSession().setAttribute("communities", communities);
		request.getSession().setAttribute("numPartitions", numPartitions);
		request.getSession().setAttribute("layerInfos", layerInfos);		
		request.getSession().setAttribute("partitions", partitions);
	}
	
	private void prepareModelForUnchangedStuff(HttpServletRequest request, Model model) {		
		// Retrieves values from session
		int numPartitions = (int) request.getSession().getAttribute("numPartitions");
		List<LayerInfo> layerInfos = (List<LayerInfo>) request.getSession().getAttribute("layerInfos");		
		List<LayerOfCommunities> partitions = (List<LayerOfCommunities>) request.getSession().getAttribute("partitions");
		LayerOfCommunities finalLayer = partitions.get(partitions.size()-1);
		String definition = (String) request.getSession().getAttribute("definition");
		String name = (String) request.getSession().getAttribute("name");
		String person = (String) request.getSession().getAttribute("person");
		String urlpt = (String) request.getSession().getAttribute("urlpt");
		Double lower = (Double) request.getSession().getAttribute("lowerValue");
		boolean showNumEdges = (boolean) request.getSession().getAttribute("showNumEdgesValue");
		boolean includeMainAuthor = (boolean) request.getSession().getAttribute("includeMainAuthorValue");
		int numCoauthors = (int) request.getSession().getAttribute("numCoauthors");
		int minNumNodesInCommunity = (int) request.getSession().getAttribute("minNumNodesInCommunity");
		
		// Put values into model
		model.addAttribute("definition", definition);
		model.addAttribute("name", name);
		model.addAttribute("person", person);
		model.addAttribute("urlpt", urlpt);
		model.addAttribute("lower", (int) (lower * 100));
		model.addAttribute("minNumNodesInCommunity", minNumNodesInCommunity);
		model.addAttribute("showNumEdges", showNumEdges);
		model.addAttribute("includeMainAuthor", includeMainAuthor);		
		model.addAttribute("numPartitions", numPartitions);
		model.addAttribute("layerInfos", layerInfos);
		model.addAttribute("totalNumEdges", finalLayer.getTotalNumEdges());
		model.addAttribute("numIntraCommunityEdges", finalLayer.getNumIntraCommunityEdges());
		model.addAttribute("numInterCommunityEdges", finalLayer.getNumInterCommunityEdges());
		model.addAttribute("numCoauthors", numCoauthors);
	}
	
	public void getASpecificLayer(int layerId, HttpServletRequest request, Model model) {		
		// Changes
		List<LayerOfCommunities> partitions = (List<LayerOfCommunities>) request.getSession().getAttribute("partitions");
		LayerOfCommunities chosenLayer = partitions.get(layerId);
		List<WebCommunity> communities = chosenLayer.getCommunities();
		
		request.getSession().removeAttribute("communities");		
		request.getSession().setAttribute("communities", communities);		
		model.addAttribute("communities", communities);
		model.addAttribute("modularity", chosenLayer.getModularity());
		
		// prepare model for unchanged stuff
		prepareModelForUnchangedStuff(request, model);
	}
}
