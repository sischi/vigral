import java.util.ArrayList;

import edu.uci.ics.jung.graph.SparseMultigraph;


public interface Algorithm {

	public ArrayList<MyPair<ElementType, String>> getRequirements();
	public void setRequirements(ArrayList<Integer> requiredIDs);
	
	public void perform();
	public MyGraph getFirstStep();
	public MyGraph getPreviousStep();
	public MyGraph getNextStep();
	public MyGraph getLastStep();
	
	public String getAlgorithmName();
}
