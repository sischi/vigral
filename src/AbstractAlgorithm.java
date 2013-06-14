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
		mGraph.resetStates();
		mSteps.clear();
		mSteps.add(new Graph(mGraph));
		mActualStep = 0;
	}
	
	public Graph getFirstStep() {
		mActualStep = 0;
		System.out.println("get step "+ mActualStep);
		return mSteps.get(mActualStep);
	}

	public Graph getPreviousStep() {
		if(mActualStep > 0)
			mActualStep--;
		System.out.println("get step "+ mActualStep);
		return mSteps.get(mActualStep);
	}

	public Graph getNextStep() {
		if(mActualStep < mSteps.size() - 1)
			mActualStep++;
		System.out.println("get step "+ mActualStep);
		return mSteps.get(mActualStep);	
	}

	public Graph getLastStep() {
		mActualStep = mSteps.size()-1;
		System.out.println("get step "+ mActualStep);
		return mSteps.get(mActualStep);
	}

}
