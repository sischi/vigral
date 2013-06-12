import java.util.ArrayList;

import edu.uci.ics.jung.graph.SparseMultigraph;


public interface Algorithm {

	public ArrayList<String> getRequirements();
	public void setRequirements(ArrayList<String> requirements);
	
	public Graph getFirstStep();
	public Graph getPreviousStep();
	public Graph getNextStep();
	public Graph getResult();
}
