package com.dblp.communities.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dblp.communities.domain.Input;
import com.dblp.communities.domain.LayerInput;
import com.dblp.communities.domain.Person;
import com.dblp.communities.graphs.LabeledUndirectedGraph;
import com.dblp.communities.io.FileDownloader;
import com.dblp.communities.parser.Hit;
import com.dblp.communities.service.AlgorithmService;

@Controller
public class HomeController {
	
	@Autowired
	private AlgorithmService algorithmService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String welcome(Model model) {
		model.addAttribute("newPerson", new Person());
		return "index";
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String detect(@ModelAttribute("newPerson") Person newPerson, BindingResult result, Model model, HttpServletRequest request) {

		List<Hit> hits = FileDownloader.searchForPerson(newPerson.getNameParts());
		
		if (hits.isEmpty()) {
			model.addAttribute("noHits", "The search yielded no results.");
			return "index";
		}
		
		request.getSession().setAttribute("hits", hits);
		model.addAttribute("input", new Input());

		return "chooseperson";
	}
	
	@RequestMapping(value = "/changeSettings", method = RequestMethod.POST)
	public String changeSettings(@ModelAttribute("newSettings") @Valid Input newSettings, BindingResult result1, @ModelAttribute("layerInput") LayerInput layerInput,
			BindingResult result2, Model model, HttpServletRequest request) {
		
		algorithmService.saveInputSettingsInSession(newSettings, request);
		algorithmService.saveInputSettingsInModel(newSettings, model);
		algorithmService.buildCollaborationNetwork(result1, request, model, newSettings, true);
		LabeledUndirectedGraph graph = algorithmService.getCollaborationNetwork(request, newSettings.isIncludeMainAuthor());
		algorithmService.executeRadicchiAlgorithm(graph, request, model, newSettings);
		
		String person = (String) request.getSession().getAttribute("person");
		
		LayerInput layerInput2 = new LayerInput();
		layerInput.setPerson(person);
		model.addAttribute("layerInput", layerInput2);
		
		Input newSettings2 = new Input();
		newSettings.setPerson(person);
		model.addAttribute("newSettings", newSettings2);
		
		return "result";
	}

	@RequestMapping(value = "/person", method = RequestMethod.POST)
	public String choosePerson(@ModelAttribute("input") @Valid Input input,	BindingResult result, Model model, HttpServletRequest request) {
		
		if (result.hasErrors()) {
			return "chooseperson";
		}		
		
		algorithmService.saveInputSettingsInSession(input, request);
		algorithmService.saveInputSettingsInModel(input, model);
		algorithmService.buildCollaborationNetwork(result, request, model, input, false);
		LabeledUndirectedGraph graph = algorithmService.getCollaborationNetwork(request, input.isIncludeMainAuthor());
		algorithmService.executeRadicchiAlgorithm(graph, request, model, input);			
		
		LayerInput layerInput = new LayerInput();
		layerInput.setPerson(input.getPerson());
		model.addAttribute("layerInput", layerInput);
		
		Input newSettings = new Input();
		newSettings.setPerson(input.getPerson());
		model.addAttribute("newSettings", newSettings);
		
		return "result";
	}
	
	@RequestMapping(value = "/changeLayer")
	public String changeLayer(@ModelAttribute("layerInput") @Valid LayerInput layerInput,
			@ModelAttribute("newSettings") Input newSettings, BindingResult result, Model model, HttpServletRequest request) {
		
		algorithmService.getASpecificLayer(layerInput.getLayerId(), request, model);
		
		return "result";
	}
	
	@RequestMapping(value = "/error")
	public String customError(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "notfound";
	}
	
	@RequestMapping(value = "/algorithm", method = RequestMethod.GET)
	public String algorithm(Model model) {
		return "algorithm";
	}
	
	@RequestMapping(value = "/input", method = RequestMethod.GET)
	public String input(Model model) {
		return "input";
	}
}
