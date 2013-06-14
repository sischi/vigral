import java.util.ArrayList;


public abstract class AbstractAlgorithm implements Algorithm {

	/**
	 * the graph object
	 */
	protected MyGraph mGraph;
	/**
	 * a list of graphs that represent the single steps
	 */
	protected ArrayList<MyGraph> mSteps;

	protected int mActualStep;
	
	public AbstractAlgorithm() {
		mSteps = new ArrayList<MyGraph>();
		mActualStep = 0;
	}
	
	/**
	 * sets the graph object on what the algorithm will work
	 * @param g the graph object
	 */
	public void setGraph(MyGraph g) {
		mGraph = new MyGraph(g);
		mGraph.resetStates();
		mSteps.clear();
		mSteps.add(new MyGraph(mGraph));
		mActualStep = 0;
	}
	
	public MyGraph getFirstStep() {
		mActualStep = 0;
		System.out.println("get step "+ mActualStep);
		return mSteps.get(mActualStep);
	}

	public MyGraph getPreviousStep() {
		if(mActualStep > 0)
			mActualStep--;
		System.out.println("get step "+ mActualStep);
		return mSteps.get(mActualStep);
	}

	public MyGraph getNextStep() {
		if(mActualStep < mSteps.size() - 1)
			mActualStep++;
		System.out.println("get step "+ mActualStep);
		return mSteps.get(mActualStep);	
	}

	public MyGraph getLastStep() {
		mActualStep = mSteps.size()-1;
		System.out.println("get step "+ mActualStep);
		return mSteps.get(mActualStep);
	}

}
