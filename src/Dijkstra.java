import java.util.ArrayList;


public class Dijkstra extends AbstractAlgorithm {

	private int mSrcVertexID;
	private int mDestVertexID;
	
	@Override
	public ArrayList<Pair<ElementType, String>> getRequirements() {
		ArrayList<Pair<ElementType, String>> requires = new ArrayList<Pair<ElementType, String>>();
		
		requires.add(new Pair<ElementType, String>(ElementType.VERTEX, "Source Vertex"));
		requires.add(new Pair<ElementType, String>(ElementType.VERTEX, "Destination Vertex"));
		
		return requires;
	}

	@Override
	public void setRequirements(ArrayList<Integer> requiredIDs) {
		mSrcVertexID = (int) requiredIDs.get(0);
		mDestVertexID = (int) requiredIDs.get(1);
		
		System.out.println("start: "+ mSrcVertexID +", dest:"+ mDestVertexID);
	}

	@Override
	public Graph getFirstStep() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Graph getPreviousStep() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Graph getNextStep() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Graph getResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAlgorithmName() {
		return "Dijkstra";
	}

}
