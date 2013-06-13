import java.util.ArrayList;


public abstract class AbstractAlgorithm implements Algorithm {

	/**
	 * the graph object
	 */
	protected Graph mGraph;
	/**
	 * a list of graphs that represent the single steps
	 */
	protected ArrayList<Graph> mSteps;

	protected int mActualStep;
	
	public AbstractAlgorithm() {
		mSteps = new ArrayList<Graph>();
		mActualStep = 0;
	}
	
	/**
	 * sets the graph object on what the algorithm will work
	 * @param g the graph object
	 */
	public void setGraph(Graph g) {
		mGraph = new Graph(g);
		mSteps.clear();
		mSteps.add(new Graph(mGraph));
		mActualStep = 0;
	}

}
