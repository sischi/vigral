package de.chiller.vigral.algorithm;

import java.util.ArrayList;

import de.chiller.vigral.graph.ElementType;
import de.chiller.vigral.graph.Graph;
import de.chiller.vigral.graph.GraphElement;
import de.chiller.vigral.util.Pair;

/**
 * Interface that have to implemented by all Algorithms
 * @author Simon Schiller
 *
 */
public interface Algorithm {

	/**
	 * this method is called to get the requirements this explicit algorithm has.
	 * as example the dijkstra would have to be set an start vertex and an optionally end vertex
	 * @return returns a list of elementtype and a human readable string to tell the user what he has to choose
	 */
	public ArrayList<Pair<ElementType, String>> getRequirements();
	
	/**
	 * used to set the requirements to the algorithm
	 * @param requiredIDs a list of the IDs of the vertices or edges required in the order of the requirements list
	 * sent by "getrequirements"
	 */
	public void setRequirements(ArrayList<GraphElement> requiredElements);
	
	/**
	 * this function implements the algorithm
	 */
	public void perform();
	
	/**
	 * 
	 * @return returns the first step
	 */
	public Pair<ArrayList<Graph>, String> getFirstStep();
	/**
	 * 
	 * @return returns the previous step
	 */
	public Pair<ArrayList<Graph>, String> getPreviousStep();
	/**
	 * 
	 * @return returns the next step
	 */
	public Pair<ArrayList<Graph>, String> getNextStep();
	/**
	 * 
	 * @return returns the last step
	 */
	public Pair<ArrayList<Graph>, String> getLastStep();
	
	/**
	 * @return returns a human readable string identifying this algorithm 
	 */
	public String getAlgorithmName();
}
