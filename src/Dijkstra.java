import java.util.ArrayList;


public class Dijkstra extends AbstractAlgorithm {

	private int mSrcVertexID;
	private int mDestVertexID;

	
	@Override
	public ArrayList<MyPair<ElementType, String>> getRequirements() {
		ArrayList<MyPair<ElementType, String>> requires = new ArrayList<MyPair<ElementType, String>>();
		
		requires.add(new MyPair<ElementType, String>(ElementType.VERTEX, "Source Vertex"));
		requires.add(new MyPair<ElementType, String>(ElementType.VERTEX, "Destination Vertex"));
		
		return requires;
	}

	@Override
	public void setRequirements(ArrayList<Integer> requiredIDs) {
		mSrcVertexID = (int) requiredIDs.get(0);
		mDestVertexID = (int) requiredIDs.get(1);
		
		System.out.println("start: "+ mSrcVertexID +", dest:"+ mDestVertexID);
	}

	@Override
	public String getAlgorithmName() {
		return "Dijkstra";
	}

	@Override
	public void perform() {
		for(Vertex v : mGraph.getVertices()) {
			System.out.println(v.debug());
			v.setState(ElementState.ACTIVE);
			mSteps.add(new Graph(mGraph));
			v.setState(ElementState.VISITED);
			mSteps.add(new Graph(mGraph));
		}
		System.out.println("FINISHED!");
	}

}
