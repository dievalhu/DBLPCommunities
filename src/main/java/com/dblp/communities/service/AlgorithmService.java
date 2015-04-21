package com.dblp.communities.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.dblp.communities.domain.Input;
import com.dblp.communities.graphs.LabeledUndirectedGraph;
import com.dblp.communities.graphs.LabeledUndirectedMultigraph;

public interface AlgorithmService {
	
	/**
	 * Builds the collaboration network of the author given in the 'input' as a multigraph. If the
	 * user has searched for the same author as he did the previous time he did a
	 * search, then the network is not built form scrath but the network is
	 * retrieved from the session.
	 * 
	 * @param result
	 * @param request
	 * @param model
	 * @param input
	 * @param newSettings
	 * @return
	 */
	public LabeledUndirectedMultigraph buildCollaborationMultiNetwork(BindingResult result, HttpServletRequest request, Model model, Input input,
			boolean newSettings);
	
	/**
	 * Builds the collaboration network of the author given in the 'input'. If the
	 * user has searched for the same author as he did the previous time he did a
	 * search, then the network is not built form scrath but the network is
	 * retrieved from the session.
	 * 
	 * @param result
	 * @param request
	 * @param model
	 * @param input
	 * @return
	 */
	public LabeledUndirectedGraph buildCollaborationNetwork(BindingResult result, HttpServletRequest request, 
			Model model, Input input, boolean newSettings);

	/**
	 * Executes the Radicchi algorithm with the parameters set in the 'input', stores
	 * necessary objects in the session, puts necessary information into the 'model',
	 * and returns the logical view name that a controller should send the user to.
	 * 
	 * @param result
	 * @param request
	 * @param model
	 * @param input
	 * @return the logical view name that a controller should send the user to.
	 */
	public void executeRadicchiAlgorithm(LabeledUndirectedGraph graph, HttpServletRequest request, Model model, Input input);
	
	/**
	 * Changes the partition of the current run of the Radicchi algorithm.
	 * 
	 * @param layerId
	 * @param request
	 * @param model
	 * @return
	 */
	public void getASpecificLayer(int layerId, HttpServletRequest request, Model model);
	
	/**
	 * Save input settings in session.
	 * 
	 * @param request
	 * @param input
	 */
	public void saveInputSettingsInSession(Input input, HttpServletRequest request);
	
	/**
	 * Save input settings in model. This is part of preparing the view.
	 * 
	 * @param input
	 * @param model
	 */
	public void saveInputSettingsInModel(Input input, Model model);
}
