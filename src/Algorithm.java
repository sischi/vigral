import java.util.ArrayList;

import edu.uci.ics.jung.graph.SparseMultigraph;

/**
 * Interface that have to implemented by all Algorithms
 * @author Timmae
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
	public void setRequirements(ArrayList<Integer> requiredIDs);
	
	/**
	 * this function implements the algorithm
	 */
	public void perform();
	
	public Graph getFirstStep();
	public Graph getPreviousStep();
	public Graph getNextStep();
	public Graph getLastStep();
	
	/**
	 * @return returns a human readable string identifying this algorithm 
	 */
	public String getAlgorithmName();
}
