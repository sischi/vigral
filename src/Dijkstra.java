import java.util.ArrayList;


public class Dijkstra extends AbstractAlgorithm {

	Vertex mSrcVertex;
	Vertex mDestVertex;
	
	@Override
	public ArrayList<Pair<ElementType, String>> getRequirements() {
		ArrayList<Pair<ElementType, String>> requires = new ArrayList<Pair<ElementType, String>>();
		
		requires.add(new Pair<ElementType, String>(ElementType.VERTEX, "Source Vertex"));
		requires.add(new Pair<ElementType, String>(ElementType.VERTEX, "Destination Vertex"));
		
		return requires;
	}

	@Override
	public void setRequirements(ArrayList<Pair<ElementType, Object>> requirements) {
		mSrcVertex = (Vertex) requirements.get(0).getR();
		mDestVertex = (Vertex) requirements.get(1).getR();
		
		System.out.println("start: "+ mSrcVertex.toString() +", dest"+ mDestVertex.toString());
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
