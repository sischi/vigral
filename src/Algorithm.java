import java.util.ArrayList;

import edu.uci.ics.jung.graph.SparseMultigraph;


public interface Algorithm {

	public ArrayList<Pair<ElementType, String>> getRequirements();
	public void setRequirements(ArrayList<Integer> requiredIDs);
	
	public void perform();
	public Graph getFirstStep();
	public Graph getPreviousStep();
	public Graph getNextStep();
	public Graph getResult();
	
	public String getAlgorithmName();
}
